package MyMeds.Services;

import MyMeds.App.*;
import MyMeds.Dto.ApprovedRecipeData;
import MyMeds.Exceptions.UserNotFoundException;
import MyMeds.Repositorys.DoctorRepository;
import MyMeds.Repositorys.PatientRepository;
import MyMeds.Repositorys.PharmacyRepository;
import MyMeds.Repositorys.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    public boolean addRecipe(Integer patientId,Integer docId, String drugName) {
        Optional<Doctor> doc_entity = doctorRepository.findById(docId);
        Optional<Patient> patient_entity = patientRepository.findById(patientId);
        if (!doc_entity.isPresent() && !patient_entity.isPresent()) {
            throw new UserNotFoundException();
        } else {
            Doctor doc = doc_entity.get();
            Patient p = patient_entity.get();
            if (!doc.HasPatient(p)) {
                return false;
            } else {
                //crea una receta en estado IN_PROCESS
                Recipe r = new Recipe();
                r.setDoctorID(docId);
                r.setPatientID(patientId);
                r.setDrugName(drugName);
                r.setDoctor(doc);
                doc.addRecipe(r);
                recipeRepository.save(r);
                doctorRepository.save(doc);
                return true;
            }
        }
    }

    public boolean createRecipe(Integer doctorID, ApprovedRecipeData dto){
        Optional<Doctor> doctorFound=doctorRepository.findById(doctorID);
        if (doctorFound.isPresent()){
            if(dto.getDocSignature()==null){
                return false;
            }
            Optional<Recipe> recipe = recipeRepository.findById(dto.getRecipeID());
            Optional<Pharmacy> pharmacy = pharmacyRepository.findById(dto.getPharmacyID());
            if(!recipe.isPresent() || !pharmacy.isPresent()){return false;}
            Recipe r = recipe.get();
            Pharmacy p = pharmacy.get();

            r.setDocSignature(dto.getDocSignature());
            r.setPharmacyID(dto.getPharmacyID());
            r.setPharmacy(pharmacy.get());
            r.setStatus(RecipeStatus.APPROVED);

            p.addRecipe(r);

            recipeRepository.save(r);
            doctorRepository.save(doctorFound.get());
            pharmacyRepository.save(p);
            return true;
        }
        return false;
    }

    //-------------------------BUSQUEDAS--------------------------------------------------------------------------------
    public List<recipeDTO> findByRecipeStatusPatient(RecipeStatus status, Integer patientID){
        List<Recipe> recipes = recipeRepository.findByStatusAndID(status, patientID);
        List<recipeDTO> answer = new ArrayList<>();
        for(Recipe r : recipes){
            answer.add(new recipeDTO(r.getDocSignature(), r.getDrugName(),r.getRecipeID(),
                    r.getPatientID(),r.getDoctorID(),r.getPharmacyID(),
                    patientRepository.findById(r.getPatientID()).get().getUsername(),
                    doctorRepository.findById(r.getDoctorID()).get().getUsername()));
        }
        return answer;
    }

    public List<recipeDTO> findByRecipeStatusDoctor(RecipeStatus status, Integer doctorID){
        List<Recipe> recipes = recipeRepository.findByStatusAndIDDoctor(status, doctorID);
        List<recipeDTO> answer = new ArrayList<>();
        for(Recipe r : recipes){
            answer.add(new recipeDTO(r.getDocSignature(), r.getDrugName(),r.getRecipeID(),
                    r.getPatientID(),r.getDoctorID(),r.getPharmacyID(),
                    patientRepository.findById(r.getPatientID()).get().getUsername(),
                    doctorRepository.findById(r.getDoctorID()).get().getUsername()));
        }
        return answer;
    }

    public List<recipeDTO> findeByRecipeStatusPharmacy(RecipeStatus status, Integer pharmacyID){
        List<Recipe> recipes = recipeRepository.findByStatusAndPharmacyID(status, pharmacyID);
        List<recipeDTO> answer = new ArrayList<>();
        for(Recipe r : recipes){
            answer.add(new recipeDTO(r.getDocSignature(), r.getDrugName(),r.getRecipeID(),
                    r.getPatientID(),r.getDoctorID(),r.getPharmacyID(),
                    patientRepository.findById(r.getPatientID()).get().getUsername(),
                    doctorRepository.findById(r.getDoctorID()).get().getUsername()));
        }
        return answer;
    }

    public List<recipeDTO> findeAllRecipesForPharmacy(Integer pharmacyID){
        List<Recipe> recipes = recipeRepository.findAllForPharmacy(pharmacyID);
        List<recipeDTO> answer = new ArrayList<>();
        for(Recipe r : recipes){
            answer.add(new recipeDTO(r.getDocSignature(), r.getDrugName(),r.getRecipeID(),
                    r.getPatientID(),r.getDoctorID(),r.getPharmacyID(),
                    patientRepository.findById(r.getPatientID()).get().getUsername(),
                    doctorRepository.findById(r.getDoctorID()).get().getUsername()));
        }
        return answer;
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

    //---------------------------------DTO------------------------------------------------------------------------------

    public record recipeDTO(String docSignature, String drugName, Integer recipeID,
                            Integer patientID, Integer doctorID, Integer pharmacyID,
                            String patientName, String doctorName){}
    /**Pharmacy mark recipe**/

    public boolean markRecipe(Integer recipeID){
        Optional<Recipe> recipe = recipeRepository.findById(recipeID);
        if (recipe.isPresent()) {
            recipe.ifPresent(r -> r.setStatus(RecipeStatus.DISPENSED));
            recipeRepository.save(recipe.get());
            return true;
        }
        return false;
    }
}
