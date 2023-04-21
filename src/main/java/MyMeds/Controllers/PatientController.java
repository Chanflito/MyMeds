package MyMeds.Controllers;

import MyMeds.App.*;
import MyMeds.Exceptions.UserRegisteredException;
import MyMeds.Services.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patient")
@CrossOrigin
public class PatientController {
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

    @PutMapping("/{id}/makeRequest")
    public ResponseEntity<?> makeRequestToDoctorById(@PathVariable Integer id,@RequestBody RequestData data){
        //If isDone == false, doctor does not have a signature
        boolean isDone = this.userService.addRequest(id,data.getDocId(), data.getDrugName());
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
        if(!userService.findPatientByID(patientID)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Doctor> doctors = userService.getAllDoctorsFromPatient(patientID);
        List<DoctorForPatient> answer = new ArrayList<>();
        for(Doctor doctor : doctors){
            DoctorForPatient doc = userService.DoctorWithUsernameID(doctor.getPrimarykey(), doctor.getUsername());
            answer.add(doc);
        }
        return new ResponseEntity<>(answer, HttpStatus.ACCEPTED);
    }

    @GetMapping(path="/viewRequests/{id}")
    public ResponseEntity<?> viewRequest(@PathVariable("id") Integer patientId){
        List<RequestForPatient> requestForPatientList=userService.getAllRequestsFromPatient(patientId);
        if (!requestForPatientList.isEmpty()){
            return new ResponseEntity<>(requestForPatientList,HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); //Patient has no request to doctors.
    }
}
