package MyMeds.App;

public class RequestForDoctor {
    private String patientUsername;
    private String drugName;
    private Integer requestID;

    public RequestForDoctor(String drugName, String patientID, Integer requestID) {
        this.patientUsername = patientID;
        this.drugName = drugName;
        this.requestID = requestID;
    }

    public String getPatientUsername() {
        return patientUsername;
    }

    public void setPatientUsername(String patientUsername) {
        this.patientUsername = patientUsername;
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
