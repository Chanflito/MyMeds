package MyMeds.Database;
import MyMeds.App.Doctor;
import MyMeds.App.Patient;
import MyMeds.App.Pharmacy;
import MyMeds.Repositorys.DoctorRepository;
import MyMeds.Repositorys.PatientRepository;
import MyMeds.Repositorys.PharmacyRepository;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;

@Configuration
public class LoadDatabase {
    private static final Logger log= LoggerFactory.getLogger(LoadDatabase.class);
    //Just run this code one time , to create new objects on the database
    @Bean
    CommandLineRunner initDatabase(DoctorRepository doctorRepository,
                                   PatientRepository patientRepository,
                                   PharmacyRepository pharmacyRepository){
        return args -> {
//            Doctor doctor1=new Doctor(2322222,"Marcos Medina","contraseña123","marcos_medina@gmail.com");
//            Doctor doctor2=new Doctor(1111234,"Maria Garcia","con123","MariaGarcia78@gmail.com");
//            Doctor doctor3=new Doctor(3322131,"Juan Perez","miPassword11","JuanPerez45@gmail.com");
//            Doctor doctor4=new Doctor(1111234,"Ana Martinez","pass1333","AnaMartinez32@gmail.com");
//            Doctor doctor5=new Doctor(9033221,"Luis Sanchez ","luissanchez122","LuisSanchez21@gmail.com");
//
//            Patient patient1=new Patient(12345678,"laura.gonzalez@gmail.com","Laura Gonzales","LGo#1234");
//            Patient patient2=new Patient(23456789,"carlos.martinez@gmail.com","Carlos Martinez","CMart#5678");
//            Patient patient3=new Patient(34567890,"ana.rodriguez@gmail.com","Ana Rodriguez","ARod#9012");
//            Patient patient4=new Patient(45678901,"jorge.gomez@gmail.com","Jorge Gomez","JGom#2345");
//            Patient patient5=new Patient(56789012,"isabel.lopez@gmail.com","Isabel Lopez","ILop#6789");
//
//            Pharmacy farmacia1 = new Pharmacy(92893285, "Farmacia Los Pinos", "contraseña123", "farmacialospinos@gmail.com");
//            Pharmacy farmacia2 = new Pharmacy(32131221, "Farmacia San José", "contraseña456", "farmaciasanjose@gmail.com");
//            Pharmacy farmacia3 = new Pharmacy(86622133, "Farmacia del Parque", "contraseña789", "farmaciadelparque@gmail.com");
//            Pharmacy farmacia4 = new Pharmacy(48484491, "Farmacia Nuestra Señora", "contraseña012", "farmacianuestraseñora@gmail.com");
//            Pharmacy farmacia5 = new Pharmacy(99883312, "Farmacia Central", "contraseña345", "farmaciacentral@gmail.com");
//
//            doctorRepository.save(doctor1);
//            doctorRepository.save(doctor2);
//            doctorRepository.save(doctor3);
//            doctorRepository.save(doctor4);
//            doctorRepository.save(doctor5);
//
//            patientRepository.save(patient1);
//            patientRepository.save(patient2);
//            patientRepository.save(patient3);
//            patientRepository.save(patient4);
//            patientRepository.save(patient5);
//
//            pharmacyRepository.save(farmacia1);
//            pharmacyRepository.save(farmacia2);
//            pharmacyRepository.save(farmacia3);
//            pharmacyRepository.save(farmacia4);
//            pharmacyRepository.save(farmacia5);
       };
    }
}
