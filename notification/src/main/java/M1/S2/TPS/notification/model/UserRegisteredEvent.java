package M1.S2.TPS.notification.model;

public record UserRegisteredEvent(
        String type,
        String eventId,
        String occurredAt,
        Data data
) {
    public record Data(
            String userId,
            String email,
            String tokenClear
    ) {
    }
}
