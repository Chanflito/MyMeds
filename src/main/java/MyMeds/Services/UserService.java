package MyMeds.Services;

import MyMeds.App.*;
import MyMeds.Dto.ApprovedRecipeData;
import MyMeds.Dto.DoctorForPatient;
import MyMeds.Dto.RecipeDTO;
import MyMeds.Exceptions.UserNotFoundException;
import MyMeds.Exceptions.UserRegisteredException;
import MyMeds.Repositorys.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    //------------------------------------ENTITY-GETTERS------------------------------------------------------------
    public List<Doctor> getDoctors(){
        return doctorRepository.findAll();
    }

    public List<Patient> getPatients(){
        return  patientRepository.findAll();
    }

    public List<Pharmacy> getPharmacys(){
        return  pharmacyRepository.findAll();
    }

    public List<Patient> getAllPatients(Integer doctorID){
        return doctorRepository.findByPatients(doctorID);
    }

    public List<doctorDTO> getAllDoctorsFromPatient(Integer patientID){
        if(!this.findPatientByID(patientID)){
            return null;
        }
        List<Doctor> doctors = patientRepository.findDoctors(patientID);
        List<doctorDTO> answer = new ArrayList<>();
        for(Doctor doctor : doctors){
            answer.add(new doctorDTO(doctor.getPrimarykey(), doctor.getUsername()));
        }
        return answer;
    }

    public List<pharmacyDTO> getAllPharmacys(){
        List<pharmacyDTO> pharmacyDTOList=new ArrayList<>();
        List<Pharmacy> pharmacies=pharmacyRepository.findAll();
        for (Pharmacy p: pharmacies) {
            pharmacyDTOList.add(new pharmacyDTO(p.getPrimarykey(),p.getUsername()));
        }
        return pharmacyDTOList;
    }

    //GETBYID
    public Optional<Doctor> getDoctorById(Integer id){
        return Optional.ofNullable(doctorRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }
    public Optional<Patient> getPatientById(Integer id){
        return Optional.ofNullable(patientRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    public Optional<Pharmacy> getPharmacyById(Integer id){
        return Optional.ofNullable(pharmacyRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }




    //------------------------------DELETE--------------------------------------------------------------

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

    //-------------------------------------------------SETTERS--------------------------------------------

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

    //---------------REGISTER-----------------------------------------------------------------------------
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

    //--------------------LOGIN PART-------------------------------------------------------------------

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


    //---------------------------il-Doctore-Functions------------------------------------------------------

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

    //-------------------------MAP-DTOS---------------------------------------------------------------

    public record doctorDTO(Integer doctorID, String doctorUsername){}

    public record pharmacyDTO(Integer pharmacyID,String pharmacyName){}
    //Utiliza el repositorio para buscar los doctores en la base de datos.
    //Record hace exactamente lo mismo


    //-------------------------FINDERS---------------------------------------------------------------
    public boolean findPatientByID(Integer id){
        return patientRepository.findById(id).isPresent();
    }






}