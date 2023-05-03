package MyMeds.App;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Recipe {
    @Column
    private String docSignature;
    @Column
    private String drugName;
    @Column
    private Integer pharmacyID;
    @Column
    private boolean recipeIsMark = false;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recipeID;

    @ManyToOne
    @JoinColumn(name="doctor_id")
    @JsonIgnore
    private Doctor doctor;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinTable(name = "pharmacy_recipe")
    @JsonIgnore
    private Pharmacy pharmacy;

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public Recipe(String docSignature, String drugName, Integer pharmacyID){
        this.docSignature = docSignature;
        this.drugName = drugName;
        this.pharmacyID = pharmacyID;
    }

    public Recipe(){}
//GETTERS AND SETTERS

    public String getDocSignature() {
        return docSignature;
    }

    public void setDocSignature(String docSignature) {
        this.docSignature = docSignature;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public Integer getPharmacyID() {
        return pharmacyID;
    }

    public void setPharmacyID(Integer pharmacyID) {
        this.pharmacyID = pharmacyID;
    }

    public boolean isRecipeIsMark() {
        return recipeIsMark;
    }

    public void setRecipeIsMark(boolean recipeIsMark) {
        this.recipeIsMark = recipeIsMark;
    }

    public Integer getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(Integer recipeID) {
        this.recipeID = recipeID;
    }
}
