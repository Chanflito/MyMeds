package MyMeds.Controllers;

import MyMeds.App.*;

import MyMeds.Exceptions.UserRegisteredException;
import MyMeds.Services.DrugService;
import MyMeds.Services.RecipeService;
import MyMeds.Services.UserService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @Autowired
    DrugService drugService;
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

    @GetMapping(path = "/viewRecipes/{id}")
    public ResponseEntity<?> viewRecipesByIndex(@PathVariable("id")Integer doctorID,
                                                @RequestParam(value = "status",required = false) RecipeStatus status,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "10") int size,@RequestParam(value = "patientID",required = false)
                                                Integer patientID){
        Pageable pageable = PageRequest.of(page, size);
        List<RecipeService.recipeDTO> recipes=recipeService.findByRecipeStatusDoctor(status,doctorID,pageable,patientID);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }
    @PutMapping(path = "/AproveRecipe/{id}")
    public ResponseEntity<?>AproveAndSendRecipe(@PathVariable("id") Integer doctorID,@RequestParam("recipeID")Integer recipeID) throws IOException, WriterException, MessagingException {
        boolean response = recipeService.createRecipe(doctorID, recipeID);
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

    @GetMapping(path = "/filterDrugByBrandName/{brandName}")
    public ResponseEntity<?> filterDrugsInMyMeds(@PathVariable("brandName")String brandName){
        return new ResponseEntity<>(drugService.filterDrugsByBrandNameOnMyMeds(brandName),HttpStatus.OK);
    }
    @PutMapping(path="/addDrugToPatient/{doctorID}")
    public ResponseEntity<?> addDrugToPatient(@PathVariable("doctorID") Integer doctorID,@RequestParam("patientID") Integer patientID,
                                              @RequestParam("drugID") Integer drugID){
        boolean response= drugService.addDrugToPatient(patientID,drugID,doctorID);
        if (response){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
    @DeleteMapping(path = "/removePatientDrug/{doctorID}")
    public ResponseEntity<?> removePatientDrug(@PathVariable("doctorID") Integer doctorID,@RequestParam("patientID") Integer patientID,
                                               @RequestParam("drugID")Integer drugID){
        boolean response= drugService.removeDrugForPatient(patientID,drugID,doctorID);
        if (response){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping(path = "/getPatientDrugs/{doctorID}")
    public ResponseEntity<?> getDrugsForPatientAsDoctor(@PathVariable("doctorID") Integer doctorID,@RequestParam("patientID") Integer patientID){
        List<DrugService.myMedsDrugDTO> patientDrugs= drugService.getPatientDrugsAsDoctor(doctorID,patientID);
        if (!patientDrugs.isEmpty()){
            return new ResponseEntity<>(patientDrugs,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}