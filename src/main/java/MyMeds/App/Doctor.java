package MyMeds.App;
import java.util.ArrayList;
import java.util.List;

class Doctor extends User{

    private String signature;

    public Doctor(Integer regist_number, String username, String password, String signature, Integer regist_number){
        super(regist_number, username, password);
        this.signature = signature;
    
    }

//GETTERS AND SETTERS
    public Integer getRegistNumber(){
        return super.getPrimarykey();
    }
    public String getUsername(){
        return super.getUsername();
    }
    public String getPassword(){
        return super.getPassword();
    }

    public String getSignature(){
        return signature;
    }

//METHODS

    private Recipe MakeRecipe(String drug_name, Integer ph_regist_number){
        //Creates a recipe
        return new Recipe(this.signature, this.getPrimarykey(), drug_name, ph_regist_number);
    }


    public Recipe MakeAndSend(boolean is_reject, String drug_name, Integer ph_regist_number){
        //If return is is_reject equals true, the recipe is rejected
        //reject is always false unless is changed
        if(!is_reject){
            return this.MakeRecipe(drug_name, ph_regist_number);
        }
        else{
            return null;
        }
    }
    
    public void RegistPatient(Integer dni){
        //Adds a new patient to his DB
    }

}