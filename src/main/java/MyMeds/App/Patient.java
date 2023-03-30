package MyMeds.App;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public
class Patient extends User{
    @Column(unique = true, nullable = false)
    private String mail;
    @ManyToMany
    @JoinTable(name="doctor_patient", joinColumns = @JoinColumn(name = "patient_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    )
    @JsonIgnore
    private List<Doctor> doctors;
    @Column(unique = true)
    private String token= UUID.randomUUID().toString();

    @Column
    private Integer healthInsuarence; //Awaits until being set
    @Transient
    private final UserType userType=UserType.PATIENT;

    public Patient(){}

    public Patient(Integer dni, String mail, String username, String password){
        super(dni, username, password);
        this.mail = mail;
        this.doctors = new ArrayList<>();
    }

//GETTERS Y SETTERS

    public String getToken() {
        return token;
    }

    public Integer getDni(){
        return super.getPrimarykey();
    }
    public String getPassword(){
        return super.getPassword();
    }
    public String getUsername(){
        return super.getUsername();
    }
    public String getMail(){
        return this.mail;
    }
    public Integer getHealthInsuarence() {
        return healthInsuarence;
    }
    public UserType getUserType() {
        return userType;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public List<Doctor> getDoctors(){return this.doctors;}

    public void setMail(String mail) {
        this.mail = mail;
    }
    public void setHealthInsuarence(Integer healthInsuarence) {
        this.healthInsuarence = healthInsuarence;
    }
    public void addDoctor(Doctor d){if(!doctors.contains(d)){doctors.add(d);}}
    public void removeDoctor(Doctor d){
        doctors.remove(d);
    }

    //METHODS
    //Patient gives the simpliest information to app
    public Request RequestRecipie(String docUsername, String phUsername, String drugName){
        //Mades a request for a drug that will be bought in a specific pharmacy
        return new Request(docUsername, phUsername, drugName);
    }

    public void Pay(){
        //Pays for its drugs
    }
}