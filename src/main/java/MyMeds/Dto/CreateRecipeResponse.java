package MyMeds.Dto;

import java.util.List;

public class CreateRecipeResponse {
    private boolean sucess;
    private List<DrugDTO> drugDTOS;

    public CreateRecipeResponse(boolean sucess, List<DrugDTO> drugDTOS) {
        this.sucess = sucess;
        this.drugDTOS = drugDTOS;
    }

    public boolean isSucess() {
        return sucess;
    }

    public void setSucess(boolean sucess) {
        this.sucess = sucess;
    }

    public List<DrugDTO> getDrugDTOS() {
        return drugDTOS;
    }

    public void setDrugDTOS(List<DrugDTO> drugDTOS) {
        this.drugDTOS = drugDTOS;
    }
}