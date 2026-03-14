package M1.S2.TPS.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import M1.S2.TPS.exception.InvalidDataException;
import M1.S2.TPS.entities.Credential;
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
        // Vérifier la force du mot de passe avant de le hascher
        checkPasswordStrength(password);
        log.info("Hachage du mot de passe");
        return passwordEncoder.encode(password);
    }

    public boolean verifyPassword(String password, String hash) {
        if (password == null || hash == null) {
            log.warn("Tentative de vérification avec password ou hash null");
            return false;
        }
        log.info("Vérification du mot de passe");
        return passwordEncoder.matches(password, hash);
    }

    public boolean verifyPassword(Object identity, String password) {
        if (identity == null || password == null) {
            log.warn("Tentative de vérification avec identity ou password null");
            return false;
        }
        try {
            M1.S2.TPS.entities.Identity id = (M1.S2.TPS.entities.Identity) identity;
            Credential credential = id.getCredential();
            if (credential == null) {
                log.warn("Aucune credential trouvée pour l'identity");
                return false;
            }
            log.info("Vérification du mot de passe");
            return passwordEncoder.matches(password, credential.getPasswordHash());
        } catch (ClassCastException e) {
            log.error("Erreur lors du cast de l'identity", e);
            return false;
        }
    }

    /**
     * - Min 8 caractères
     * - Au moins 1 majuscule
     * - Au moins 1 minuscule
     * - Au moins 1 chiffre
     * - Au moins 1 caractère spécial
    **/
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
            errors.append("Le mot de passe doit contenir au least une minuscule. ");
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
