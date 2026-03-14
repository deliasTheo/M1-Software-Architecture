package M1.S2.TPS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import M1.S2.TPS.config.AuthServiceProperties;
import M1.S2.TPS.config.MessagingProperties;

@SpringBootApplication
@EnableConfigurationProperties({MessagingProperties.class, AuthServiceProperties.class})
public class AdminServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminServiceApplication.class, args);
    }
}
