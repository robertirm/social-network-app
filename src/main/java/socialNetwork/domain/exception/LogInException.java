package socialNetwork.domain.exception;

public class LogInException extends RuntimeException {
    public LogInException() {
        super("please log in");
    }
}
