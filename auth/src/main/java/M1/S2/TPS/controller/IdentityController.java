package M1.S2.TPS.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import M1.S2.TPS.dto.IdentityDTO;
import M1.S2.TPS.dto.TokenDTO;
import M1.S2.TPS.service.AuthentificationService;
import M1.S2.TPS.service.EmailVerificationService;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/auth_service/identity")
@RequiredArgsConstructor
public class IdentityController {
    private final AuthentificationService authentificationService;
    private final EmailVerificationService emailVerificationService;
    
    // POST : /auth_service/identity/register
    // GET : /auth_service/identity/login
    // GET : /auth_service/identity/validate_email?token=abc123

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody IdentityDTO identity) {
        authentificationService.register(identity);
        return ResponseEntity.ok("Compte créé");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody IdentityDTO identity) {
        TokenDTO token = new TokenDTO(authentificationService.login(identity).getTokenHash());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/validate_email")
    public ResponseEntity<String> validateEmail(@RequestParam String token) {
        emailVerificationService.validateEmail(token);
        return ResponseEntity.ok("Email validé");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokenDTO token) {
        authentificationService.logout(token.getToken());
        return ResponseEntity.ok("Déconnexion réussie");
    }

}
