package M1.S2.TPS.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import M1.S2.TPS.entities.Token;
import M1.S2.TPS.service.TokenService;

@RestController
@RequestMapping("/api/tokens")
public class TokenController {
    
    @Autowired
    private TokenService tokenService;
    
    @PostMapping
    public ResponseEntity<Token> create(@RequestBody Token token) {
        try {
            Token createdToken = tokenService.create(token);
            return new ResponseEntity<>(createdToken, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Token> getById(@PathVariable Long id) {
        Optional<Token> token = tokenService.getById(id);
        return token.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/value/{value}")
    public ResponseEntity<Token> getByValue(@PathVariable String value) {
        Optional<Token> token = tokenService.getByValue(value);
        return token.map(value2 -> new ResponseEntity<>(value2, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping
    public ResponseEntity<List<Token>> getAll() {
        List<Token> tokens = tokenService.getAll();
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }
    
    @GetMapping("/expired")
    public ResponseEntity<List<Token>> getExpiredTokens() {
        List<Token> expiredTokens = tokenService.getExpiredTokens();
        return new ResponseEntity<>(expiredTokens, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Token> update(@PathVariable Long id, @RequestBody Token tokenDetails) {
        try {
            Token updatedToken = tokenService.update(id, tokenDetails);
            return new ResponseEntity<>(updatedToken, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            tokenService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
