package MyMeds.Controllers;

import MyMeds.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
@CrossOrigin
public class TokenController {
    @Autowired
    UserService userService;

}
