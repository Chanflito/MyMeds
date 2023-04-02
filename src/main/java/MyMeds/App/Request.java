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

    @OneToMany//Many doctors can have assaigned to a Request, but the request is just for one doctor
    @JoinTable(name="doctor_request", joinColumns = @JoinColumn(name = "request_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    )
    @JsonIgnore
    private List<Doctor> assigned;

    public Request(){}//Constructor for spring

    public Request(String docUsername, String phUsername, String drugName){
        this.docUsername = docUsername;
        this.phUsername = phUsername;
        this.drugName = drugName;
        this.assigned = new ArrayList<>();
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

    //SETTERS
    public void addAssignedDoctor(Doctor doc){if(!assigned.contains(doc)){assigned.add(doc);}}
    public void removeAssignedDoctor(Doctor doc){if(assigned.contains(doc)){assigned.remove(doc);}}

}
