package MyMeds.App;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public
class Patient extends User{
    @Column
    private String mail;
    @Column
    private Integer healthInsuarence; //Awaits until being set

    public Patient(){}
    public Patient(Integer dni,String mail, String username, String password){
        super(dni, username, password);
        this.mail = mail;
    }

//GETTERS
    public Integer getDni(){
        return super.getPrimarykey();
    }
    public String getPassword(){
        return super.getPassword();
    }
    public String getUsername(){
        return super.getUsername();
    }

    public String getMail(){
        return this.mail;
    }

//METHODS

                                    //Patient gives the simpliest information to app
    public Request RequestRecipie(String docUsername, String phUsername, String drugName){
        //Mades a request for a drug that will be bought in a specific pharmacy
        return new Request(docUsername, phUsername, drugName);
    }

    public void Pay(){
        //Pays for its drugs
    }

    public void AddHealthInsuarence(Integer healthInsuarence){
        //Kind of SETTER
        this.healthInsuarence = healthInsuarence;
    }
}