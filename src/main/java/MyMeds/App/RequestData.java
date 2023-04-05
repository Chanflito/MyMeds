package MyMeds.App;

public class RequestData {
    private String drugName;
    private Integer docId;
    public RequestData(){}
    public RequestData(String drugName, Integer docId){
        this.drugName = drugName;
        this.docId = docId;
    }

    public String getDrugName() {return drugName;}
    public Integer getDocId(){return docId;}
}
