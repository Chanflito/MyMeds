package MyMeds.App;

public class Recipe {
    private String docSignature;
    private Integer docRegistNumber;
    private String drugName;
    private Integer phRegistNumber;
    private boolean mark = false;
    private Integer key = hashCode();

    public Recipe(String docSignature, Integer docRegistNumber, String drugName, Integer phRegistNumber){
        this.docSignature = docSignature;
        this.docRegistNumber = docRegistNumber;
        this.drugName = drugName;
        this.phRegistNumber = phRegistNumber;
    }

//GETTERS AND SETTERS
    public void SetMark(){
        this.mark = true;
    }
    public Integer getPrimaryKey(){
        return key;
    }
    public String getDocSignature(){
        return docSignature;
    }
    public Integer getDocRegistNumber(){
        return docRegistNumber;
    }
    public String getDrugName(){
        return drugName;
    }
    public Integer ph_regist_number(){
        return phRegistNumber;
    }
}
