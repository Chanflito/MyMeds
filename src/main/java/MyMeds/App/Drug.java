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
    private String brandName;
    @Column
    private String strength;

    @Column
    private String dosageForm;

    @OneToMany(mappedBy = "drug",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<StockPharmacy> stockPharmacyList;
    @ManyToMany
    @JoinTable(name = "recipe_drug",joinColumns = @JoinColumn(name = "drug_id",referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "recipe_id",referencedColumnName = "recipeID"))
    private List<Recipe> recipes;

    @ManyToMany
    @JoinTable(name = "patient_drug",joinColumns = @JoinColumn(name="drug_id",referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "patient_id",referencedColumnName = "id"))
    @JsonIgnore
    private List<Patient> patients;
    public Drug() {
        this.recipes=new ArrayList<>();
    }

    public Drug(String brandName, String strength,String dosageForm) {
        this.brandName = brandName;
        this.strength = strength;
        this.dosageForm=dosageForm;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }


    public List<StockPharmacy> getStockPharmacyList() {
        return stockPharmacyList;
    }

    public void setStockPharmacyList(List<StockPharmacy> stockPharmacyList) {
        this.stockPharmacyList = stockPharmacyList;
    }

    public Integer getId() {
        return id;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }
}
