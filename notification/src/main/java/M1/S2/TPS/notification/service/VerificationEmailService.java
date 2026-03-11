package M1.S2.TPS.notification.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class VerificationEmailService {

    private final JavaMailSender mailSender;

    @Value("${app.public-base-url:http://localhost:8080}")
    private String publicBaseUrl;

    @Value("${app.mail.from:no-reply@m1.local}")
    private String mailFrom;

    public VerificationEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String to, String tokenId, String tokenClear) {
        String verificationLink = publicBaseUrl + "/auth_service/identity/validate_email?tokenId="
                + tokenId + "&t=" + tokenClear;

        String body = "Bonjour,\n\n"
                + "Merci pour votre inscription. Cliquez sur ce lien pour valider votre e-mail :\n"
                + verificationLink + "\n\n"
                + "Ce lien expire bientôt.\n\n"
                + "M1 Software Architecture";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(to);
        message.setSubject("Vérification de votre e-mail");
        message.setText(body);

        mailSender.send(message);
    }
}
