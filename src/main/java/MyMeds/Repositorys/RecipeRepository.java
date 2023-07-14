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

    Recipe findByRecipeID(Integer recipeID);

    @Query("SELECT r FROM Recipe r WHERE r.status = :status AND r.patientID = :patientID" )
    List<Recipe> findByStatusAndID(@Param("status") RecipeStatus status,@Param("patientID") Integer patientID,Pageable pageable);
    @Query("SELECT count(r)FROM Recipe r WHERE r.status = :status AND r.patientID = :patientID" )
    Integer totalRecipesForPatient(@Param("status") RecipeStatus status,@Param("patientID") Integer patientID);
    @Query("SELECT r FROM Recipe r WHERE (:status IS NULL OR r.status = :status) AND r.pharmacyID = :pharmacyID")
    Page<Recipe> findByStatusAndPharmacyID(@Param("status") RecipeStatus status, @Param("pharmacyID") Integer pharmacyID,Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE (:status IS NULL OR r.status = :status) AND r.pharmacyID= :pharmacyID AND CAST(r.patient.id AS STRING) LIKE CONCAT(CAST(:patientID AS STRING),'%')")
    Page<Recipe> findByStatusAndPharmacyIDAndPatientID(@Param("status") RecipeStatus status, @Param("pharmacyID") Integer pharmacyID,@Param("patientID") Integer patientID,Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE (:status IS NULL OR r.status = :status) AND r.pharmacyID = :pharmacyID AND LOWER(r.doctor.username) LIKE CONCAT('%', LOWER(:username), '%')")
    Page<Recipe> findByStatusAndPharmacyIDAndDoctorName(@Param("status") RecipeStatus status, @Param("pharmacyID") Integer pharmacyID,@Param("username") String doctorName,Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE (:status IS NULL OR r.status = :status) AND r.pharmacyID=:pharmacyID AND LOWER(r.doctor.username) LIKE CONCAT(LOWER(:username),'%') AND CAST(r.patient.id AS STRING) LIKE CONCAT(CAST(:patientID AS STRING),'%')")
    Page<Recipe> findByStatusAndPharmacyIDAndDoctorNameAndPatientID(@Param("status") RecipeStatus status, @Param("pharmacyID") Integer pharmacyID,@Param("username") String doctorName,Pageable pageable,@Param("patientID")Integer patientID);

    @Query("SELECT count(r) FROM Recipe r WHERE (:status IS NULL OR r.status = :status) AND r.pharmacyID=:pharmacyID AND (:patientID IS NULL OR CAST(r.patient.id AS STRING) LIKE CONCAT(CAST(:patientID AS STRING),'%')) AND (:username IS NULL OR LOWER(r.doctor.username) LIKE CONCAT(LOWER(:username),'%'))")
    Integer countRecipesPharmacyByStatusAndPatientIDAndDoctorUsername(@Param("status") RecipeStatus status, @Param("pharmacyID") Integer pharmacyID,@Param("patientID")Integer patientID,@Param("username") String doctorName);
    @Query("SELECT r FROM Recipe r WHERE  r.pharmacyID = :pharmacyID AND (:status IS NULL OR r.status = :status)")
    List<Recipe> findAllForPharmacy(@Param("pharmacyID") Integer pharmacyID,@Param("status")RecipeStatus status);
    //Retorna las recetas en el caso que no me pasen como parametro el status.
    @Query("SELECT r FROM Recipe r WHERE r.doctorID=:doctorID")
    Page<Recipe> findRecipesDoctorID(@Param("doctorID") Integer doctorID, Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE r.doctorID = :doctorID AND CAST(r.patient.id AS STRING) LIKE CONCAT('%', CAST(:patientID AS STRING), '%')")
    Page<Recipe> findRecipesByDoctorIDAndPatientID(@Param("doctorID") Integer doctorID, @Param("patientID") Integer patientID, Pageable pageable);


    @Query("SELECT r FROM Recipe r WHERE r.doctorID=:doctorID AND CAST(r.patient.id AS STRING) LIKE CONCAT(CAST(:patientID AS STRING),'%') AND r.status=:status")
    Page<Recipe> findRecipesByDoctorIDAndPatientIDAndStatus(@Param("doctorID")Integer doctorID,@Param("patientID")Integer patientID,
                                                            @Param("status")RecipeStatus status,Pageable pageable);
    @Query("SELECT r FROM Recipe r WHERE r.status = :status AND r.doctorID = :doctorID" )
    Page<Recipe> findByStatusAndIDDoctor(@Param("status") RecipeStatus status, @Param("doctorID") Integer doctorID,Pageable pageable);

    @Query("SELECT count(r) FROM Recipe r WHERE (:status IS NULL OR r.status = :status) AND (:patientID IS NULL OR CAST(r.patient.id AS STRING) LIKE CONCAT(CAST(:patientID AS STRING),'%')) AND r.doctorID=:doctorID")
    Integer countRecipesByStatusAndPatientIDAndDoctorID(@Param("doctorID")Integer doctorID,@Param("patientID")Integer patientID,@Param("status")RecipeStatus status);

}
