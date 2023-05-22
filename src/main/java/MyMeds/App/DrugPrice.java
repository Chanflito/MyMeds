package MyMeds.App;

import jakarta.persistence.*;

@Entity
public class DrugPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "drug_id")
    private Drug drug;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    private double price=0.0; // Por default es 0 al momento de crearse el medicamento.

    public DrugPrice(Drug drug, Pharmacy pharmacy, double price) {
        this.drug = drug;
        this.pharmacy = pharmacy;
        this.price = price;
    }

    public DrugPrice() {
    }


    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
