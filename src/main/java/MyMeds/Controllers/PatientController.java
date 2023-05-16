package MyMeds.Controllers;

import MyMeds.App.*;
import MyMeds.Dto.InProcessRecipeData;
import MyMeds.Exceptions.UserRegisteredException;
import MyMeds.Services.RecipeService;
import MyMeds.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
@CrossOrigin
public class PatientController {
    @Autowired
    RecipeService recipeService;
    @Autowired
    UserService userService;
    @GetMapping("/getPatients")
    public ResponseEntity<?> getPatients(){
        return new ResponseEntity<>(userService.getPatients(), HttpStatus.FOUND);
    }

    @PostMapping("/savePatient")
    public ResponseEntity<?> savePatient(@RequestBody Patient patient){
        if (this.userService.registerPatient(patient)==null){
            throw new UserRegisteredException();
        }
        return new ResponseEntity<>(patient,HttpStatus.CREATED);
    }

    @GetMapping(path="/getPatientById/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable("id") Integer id){
        return new ResponseEntity<>(userService.getPatientById(id),HttpStatus.FOUND);
    }

    @DeleteMapping("/deletePatientById/{id}")
    public ResponseEntity<?> deletePatientById(@PathVariable("id") Integer id){
        boolean founded=this.userService.deletePatientById(id);
        if (founded){
            return new ResponseEntity<>(founded, HttpStatus.FOUND);
        }else{
            return new ResponseEntity<>(!founded, HttpStatus.NOT_FOUND);
        }
    }
    //Podemos cambiarle la contraseña al paciente.
    @PutMapping("/changePatientPassword/{id}")
    public ResponseEntity<?> changePatientPassword(@PathVariable("id") Integer id, @RequestBody Patient patient){
        return new ResponseEntity<>(this.userService.changePatientPasswordByID(id,patient),HttpStatus.OK);
    }
    //Añadimos una obra social al paciente.
    @PutMapping("/{id}/addInsurance")
    public ResponseEntity<?> addHealthInsurance(@PathVariable("id") Integer id,@RequestBody Integer healthInsurance){
        return new ResponseEntity<>(this.userService.addHealthInsuranceById(healthInsurance,id),HttpStatus.OK);
    }

    @PutMapping("/{id}/makeRecipe")
    public ResponseEntity<?> makeRequestForRecipeToDoctor(@PathVariable Integer id,@RequestBody InProcessRecipeData data){
        //If isDone == false, doctor does not have a signature
        boolean isDone = this.recipeService.addRecipe(id,data.getDocId(), data.getDrugName());
        if (isDone){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path="/tokenPatient")
    public ResponseEntity<?> checkToken(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path="/viewDoctors/{id}")
    public ResponseEntity<?> viewDoctors(@PathVariable("id") Integer patientID){
        //Retorna una lista con la informacion limitada sobre los doctores que tiene el paciente
        List<UserService.doctorDTO> answer = userService.getAllDoctorsFromPatient(patientID);
        if(answer != null){
            return new ResponseEntity<>(answer, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping(path="/viewRecipes/{id}")
    public ResponseEntity<?> viewRecipes(@PathVariable("id") Integer patientID, @RequestParam("status") RecipeStatus status){
        List<RecipeService.recipeDTO> recipies= recipeService.findByRecipeStatusPatient(status, patientID);
        return new ResponseEntity<>(recipies,HttpStatus.ACCEPTED);
    }
}
