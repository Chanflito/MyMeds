package MyMeds.App;

public class Recipe {
    private String doc_signature;
    private Integer doc_regist_number;
    private String drug_name;
    private Integer ph_regist_number;
    private boolean mark = false;
    private Integer key = hashCode();

    public Recipe(String doc_dignature, Integer doc_regist_number, String drug_name, Integer ph_regist_number){
        this.doc_signature = doc_dignature;
        this.doc_regist_number = doc_regist_number;
        this.drug_name = drug_name;
        this.ph_regist_number = ph_regist_number; 
    }

//GETTERS AND SETTERS
    public void SetMark(){
        this.mark = true;
    }
    public Integer getPrimaryKey(){
        return key;
    }
    public String getDocSignature(){
        return doc_signature;
    }
    public Integer getDocRegistNumber(){
        return doc_regist_number;
    }
    public String getDrugName(){
        return drug_name;
    }
    public Integer ph_regist_number(){
        return ph_regist_number;
    }
}
