package MyMeds.App;

import jakarta.persistence.*;

@Entity
public class Doctor extends User{
    @Column
    private String signature;
    public Doctor(){}
    public Doctor(Integer registerNumber, String userName, String password, String signature){
        super(registerNumber, userName, password);
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

    private Recipe MakeRecipe(String drugName, Integer phRegistNumber){
        //Creates a recipe
        return new Recipe(this.signature, this.getPrimarykey(), drugName, phRegistNumber);
    }


    public Recipe MakeAndSend(boolean isReject, String drugName, Integer phRegistNumber){
        //If return is is_reject equals true, the recipe is rejected
        //reject is always false unless is changed
        if(!isReject){
            return this.MakeRecipe(drugName, phRegistNumber);
        }
        else{
            return null;
        }
    }
    
    public void RegistPatient(Integer dni){
        //Adds a new patient to his DB
    }

}