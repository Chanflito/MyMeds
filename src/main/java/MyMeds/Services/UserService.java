package MyMeds.Services;

import MyMeds.App.Doctor;
import MyMeds.App.Patient;
import MyMeds.App.Pharmacy;
import MyMeds.Exceptions.UserNotFoundException;
import MyMeds.Interfaces.DoctorRepository;
import MyMeds.Interfaces.PatientRepository;
import MyMeds.Interfaces.PharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired //No tengo que instnaciarlo spring ya lo sabe.
    DoctorRepository doctorRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    PharmacyRepository pharmacyRepository;

    public List<Doctor> getDoctors(){
        return doctorRepository.findAll();
    }

    public List<Patient> getPatients(){
        return  patientRepository.findAll();
    }

    public List<Pharmacy> getPharmacys(){
        return  pharmacyRepository.findAll();
    }

    public Doctor saveDoctor(Doctor doctor){
        return doctorRepository.save(doctor);
    }

    public Patient savePatient(Patient patient){
        return patientRepository.save(patient);
    }

    public Pharmacy savePharmacy(Pharmacy pharmacy){
        return pharmacyRepository.save(pharmacy);
    }

    public Optional<Doctor> getDoctorById(Integer id){
        return Optional.ofNullable(doctorRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }
    public Optional<Patient> getPatientById(Integer id){
        return Optional.ofNullable(patientRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }


    public Optional<Pharmacy> getPharmacyById(Integer id){
        return Optional.ofNullable(pharmacyRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

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
}
