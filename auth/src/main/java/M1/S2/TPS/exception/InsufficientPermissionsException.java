package M1.S2.TPS.exception;

public class InsufficientPermissionsException extends RuntimeException {
    public InsufficientPermissionsException(String service) {
        super("Droits insuffisants pour le service demandé: " + service);
    }
}
