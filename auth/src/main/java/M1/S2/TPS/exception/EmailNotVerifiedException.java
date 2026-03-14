package M1.S2.TPS.exception;

public class EmailNotVerifiedException extends RuntimeException {
    public EmailNotVerifiedException() {
        super("Email non validé");
    }
}
