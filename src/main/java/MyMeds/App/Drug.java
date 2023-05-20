package MyMeds.App;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Drug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int drugID;
    @Column(nullable = false)
    private String drugName;
    @Column(nullable = false)
    private String drugDose;


    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @JsonIgnore
    private Recipe recipe;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="patient_id")
    @JoinTable(name="patient_drug")
    @JsonIgnore
    private Patient patientDrug;


    public Drug(){
    }


    public Patient getPatientDrug() {
        return patientDrug;
    }

    public void setPatientDrug(Patient patientDrug) {
        this.patientDrug = patientDrug;
    }

    public Recipe getRecipe() {
        return recipe;
    }



    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public int getId() {
        return drugID;
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
}
