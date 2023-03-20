package MyMeds.Controllers;

import MyMeds.App.Pharmacy;
import MyMeds.Exceptions.UserNotFoundException;
import MyMeds.Interfaces.PharmacyRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class PharmacyController {
    private final PharmacyRepository pharmacyRepository;

    public PharmacyController(PharmacyRepository pharmacyRepository){
        this.pharmacyRepository=pharmacyRepository;
    }
    @GetMapping("/pharmacy")
    List<Pharmacy> all(){
        return  pharmacyRepository.findAll();
    }
    @PostMapping("/pharmacy")
    Pharmacy pharmacy(@RequestBody Pharmacy pharmacy){
        return pharmacyRepository.save(pharmacy);
    }
    @GetMapping("/pharmacy/{primaryKey}")
    Pharmacy getPharmacyById(@PathVariable Integer primaryKey){
        return pharmacyRepository.findById(primaryKey).orElseThrow(
                ()->new UserNotFoundException(primaryKey)
        );
    }
    @DeleteMapping("/pharmacy/{primaryKey}")
    void deletePharmacy(@PathVariable Integer primaryKey) {
        pharmacyRepository.deleteById(primaryKey);
    }
}
