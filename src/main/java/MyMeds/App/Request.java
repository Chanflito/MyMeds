package MyMeds.App;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Request {
    //Data on a request should never be null
    @Id
    private Integer request_id = hashCode();
    @Column(nullable = false)
    private String docUsername;
    @Column(nullable = false)
    private String pUsername;
    @Column(nullable = false)
    private String drugName;

    @Column(nullable = false)
    private Integer patientID;
    @Column(nullable = false)
    private Integer doctorID;
    @ManyToOne(cascade = CascadeType.PERSIST)//Many doctors can have assaigned to a Request, but the request is just for one doctor
    @JoinTable(name = "doctor_requests")
    @JsonIgnore
    private Doctor doctor;

    public Request(){}//Constructor for spring

    public Request(String docUsername, String phUsername, String drugName, Integer patientID,Integer doctorID){
        this.docUsername = docUsername;
        this.pUsername = phUsername;
        this.drugName = drugName;
        this.patientID=patientID;
        this.doctorID=doctorID;
    }

    //GETTERS
    public Integer getRequestId(){
        return request_id;
    }
    public String getDocUsername(){
        return docUsername;
    }
    public String getPUsername(){
        return pUsername;
    }
    public String getDrugName(){
        return drugName;
    }
    public Doctor getDoctorAssigned(){return doctor;}

    public Integer getRequest_id() {
        return request_id;
    }

    public String getpUsername() {
        return pUsername;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public Integer getDoctorID() {
        return doctorID;
    }

    //SETTERS
    public void setDoctor(Doctor doc){doctor = doc;}
}
