package MyMeds.Controllers;

import MyMeds.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
@CrossOrigin
public class TokenController {
    @Autowired
    UserService userService;
    //Si alguno de los 3 es true, quiere decir que el token sigue siendo el mismo, de lo contrario va a retornar false
    //Si los 3 es false, me redirecciona al login.
    @GetMapping(path = {"/{id}/checkToken"})
    public boolean checkUserToken(@PathVariable Integer id,@RequestBody String token){
        Boolean doctorToken=userService.checkDoctorTokenById(id,token);
        Boolean patientToken=userService.checkPatientTokenById(id,token);
        Boolean pharmacyToken=userService.checkPharmacyTokenById(id,token);
        return doctorToken || patientToken || pharmacyToken;
    }
    //Si retorna true, es porque logro cambiar el token del usuario. Si llega a retornar false, no logra cambiar el token.
    @PutMapping(path = {"/{id}"})
    public boolean changeUserToken(@PathVariable Integer id){
        Boolean doctorToken=userService.changeDoctorToken(id);
        Boolean patientToken=userService.changePatientToken(id);
        Boolean pharmacyToken=userService.changePharmacyToken(id);
        return doctorToken||patientToken||pharmacyToken;
    }
}
