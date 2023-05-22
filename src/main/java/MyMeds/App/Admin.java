package MyMeds.App;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Admin extends User{
    @Transient
    private final UserType userType=UserType.ADMIN;

    public Admin(Integer id, String username, String password) {
        super(id, username, password);
    }

    public Admin(){}

    @ManyToMany
    @JoinTable(name="admin_drug",
                joinColumns = @JoinColumn(name = "admin_id",referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name="drug_id",referencedColumnName = "id"))
    @JsonIgnore
    private List<Drug> drugList=new ArrayList<>();

    public List<Drug> getDrugList() {
        return drugList;
    }

    public void setDrugList(List<Drug> drugList) {
        this.drugList = drugList;
    }

    @Override
    public UserType getUserType() {
        return userType;
    }
}
