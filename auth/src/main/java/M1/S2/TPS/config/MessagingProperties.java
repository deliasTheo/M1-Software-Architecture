package M1.S2.TPS.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.mq")
public record MessagingProperties(
        String exchange,
        String userRegisteredRoutingKey
) {
}
