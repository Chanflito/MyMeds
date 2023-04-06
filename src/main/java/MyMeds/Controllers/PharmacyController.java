package MyMeds.Controllers;

import MyMeds.App.Pharmacy;
import MyMeds.Exceptions.UserRegisteredException;
import MyMeds.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pharmacy")
@CrossOrigin
public class PharmacyController {
    @Autowired
    UserService userService;
    @GetMapping("/getPharmacy")
    public ResponseEntity<?> getPharmacy(){
        return new ResponseEntity<>(userService.getPharmacys(), HttpStatus.FOUND);
    }

    @PostMapping("/savePharmacy")
    public ResponseEntity<?> savePharmacy(@RequestBody Pharmacy pharmacy){
        if (this.userService.registerPharmacy(pharmacy)==null){
            throw new UserRegisteredException();
        }
        return new ResponseEntity<>(pharmacy,HttpStatus.CREATED);
    }

    @GetMapping(path="/getPharmacyById/{id}")
    public ResponseEntity<?> getPharmacyById(@PathVariable("id") Integer id){
        return new ResponseEntity<>(this.userService.getPharmacyById(id),HttpStatus.FOUND);
    }

    @DeleteMapping("/deletePharmacyById/{id}")//Destructive method bullshit.
    public String deletePharmacyById(@PathVariable("id") Integer id){
        boolean founded=this.userService.deletePharmacyById(id);
        if (founded){
            return "User deleted with ID "+ id;
        }else{
            return "Can't delete user with ID "+ id;
        }
    }
}
