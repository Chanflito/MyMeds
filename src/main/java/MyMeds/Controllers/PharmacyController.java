package MyMeds.Controllers;


import MyMeds.App.Pharmacy;
import MyMeds.App.RecipeStatus;
import MyMeds.Dto.CreateRecipeResponse;
import MyMeds.Dto.DrugStockDTO;
import MyMeds.Dto.RecipePageDTO;
import MyMeds.Exceptions.InvalidJsonException;
import MyMeds.Exceptions.UserRegisteredException;
import MyMeds.Services.DrugService;
import MyMeds.Services.RecipeService;
import MyMeds.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    //Modificar este metodo mañana, agregarle el paginado.
    @GetMapping(path = "/getRecipesByStatus/{id}")
    public ResponseEntity<?> getRecipesByStatus(@PathVariable("id") Integer pharmacyID,
                                                @RequestParam(value = "status",required = false) RecipeStatus status,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "10") int size,@RequestParam(value ="patientID",required = false)Integer patientID,
                                                @RequestParam(value = "doctorUsername",required = false)String doctorUsername) {
        Pageable pageable = PageRequest.of(page, size);
        List<RecipeService.recipeDTO> recipes=recipeService.findByRecipeStatusPharmacy(status,pharmacyID,patientID,doctorUsername,pageable);
        int totalRecipes= recipeService.countByRecipeStatusPharmacy(status, pharmacyID,patientID,doctorUsername);
        int totalPages = (int) Math.ceil((double) totalRecipes / size);
        RecipePageDTO recipePageDTO=new RecipePageDTO(recipes,totalPages);
        return new ResponseEntity<>(recipePageDTO, HttpStatus.OK);
    }

    @GetMapping(path = "/tokenPharmacy")
    public ResponseEntity<?> checkToken() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/viewRecipeHistory/{id}")
    public ResponseEntity<?> viewRecipeHistory(@PathVariable("id") Integer pharmacyID,@RequestParam("status")RecipeStatus status) {
        return new ResponseEntity<>(recipeService.findeAllRecipesForPharmacy(pharmacyID,status), HttpStatus.OK);
    }

    @PutMapping(path = "/markRecipe/{id}")
    public ResponseEntity<?> markRecipe(@PathVariable("id") Integer recipeID,@RequestParam("pharmacyID") Integer pharmacyID) {
        CreateRecipeResponse response= recipeService.markRecipe(recipeID,pharmacyID);
        //Si no contiene stock, se menciona que drogas no tienen stock.
        if (!response.isSucess() && !response.getDrugDTOS().isEmpty()){
            return new ResponseEntity<>(response,HttpStatus.CONFLICT);
        }
        //De lo contrario, retorna un 200.
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/rejectRecipe/{id}")
    public ResponseEntity<?> rejectRecipe(@PathVariable("id") Integer recipeID){
        boolean response=recipeService.rejectRecipeAsPharmacy(recipeID);
        if (response){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
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

    @GetMapping(path = "/getDrugNameFromFDA/{brandName}")
    public ResponseEntity<?> getDrugName(@PathVariable("brandName")String brandName) throws IOException {
        try {
            List<String> fdaDrugDTOS = drugService.filterDrugInFDAByBrandName(brandName);
            if (!fdaDrugDTOS.isEmpty()) {
                return new ResponseEntity<>(fdaDrugDTOS, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            return new ResponseEntity<>("Brand name not found", HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping(path="/addDrugToPharmacy/{id}")//Agrega la droga al stock de farmacia y a myMeds.
    public ResponseEntity<?> addDrugToPharmacyAndMyMeds(@PathVariable("id") Integer pharmacyID,
                                               @RequestBody DrugService.fdaDrugDTO drugDTO){
        boolean response= drugService.addDrugToPharmacy(pharmacyID, drugDTO);
        if (response){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping(path="/getAllDrugsFromMyMeds")
    public ResponseEntity<?> getAllDrugsFromMeds(){
        return new ResponseEntity<>(drugService.getAllDrugsFromMyMeds(),HttpStatus.OK);
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
        RecipeService.recipeDTO result=recipeService.Exists(Integer.valueOf(scanResult));
        return result!=null ? new ResponseEntity<>(result,HttpStatus.OK): new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(path = "/loadMassiveStock/{pharmacyID}")
    public ResponseEntity<?> loadMassiveStock(@PathVariable("pharmacyID")Integer pharmacyID,
                                              @RequestBody List<DrugService.pharmacyDrugDTO> pharmacyDrugDTOS){
        try {
            drugService.loadMassiveStock(pharmacyID, pharmacyDrugDTOS);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidJsonException e) {
            return new ResponseEntity<>("Invalid JSON data", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path="/deleteDrugStock/{pharmacyID}")
    public ResponseEntity<?> deleteDrugFromStockPharmacy(@PathVariable("pharmacyID") Integer pharmacyID,
                                                         @RequestParam("drugID") Integer drugID){
        boolean response= drugService.deleteDrugInPharmacy(pharmacyID,drugID);
        if (response){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
