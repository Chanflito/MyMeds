package MyMeds.Repositorys;

import MyMeds.App.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Integer> {
    Patient findByMail(String mail);
    Patient findByPassword(String password);
}