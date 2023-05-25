package MyMeds.App;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


@Entity
public class Drug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String brandName;
    @Column
    private String strength;

    @Column
    private String dosageForm;

    @Column
    private Integer stock;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Pharmacy pharmacy;
    public Drug() {
    }

    public Drug(String brandName, String strength,String dosageForm,Integer stock) {
        this.brandName = brandName;
        this.strength = strength;
        this.dosageForm=dosageForm;
        this.stock=stock;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }
}
