package MyMeds.Repositorys;

import MyMeds.App.Recipe;
import MyMeds.App.RecipeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Recipe> findByStatusAndIDDoctor(@Param("status") RecipeStatus status, @Param("doctorID") Integer doctorID,Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE r.status = :status AND r.pharmacyID = :pharmacyID")
    List<Recipe> findByStatusAndPharmacyID(@Param("status") RecipeStatus status, @Param("pharmacyID") Integer pharmacyID);

    @Query("SELECT r FROM Recipe r WHERE  r.pharmacyID = :pharmacyID")
    List<Recipe> findAllForPharmacy(@Param("pharmacyID") Integer pharmacyID);
    //Retorna las recetas en el caso que no me pasen como parametro el status.
    @Query("SELECT r FROM Recipe r WHERE r.doctorID=:doctorID")
    Page<Recipe> findRecipesDoctorID(@Param("doctorID") Integer doctorID, Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE r.doctorID = :doctorID AND CAST(r.patient.id AS CHAR) LIKE CONCAT('%', CAST(:patientID AS CHAR))")
    Page<Recipe> findRecipesByDoctorIDAndPatientID(@Param("doctorID") Integer doctorID,@Param("patientID") Integer patientID,Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE r.doctorID=:doctorID AND CAST(r.patient.id AS CHAR) LIKE CONCAT('%',CAST(:patientID AS CHAR) ) AND r.status=:status")
    Page<Recipe> findRecipesByDoctorIDAndPatientIDAndStatus(@Param("doctorID")Integer doctorID,@Param("patientID")Integer patientID,
                                                            @Param("status")RecipeStatus status,Pageable pageable);

}
