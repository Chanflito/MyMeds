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
        Patient patientWithPassword=patientRepository.findByPassword(password);
        if (patientWithMail!=null && patientWithPassword!=null){
            if (Objects.equals(patientWithMail.getPrimarykey(), patientWithPassword.getPrimarykey())){
                return patientWithMail;
            }
        }
        return null;
    }

    public Doctor checkLoginDoctor(String mail,String password){
        Doctor doctorWithMail=doctorRepository.findByMailIgnoreCase(mail);
        Doctor doctorWithPassword=doctorRepository.findByPassword(password);
        if (doctorWithMail!=null && doctorWithPassword!=null){
            if (Objects.equals(doctorWithMail.getPrimarykey(), doctorWithPassword.getPrimarykey())){
                return doctorWithMail;
            }
        }
        return null;
    }

    public Pharmacy checkLoginPharmacy(String mail,String password){
        Pharmacy pharmacyWithMail=pharmacyRepository.findByMailIgnoreCase(mail);
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
        if (!requestRepository.existsById(id)){
            return false;
        }
        requestRepository.deleteById(id);
        return true;
    }

    public boolean addRequest(Integer patientId,Integer docId, String drugName){
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
                Request req = new Request(doc.getUsername(), p.getUsername(), drugName);
                requestRepository.save(req);
                Request req2 = requestRepository.findById(req.getPrimaryKey()).get();
                req2.setDoctor(doc);
                doc.addRequest(req2);
                requestRepository.save(req2);
                doctorRepository.save(doc);
                return true;
            }
        }

    }
    //-------------------------REQUESTS-FROM-DOCTORS-TO-PATIENTS--------------------------------------
    public List<Patient> getAllPatients(Integer doctorID){
        return doctorRepository.findByPatients(doctorID);
    }
    //-------------------------Token Manage-----------------------------------------------------------
    //Primero busca el paciente en la base de datos, si lo encuentra
    //verifica si el token que tiene ese paciente es el mismo con el que tiene en el localStorage, si es el mismo retorna true
    //otherwise, false.

}