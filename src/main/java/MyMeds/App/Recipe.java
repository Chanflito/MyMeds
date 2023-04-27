package MyMeds.App;

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
    private Doctor doctor;

    public Recipe(String docSignature,String drugName, Integer pharmacyID){
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
