package MyMeds.Repositorys;

import MyMeds.App.Doctor;
import MyMeds.App.Patient;
import MyMeds.App.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Integer> {
    Doctor findByMailIgnoreCase(String mail);
    Doctor findByPassword(String password);
    @Query("SELECT p.id, p.username FROM Doctor d JOIN d.patients p WHERE d.id = ?1")
    //@Query("SELECT p FROM Doctor d JOIN d.patients p WHERE d.id = ?1") Forma antigua poco segura.
    List<Patient> findByPatients(Integer doctorID);
    @Query("SELECT r FROM Doctor d JOIN d.requests r WHERE d.id = ?1")
    List<Request> findByRequests(Integer doctorID);
}
