package MyMeds.Exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Integer primarykey){
        super("Could not find user with ID "+ primarykey);
    }
}
