package M1.S2.TPS.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.mq")
public record MessagingProperties(
        String exchange,
        String deadLetterExchange,
        String userRegisteredRoutingKey,
        String userRegisteredQueue,
        String userRegisteredDlqRoutingKey,
        String userRegisteredDlq
) {
}
