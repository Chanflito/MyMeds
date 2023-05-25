package MyMeds.Repositorys;

import MyMeds.App.Drug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugRepository extends JpaRepository<Drug,Integer> {
}
