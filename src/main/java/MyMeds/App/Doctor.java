package MyMeds.App;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.*;

@Entity
public class Doctor extends User{
    public Doctor(){}

    @Column(unique = true, nullable = false)
    private String mail;
    @Transient
    private final UserType userType=UserType.DOCTOR;

    @OneToMany(mappedBy = "doctor")
    @JsonIgnore
    private List<Recipe> recipes;

    @ManyToMany(mappedBy = "doctors")
    private List<Patient> patients;


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

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public String getMail() {
        return mail;
    }
    public List<Patient> getPatients(){return patients;}

    public void setMail(String mail) {
        this.mail = mail;
    }
    public void addPatient(Optional<Patient> p){if(!patients.contains(p.get())){patients.add(p.get());}}
    public void removePatient(Optional<Patient> p){
        if(patients.contains(p.get())){patients.remove(p.get());}
    }
    public void addRecipe(Recipe r){if (!recipes.contains(r)){recipes.add(r);}}
    public void removeRecipe(Recipe r){if(recipes.contains(r)){recipes.remove(r);}}


    @Override
    public UserType getUserType() {
        return userType;
    }

    //METHODS
    
    public boolean HasPatient(Patient p){
        return patients.contains(p);
    }

    public Boolean searchPatient(Integer patientId){
        List<Patient> patientList=getPatients();
        for (Patient patient : patientList) {
            if (Objects.equals(patient.getPrimarykey(), patientId)) {
                return true;
            }
        }
        return false;
    }
 }