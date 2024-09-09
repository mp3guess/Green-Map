package tis.techacademy.green_map.exception.authentication;

public class ForgotPasswordCouldNotSendEmailException extends RuntimeException {
    public ForgotPasswordCouldNotSendEmailException(String message) {
        super(message);
    }
}
