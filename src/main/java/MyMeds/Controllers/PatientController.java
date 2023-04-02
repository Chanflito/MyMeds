package MyMeds.Controllers;

import MyMeds.App.Patient;
import MyMeds.App.Request;
import MyMeds.Exceptions.UserRegisteredException;
import MyMeds.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patient")
@CrossOrigin
public class PatientController {
    @Autowired
    UserService userService;
    @GetMapping()
    public List<Patient> getPatients(){
        return userService.getPatients();
    }

    @PostMapping
    public Patient savePatient(@RequestBody Patient patient){
        if (this.userService.registerPatient(patient)==null){
            throw new UserRegisteredException();
        }
        return patient;
    }

    @GetMapping(path="/{id}")
    public Optional<Patient> getPatientById(@PathVariable("id") Integer id){
        return this.userService.getPatientById(id);
    }

    @DeleteMapping("/{id}")
    public String deletePatientById(@PathVariable("id") Integer id){
        boolean founded=this.userService.deletePatientById(id);
        if (founded){
            return "User deleted by ID"+ id;
        }else{
            return "Can't delete user by ID"+ id;
        }
    }
    //Podemos cambiarle la contraseña al paciente.
    @PutMapping("/{id}")
    public Optional<Patient> changePatientPassword(@PathVariable("id") Integer id, @RequestBody Patient patient){
        return this.userService.changePatientPasswordByID(id,patient);
    }
    //Añadimos una obra social al paciente.
    @PutMapping("/{id}/addInsurance")
    public Optional<Patient> addHealthInsurance(@PathVariable("id") Integer id,@RequestBody Integer healthInsurance){
        return this.userService.addHealthInsuranceById(healthInsurance,id);
    }

    @PutMapping("/{id}/makeRequest")
    public String makeRequestToDoctorById(@PathVariable Integer id,@RequestBody Integer docId, @RequestBody String drugName){
        //If isDone == false, doctor does not have a signature
        boolean isDone = this.userService.addRequest(id,docId, drugName);
        if(isDone){
            return "Request from patient" + id + " has been done to doctor " + docId + "for " + drugName;
        }
        else{
            return "Doctor has no signature yet...";
        }
    }
}
