package MyMeds.Repositorys;

import MyMeds.App.Doctor;
import MyMeds.App.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Integer> {
    Patient findByMailIgnoreCase(String mail);
    Patient findByPassword(String password);
    @Query("SELECT d FROM Patient p JOIN p.doctors d WHERE p.id = ?1")
    List<Doctor> findDoctors(Integer patientID);
    @Query("SELECT count(p)>0 FROM Patient p INNER JOIN p.drugList pd WHERE p.id=:patientID AND pd.id=:drugID")
    boolean patientContainsDrug(Integer drugID,Integer patientID);
}
