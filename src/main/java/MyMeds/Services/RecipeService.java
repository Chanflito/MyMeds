package MyMeds.Services;

import MyMeds.App.*;
import MyMeds.Dto.CreateRecipeResponse;
import MyMeds.Dto.DrugDTO;
import MyMeds.Exceptions.UserNotFoundException;
import MyMeds.Repositorys.*;
import MyMeds.email.EmailServiceImpl;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    @Autowired //No tengo que instnaciarlo spring ya lo sabe.
    DoctorRepository doctorRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    PharmacyRepository pharmacyRepository;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    EmailServiceImpl emailService;
    @Autowired
    DrugService drugService;
    @Autowired
    StockPharmacyRepository stockPharmacyRepository;

    @Autowired
    DrugRepository drugRepository;

    public CreateRecipeResponse addRecipe(Integer patientId, Integer docId, List<Integer>drugsID, Integer pharmacyID) {
        if (drugsID.isEmpty()){
            return new CreateRecipeResponse(false,null);
        }
        List<MyMeds.Dto.DrugDTO> drugsStock=stockPharmacyRepository.findDrugsWithStock(drugsID,pharmacyID);
        Optional<Doctor> doc_entity = doctorRepository.findById(docId);
        Optional<Patient> patient_entity = patientRepository.findById(patientId);
        if (!doc_entity.isPresent() && !patient_entity.isPresent()) {
            throw new UserNotFoundException();
        } else {
            Doctor doc = doc_entity.get();
            Patient p = patient_entity.get();
            if (!doc.HasPatient(p )) {
                return new CreateRecipeResponse(false,null);
            } else {
                if (drugsStock.isEmpty() ){
                    List<MyMeds.Dto.DrugDTO> drugsWithoutStockInPharmacy=new ArrayList<>();
                    for(Integer d:drugsID){
                        Optional<Drug> drug=drugRepository.findById(d);
                        drugsWithoutStockInPharmacy.add(new DrugDTO(drug.get().getId(),drug.get().getBrandName(),drug.get().getDosageForm(),drug.get().getStrength()));

                    }
                    return new CreateRecipeResponse(false,drugsWithoutStockInPharmacy);
                }
                if (drugsStock.size() < drugsID.size()) {
                    List<MyMeds.Dto.DrugDTO> drugsWithNoStock = drugsID.stream()
                            .filter(drugId -> drugsStock.stream().noneMatch(dto -> dto.getDrugID().equals(drugId)))
                            .map(drugId -> {
                                Optional<Drug> drugOptional = drugRepository.findById(drugId);
                                if (drugOptional.isPresent()) {
                                    Drug drug = drugOptional.get();
                                    return new DrugDTO(drug.getId(), drug.getBrandName(), drug.getDosageForm(), drug.getStrength());
                                }
                                return null;
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    return new CreateRecipeResponse(false,drugsWithNoStock);
                }
                else{
                    Recipe recipe=new Recipe();
                    recipe.setPatient(patient_entity.get());
                    recipe.setStatus(RecipeStatus.IN_PROGRESS);
                    recipe.setDoctorID(docId);
                    recipe.setPatientID(patientId);
                    recipe.setDoctor(doc);
                    recipe.setPharmacyID(pharmacyID);
                    for (Integer d: drugsID) {
                        Optional<Drug> drug=drugRepository.findById(d);
                        if (drug.isPresent()){
                            recipe.getDrugs().add(drug.get());
                            drug.get().getRecipes().add(recipe);
                            recipeRepository.save(recipe);
                            drugRepository.save(drug.get());
                        }
                    }
                    return new CreateRecipeResponse(true,null);
                }
            }
        }
    }

    public boolean createRecipe(Integer doctorID, Integer recipeID) throws IOException, WriterException, MessagingException {
        Optional<Doctor> doctorFound=doctorRepository.findById(doctorID);
        if (doctorFound.isPresent()){
            Optional<Recipe> recipe = recipeRepository.findById(recipeID);
            Optional<Pharmacy> pharmacy = pharmacyRepository.findById(recipe.get().getPharmacyID());
            if(!recipe.isPresent() || !pharmacy.isPresent()){return false;}
            Recipe r = recipe.get();
            Pharmacy p = pharmacy.get();
            r.setPharmacy(pharmacy.get());
            r.setStatus(RecipeStatus.APPROVED);
            p.addRecipe(r);
            sendQR(r.getRecipeID(), r.getPatient().getMail());
            recipeRepository.save(r);
            doctorRepository.save(doctorFound.get());
            pharmacyRepository.save(p);
            return true;
        }
        return false;
    }

    //-------------------------BUSQUEDAS--------------------------------------------------------------------------------
    public List<recipeDTO> findByRecipeStatusPatient(RecipeStatus status, Integer patientID){
        //Ve todos los tipos de receta menos las Dispensed
        List<Recipe> recipes = recipeRepository.findByStatusAndID(status, patientID);
        List<recipeDTO> answer = new ArrayList<>();
        boolean hasPharmacy = true;
        if(status.equals(RecipeStatus.IN_PROGRESS) || status.equals(RecipeStatus.DECLINED)){hasPharmacy = true;}
        //casos donde no tiene farmacia asignada
        if(hasPharmacy) {
            for (Recipe r : recipes) {
                answer.add(constructRecipeDTO(r, r.getPharmacyID()));
            }
            return answer;
        }
        else{
            for (Recipe r : recipes) {
                answer.add(constructRecipeDTO(r, null));
            }
            return answer;
        }
    }

    //Status solo puede ser In_Progress
    public List<recipeDTO> findByRecipeStatusDoctor(RecipeStatus status, Integer doctorID){
        List<Recipe> recipes = recipeRepository.findByStatusAndIDDoctor(status, doctorID);
        List<recipeDTO> answer = new ArrayList<>();
        for(Recipe r : recipes){
            answer.add(constructRecipeDTO(r, r.getPharmacyID()));
        }
        return answer;
    }

    public List<recipeDTO> findeByRecipeStatusPharmacy(RecipeStatus status, Integer pharmacyID){
        //Los estados son approved o dispensed, siempre tienen una farmacia
        List<Recipe> recipes = recipeRepository.findByStatusAndPharmacyID(status, pharmacyID);
        List<recipeDTO> answer = new ArrayList<>();
        for(Recipe r : recipes){
            answer.add(constructRecipeDTO(r, pharmacyID));
        }
        return answer;
    }

    public List<recipeDTO> findeAllRecipesForPharmacy(Integer pharmacyID){
        List<Recipe> recipes = recipeRepository.findAllForPharmacy(pharmacyID);
        List<recipeDTO> answer = new ArrayList<>();
        for(Recipe r : recipes){
            answer.add(constructRecipeDTO(r, pharmacyID));
        }
        return answer;
    }

    public boolean Exists(Integer recipeID){
        Optional<Recipe> r = recipeRepository.findById(recipeID);
        if(r.isPresent()){
            if(r.get().getStatus().equals(RecipeStatus.APPROVED)){
                return true;
            }
            return false;

        }
        return false;
    }

    //--------------------------RECHAZAR--------------------------------------------------------------------------------
    public boolean DeclineRecipe(Integer recipeID){
        Optional<Recipe> r = recipeRepository.findById(recipeID);
        if(!r.isPresent()){return false;}
        Recipe recipe = r.get();
        recipe.setStatus(RecipeStatus.DECLINED);
        recipeRepository.save(recipe);
        return true;
    }
    //-----------------------------------MAIL---------------------------------------------------------------------------

    public void sendQR(Integer recipeID, String patientMail) throws IOException, WriterException, MessagingException {
        QRcode.generateQRForRecipe(recipeID);
        String projectDir = System.getProperty("user.dir");
        String qrDir = projectDir + "/src/main/resources/RecipesQrs/";
        File QR = new File(qrDir, recipeID.toString());
        emailService.sendMail(QR, patientMail, "Show this Qr to your pharmacy (;", recipeID);
    }

    //---------------------------------DTO------------------------------------------------------------------------------

    public record recipeDTO(List<DrugDTO> drug, Integer recipeID,
                            Integer patientID, Integer doctorID, Integer pharmacyID,
                            String patientName, String doctorName, String pharmacyName){}
    /**Pharmacy mark recipe**/

    public recipeDTO constructRecipeDTO(Recipe r, Integer pID){
        List<DrugDTO> drugDTOList=new ArrayList<>();
        for (Drug d: r.getDrugs()) {
            drugDTOList.add(new DrugDTO(d.getId(),d.getBrandName(),d.getDosageForm(),d.getStrength()));
        }
        if(pID == null){
            return new recipeDTO(drugDTOList,r.getRecipeID(),
                    r.getPatientID(),r.getDoctorID(),null,
                    patientRepository.findById(r.getPatientID()).get().getUsername(),
                    doctorRepository.findById(r.getDoctorID()).get().getUsername(),
                    null);
        }
        return new recipeDTO(drugDTOList,r.getRecipeID(),
                r.getPatientID(),r.getDoctorID(), pID,
                patientRepository.findById(r.getPatientID()).get().getUsername(),
                doctorRepository.findById(r.getDoctorID()).get().getUsername(),
                pharmacyRepository.findById(pID).get().getUsername());
    }

    public boolean markRecipe(Integer recipeID){
        Optional<Recipe> recipe = recipeRepository.findById(recipeID);
        if (recipe.isPresent()) {
            recipe.ifPresent(r -> r.setStatus(RecipeStatus.DISPENSED));
            recipeRepository.save(recipe.get());
            return true;
        }
        return false;
    }

    public boolean rejectRecipeAsPharmacy(Integer recipeID){
        Optional<Recipe> recipe = recipeRepository.findById(recipeID);
        if (recipe.isPresent()) {
            recipe.ifPresent(r -> r.setStatus(RecipeStatus.REJECTED));
            recipeRepository.save(recipe.get());
            return true;
        }
        return false;
    }
}
