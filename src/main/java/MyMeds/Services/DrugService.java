package MyMeds.Services;

import MyMeds.App.Drug;
import MyMeds.App.Pharmacy;
import MyMeds.Repositorys.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    public record fdaDrugDTO(String brandName, String strength, String dosageForm){
    }

    public record pharmacyDrugDTO(String brandName,String strength,String dosageForm,Integer stock){}
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
    //Pendiente probar e implementar en el controller este metodo.
    public List<fdaDrugDTO> getDetailsFromBrandNameDrug(String brandName) throws IOException {
        String url = "https://api.fda.gov/drug/drugsfda.json?search=products.brand_name:"+brandName+"&limit=20";
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

                fdaDrugDTOS.add(fdaDrugDto);
            }
        }
        return fdaDrugDTOS;
    }
    /**El drugDTO debe tener los mismos datos que los medicamentos buscados en openFDA, nada mas que le agregamos el stock
     * que deberia tener la farmacia de ese producto.*/
    public boolean addDrugToPharmacy(Integer pharmacyID,pharmacyDrugDTO drugDTO){
        Optional<Pharmacy> pharmacy= pharmacyRepository.findById(pharmacyID);
        if (pharmacy.isPresent()){
            Drug drug=new Drug(drugDTO.brandName(), drugDTO.strength(), drugDTO.dosageForm(), drugDTO.stock());
            drug.setPharmacy(pharmacy.get());
            drugRepository.save(drug);
            pharmacyRepository.save(pharmacy.get());
            return true;
        }
        return false;
    }
}
