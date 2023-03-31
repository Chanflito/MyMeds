package MyMeds.Repositorys;

import MyMeds.App.Doctor;
import MyMeds.App.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Integer> {
    Doctor findByMail(String mail);
    Doctor findByPassword(String password);
    //"SELECT p.id, p.username FROM Doctor d JOIN d.patients p WHERE d.id = ?1" Deberia ser asi
    @Query("SELECT p FROM Doctor d JOIN d.patients p WHERE d.id = ?1")
    List<Patient>findByPatients(Integer doctorID);
}
