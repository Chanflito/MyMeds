package MyMeds.App;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class RecipeDTO {

    private String docSignature;
    private String drugName;
    private Integer recipeID;
    private Integer patientID;
    private Integer doctorID;
    private Integer pharmacyID;

    private String patientUsername;

    private String doctorUsername;

    public RecipeDTO(String docSignature, String drugName, Integer recipeID,
                     Integer patientID, Integer doctorID, Integer pharmacyID,
                     String patientUsername,String doctorUsername) {
        this.docSignature = docSignature;
        this.drugName = drugName;
        this.recipeID = recipeID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.pharmacyID = pharmacyID;
        this.patientUsername=patientUsername;
        this.doctorUsername=doctorUsername;
    }

    public String getDoctorName() {
        return this.doctorUsername;
    }

    public String getPatientName() {
        return this.patientUsername;
    }

    public String getDocSignature() {
        return docSignature;
    }

    public String getDrugName() {
        return drugName;
    }

    public Integer getRecipeID() {
        return recipeID;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public Integer getDoctorID() {
        return doctorID;
    }

    public Integer getPharmacyID() {
        return pharmacyID;
    }
}
