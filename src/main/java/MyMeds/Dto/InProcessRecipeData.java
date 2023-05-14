package MyMeds.Dto;

public class InProcessRecipeData {
    private String drugName;
    private Integer docId;
    public InProcessRecipeData(){}
    public InProcessRecipeData(String drugName, Integer docId){
        this.drugName = drugName;
        this.docId = docId;
    }

    public String getDrugName() {return drugName;}
    public Integer getDocId(){return docId;}
}
