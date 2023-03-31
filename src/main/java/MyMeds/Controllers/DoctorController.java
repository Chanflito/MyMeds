package MyMeds.Controllers;

import MyMeds.App.Doctor;

import MyMeds.App.Patient;
import MyMeds.Exceptions.UserRegisteredException;
import MyMeds.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/doctor")
@CrossOrigin
public class DoctorController {
    @Autowired//Instancia Spring el servicio.
    UserService userService;

    @GetMapping()//Retorna todos los docotores que se encuentran en la base de datos en formato JSON
    public List<Doctor> getDoctors() {
        return userService.getDoctors();
    }

    @PostMapping//Recibe una Request con un username, password etc y crea un nuevo doctor en la base de datos.
    public Doctor saveDoctor(@RequestBody Doctor doctor) {
        if (this.userService.registerDoctor(doctor)==null){
            throw new UserRegisteredException();
        }
        return doctor;
    }
    @GetMapping(path = "/{id}")
//Busca los doctores en la base de datos mediante un ID y retorna el doctor en formato JSON con sus atributos.
    public Optional<Doctor> getDoctorById(@PathVariable("id") Integer id) {
        return this.userService.getDoctorById(id);
    }

    @DeleteMapping("/{id}")
//Busca en la base de datos el doctor y lo borra, para hacer uso de la misma hay que hacer un DELETE en la url /doctor/id
    public String deleteDoctorById(@PathVariable("id") Integer id) {
        boolean founded = this.userService.deleteDoctorById(id);
        if (founded) {
            return "User deleted with ID " + id;
        } else {
            return "Can't delete user with ID " + id;
        }
    }

    @PutMapping("/addpatient/{id}")
    public Optional<Doctor> uploadPatientById(@PathVariable("id")Integer doc_id,@RequestBody Integer p_id){
        return this.userService.uploadPatientById(p_id, doc_id);
    }

    @GetMapping("/listpatients/{id}")
    public List<Patient> getPatientList(@PathVariable("id") Integer id){
        return this.userService.getAllPatients(id);
    }
}