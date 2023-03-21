package MyMeds.Controllers;

import MyMeds.App.Patient;
import MyMeds.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    UserService userService;
    @GetMapping()
    public List<Patient> getPatients(){
        return userService.getPatients();
    }

    @PostMapping
    public Patient savePatient(@RequestBody Patient patient){
        return this.userService.savePatient(patient);
    }

    @GetMapping(path="/{id}")
    public Optional<Patient> getPatientById(@PathVariable("id") Integer id){
        return this.userService.getPatientById(id);
    }

    @DeleteMapping("/{id}")
    public String deletePatientById(@PathVariable("id") Integer id){
        boolean founded=this.userService.deletePatientById(id);
        if (founded){
            return "User deleted with ID"+ id;
        }else{
            return "Can't delete user with ID"+ id;
        }
    }
}
