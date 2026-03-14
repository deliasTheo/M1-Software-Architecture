package M1.S2.TPS.exception;

public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException(String service) {
        super("Service inexistant: " + service);
    }
}
