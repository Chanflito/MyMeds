package MyMeds.Services;

import MyMeds.App.*;
import MyMeds.Exceptions.UserNotFoundException;
import MyMeds.Exceptions.UserRegisteredException;
import MyMeds.Repositorys.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    @Autowired //No tengo que instnaciarlo spring ya lo sabe.
    DoctorRepository doctorRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    PharmacyRepository pharmacyRepository;
    @Autowired
    RecipeRepository recipeRepository;


    public record pharmacyDTO(Integer pharmacyID,String pharmacyName){}
    //Utiliza el repositorio para buscar los doctores en la base de datos.
    public List<Doctor> getDoctors(){
        return doctorRepository.findAll();
    }

    public List<Patient> getPatients(){
        return  patientRepository.findAll();
    }

    public List<Pharmacy> getPharmacys(){
        return  pharmacyRepository.findAll();
    }

    //---------------REGISTER PART-------------------------------------------
    public Doctor registerDoctor(Doctor doctor){
        if (doctorRepository.findById(doctor.getPrimarykey()).isEmpty() &&
                doctorRepository.findByMailIgnoreCase(doctor.getMail())==null &&
                patientRepository.findByMailIgnoreCase(doctor.getMail())==null &&
                pharmacyRepository.findByMailIgnoreCase(doctor.getMail())==null){
            return doctorRepository.save(doctor);
        }
        return null;
    }

    public Patient registerPatient(Patient patient){
        if (patientRepository.findById(patient.getPrimarykey()).isEmpty() &&
                patientRepository.findByMailIgnoreCase(patient.getMail())==null &&
                pharmacyRepository.findByMailIgnoreCase(patient.getMail())==null &&
                doctorRepository.findByMailIgnoreCase(patient.getMail())==null){
            return patientRepository.save(patient);
        }
        return null;
    }

    public Pharmacy registerPharmacy(Pharmacy pharmacy){
        if (pharmacyRepository.findById(pharmacy.getPrimarykey()).isEmpty() &&
                patientRepository.findByMailIgnoreCase(pharmacy.getMail())==null &&
                pharmacyRepository.findByMailIgnoreCase(pharmacy.getMail())==null &&
                doctorRepository.findByMailIgnoreCase(pharmacy.getMail())==null){
            return pharmacyRepository.save(pharmacy);
        }
        return null;
    }

    //-----------------------------------------------------------------------------------
    //Busca un doctor con un ID, si lo encuentra retorna el objeto en formato JSON, sino lo encuentra retorna una excepcion.
    public Optional<Doctor> getDoctorById(Integer id){
        return Optional.ofNullable(doctorRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }
    public Optional<Patient> getPatientById(Integer id){
        return Optional.ofNullable(patientRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }


    public Optional<Pharmacy> getPharmacyById(Integer id){
        return Optional.ofNullable(pharmacyRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }
    //Si encuentra el doctor lo borra y retorna true, caso contrario retorna false.
    public boolean deleteDoctorById(Integer id){
        if (!doctorRepository.existsById(id)){
            return false;
        }
        doctorRepository.deleteById(id);
        return true;
    }

    public boolean deletePatientById(Integer id){
        if (!patientRepository.existsById(id)){
            return false;
        }
        patientRepository.deleteById(id);
        return true;
    }

    public boolean deletePharmacyById(Integer id){
        if (!pharmacyRepository.existsById(id)){
            return false;
        }
        pharmacyRepository.deleteById(id);
        return true;
    }
    //Cambiamos la contraseña del paciente mediante su ID.
    public Optional<Patient> changePatientPasswordByID(Integer id, Patient patient){
        return Optional.ofNullable(patientRepository.findById(id).map(p -> {
            p.setPassword(patient.getPassword());
            return patientRepository.save(p);
        }).orElseThrow(() -> new UserNotFoundException(id)));
    }
    public Optional<Patient> addHealthInsuranceById(Integer healthinsurance,Integer id){
        return Optional.of(patientRepository.findById(id).map(p->{
            p.setHealthInsuarence(healthinsurance);
            return patientRepository.save(p);
        })).orElseThrow(()->new UserNotFoundException(id));
    }
    //Verificamos si el paciente  existe, sino existe retornamos Null.

    //--------------------LOGIN PART--------------------------------------------------

    //Verifica si el paciente se encuentra registrador, si encuentra el mail y la contraseña se verifica si ambos coinciden con el primaryKey.
    public Patient checkLoginPatient(String mail,String password){
        Patient patientWithMail=patientRepository.findByMailIgnoreCase(mail);
        if (patientWithMail!=null){
            if (Objects.equals(patientWithMail.getPassword(), password)){
                return patientWithMail;
            }
        }
        return null;
    }

    public Doctor checkLoginDoctor(String mail,String password){
        Doctor doctorWithMail=doctorRepository.findByMailIgnoreCase(mail);
        if (doctorWithMail!=null){
            if (Objects.equals(doctorWithMail.getPassword(),password)){
                return doctorWithMail;
            }
        }
        return null;
    }

    public Pharmacy checkLoginPharmacy(String mail,String password){
        Pharmacy pharmacyWithMail=pharmacyRepository.findByMailIgnoreCase(mail);
        if (pharmacyWithMail!=null){
            if (Objects.equals(pharmacyWithMail.getPassword(),password)){
                return pharmacyWithMail;
            }
        }
        return null;
    }
    //--------------------------------------------------------------------------------------------------
    //---------------------------DOCTOREEE--------------------------------------------------------------

    public Optional<Doctor> uploadPatientById(Integer p_id, Integer doc_id){
        return Optional.of(doctorRepository.findById(doc_id).map(doc->{
            Optional<Patient> search = patientRepository.findById(p_id);
            boolean found=doc.searchPatient(p_id);
            if(search.isPresent() && !found){
                doc.addPatient(search);
                search.get().addDoctor(doc);
                patientRepository.save(search.get());
                return doctorRepository.save(doc);
            }
            else if (found){
                throw new UserRegisteredException();
            }
            throw new UserNotFoundException(p_id);
        })).orElseThrow(()->new UserNotFoundException(doc_id));
    }

    public Optional<Patient> deleteDoctorPatientById(Integer patientID, Integer doctorID){
        return Optional.of(doctorRepository.findById(doctorID).map(doc->{
            Optional<Patient> search = patientRepository.findById(patientID);
            boolean found=doc.searchPatient(patientID);
            if(search.isPresent() && found){
                doc.removePatient(search);
                search.get().removeDoctor(doc);
                doctorRepository.save(doc);
                return patientRepository.save(search.get());
            }
            else if (found){
                throw new UserRegisteredException();
            }
            throw new UserNotFoundException(patientID);
        })).orElseThrow(()->new UserNotFoundException(doctorID));
    }


    //--------------------------REQUESTS-FROM-PATIENTS-TO-DOCTORS--------------------------------------


    public boolean addRecipe(Integer patientId,Integer docId, String drugName){
        Optional<Doctor> doc_entity = doctorRepository.findById(docId);
        Optional<Patient> patient_entity = patientRepository.findById(patientId);
        if(!doc_entity.isPresent() && !patient_entity.isPresent()){
            throw new UserNotFoundException();
        }
        else{
            Doctor doc = doc_entity.get();
            Patient p = patient_entity.get();
            if(!doc.HasPatient(p)){
                return false;
            }
            else{
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
    //-------------------------MAP DTOS---------------------------------------------------------------

    public DoctorForPatient DoctorWithUsernameID(Integer doctorID, String doctorUsername){
        return new DoctorForPatient(doctorID, doctorUsername);
    }

    //-------------------------REQUESTS-FROM-DOCTORS-TO-PATIENTS--------------------------------------
    public List<Patient> getAllPatients(Integer doctorID){
        return doctorRepository.findByPatients(doctorID);
    }

    //------------------------DOCTOR-FOR-PATIENTS-----------------------------------------------------
    public List<Doctor> getAllDoctorsFromPatient(Integer patientID){
        return patientRepository.findByDoctors(patientID);
    }
    //-------------------------FINDERS---------------------------------------------------------------
    public boolean findPatientByID(Integer id){
        return patientRepository.findById(id).isPresent();
    }


    /**----------------CREATE RECIPE-----------------------**/

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
    //------------------SEND RECIPE----------------------------------------------------------------------------

    public boolean sendRecipe(Integer recipeID, Integer pharmacyID){
        return true;
    }

    //-------------------------Metodos nuevos---------------------------------------------------------

    public List<RecipeDTO> findByRecipeStatusPatient(RecipeStatus status, Integer patientID){
        List<Recipe> recipes = recipeRepository.findByStatusAndID(status, patientID);
        List<RecipeDTO> answer = new ArrayList<>();
        for(Recipe r : recipes){
            answer.add(new RecipeDTO(r.getDocSignature(), r.getDrugName(),r.getRecipeID(),
                    r.getPatientID(),r.getDoctorID(),r.getPharmacyID(),
                    patientRepository.findById(r.getPatientID()).get().getUsername(),
                    doctorRepository.findById(r.getDoctorID()).get().getUsername()));
        }
        return answer;
    }

    public List<RecipeDTO> findByRecipeStatusDoctor(RecipeStatus status, Integer doctorID){
        List<Recipe> recipes = recipeRepository.findByStatusAndIDDoctor(status, doctorID);
        List<RecipeDTO> answer = new ArrayList<>();
        for(Recipe r : recipes){
            answer.add(new RecipeDTO(r.getDocSignature(), r.getDrugName(),r.getRecipeID(),
                    r.getPatientID(),r.getDoctorID(),r.getPharmacyID(),
                    patientRepository.findById(r.getPatientID()).get().getUsername(),
                    doctorRepository.findById(r.getDoctorID()).get().getUsername()));
        }
        return answer;
    }

    public boolean DeclineRecipe(Integer recipeID){
        Optional<Recipe> r = recipeRepository.findById(recipeID);
        if(!r.isPresent()){return false;}
        Recipe recipe = r.get();
        recipe.setStatus(RecipeStatus.DECLINED);
        recipeRepository.save(recipe);
        return true;
    }

    public List<pharmacyDTO> getAllPharmacys(){
        List<pharmacyDTO> pharmacyDTOList=new ArrayList<>();
        List<Pharmacy> pharmacies=pharmacyRepository.findAll();
        for (Pharmacy p: pharmacies) {
            pharmacyDTOList.add(new pharmacyDTO(p.getPrimarykey(),p.getUsername()));
        }
        return pharmacyDTOList;
    }

}