package M1.S2.TPS.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import M1.S2.TPS.service.AuthorizationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth_service/authorization")
@RequiredArgsConstructor
public class AuthorityController {
    private final AuthorizationService authorizationService;

    // POST : /auth_service/authorization/validate
    // headers : Authorization: Bearer <token>
    // X-Target-Service: A | B 

    @PostMapping("/validate")
    public ResponseEntity<String> validate(
            @RequestHeader("Authorization") String authorizationHeader, 
            @RequestHeader("X-Target-Service") String targetService) {
        authorizationService.validateAccess(authorizationHeader, targetService);
        return ResponseEntity.ok("Accès autorisé");
    }
}
