package MyMeds.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.IOException;

@Service
public class EmailServiceImpl implements EmailService{

   @Value("${spring.mail.username}")
   private String MyMail;

   @Autowired
   private JavaMailSender javaMailSender;


   @Override
   public String sendMail(File QR, String to, String body, Integer recipeID) throws MessagingException, IOException {
       MimeMessage mimeMessage = javaMailSender.createMimeMessage();
       MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
       mimeMessageHelper.setFrom(MyMail);
       mimeMessageHelper.setTo(to);
       mimeMessageHelper.setSubject("MyMed recipe");
       mimeMessageHelper.setText(body);
       mimeMessageHelper.addAttachment(String.valueOf(recipeID)+".jpg", QR);
       javaMailSender.send(mimeMessage);
       return "sent";
    }

}
