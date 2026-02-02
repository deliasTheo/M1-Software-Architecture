package M1.S2.TPS.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import M1.S2.TPS.entities.Token;
import M1.S2.TPS.repository.TokenRepository;

@Service
@Transactional
public class TokenService {
    
    @Autowired
    private TokenRepository tokenRepository;
    
    public Token create(Token token) {
        if (token.getValue() == null || token.getValue().isEmpty()) {
            throw new IllegalArgumentException("Token value cannot be null or empty");
        }
        if (token.getExpiresAt() == null) {
            throw new IllegalArgumentException("Token expiration date cannot be null");
        }
        return tokenRepository.save(token);
    }
    

    public Optional<Token> getById(Long id) {
        return tokenRepository.findById(id);
    }
    
    public Optional<Token> getByValue(String value) {
        return tokenRepository.findByValue(value);
    }
    
    public List<Token> getAll() {
        return tokenRepository.findAll();
    }
    
    public Token update(Long id, Token tokenDetails) {
        Optional<Token> token = tokenRepository.findById(id);
        if (token.isEmpty()) {
            throw new RuntimeException("Token not found with id: " + id);
        }
        
        Token existingToken = token.get();
        if (tokenDetails.getValue() != null) {
            existingToken.setValue(tokenDetails.getValue());
        }
        if (tokenDetails.getExpiresAt() != null) {
            existingToken.setExpiresAt(tokenDetails.getExpiresAt());
        }
        
        return tokenRepository.save(existingToken);
    }
    
    public void delete(Long id) {
        if (!tokenRepository.existsById(id)) {
            throw new RuntimeException("Token not found with id: " + id);
        }
        tokenRepository.deleteById(id);
    }
    
    public List<Token> getExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        return tokenRepository.findAll()
                .stream()
                .filter(token -> token.getExpiresAt().isBefore(now))
                .toList();
    }
}
