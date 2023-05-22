package MyMeds.App;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Drug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String drugName;
    @Column
    private String drugDose;
    @ManyToMany(mappedBy = "drugList")
    private List<Admin> admins=new ArrayList<>();

    @OneToMany(mappedBy = "drug",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<DrugPrice> prices=new ArrayList<>();


    public Drug() {
    }

    public Drug(String drugName, String drugDose) {
        this.drugName = drugName;
        this.drugDose = drugDose;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugDose() {
        return drugDose;
    }

    public void setDrugDose(String drugDose) {
        this.drugDose = drugDose;
    }


    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }

    public List<DrugPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<DrugPrice> prices) {
        this.prices = prices;
    }
}
