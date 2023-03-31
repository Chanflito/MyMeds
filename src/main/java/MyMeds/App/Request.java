package MyMeds.App;

import jakarta.persistence.*;

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

}
