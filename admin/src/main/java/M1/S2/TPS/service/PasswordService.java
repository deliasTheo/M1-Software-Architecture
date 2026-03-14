package M1.S2.TPS.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import M1.S2.TPS.exception.InvalidDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordService {
    private final BCryptPasswordEncoder passwordEncoder;

    public String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new InvalidDataException("Le mot de passe ne peut pas être vide");
        }
        checkPasswordStrength(password);
        log.info("Hachage du mot de passe");
        return passwordEncoder.encode(password);
    }

    public void checkPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            throw new InvalidDataException("Le mot de passe ne peut pas être vide");
        }

        StringBuilder errors = new StringBuilder();

        if (password.length() < 8) {
            errors.append("Le mot de passe doit contenir au moins 8 caractères. ");
        }
        if (!password.matches(".*[A-Z].*")) {
            errors.append("Le mot de passe doit contenir au moins une majuscule. ");
        }
        if (!password.matches(".*[a-z].*")) {
            errors.append("Le mot de passe doit contenir au moins une minuscule. ");
        }
        if (!password.matches(".*[0-9].*")) {
            errors.append("Le mot de passe doit contenir au moins un chiffre. ");
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?].*")) {
            errors.append("Le mot de passe doit contenir au moins un caractère spécial. ");
        }

        if (errors.length() > 0) {
            log.warn("Mot de passe faible: {}", errors.toString());
            throw new InvalidDataException(errors.toString());
        }

        log.info("Le mot de passe respecte les critères de sécurité");
    }
}
