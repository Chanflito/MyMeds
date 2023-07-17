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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class EmailServiceImpl implements EmailService{

   @Value("${spring.mail.username}")
   private String MyMail;

   @Autowired
   private JavaMailSender javaMailSender;

   private static MimeMessage cachedMimeMessage;
   @Override
   public String sendMail(File QR, String to, String body, Integer recipeID) throws MessagingException, IOException {
       if (cachedMimeMessage==null){
           cachedMimeMessage=javaMailSender.createMimeMessage();
           MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(cachedMimeMessage,true);
           mimeMessageHelper.setFrom(MyMail);
       }
       MimeMessage mimeMessage = javaMailSender.createMimeMessage();
       MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
       mimeMessageHelper.setFrom(MyMail);
       mimeMessageHelper.setTo(to);
       mimeMessageHelper.setSubject("MyMeds recipe");
       mimeMessageHelper.setText(body);
       mimeMessageHelper.addAttachment(recipeID +".jpg", QR);
       ExecutorService emailExecutor= Executors.newSingleThreadExecutor();
       emailExecutor.execute(() -> {
           javaMailSender.send(mimeMessage);
           if (QR.exists())QR.delete();
       });
       emailExecutor.shutdown();
       return "sent";
    }
}
