package MyMeds.Repositorys;

import MyMeds.App.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Integer> {
    Doctor findByMailIgnoreCase(String mail);
    Doctor findByPassword(String password);
    //@Query("SELECT p.id, p.username FROM Doctor d JOIN d.patients p WHERE d.id = ?1")
    @Query("SELECT p FROM Doctor d JOIN d.patients p WHERE d.id = ?1")
    List<Patient> findByPatients(Integer doctorID);

    @Query("SELECT r FROM Recipe r WHERE r.status = :status AND r.doctorID = :doctorID")
    List<Recipe> findByStatus(@Param("status")RecipeStatus status, @Param("doctorID") Integer doctorID);
}
