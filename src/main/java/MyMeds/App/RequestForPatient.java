package MyMeds.App;

public class RequestForPatient {
    private String doctorUsername;
    private String drugName;
    private Integer requestID;

    public RequestForPatient(String doctorUsername, String drugName, Integer requestID) {
        this.doctorUsername = doctorUsername;
        this.drugName = drugName;
        this.requestID = requestID;
    }
    public RequestForPatient(){}

    public String getDoctorUsername() {
        return doctorUsername;
    }

    public void setDoctorUsername(String doctorUsername) {
        this.doctorUsername = doctorUsername;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public Integer getRequestID() {
        return requestID;
    }

    public void setRequestID(Integer requestID) {
        this.requestID = requestID;
    }
}
