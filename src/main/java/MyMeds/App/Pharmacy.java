package MyMeds.App;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.List;
import java.util.UUID;

@Entity
public class Pharmacy extends User{
    @Column(unique = true)
    private String mail;
    @Transient
    private final UserType userType=UserType.PHARMACY;
    public Pharmacy(){}


    public Pharmacy(Integer primaryKey, String userName, String password, String mail){
        super(primaryKey, userName, password);
        this.mail=mail;
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

    @Override
    public UserType getUserType() {
        return userType;
    }

    public String getMail(){
        return this.mail;
    }

//METHODS
    public boolean VerifyDoctor(Integer docRegistNumber){
        //Looks for the doc registration number in a DB, If exists then returns true
        return false;
    }

    public List<Recipe> FindRecipe(Integer dni, boolean signed){
        //Returns all recipies form the same Patient that had not been marked
        //If is needed the mared ones, then signed must equal true
        if(!signed){
            //Make a Selection operation over de DB Recipe (dni = patient, mark = false)
            return null;
        }
        else{
            //Make a Selection operation over the DB Recepie (dni = patient) 
            return null;
        }    
    }

    public void StampRecipe(Integer recipeKey){
        //Modifies the Recipe DB given it´s primary key
    }
}