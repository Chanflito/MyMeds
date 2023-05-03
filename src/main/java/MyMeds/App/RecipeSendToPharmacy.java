package MyMeds.App;

public class RecipeSendToPharmacy {
    private Integer recipeID;
    private Integer pharmacyID;

    public RecipeSendToPharmacy(){}

    public Integer getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(Integer recipeID) {
        this.recipeID = recipeID;
    }

    public Integer getPharmacyID() {
        return pharmacyID;
    }

    public void setPharmacyID(Integer pharmacyID) {
        this.pharmacyID = pharmacyID;
    }
}
