package MyMeds.Repositorys;

import MyMeds.App.Recipe;
import MyMeds.App.RecipeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,Integer>{
    @Query("SELECT r FROM Recipe r WHERE r.status = :status AND r.patientID = :patientID" )
    List<Recipe> findByStatusAndID(@Param("status") RecipeStatus status,@Param("patientID") Integer patientID);

    @Query("SELECT r FROM Recipe r WHERE r.status = :status AND r.doctorID = :doctorID" )
    List<Recipe> findByStatusAndIDDoctor(@Param("status") RecipeStatus status,@Param("doctorID") Integer doctorID);

    @Query("SELECT r FROM Recipe r WHERE r.status = :status AND r.pharmacyID = :pharmacyID")
    List<Recipe> findByStatusAndPharmacyID(@Param("status") RecipeStatus status, @Param("pharmacyID") Integer pharmacyID);
}
