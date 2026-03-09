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

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/auth_service/identity")
@RequiredArgsConstructor
public class IdentityController {
    private final AuthentificationService authentificationService;
    
    // POST : /auth_service/identity/register
    // GET : /auth_service/identity/login
    // GET : /auth_service/identity/validate_email?token=abc123

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody IdentityDTO identity) {
        authentificationService.register(identity);
        return ResponseEntity.ok("Compte créé");
    }

    @GetMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody IdentityDTO identity) {
        TokenDTO token = new TokenDTO("token");
        return ResponseEntity.ok(token);
    }

    @GetMapping("/validate_email")
    public ResponseEntity<String> validateEmail(@RequestParam String token) {
        return ResponseEntity.ok("Email validé");
    }

}
