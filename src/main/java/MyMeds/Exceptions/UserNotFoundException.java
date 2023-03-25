package MyMeds.Exceptions;
//Cuando haces una excepcion si o si tenes que hacer un advice, sino te lo imprime por consola.
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Integer primarykey){
        super("Could not find user with ID "+ primarykey);
    }

    public UserNotFoundException(){
        super("Invalid email or password!");
    }
}
