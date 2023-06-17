package MyMeds.Repositorys;

import MyMeds.App.Drug;
import MyMeds.Dto.DrugDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DrugRepository extends JpaRepository<Drug,Integer> {
    boolean existsByBrandNameAndStrengthAndDosageForm(String brandName,String dosageForm,String strength);
    @Query("SELECT new MyMeds.Dto.DrugDTO(d.id, d.brandName, d.dosageForm, d.strength) FROM Drug d WHERE LOWER(d.brandName) LIKE LOWER(:brandName || '%')")
    List<DrugDTO> filterByBrandNameDrug(@Param("brandName") String brandName);

}
