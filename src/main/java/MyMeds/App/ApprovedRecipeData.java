package MyMeds.App;

public class ApprovedRecipeData {
    //Se usa como request body para crear una receta
    private Integer pharmacyID;
    private Integer recipeID;

    private String docSignature;

    public ApprovedRecipeData(Integer pharmacyID, Integer recipeID,String docSignature) {
        this.pharmacyID = pharmacyID;
        this.recipeID = recipeID;
        this.docSignature=docSignature;
    }

    public ApprovedRecipeData() {}

    public String getDocSignature() {
        return docSignature;
    }

    public void setDocSignature(String docSignature) {
        this.docSignature = docSignature;
    }

    public Integer getPharmacyID() {
        return pharmacyID;
    }

    public Integer getRecipeID() {
        return recipeID;
    }
}
