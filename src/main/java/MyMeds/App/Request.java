package MyMeds.App;

public class Request {
    
    private String docUsername;
    private String phUsername;
    private String drugName;
    private Integer key = hashCode();

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
