package MyMeds.App;

class Patient extends User{

    private String mail;
    private Integer health_insuarence;//Awaits until being set

    public Patient(Integer dni, String username, String password, String mail){
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
    public Request RequestRecipie(String doc_username, String ph_username, String durg_name){
        //Mades a request for a drug that will be bought in a specific pharmacy
        return new Request(doc_username, ph_username, durg_name);
    }

    public void Pay(){
        //Pays for its drugs
    }

    public void AddHealthInsuarence(Integer health_insuarence){
        //Kind of SETTER
        this.health_insuarence = health_insuarence;
    }
}