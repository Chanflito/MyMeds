package MyMeds.Controllers;

import MyMeds.App.*;
import MyMeds.Dto.CreateRecipeResponse;
import MyMeds.Dto.InProcessRecipeData;
import MyMeds.Dto.RecipePageDTO;
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

import java.util.List;

@RestController
@RequestMapping("/patient")
@CrossOrigin
public class PatientController {
    @Autowired
    RecipeService recipeService;
    @Autowired
    UserService userService;

    @Autowired
    DrugService drugService;
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
    public ResponseEntity<?> makeRequestForRecipeToDoctor(@PathVariable Integer id,@RequestBody List<Integer> drugsID,
                                                          @RequestParam ("doctorID") Integer doctorID,@RequestParam("pharmacyID") Integer pharmacyID){
        CreateRecipeResponse response = this.recipeService.addRecipe(id,doctorID, drugsID,pharmacyID);
        //Si el paciente no se encuentra en la lista del doctor, retorna un not_found.
        if (!response.isSucess() && (response.getDrugDTOS()==null || response.getDrugDTOS().isEmpty())){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //Si no contiene stock, se menciona que drogas no tienen stock.
        if (!response.isSucess() && !response.getDrugDTOS().isEmpty()){
            return new ResponseEntity<>(response,HttpStatus.CONFLICT);
        }
        //De lo contrario, retorna un 200.
        return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<?> viewRecipes(@PathVariable("id") Integer patientID,
                                         @RequestParam("status") RecipeStatus status,@RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size){
        Pageable pageable= PageRequest.of(page, size);
        List<RecipeService.recipeDTO> recipes= recipeService.findByRecipeStatusPatient(status, patientID,pageable);
        int totalRecipes=recipeService.countRecipeByStatusPatient(status, patientID);
        int totalPages = (int) Math.ceil((double) totalRecipes / size);
        RecipePageDTO recipePageDTO=new RecipePageDTO(recipes,totalPages);
        return new ResponseEntity<>(recipePageDTO, HttpStatus.OK);
    }

    @GetMapping(path="/getAllPharmacys")
    public ResponseEntity<?> getPharmacys(){
        return new ResponseEntity<>(userService.getAllPharmacys(),HttpStatus.OK);
    }

    @GetMapping(path="/getPatientDrugs/{patientID}")
    public ResponseEntity<?> getPatientDrugs(@PathVariable("patientID") Integer patientID){
        return new ResponseEntity<>(drugService.getPatientDrugs(patientID),HttpStatus.OK);
    }
}
