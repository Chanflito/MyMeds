package MyMeds.Database;

import MyMeds.App.Doctor;
import MyMeds.App.Patient;
import MyMeds.App.Pharmacy;
import MyMeds.Interfaces.DoctorRepository;
import MyMeds.Interfaces.PatientRepository;
import MyMeds.Interfaces.PharmacyRepository;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;

@Configuration
public class LoadDatabase {
    private static final Logger log= LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(DoctorRepository doctorRepository,
                                   PatientRepository patientRepository,
                                   PharmacyRepository pharmacyRepository){
        return args -> {
            log.info("Preloading"+ doctorRepository.save(
                    new Doctor(333445,"Roberto Caffaro",
                            "robertito123",
                            "robert","myMail@gmail.com")));
            log.info("Preloading"+ patientRepository.save(
                    new Patient(41555631,
                            "marcos@gmail.com","Marcos Lambertini",
                            "marcos123")
            ));

            log.info("Preloading"+ pharmacyRepository.save(
                    new Pharmacy(332221,"Farmacity",
                            "contrasena",
                            "Farmacity@gmail.com")
            ));
        };
    }
}
