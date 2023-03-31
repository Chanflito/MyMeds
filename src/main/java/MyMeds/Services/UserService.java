package MyMeds.Services;

import MyMeds.App.*;
import MyMeds.Exceptions.UserNotFoundException;
import MyMeds.Exceptions.UserRegisteredException;
import MyMeds.Repositorys.DoctorRepository;
import MyMeds.Repositorys.PatientRepository;
import MyMeds.Repositorys.PharmacyRepository;
import MyMeds.Repositorys.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired //No tengo que instnaciarlo spring ya lo sabe.
    DoctorRepository doctorRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    PharmacyRepository pharmacyRepository;
    @Autowired
    RequestRepository requestRepository;
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
        if (doctorRepository.findById(doctor.getPrimarykey()).isEmpty() && doctorRepository.findByMail(doctor.getMail())==null){
            return doctorRepository.save(doctor);
        }
        return null;
    }

    public Patient registerPatient(Patient patient){
        if (patientRepository.findById(patient.getPrimarykey()).isEmpty() && patientRepository.findByMail(patient.getMail())==null){
            return patientRepository.save(patient);
        }
        return null;
    }

    public Pharmacy registerPharmacy(Pharmacy pharmacy){
        if (pharmacyRepository.findById(pharmacy.getPrimarykey()).isEmpty() && pharmacyRepository.findByMail(pharmacy.getMail())==null){
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
        Patient patientWithMail=patientRepository.findByMail(mail);
        Patient patientWithPassword=patientRepository.findByPassword(password);
        if (patientWithMail!=null && patientWithPassword!=null){
            if (Objects.equals(patientWithMail.getPrimarykey(), patientWithPassword.getPrimarykey())){
                return patientWithMail;
            }
        }
        return null;
    }

    public Doctor checkLoginDoctor(String mail,String password){
        Doctor doctorWithMail=doctorRepository.findByMail(mail);
        Doctor doctorWithPassword=doctorRepository.findByPassword(password);
        if (doctorWithMail!=null && doctorWithPassword!=null){
            if (Objects.equals(doctorWithMail.getPrimarykey(), doctorWithPassword.getPrimarykey())){
                return doctorWithMail;
            }
        }
        return null;
    }

    public Pharmacy checkLoginPharmacy(String mail,String password){
        Pharmacy pharmacyWithMail=pharmacyRepository.findByMail(mail);
        Pharmacy pharmacyWithPassword=pharmacyRepository.findByPassword(password);
        if (pharmacyWithMail!=null && pharmacyWithPassword!=null){
            if (Objects.equals(pharmacyWithMail.getPrimarykey(),pharmacyWithPassword.getPrimarykey())){
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

    //--------------------------REQUESTS-FROM-PATIENTS-TO-DOCTORS--------------------------------------

    public List<Request> getRequests(){
        return requestRepository.findAll();
    }

    public Optional<Request> getRequestById(Integer id){
        return Optional.ofNullable(requestRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    public Request registerRequest(Request req){
        //If it finds an existing request with the same id, retuns null
        if(requestRepository.findById(req.getPrimaryKey()).isEmpty()){
            return requestRepository.save(req);
        }
        return null;
    }

    public boolean deleteRequestById(Integer id){
        return false;
    }
    //-------------------------REQUESTS-FROM-DOCTORS-TO-PATIENTS--------------------------------------
    public List<Patient> getAllPatients(Integer doctorID){
        return doctorRepository.findByPatients(doctorID);
    }
    //-------------------------Token Manage-----------------------------------------------------------
    //Primero busca el paciente en la base de datos, si lo encuentra
    //verifica si el token que tiene ese paciente es el mismo con el que tiene en el localStorage, si es el mismo retorna true
    //otherwise, false.
    public boolean checkPatientTokenById(Integer patientID, String token){
        Optional<Patient> patientFound=patientRepository.findById(patientID);
        return patientFound.filter(patient -> Objects.equals(patient.getToken(), token)).isPresent();
    }


    public boolean checkDoctorTokenById(Integer doctorID, String token){
        Optional<Doctor> doctorFound=doctorRepository.findById(doctorID);
        return doctorFound.filter(doctor -> Objects.equals(doctor.getToken(), token)).isPresent();
    }

    public boolean checkPharmacyTokenById (Integer pharmacyID, String token){
        Optional<Pharmacy> pharmacyFound=pharmacyRepository.findById(pharmacyID);
        return pharmacyFound.filter(pharmacy -> Objects.equals(pharmacy.getToken(), token)).isPresent();
    };
    //Cambiamos el token del usuario una vez que hace logout, debemos llamar a esta funcion.
    public boolean changePatientToken(Integer userID){
        Optional<Patient> patientFound=patientRepository.findById(userID);
        if (patientFound.isPresent()){
            patientFound.get().setToken(UUID.randomUUID().toString());
            patientRepository.save(patientFound.get());
            return true;
        }
        return false;
    }

    public boolean changePharmacyToken(Integer userID){
        Optional<Pharmacy> pharmacyFound=pharmacyRepository.findById(userID);
        if (pharmacyFound.isPresent()){
            pharmacyFound.get().setToken(UUID.randomUUID().toString());
            pharmacyRepository.save(pharmacyFound.get());
            return true;
        }
        return false;
    }
    public boolean changeDoctorToken(Integer userID){
        Optional<Doctor> doctorFound=doctorRepository.findById(userID);
        if (doctorFound.isPresent()){
            doctorFound.get().setToken(UUID.randomUUID().toString());
            doctorRepository.save(doctorFound.get());
            return true;
        }
        return false;
    }
}