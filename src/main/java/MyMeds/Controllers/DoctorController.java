package MyMeds.Controllers;

import MyMeds.App.*;

import MyMeds.Dto.ApprovedRecipeData;
import MyMeds.Exceptions.UserRegisteredException;
import MyMeds.Services.RecipeService;
import MyMeds.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/doctor")
@CrossOrigin
public class DoctorController {
    @Autowired
    RecipeService recipeService;
    @Autowired//Instancia Spring el servicio.
    UserService userService;

    @GetMapping("/getDoctors")//Retorna todos los docotores que se encuentran en la base de datos en formato JSON
    public ResponseEntity<?> getDoctors() {
        return new ResponseEntity<>(userService.getDoctors(),HttpStatus.OK);
    }

    @PostMapping("/saveDoctor")//Recibe una Request con un username, password etc y crea un nuevo doctor en la base de datos.
    public ResponseEntity<?> saveDoctor(@RequestBody Doctor doctor) {
        if (this.userService.registerDoctor(doctor)==null){
            throw new UserRegisteredException();
        }
        return new ResponseEntity<>(doctor,HttpStatus.CREATED);
    }
    @GetMapping(path = "/getDoctorById/{id}")
//Busca los doctores en la base de datos mediante un ID y retorna el doctor en formato JSON con sus atributos.
    public ResponseEntity<?> getDoctorById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(this.userService.getDoctorById(id),HttpStatus.FOUND);
    }

    @DeleteMapping("/deleteDoctorById/{id}")
//Busca en la base de datos el doctor y lo borra, para hacer uso de la misma hay que hacer un DELETE en la url /doctor/id
    public String deleteDoctorById(@PathVariable("id") Integer id) {
        boolean founded = this.userService.deleteDoctorById(id);
        if (founded) {
            return "User deleted with ID " + id;
        } else {
            return "Can't delete user with ID " + id;
        }
    }
    @GetMapping("/listpatients/{id}")
    public ResponseEntity<?> getPatientList(@PathVariable("id") Integer id){
        return new ResponseEntity<>(userService.getAllPatientsFromDoctor(id),HttpStatus.FOUND);
    }

    @PutMapping("/addpatient/{id}")
    public ResponseEntity<Optional<Doctor>> AddPatientById(@PathVariable("id")Integer doc_id, @RequestBody Integer p_id){
        return new ResponseEntity<>(userService.uploadPatientById(p_id, doc_id),HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/listpatients/{id}")
    public ResponseEntity<?> removePatientByID(@PathVariable("id") Integer doctorID,@RequestBody Integer patientID){
        return new ResponseEntity<>(this.userService.deleteDoctorPatientById(patientID,doctorID),HttpStatus.OK);
    }
    @GetMapping(path="/getPatientById/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable("id") Integer id){
        return new ResponseEntity<>(userService.getPatientById(id),HttpStatus.FOUND);
    }
    @GetMapping(path="/tokenDoctor")
    public ResponseEntity<?> checkToken(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path="/viewRecipes/{id}")
    public ResponseEntity<?> viewRecipes(@PathVariable("id") Integer doctorID, @RequestParam("status") RecipeStatus status){
        List<RecipeService.recipeDTO> recipes = recipeService.findByRecipeStatusDoctor(status, doctorID);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @PutMapping(path = "/AproveRecipe/{id}")
    public ResponseEntity<?>AproveAndSendRecipe(@PathVariable("id") Integer doctorID,@RequestBody ApprovedRecipeData requested){
        boolean response = recipeService.createRecipe(doctorID, requested);
        if (response){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(path = "/DeclineRecipe/{recipeID}")
    public ResponseEntity<?> DeclineRecipe(@PathVariable("recipeID") Integer recipeID){
        boolean done = recipeService.DeclineRecipe(recipeID);
        if(done){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path="/getAllPharmacys")
    public ResponseEntity<?> getPharmacys(){
        return new ResponseEntity<>(userService.getAllPharmacys(),HttpStatus.OK);
    }

}