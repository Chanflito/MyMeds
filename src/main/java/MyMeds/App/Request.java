package MyMeds.App;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Request {
    //Data on a request should never be null
    @Id
    private Integer key = hashCode();
    @Column(nullable = false)
    private String docUsername;
    @Column(nullable = false)
    private String phUsername;
    @Column(nullable = false)
    private String drugName;

    @ManyToOne(cascade = CascadeType.PERSIST)//Many doctors can have assaigned to a Request, but the request is just for one doctor
    @JoinColumn(name="doctor")
    @JsonIgnore
    private Doctor doctor;

    public Request(){}//Constructor for spring

    public Request(String docUsername, String phUsername, String drugName){
        this.docUsername = docUsername;
        this.phUsername = phUsername;
        this.drugName = drugName;
    }

    //GETTERS
    public Integer getPrimaryKey(){
        return key;
    }
    public String getDocUsername(){
        return docUsername;
    }
    public String getPhUsername(){
        return phUsername;
    }
    public String getDrugName(){
        return drugName;
    }
    public Doctor getDoctorAssigned(){return doctor;}

    //SETTERS
    public void setDoctor(Doctor doc){doctor = doc;}
}
