package MyMeds.Exceptions;

public class UserRegisteredException extends RuntimeException{
    public UserRegisteredException(){
        super("User is already registered.");
    }
}
