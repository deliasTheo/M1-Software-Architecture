package M1.S2.TPS.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Token invalide ou expiré");
    }
}
