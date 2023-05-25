package MyMeds.Controllers;

import MyMeds.App.Pharmacy;
import MyMeds.App.RecipeStatus;
import MyMeds.Exceptions.UserRegisteredException;
import MyMeds.Services.DrugService;
import MyMeds.Services.RecipeService;
import MyMeds.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/pharmacy")
@CrossOrigin
public class PharmacyController {
    @Autowired
    RecipeService recipeService;
    @Autowired
    UserService userService;
    @Autowired
    DrugService drugService;

    @GetMapping("/getPharmacy")
    public ResponseEntity<?> getPharmacy() {
        return new ResponseEntity<>(userService.getPharmacys(), HttpStatus.FOUND);
    }

    @PostMapping("/savePharmacy")
    public ResponseEntity<?> savePharmacy(@RequestBody Pharmacy pharmacy) {
        if (this.userService.registerPharmacy(pharmacy) == null) {
            throw new UserRegisteredException();
        }
        return new ResponseEntity<>(pharmacy, HttpStatus.CREATED);
    }

    @GetMapping(path = "/getPharmacyById/{id}")
    public ResponseEntity<?> getPharmacyById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(this.userService.getPharmacyById(id), HttpStatus.FOUND);
    }

    @DeleteMapping("/deletePharmacyById/{id}")//Destructive method bullshit.
    public boolean deletePharmacyById(@PathVariable("id") Integer id) {
        boolean founded = this.userService.deletePharmacyById(id);
        if (founded) {
            return founded;
        } else {
            return founded;
        }
    }

    @GetMapping(path = "/getRecipesByStatus/{id}")
    public ResponseEntity<?> getRecipesByStatus(@PathVariable("id") Integer pharmacyID, @RequestParam("status") RecipeStatus status) {
        List<RecipeService.recipeDTO> recipes = recipeService.findeByRecipeStatusPharmacy(status, pharmacyID);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @GetMapping(path = "/tokenPharmacy")
    public ResponseEntity<?> checkToken() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/viewRecipeHistory/{id}")
    public ResponseEntity<?> viewRecipeHistory(@PathVariable("id") Integer pharmacyID) {
        return new ResponseEntity<>(recipeService.findeAllRecipesForPharmacy(pharmacyID), HttpStatus.OK);
    }

    @PutMapping(path = "/markRecipe/{id}")
    public ResponseEntity<?> markRecipe(@PathVariable("id") Integer recipeID) {
        return new ResponseEntity<>(recipeService.markRecipe(recipeID), HttpStatus.OK);
    }

    @GetMapping(path = "/getAllDrugs/")
    public ResponseEntity<?> getAllDrugs() {
        List<String> drugList = drugService.getAllDrugsFromFDA();
        if (drugList != null) {
            return new ResponseEntity<>(drugList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/getDetailsForDrug/{brandName}")
    public ResponseEntity<?>getDetailsForDrug(@PathVariable("brandName")String brandName) throws IOException {
        try {
            List<DrugService.fdaDrugDTO> fdaDrugDTOS = drugService.getDetailsFromBrandNameDrug(brandName);
            if (!fdaDrugDTOS.isEmpty()) {
                return new ResponseEntity<>(fdaDrugDTOS, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("Brand name not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path="/addDrugToPharmacy/{id}")
    public ResponseEntity<?> addDrugToPharmacy(@PathVariable("id") Integer pharmacyID,
                                               @RequestBody DrugService.pharmacyDrugDTO drugDTO){
        boolean response= drugService.addDrugToPharmacy(pharmacyID, drugDTO);
        if (response){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
