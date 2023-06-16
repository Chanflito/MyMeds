package MyMeds.Controllers;

import MyMeds.App.Drug;
import MyMeds.App.Pharmacy;
import MyMeds.App.RecipeStatus;
import MyMeds.Dto.DrugStockDTO;
import MyMeds.Exceptions.UserRegisteredException;
import MyMeds.Repositorys.DrugRepository;
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

    @GetMapping(path = "/getAllDrugsFromFDA")
    public ResponseEntity<?> getAllDrugsFromFda() {
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

    @PostMapping(path="/addDrugToPharmacyAndMyMeds/{id}")//Agrega la droga al stock de farmacia y a myMeds.
    public ResponseEntity<?> addDrugToPharmacyAndMyMeds(@PathVariable("id") Integer pharmacyID,
                                               @RequestBody DrugService.pharmacyDrugDTO drugDTO){
        boolean response= drugService.addDrugToPharmacyAndMyMeds(pharmacyID, drugDTO);
        if (response){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping(path="/getAllDrugsFromMyMeds")
    public ResponseEntity<?> getAllDrugsFromMeds(){
        return new ResponseEntity<>(drugService.getAllDrugsFromMyMeds(),HttpStatus.OK);
    }

    @PutMapping(path = "/addDrugToPharmacy/{id}") //Agregamos droga existente en el sistema de MyMeds a una farmacia y le asignamos su stock.
    public ResponseEntity<?> addDrugToPharmacy(@PathVariable("id")Integer pharmacyID,
                                               @RequestParam("drugID") Integer drugID,@RequestParam("stock") Integer stock){
        boolean response= drugService.addDrugToPharmacy(pharmacyID, drugID, stock);
        if (response){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/getAllDrugsFromPharmacy/{id}")
    public ResponseEntity<?>getAllDrugsFromPharmacy(@PathVariable("id") Integer pharmacyID){
        List<DrugStockDTO> drugStockDTOS=drugService.getAllDrugsByPharmacyID(pharmacyID);
        if (drugStockDTOS.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(drugStockDTOS,HttpStatus.OK);
    }

    @PutMapping(path="/setStockForDrugOfPharmacy/{id}")
    public ResponseEntity<?> setStock(@PathVariable("id")Integer pharmacyID,
                                      @RequestParam("stock") Integer stock,@RequestParam("drugID") Integer drugID) {
        boolean response = drugService.setDrugStockByPharmacyID(pharmacyID, stock, drugID);
        if (response) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping(path="/verifyByQr/{scanResult}")
    public ResponseEntity<?> verifyByQr(@PathVariable("scanResult") String scanResult){
        System.out.println(Integer.valueOf(scanResult));
        if(recipeService.Exists(Integer.valueOf(scanResult))){
            return new ResponseEntity<>(true, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);

    }
}
