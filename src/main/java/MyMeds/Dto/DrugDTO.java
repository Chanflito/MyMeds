package MyMeds.Dto;

public class DrugDTO {
    Integer drugID;
    String brandName;
    String dosageForm;
    String strength;

    public DrugDTO(Integer drugID, String brandName, String dosageForm, String strength) {
        this.drugID = drugID;
        this.brandName = brandName;
        this.dosageForm = dosageForm;
        this.strength = strength;
    }


    public Integer getDrugID() {
        return drugID;
    }

    public void setDrugID(Integer drugID) {
        this.drugID = drugID;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }
}
