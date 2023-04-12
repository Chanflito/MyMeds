package MyMeds.Controllers;

import MyMeds.App.Patient;
import MyMeds.App.Request;
import MyMeds.App.RequestData;
import MyMeds.Exceptions.UserRegisteredException;
import MyMeds.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patient")
@CrossOrigin
public class PatientController {
    @Autowired
    UserService userService;
    @GetMapping("/getPatients")
    public ResponseEntity<?> getPatients(){
        return new ResponseEntity<>(userService.getPatients(), HttpStatus.FOUND);
    }

    @PostMapping("/savePatient")
    public ResponseEntity<?> savePatient(@RequestBody Patient patient){
        if (this.userService.registerPatient(patient)==null){
            throw new UserRegisteredException();
        }
        return new ResponseEntity<>(patient,HttpStatus.CREATED);
    }

    @GetMapping(path="/getPatientById/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable("id") Integer id){
        return new ResponseEntity<>(userService.getPatientById(id),HttpStatus.FOUND);
    }

    @DeleteMapping("/deletePatientById/{id}")
    public String deletePatientById(@PathVariable("id") Integer id){
        boolean founded=this.userService.deletePatientById(id);
        if (founded){
            return "User deleted by ID"+ id;
        }else{
            return "Can't delete user by ID"+ id;
        }
    }
    //Podemos cambiarle la contraseña al paciente.
    @PutMapping("/changePatientPassword/{id}")
    public ResponseEntity<?> changePatientPassword(@PathVariable("id") Integer id, @RequestBody Patient patient){
        return new ResponseEntity<>(this.userService.changePatientPasswordByID(id,patient),HttpStatus.OK);
    }
    //Añadimos una obra social al paciente.
    @PutMapping("/{id}/addInsurance")
    public ResponseEntity<?> addHealthInsurance(@PathVariable("id") Integer id,@RequestBody Integer healthInsurance){
        return new ResponseEntity<>(this.userService.addHealthInsuranceById(healthInsurance,id),HttpStatus.OK);
    }

    @PutMapping("/{id}/makeRequest")
    public ResponseEntity<?> makeRequestToDoctorById(@PathVariable Integer id,@RequestBody RequestData data){
        //If isDone == false, doctor does not have a signature
        boolean isDone = this.userService.addRequest(id,data.getDocId(), data.getDrugName());
        if (isDone){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping(path="/tokenPatient")
    public ResponseEntity<?> checkToken(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
