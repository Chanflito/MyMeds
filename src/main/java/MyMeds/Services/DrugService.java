package MyMeds.Services;

import MyMeds.App.*;
import MyMeds.Dto.DrugDTO;
import MyMeds.Dto.DrugStockDTO;
import MyMeds.Exceptions.InvalidJsonException;
import MyMeds.Repositorys.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DrugService {
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    PharmacyRepository pharmacyRepository;
    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    DrugRepository drugRepository;

    @Autowired
    StockPharmacyRepository stockPharmacyRepository;

    @Autowired
    UserService userService;

    public record fdaDrugDTO(String brandName, String strength, String dosageForm){
    }

    public record pharmacyDrugDTO(String brandName,String strength,String dosageForm,Integer stock){}

    public record myMedsDrugDTO(Integer drugID, String brandName,String strength,String dosageForm){}

    public List<String> getAllDrugsFromFDA(){
        String url = "https://api.fda.gov/drug/drugsfda.json?count=products.brand_name.exact&limit=999";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        List<String> drugList = new ArrayList<>();

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response.getBody());

                if (rootNode.has("results")) {
                    JsonNode resultsNode = rootNode.get("results");
                    if (resultsNode.isArray()) {
                        for (JsonNode resultNode : resultsNode) {
                            if (resultNode.has("term")) {
                                String drugName = resultNode.get("term").asText();
                                drugList.add(drugName);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return drugList;
    }
    public List<String> filterDrugInFDAByBrandName(String brandName) throws IOException {
        String b=brandName.replaceAll(" ", "-");
        String url = "https://api.fda.gov/drug/drugsfda.json?search=products.brand_name:"+b+"&limit=999";
        ObjectMapper mapper=new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new URL(url));

        JsonNode resultsNode = rootNode.get("results");
        List<String> fdaDrugDTOS = new ArrayList<>();

        for (JsonNode resultNode : resultsNode) {
            JsonNode activeIngredientsNode = resultNode.get("products").get(0).get("active_ingredients");

            if (activeIngredientsNode != null && activeIngredientsNode.isArray() && activeIngredientsNode.size() > 0) {
                String name =resultNode.get("products").get(0).get("brand_name").asText();
                if (!fdaDrugDTOS.contains(name)){
                    fdaDrugDTOS.add(name);
                }
            }
        }
        return fdaDrugDTOS;
    }
    public List<fdaDrugDTO> getDetailsFromBrandNameDrug(String brandName) throws IOException {
        String b=brandName.replaceAll(" ", "-");
        String url = "https://api.fda.gov/drug/drugsfda.json?search=products.brand_name:"+b+"&limit=999";
        ObjectMapper mapper=new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new URL(url));

        JsonNode resultsNode = rootNode.get("results");
        List<fdaDrugDTO> fdaDrugDTOS = new ArrayList<>();

        for (JsonNode resultNode : resultsNode) {
            JsonNode activeIngredientsNode = resultNode.get("products").get(0).get("active_ingredients");

            if (activeIngredientsNode != null && activeIngredientsNode.isArray() && activeIngredientsNode.size() > 0) {
                JsonNode activeIngredientNode = activeIngredientsNode.get(0);
                String strength = activeIngredientNode.get("strength").asText();
                String dosageForm = resultNode.get("products").get(0).get("dosage_form").asText();
                fdaDrugDTO fdaDrugDto = new fdaDrugDTO(resultNode.get("products").get(0).get("brand_name").asText()
                        , strength,
                        dosageForm);
                if (!fdaDrugDTOS.contains(fdaDrugDto)){
                    fdaDrugDTOS.add(fdaDrugDto);
                }
            }
        }
        return fdaDrugDTOS;
    }
    /**El drugDTO debe tener los mismos datos que los medicamentos buscados en openFDA, nada mas que le agregamos el stock
     * que deberia tener la farmacia de ese producto.*/
    public boolean addDrugToPharmacy(Integer pharmacyID,fdaDrugDTO drugDTO){
        Optional<Pharmacy> pharmacy=pharmacyRepository.findById(pharmacyID);
        if (drugDTO.dosageForm()==null || drugDTO.brandName()==null || drugDTO.strength()==null){
            return false;
        }
        if (pharmacy.isPresent()){
            Integer drugInMyMeds= drugRepository.getDrugByBrandNameAndDosageFormAndStrength(drugDTO.brandName(), drugDTO.dosageForm(), drugDTO.strength());
            Integer drugInPharmacy=stockPharmacyRepository.getDrugInPharmacy(drugDTO.brandName(), drugDTO.dosageForm(), drugDTO.strength(), pharmacyID);
            if (drugInPharmacy!=null && drugInMyMeds!=null){
                return false;
            }
            if (drugInPharmacy==null && drugInMyMeds!=null){
                Optional<Drug> drug=drugRepository.findById(drugInMyMeds);
                StockPharmacy stockPharmacy=new StockPharmacy();
                stockPharmacy.setPharmacy(pharmacy.get());
                stockPharmacy.setDrug(drug.get());
                stockPharmacy.setStock(0);
                drugRepository.save(drug.get());
                pharmacyRepository.save(pharmacy.get());
                stockPharmacyRepository.save(stockPharmacy);
                return true;
            }
            if (drugInPharmacy==null){
                Drug drug=new Drug(drugDTO.brandName(), drugDTO.strength(), drugDTO.dosageForm());
                StockPharmacy stockPharmacy=new StockPharmacy();
                stockPharmacy.setPharmacy(pharmacy.get());
                stockPharmacy.setDrug(drug);
                stockPharmacy.setStock(0);
                drugRepository.save(drug);
                pharmacyRepository.save(pharmacy.get());
                stockPharmacyRepository.save(stockPharmacy);
                return true;
            }
        }
        return false;
    }
    public List<myMedsDrugDTO> getAllDrugsFromMyMeds(){
        List<Drug> drugs=drugRepository.findAll();
        List<myMedsDrugDTO> myMedsDrugDTOS=new ArrayList<>();
        for (Drug d: drugs) {
            myMedsDrugDTOS.add(new myMedsDrugDTO(d.getId(),d.getBrandName(),d.getStrength(),d.getDosageForm()));
        }
        return myMedsDrugDTOS;
    }

    public List<DrugDTO> filterDrugsByBrandNameOnMyMeds(String brandName){
        return drugRepository.filterByBrandNameDrug(brandName);
    }
    //Agregamos la droga ya existente en myMeds al stock de la farmacia (la droga no la tenia la farmacia en si)

    public List<DrugStockDTO> getAllDrugsByPharmacyID(Integer pharmacyID){
        Optional<Pharmacy> pharmacy=pharmacyRepository.findById(pharmacyID);
        if (pharmacy.isPresent()){
            return stockPharmacyRepository.getDrugStockByPharmacyID(pharmacyID);
        }
        return null;
    }

    public boolean setDrugStockByPharmacyID(Integer pharmacyID, Integer stock,Integer drugID){
        Optional<Pharmacy> pharmacy=pharmacyRepository.findById(pharmacyID);
        if (pharmacy.isPresent()){
            StockPharmacy stockPharmacy=stockPharmacyRepository.getStockWithDrugIDAndPharmacyID(drugID,pharmacyID);
            if (stockPharmacy!=null){
                stockPharmacy.setStock(stock);
                stockPharmacyRepository.save(stockPharmacy);
                return true;
            }
        }
        return false;
    }

    //Antes de esto, el medico deberia hacer una busqueda de la droga, es decir en getAllDrugsFromMyMeds
    public boolean addDrugToPatient(Integer patientID,Integer drugID,Integer doctorID) {
        List<UserService.patientDTO> patientDTOList = userService.getAllPatientsFromDoctor(doctorID);
        Optional<Patient> patient = patientRepository.findById(patientID);
        Optional<Drug> drug=drugRepository.findById(drugID);
        if (patient.isPresent() &&  drug.isPresent()) {
            for (UserService.patientDTO p : patientDTOList) {
                if (Objects.equals(p.dni(), patientID )&& !patientRepository.patientContainsDrug(drugID,patientID)){
                    patient.get().getDrugList().add(drug.get());
                    drug.get().getPatients().add(patient.get());
                    patientRepository.save(patient.get());
                    drugRepository.save(drug.get());
                    return true;
                }
            }
        }
        return false;
    }
    public boolean removeDrugForPatient(Integer patientID,Integer drugID,Integer doctorID){
        Optional<Patient> patient=patientRepository.findById(patientID);
        Optional<Drug> drug=drugRepository.findById(drugID);
        Optional<Doctor> doctor=doctorRepository.findById(doctorID);
        if (patient.isPresent() && drug.isPresent() && doctor.isPresent()){
            List<Drug> patientDrugs=patient.get().getDrugList();
            if (patientRepository.patientContainsDrug(drugID,patientID) && doctor.get().HasPatient(patient.get())){
                patientDrugs.remove(drug.get());
                drug.get().getPatients().remove(patient.get());
                patientRepository.save(patient.get());
                drugRepository.save(drug.get());
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }
    public List<myMedsDrugDTO> getPatientDrugs(Integer patientID){
        Optional<Patient> patient=patientRepository.findById(patientID);
        List<myMedsDrugDTO> myMedsDrugDTOS=new ArrayList<>();
        if (patient.isPresent()){
            List<Drug>drugs=patient.get().getDrugList();
            for (Drug d: drugs) {
                myMedsDrugDTOS.add(new myMedsDrugDTO(d.getId(),d.getBrandName(),d.getStrength(),d.getDosageForm()));
            }
        }
        return myMedsDrugDTOS;
    }

    public List<myMedsDrugDTO> getPatientDrugsAsDoctor(Integer doctorID,Integer patientID){
        Optional<Patient> patient=patientRepository.findById(patientID);
        Optional<Doctor> doctor=doctorRepository.findById(doctorID);
        List<myMedsDrugDTO> myMedsDrugDTOS=new ArrayList<>();
        if (patient.isPresent() && doctor.isPresent() && doctor.get().HasPatient(patient.get())){
            List<Drug>drugs=patient.get().getDrugList();
            for (Drug d: drugs) {
                myMedsDrugDTOS.add(new myMedsDrugDTO(d.getId(),d.getBrandName(),d.getStrength(),d.getDosageForm()));
            }
        }
        return myMedsDrugDTOS;
    }
    public void loadMassiveStock(Integer pharmacyID,List<pharmacyDrugDTO> pharmacyDrugDTOS) throws InvalidJsonException {
        Optional<Pharmacy> pharmacy=pharmacyRepository.findById(pharmacyID);
        if (pharmacy.isPresent()){
            for(pharmacyDrugDTO d: pharmacyDrugDTOS){
                //En el dado caso de que alguno de los datos del json sean invalidos se interrumpe la carga del mismo.
                if (d.dosageForm()==null || d.brandName()==null || d.strength()==null || d.stock()==null){
                    throw new InvalidJsonException("Invalid JSON data.");
                }
                StockPharmacy stockPharmacy=new StockPharmacy();
                stockPharmacy.setPharmacy(pharmacy.get());
                Integer drugInMyMeds= drugRepository.getDrugByBrandNameAndDosageFormAndStrength(d.brandName(), d.dosageForm(), d.strength());
                Integer drugInPharmacy=stockPharmacyRepository.getDrugInPharmacy(d.brandName(), d.dosageForm(), d.strength(), pharmacyID);
                //Si la droga ya existe en MyMeds y en la farmacia, carga unicamente el valor del stock.
                if (drugInPharmacy!=null && drugInMyMeds!=null){
                    StockPharmacy stockToChange=stockPharmacyRepository.findById(drugInPharmacy).get();
                    Optional<Drug> drug=drugRepository.findById(drugInMyMeds);
                    stockToChange.setStock(d.stock());
                    drugRepository.save(drug.get());
                    stockPharmacyRepository.save(stockToChange);
                    pharmacyRepository.save(pharmacy.get());
                }
                if (drugInPharmacy==null && drugInMyMeds!=null){
                    Optional<Drug> drug=drugRepository.findById(drugInMyMeds);
                    stockPharmacy.setStock(d.stock());
                    stockPharmacy.setDrug(drug.get());
                    drugRepository.save(drug.get());
                    pharmacyRepository.save(pharmacy.get());
                    stockPharmacyRepository.save(stockPharmacy);
                }
                if (drugInPharmacy==null){
                    stockPharmacy.setStock(d.stock());
                    Drug drug=new Drug(d.brandName(), d.strength(), d.dosageForm());;
                    stockPharmacy.setDrug(drug);
                    drugRepository.save(drug);
                    pharmacyRepository.save(pharmacy.get());
                    stockPharmacyRepository.save(stockPharmacy);
                }
            }
        }
    }
}
