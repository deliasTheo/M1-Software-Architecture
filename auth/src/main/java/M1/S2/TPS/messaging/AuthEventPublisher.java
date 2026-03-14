package M1.S2.TPS.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import M1.S2.TPS.config.MessagingProperties;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final MessagingProperties messagingProperties;

    public void publishUserRegistered(UserRegisteredEvent event) {
        String payload = toJson(event);
        rabbitTemplate.convertAndSend(
                messagingProperties.exchange(),
                messagingProperties.userRegisteredRoutingKey(),
                payload
        );
    }

    private String toJson(UserRegisteredEvent event) {
        UserRegisteredEvent.UserRegisteredData data = event.data();
        return String.format(
                "{\"type\":\"%s\",\"eventId\":\"%s\",\"occurredAt\":\"%s\",\"data\":{\"userId\":%d,\"email\":\"%s\",\"tokenClear\":\"%s\"}}",
                esc(event.type()),
                esc(event.eventId()),
                esc(event.occurredAt()),
                data.userId(),
                esc(data.email()),
                esc(data.tokenClear())
        );
    }

    private String esc(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
