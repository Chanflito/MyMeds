package MyMeds.Controllers;

import MyMeds.App.Pharmacy;
import MyMeds.Exceptions.UserRegisteredException;
import MyMeds.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pharmacy")
@CrossOrigin
public class PharmacyController {
    @Autowired
    UserService userService;
    @GetMapping()
    public List<Pharmacy>getPharmacy(){
        return userService.getPharmacys();
    }

    @PostMapping
    public Pharmacy savePharmacy(@RequestBody Pharmacy pharmacy){
        if (this.userService.registerPharmacy(pharmacy)==null){
            throw new UserRegisteredException();
        }
        return pharmacy;
    }

    @GetMapping(path="/{id}")
    public Optional<Pharmacy> getPharmacyById(@PathVariable("id") Integer id){
        return this.userService.getPharmacyById(id);
    }

    @DeleteMapping("/{id}")
    public String deletePharmacyById(@PathVariable("id") Integer id){
        boolean founded=this.userService.deletePharmacyById(id);
        if (founded){
            return "User deleted with ID "+ id;
        }else{
            return "Can't delete user with ID "+ id;
        }
    }
}
