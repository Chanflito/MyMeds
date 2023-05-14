package MyMeds.Repositorys;

import MyMeds.App.Pharmacy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy,Integer> {
    Pharmacy findByMailIgnoreCase(String mail);
    Pharmacy findByPassword(String password);

    List<Pharmacy> findAll();
}
