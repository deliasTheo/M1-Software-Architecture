package M1.S2.TPS.notification.messaging;

import M1.S2.TPS.notification.config.MessagingProperties;
import M1.S2.TPS.notification.model.UserRegisteredEvent;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class MockEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final MessageConverter messageConverter;
    private final MessagingProperties messagingProperties;

    public MockEventPublisher(
            RabbitTemplate rabbitTemplate,
            MessageConverter messageConverter,
            MessagingProperties messagingProperties
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.messageConverter = messageConverter;
        this.messagingProperties = messagingProperties;
    }

    public UserRegisteredEvent publishMockUserRegistered(String userId, String email) {
        String eventId = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();
        String tokenId = "tok_" + UUID.randomUUID();
        String tokenClear = UUID.randomUUID().toString();

        UserRegisteredEvent event = new UserRegisteredEvent(
                "UserRegistered",
                eventId,
                OffsetDateTime.now().toString(),
                new UserRegisteredEvent.Data(userId, email, tokenId, tokenClear)
        );

        Message message = messageConverter.toMessage(event, new MessageProperties());
        message = MessageBuilder
                .withBody(message.getBody())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding(StandardCharsets.UTF_8.name())
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .setHeader("x-correlation-id", correlationId)
                .setHeader("x-schema-version", 1)
                .build();

        rabbitTemplate.send(
                messagingProperties.exchange(),
                messagingProperties.userRegisteredRoutingKey(),
                message
        );

        return event;
    }
}
