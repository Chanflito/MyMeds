package MyMeds.App;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static MyMeds.App.RecipeStatus.*;

@Entity
public class Recipe {
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

    @ManyToMany(mappedBy ="recipes")
    @JsonIgnore
    private List<Drug> drugs;


    @JoinColumn
    private RecipeStatus status = IN_PROGRESS;

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }


    public Recipe(){
        this.drugs=new ArrayList<>();
    }
//GETTERS AND SETTERS

    public RecipeStatus getStatus() {
        return status;
    }
    public void setStatus(RecipeStatus status) {
        this.status = status;
    }



    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
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

    public List<Drug> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<Drug> drugs) {
        this.drugs = drugs;
    }
}
