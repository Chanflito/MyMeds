package MyMeds.Controllers;

import MyMeds.App.Pharmacy;
import MyMeds.App.RecipeStatus;
import MyMeds.Exceptions.UserRegisteredException;
import MyMeds.Services.RecipeService;
import MyMeds.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/pharmacy")
@CrossOrigin
public class PharmacyController {
    @Autowired
    RecipeService recipeService;
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
    public boolean deletePharmacyById(@PathVariable("id") Integer id){
        boolean founded=this.userService.deletePharmacyById(id);
        if (founded){
            return founded;
        }else{
            return founded;
        }
    }

    @GetMapping(path= "/getRecipesByStatus/{id}")
    public ResponseEntity<?> getRecipesByStatus(@PathVariable("id") Integer pharmacyID, @RequestParam("status")RecipeStatus status){
        List<RecipeService.recipeDTO> recipes = recipeService.findeByRecipeStatusPharmacy(status, pharmacyID);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @GetMapping(path="/tokenPharmacy")
    public ResponseEntity<?> checkToken(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path="/viewRecipeHistory/{id}")
    public ResponseEntity<?> viewRecipeHistory(@PathVariable("id") Integer pharmacyID){
        return new ResponseEntity<>(recipeService.findeAllRecipesForPharmacy(pharmacyID), HttpStatus.OK);
    }

    @PutMapping(path="/markRecipe/{id}")
    public ResponseEntity<?> markRecipe(@PathVariable("id")Integer recipeID){
        return new ResponseEntity<>(recipeService.markRecipe(recipeID), HttpStatus.OK);
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
