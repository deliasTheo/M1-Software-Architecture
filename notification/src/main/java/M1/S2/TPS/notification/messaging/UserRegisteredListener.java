package M1.S2.TPS.notification.messaging;

import M1.S2.TPS.notification.config.MessagingProperties;
import M1.S2.TPS.notification.model.UserRegisteredEvent;
import M1.S2.TPS.notification.service.VerificationEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredListener {

    private static final Logger logger = LoggerFactory.getLogger(UserRegisteredListener.class);

    private final VerificationEmailService verificationEmailService;
    private final MessagingProperties messagingProperties;

    public UserRegisteredListener(
            VerificationEmailService verificationEmailService,
            MessagingProperties messagingProperties
    ) {
        this.verificationEmailService = verificationEmailService;
        this.messagingProperties = messagingProperties;
    }

    @RabbitListener(queues = "${app.mq.user-registered-queue}")
    public void onUserRegistered(UserRegisteredEvent event) {
        if (event == null || event.data() == null) {
            throw new IllegalArgumentException("Invalid UserRegistered event: missing payload");
        }

        if (event.data().email() == null || event.data().email().isBlank()) {
            throw new IllegalArgumentException("Invalid UserRegistered event: missing email");
        }

        if (event.data().tokenClear() == null || event.data().tokenClear().isBlank()) {
            throw new IllegalArgumentException(
                    "Invalid UserRegistered event: missing tokenClear (required in simple TP flow)"
            );
        }

        logger.info("Received event type={} eventId={} queue={}",
                event.type(), event.eventId(), messagingProperties.userRegisteredQueue());

        verificationEmailService.sendVerificationEmail(
                event.data().email(),
                event.data().tokenClear()
        );

        logger.info("Verification email sent to={} for userId={}", event.data().email(), event.data().userId());
    }
}
