package MyMeds.Controllers;

import MyMeds.App.Patient;
import MyMeds.Exceptions.UserNotFoundException;
import MyMeds.Interfaces.PatientRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PatientController {
    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository){
        this.patientRepository=patientRepository;
    }

    @GetMapping("/patient")
    List<Patient> all(){
        return  patientRepository.findAll();
    }

    @PostMapping("/patient")
    Patient newPatient(@RequestBody Patient patient){
        return patientRepository.save(patient);
    }

    @GetMapping("/patient/{primaryKey}")
    Patient getPatientById(@PathVariable Integer primaryKey){
        return patientRepository.findById(primaryKey).orElseThrow(
                ()->new UserNotFoundException(primaryKey)
        );
    }

    @DeleteMapping("/patient/{primaryKey}")
    void deletePatient(@PathVariable Integer primaryKey) {
        patientRepository.deleteById(primaryKey);
    }
}
