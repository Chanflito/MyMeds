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
//            Doctor doctor1 = new Doctor(101, "Marcos Medina", "123", "marcos_medina@gmail.com");
//            Doctor doctor2 = new Doctor(102, "Maria Garcia", "123", "maria_garcia@gmail.com");
//            Doctor doctor3 = new Doctor(103, "Juan Perez", "123", "juan_perez@gmail.com");
//            Doctor doctor4 = new Doctor(104, "Ana Martinez", "123", "ana_martinez@gmail.com");
//            Doctor doctor5 = new Doctor(105, "Luis Sanchez", "123", "luis_sanchez@gmail.com");
//
//            Patient patient1 = new Patient(201, "laura_gonzales@gmail.com", "Laura Gonzales", "123");
//            Patient patient2 = new Patient(202, "carlos_martinez@gmail.com", "Carlos Martinez", "123");
//            Patient patient3 = new Patient(203, "anarodriguez@gmail.com", "Ana Rodriguez", "123");
//            Patient patient4 = new Patient(204, "jorge_gomez@gmail.com", "Jorge Gomez", "123");
//            Patient patient5 = new Patient(205, "isabel_lopez@gmail.com", "Isabel Lopez", "123");
//
//            Pharmacy pharmacy1 = new Pharmacy(301, "Farmacia Los Pinos", "123", "farmacia_los_pinos@gmail.com");
//            Pharmacy pharmacy2 = new Pharmacy(302, "Farmacia San José", "123", "farmacia_san_jose@gmail.com");
//            Pharmacy pharmacy3 = new Pharmacy(303, "Farmacia del Parque", "123", "farmacia_del_parque@gmail.com");
//            Pharmacy pharmacy4 = new Pharmacy(304, "Farmacia Nuestra Señora", "123", "farmacia_nuestra_senora@gmail.com");
//            Pharmacy pharmacy5 = new Pharmacy(305, "Farmacia Central", "123", "farmacia_central@gmail.com");
//
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
//            pharmacyRepository.save(pharmacy1);
//            pharmacyRepository.save(pharmacy2);
//            pharmacyRepository.save(pharmacy3);
//            pharmacyRepository.save(pharmacy4);
//            pharmacyRepository.save(pharmacy5);
       };
    }
}
