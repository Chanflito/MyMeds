package MyMeds.Controllers;

import MyMeds.App.Doctor;
import MyMeds.App.Patient;
import MyMeds.App.Pharmacy;
import MyMeds.Exceptions.UserRegisteredException;
import MyMeds.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@CrossOrigin
public class RegisterController {
    @Autowired
    UserService userService;
    @PostMapping("/savePharmacy")
    public ResponseEntity<?> savePharmacy(@RequestBody Pharmacy pharmacy) {
        if (this.userService.registerPharmacy(pharmacy) == null) {
            throw new UserRegisteredException();
        }
        return new ResponseEntity<>(pharmacy, HttpStatus.CREATED);
    }

    @PostMapping("/saveDoctor")//Recibe una Request con un username, password etc y crea un nuevo doctor en la base de datos.
    public ResponseEntity<?> saveDoctor(@RequestBody Doctor doctor) {
        if (this.userService.registerDoctor(doctor)==null){
            throw new UserRegisteredException();
        }
        return new ResponseEntity<>(doctor,HttpStatus.CREATED);
    }

    @PostMapping("/savePatient")
    public ResponseEntity<?> savePatient(@RequestBody Patient patient){
        if (this.userService.registerPatient(patient)==null){
            throw new UserRegisteredException();
        }
        return new ResponseEntity<>(patient,HttpStatus.CREATED);
    }
}
