package MyMeds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootApplication
public class MyMedsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyMedsApplication.class, args);
	}
	
}
