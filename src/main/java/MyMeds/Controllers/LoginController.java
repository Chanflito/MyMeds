package MyMeds.Controllers;

import MyMeds.App.AccountCreateRequest;
import MyMeds.App.Doctor;
import MyMeds.App.Patient;
import MyMeds.App.Pharmacy;
import MyMeds.Exceptions.UserNotFoundException;
import MyMeds.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {
    @Autowired
    UserService userService;
    //Verificamos si el usuario existe en nuestra base de datos, de lo contrario retornamos excepcion.
    @PostMapping
    public Object checkUser(@RequestBody AccountCreateRequest request){
        Doctor doctor=userService.checkLoginDoctor(request.getMail(), request.getPassword());
        Patient patient=userService.checkLoginPatient(request.getMail(), request.getPassword());
        Pharmacy pharmacy=userService.checkLoginPharmacy(request.getMail(), request.getPassword());
        if (patient!=null){
            return patient;
        }
        if (doctor!=null){
            return doctor;
        }
        if (pharmacy!=null){
            return pharmacy;
        }
        else{
            throw new UserNotFoundException();
        }}
}
