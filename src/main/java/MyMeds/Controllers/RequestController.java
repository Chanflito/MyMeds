package MyMeds.Controllers;

import MyMeds.App.Request;
import MyMeds.Exceptions.RequestRegisteredException;
import MyMeds.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/request")
@CrossOrigin //?? should I?
public class RequestController {
    @Autowired
    UserService userService;

    @GetMapping
    public List<Request> getRequests(){return userService.getRequests();}

    @GetMapping("/{id}")
    public Optional<Request> getRequestById(@PathVariable("id") Integer id){
        return userService.getRequestById(id);
    }

    @PostMapping
    public Request saveRequest(@RequestBody Request req){
        //Request is made to a doctor by a patient to get a Recipie
        if(userService.registerRequest(req) == null){
            //request already registered
            throw new RequestRegisteredException();
        }
        return req;
    }

    @DeleteMapping("/{id}")
    public String deleteRequestById(@PathVariable("id") Integer id){
        boolean found = userService.deleteRequestById(id);
        return "";
    }

}
