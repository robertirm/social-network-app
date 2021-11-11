package socialNetwork.domain.exception;

public class EntityNullException extends RuntimeException{
    public EntityNullException() {
        super("entity must not be null");
    }
}
