package MyMeds.Repositorys;

import MyMeds.App.StockPharmacy;
import MyMeds.Dto.DrugStockDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockPharmacyRepository  extends JpaRepository<StockPharmacy,Integer>{
    @Query("SELECT count(d)>0 FROM StockPharmacy d Where d.drug.id= :drugID AND d.pharmacy.id=  :pharmacyID")
    boolean existsDrugByIDAndPharmacyID(@Param("drugID")Integer drugID,@Param("pharmacyID")Integer pharmacyID);

    @Query("SELECT new MyMeds.Dto.DrugStockDTO(d.id,d.brandName,d.dosageForm,d.strength,sp.stock) FROM StockPharmacy sp INNER JOIN Drug d ON d.id=sp.drug.id WHERE sp.pharmacy.id= :pharmacyID")
    List<DrugStockDTO> getDrugStockByPharmacyID(@Param("pharmacyID") Integer pharmacyID);

    @Query("SELECT s From StockPharmacy s Where s.drug.id= :drugID AND s.pharmacy.id= :pharmacyID")
    StockPharmacy getStockWithDrugIDAndPharmacyID(@Param("drugID")Integer drugID, @Param("pharmacyID")Integer pharmacyID);
    //Me devuelve una lista de drogas que no tienen stock la farmacia.

    @Query("SELECT new MyMeds.Dto.DrugDTO(p.drug.id, p.drug.brandName,p.drug.dosageForm, p.drug.strength) " +
            "FROM StockPharmacy p " +
            "WHERE p.drug.id IN (:drugsID) AND p.pharmacy.id = :pharmacyID AND p.stock > 0")
    List<MyMeds.Dto.DrugDTO> findDrugsWithStock(@Param("drugsID") List<Integer> drugIds, @Param("pharmacyID") Integer pharmacyID);

    @Query("SELECT d.id FROM StockPharmacy d WHERE d.drug.brandName=:brandName AND d.drug.dosageForm=:dosageForm AND d.drug.strength=:strength AND d.pharmacy.id=:pharmacyID")
    Integer getDrugInPharmacy(@Param("brandName") String brandName,@Param("dosageForm") String dosageForm,@Param("strength")String strength,@Param("pharmacyID")Integer pharmacyID);

}
