package MyMeds.Repositorys;

import MyMeds.App.Drug;
import MyMeds.App.Pharmacy;

import MyMeds.Services.DrugService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy,Integer> {
    Pharmacy findByMailIgnoreCase(String mail);
    List<Pharmacy> findAll();
}
