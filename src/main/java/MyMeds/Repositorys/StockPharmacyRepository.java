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
}