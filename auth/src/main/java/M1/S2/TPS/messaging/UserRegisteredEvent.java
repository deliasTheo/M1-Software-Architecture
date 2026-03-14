package M1.S2.TPS.messaging;

public record UserRegisteredEvent(
        String type,
        String eventId,
        String occurredAt,
        UserRegisteredData data
) {

    public record UserRegisteredData(
            Integer userId,
            String email,
            String tokenClear
    ) {
    }
}
