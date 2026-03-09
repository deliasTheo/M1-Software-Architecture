package M1.S2.TPS.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Email déjà existant: " + email);
    }
}
