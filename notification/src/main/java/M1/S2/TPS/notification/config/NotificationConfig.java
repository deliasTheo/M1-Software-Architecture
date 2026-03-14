package M1.S2.TPS.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(MessagingProperties.class)
public class NotificationConfig {

    @Bean
    public DirectExchange authEventsExchange(MessagingProperties properties) {
        return new DirectExchange(properties.exchange(), true, false);
    }

    @Bean
    public DirectExchange dlxExchange(MessagingProperties properties) {
        return new DirectExchange(properties.deadLetterExchange(), true, false);
    }

    @Bean
    public Queue userRegisteredQueue(MessagingProperties properties) {
        return QueueBuilder.durable(properties.userRegisteredQueue())
                .withArguments(Map.of(
                        "x-dead-letter-exchange", properties.deadLetterExchange(),
                        "x-dead-letter-routing-key", properties.userRegisteredDlqRoutingKey()
                ))
                .build();
    }

    @Bean
    public Queue userRegisteredDlq(MessagingProperties properties) {
        return QueueBuilder.durable(properties.userRegisteredDlq()).build();
    }

    @Bean
    public Binding userRegisteredBinding(
            Queue userRegisteredQueue,
            DirectExchange authEventsExchange,
            MessagingProperties properties
    ) {
        return BindingBuilder.bind(userRegisteredQueue)
                .to(authEventsExchange)
                .with(properties.userRegisteredRoutingKey());
    }

    @Bean
    public Binding userRegisteredDlqBinding(
            Queue userRegisteredDlq,
            DirectExchange dlxExchange,
            MessagingProperties properties
    ) {
        return BindingBuilder.bind(userRegisteredDlq)
                .to(dlxExchange)
                .with(properties.userRegisteredDlqRoutingKey());
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
