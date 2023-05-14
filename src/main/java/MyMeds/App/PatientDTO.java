package MyMeds.App;

public class PatientDTO {
    String username;
    Integer dni;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public PatientDTO(String username, Integer dni) {
        this.username = username;
        this.dni = dni;
    }
}
