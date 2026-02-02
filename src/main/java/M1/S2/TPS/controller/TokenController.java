package M1.S2.TPS.controller;

import M1.S2.TPS.entities.Token;
import M1.S2.TPS.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
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
