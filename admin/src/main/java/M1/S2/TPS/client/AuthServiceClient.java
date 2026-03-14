package M1.S2.TPS.client;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import M1.S2.TPS.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthServiceClient {

    private final RestClient restClient;

    public String verifyRole(String authorizationHeader) {
        log.info("Vérification du rôle via auth service");
        
        try {
            String role = restClient.get()
                    .uri("/auth_service/authorization/verifyRole")
                    .header("Authorization", authorizationHeader)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        log.warn("Erreur d'authentification: {}", response.getStatusCode());
                        throw new UnauthorizedException("Token invalide ou expiré");
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        log.error("Erreur serveur auth: {}", response.getStatusCode());
                        throw new RuntimeException("Erreur du service d'authentification");
                    })
                    .body(String.class);
            
            log.info("Rôle vérifié: {}", role);
            return role;
        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erreur lors de la vérification du rôle", e);
            throw new UnauthorizedException("Erreur de vérification du token");
        }
    }
}
