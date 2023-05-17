package MyMeds.Dto;

public class DoctorForPatient {

    private Integer doctorID;
    private String doctorUsername;

    public DoctorForPatient(Integer doctorID, String doctorUsername){
        this.doctorID = doctorID;
        this.doctorUsername = doctorUsername;
    }

    public Integer getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(Integer doctorID) {
        this.doctorID = doctorID;
    }

    public String getDoctorUsername() {
        return doctorUsername;
    }

    public void setDoctorUsername(String doctorUsername) {
        this.doctorUsername = doctorUsername;
    }

}
