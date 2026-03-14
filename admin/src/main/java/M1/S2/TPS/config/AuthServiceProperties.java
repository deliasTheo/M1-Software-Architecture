package M1.S2.TPS.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.auth-service")
public record AuthServiceProperties(
        String url
) {
}
