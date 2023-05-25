package MyMeds.App;

import jakarta.persistence.*;

@Entity
public class StockPharmacy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    private Pharmacy pharmacy;

    @ManyToOne
    private Drug drug;

    @Column
    private Integer stock=0; //Por defecto tenemos 0 cantidad de medicamentos.


    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
