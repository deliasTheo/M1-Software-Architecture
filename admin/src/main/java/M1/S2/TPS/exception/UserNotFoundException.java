package M1.S2.TPS.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("Utilisateur non trouvé: " + email);
    }
}
