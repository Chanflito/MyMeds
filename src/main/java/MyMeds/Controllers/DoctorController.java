package MyMeds.Controllers;

import MyMeds.App.Doctor;
import MyMeds.Exceptions.UserNotFoundException;
import MyMeds.Interfaces.DoctorRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class DoctorController {
    private final DoctorRepository doctorRepository;

    public DoctorController(DoctorRepository doctorRepository){
        this.doctorRepository=doctorRepository;
    }
    @GetMapping("/doctor")
    List<Doctor> all(){
        return doctorRepository.findAll();
    }
    @PostMapping("/doctor")
    Doctor newDoctor(@RequestBody Doctor doctor){
        return doctorRepository.save(doctor);
    }
    @GetMapping("/doctor/{primaryKey}")
    Doctor getDoctorsById(@PathVariable Integer primaryKey){
        return doctorRepository.findById(primaryKey).orElseThrow(
                ()->new UserNotFoundException(primaryKey)
        );
    }
    @DeleteMapping("/doctor/{primaryKey}")
    void deleteDoctor(@PathVariable Integer primaryKey) {
        doctorRepository.deleteById(primaryKey);
    }
}
