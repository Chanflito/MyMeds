package MyMeds.App;

import jakarta.persistence.*;

import java.util.List;

@Entity
public
class Patient extends User{
    @Column(unique = true)
    private String mail;
    @ManyToMany
    @JoinTable(name="doctor_patient", joinColumns = @JoinColumn(name = "patient_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    )
    private List<Doctor> doctors;
    @Column(unique = true)
    private final Integer token=hashCode();
    @Column
    private Integer healthInsuarence; //Awaits until being set
    @Transient
    private final UserType userType=UserType.PATIENT;
    public Patient(){}

    public Integer getToken() {
        return token;
    }

    public Patient(Integer dni, String mail, String username, String password){
        super(dni, username, password);
        this.mail = mail;
    }

//GETTERS
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
    //Setters


    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setHealthInsuarence(Integer healthInsuarence) {
        this.healthInsuarence = healthInsuarence;
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