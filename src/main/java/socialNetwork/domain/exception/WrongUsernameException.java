package socialNetwork.domain.exception;

public class WrongUsernameException extends RuntimeException{
    public WrongUsernameException() {
        super("the username is invalid");
    }
}
