package MyMeds.App;

public class Request {
    
    private String doc_username;
    private String ph_username;
    private String drug_name;
    private Integer key = hashCode();

    public Request(String doc_username, String ph_username, String drug_name){
        this.doc_username = doc_username;
        this.ph_username = ph_username;
        this.drug_name = drug_name;
    }

//GETTERS
    public Integer getPrimaryKey(){
        return key;
    }
    public String getDocUsername(){
        return doc_username;
    }
    public String getPhUsername(){
        return ph_username;
    }
    public String getDrugName(){
        return drug_name;
    }

}
