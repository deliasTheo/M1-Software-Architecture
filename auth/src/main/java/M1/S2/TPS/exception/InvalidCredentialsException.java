package M1.S2.TPS.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Mauvais email ou mot de passe");
    }
}
