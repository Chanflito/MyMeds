package MyMeds.Controllers;

import MyMeds.App.Doctor;

import MyMeds.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    UserService userService;
    @GetMapping()
    public List<Doctor>getDoctors(){
        return userService.getDoctors();
    }

    @PostMapping
    public Doctor saveDoctor(@RequestBody Doctor doctor){
        return this.userService.saveDoctor(doctor);
    }

    @GetMapping(path="/{id}")
    public Optional<Doctor> getDoctorById(@PathVariable("id") Integer id){
        return this.userService.getDoctorById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteDoctorById(@PathVariable("id") Integer id){
        boolean founded=this.userService.deleteDoctorById(id);
        if (founded){
            return "User deleted with ID "+ id;
        }else{
            return "Can't delete user with ID "+ id;
        }
    }
}
