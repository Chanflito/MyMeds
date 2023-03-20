package MyMeds.Interfaces;

import MyMeds.App.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy,Integer> {
}
