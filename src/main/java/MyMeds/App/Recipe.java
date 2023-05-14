package MyMeds.App;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import static MyMeds.App.RecipeStatus.*;

@Entity
public class Recipe {
    @Column
    private String docSignature;
    @Column
    private String drugName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recipeID;
    @Column
    private Integer patientID;
    @Column
    private Integer doctorID;
    @Column
    private Integer pharmacyID;

    @ManyToOne(cascade =  CascadeType.PERSIST)
    @JoinColumn(name= "patient_id")
    @JsonIgnore
    private Patient patient;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="doctor_id")
    @JsonIgnore
    private Doctor doctor;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinTable(name = "pharmacy_recipe")
    @JsonIgnore
    private Pharmacy pharmacy;

    @JoinColumn
    private RecipeStatus status = IN_PROGRESS;

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

    public RecipeStatus getStatus() {
        return status;
    }
    public void setStatus(RecipeStatus status) {
        this.status = status;
    }

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

    public Integer getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(Integer recipeID) {
        this.recipeID = recipeID;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public Integer getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(Integer doctorID) {
        this.doctorID = doctorID;
    }

    public Patient getPatient() {
        return patient;
    }


    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
