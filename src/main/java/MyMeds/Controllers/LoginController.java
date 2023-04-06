package MyMeds.Controllers;

import MyMeds.App.AccountCreateRequest;
import MyMeds.App.Doctor;
import MyMeds.App.Patient;
import MyMeds.App.Pharmacy;
import MyMeds.Exceptions.UserNotFoundException;
import MyMeds.Services.UserService;
import MyMeds.jwt.JwtGenerator;
import MyMeds.jwt.JwtGeneratorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {
    private final UserService userService;
    private final JwtGenerator jwtGenerator;
    @Autowired
    public LoginController(UserService userService,JwtGenerator jwtGenerator){
        this.userService=userService;
        this.jwtGenerator=jwtGenerator;
    }
    //Una vez que se logea, genera un token.
    @PostMapping
    public ResponseEntity<?> checkUser(@RequestBody AccountCreateRequest request){
        Doctor doctor=userService.checkLoginDoctor(request.getMail(), request.getPassword());
        Patient patient=userService.checkLoginPatient(request.getMail(), request.getPassword());
        Pharmacy pharmacy=userService.checkLoginPharmacy(request.getMail(), request.getPassword());
        try{
            if (patient!=null){
                return new ResponseEntity<>(jwtGenerator.generateToken(patient),HttpStatus.CREATED);
            }
            if (doctor!=null){
                return new ResponseEntity<>(jwtGenerator.generateToken(doctor),HttpStatus.CREATED);
            }
            if (pharmacy!=null){
                return new ResponseEntity<>(jwtGenerator.generateToken(pharmacy),HttpStatus.CREATED);
            }
            else{
                throw new UserNotFoundException();
            }} catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

}
