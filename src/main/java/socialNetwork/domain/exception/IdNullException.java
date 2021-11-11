package socialNetwork.domain.exception;

public class IdNullException extends RuntimeException{
    public IdNullException() {
        super("id must not be null");
    }
}
