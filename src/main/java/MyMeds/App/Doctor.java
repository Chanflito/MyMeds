package MyMeds.App;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Doctor extends User{
    @Column(unique = true)
    private String signature;
    public Doctor(){}
    @Column(unique = true)
    private String token= UUID.randomUUID().toString();
    @ManyToMany(mappedBy = "doctors")
    private List<Patient> patients;
    @OneToMany(mappedBy = "doctor")
    private List<Request> requests;
    @Column(unique = true, nullable = false)
    private String mail;
    @Transient
    private final UserType userType=UserType.DOCTOR;

    public Doctor(Integer registerNumber, String userName, String password, String mail){
        super(registerNumber, userName, password);
        this.mail=mail;
        patients = new ArrayList<>();
        requests = new ArrayList<>();
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
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
    public void setSignature(String signature) {this.signature = signature;}
    public void addPatient(Optional<Patient> p){if(!patients.contains(p.get())){patients.add(p.get());}}
    public void removePatient(Optional<Patient> p){
        if(patients.contains(p.get())){patients.remove(p.get());}
    }
    public void addRequest(Request req){if (!requests.contains(req)){requests.add(req);}}
    public void removeRequest(Request req){if(requests.contains(req)){requests.remove(req);}}

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
    
    public boolean HasPatient(Patient p){
        if(patients.contains(p)){
            return true;
        }
        return false;
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