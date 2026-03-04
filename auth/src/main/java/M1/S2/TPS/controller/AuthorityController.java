package M1.S2.TPS.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth_service/authorization")
public class AuthorityController {

    // POST : /auth_service/authorization/validate
    // headers : Authorization: Bearer <token>
    // X-Target-Service: A | B 

    @PostMapping("/validate")
    public ResponseEntity<String> validate(@RequestHeader("Authorization") String token, @RequestHeader("X-Target-Service") String targetService) {
        return ResponseEntity.ok("Accès autorisé");
    }   

}
