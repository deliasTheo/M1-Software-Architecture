package M1.S2.TPS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(AuthServiceProperties authServiceProperties) {
        return RestClient.builder()
                .baseUrl(authServiceProperties.url())
                .build();
    }
}
