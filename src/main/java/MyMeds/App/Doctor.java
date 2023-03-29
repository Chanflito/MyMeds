package MyMeds.App;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Doctor extends User{
    @Column(unique = true)
    private String signature;
    public Doctor(){}
    @Column(unique = true)
    private final Integer token=hashCode();
    @ManyToMany(mappedBy = "doctors")
    @Transient
    private List<Patient> patients;
    @Column(unique = true, nullable = false)
    private String mail;
    @Transient
    private final UserType userType=UserType.DOCTOR;

    public Integer getToken() {
        return token;
    }

    public Doctor(Integer registerNumber, String userName, String password, String mail){
        super(registerNumber, userName, password);
        this.mail=mail;
        patients = new ArrayList<>();
    }


    //GETTERS AND SETTERS
    public Integer getRegisterNumber(){
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
    public UserType getUserType() {
        return userType;
    }
    public String getMail() {
        return mail;
    }
    public List<Patient> getPatients(){return patients;}

    public void setMail(String mail) {
        this.mail = mail;
    }
    public void setSignature(String signature) {this.signature = signature;}
    public void addPatient(Optional<Patient> p){if(!patients.contains(p.get())){patients.add(p.get());}}
    public void removePatient(Optional<Patient> p){
        if(patients.contains(p.get())){patients.remove(p.get());}
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