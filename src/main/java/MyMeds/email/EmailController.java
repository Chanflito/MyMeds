package MyMeds.email;

import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/mail")
public class EmailController {

    private EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendQR(File file, String to, String body, Integer recipeID) throws MessagingException, IOException {
        return emailService.sendMail(file, to, body, recipeID);
    }
}
