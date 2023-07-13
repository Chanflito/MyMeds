package MyMeds.email;

import jakarta.mail.MessagingException;


import java.io.File;
import java.io.IOException;

public interface EmailService {

    String sendMail(File file, String to, String body, Integer recipeID) throws MessagingException, IOException;
}
