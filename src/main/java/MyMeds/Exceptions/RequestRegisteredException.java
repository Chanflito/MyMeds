package MyMeds.Exceptions;

public class RequestRegisteredException extends RuntimeException {
    public RequestRegisteredException(){
        super("Request is already done.");
    }
}

