package M1.S2.TPS.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import M1.S2.TPS.dto.TokenDTO;
import M1.S2.TPS.entities.Token;
import M1.S2.TPS.mapper.TokenMapper;
import M1.S2.TPS.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TokenService {
    
    @Autowired
    private TokenRepository tokenRepository;
    private TokenMapper TokenMapper;
    
    public TokenDTO create(TokenDTO token) {
        Token tokenEntity = TokenMapper.toEntity(token);
        if (token.getValue() == null || token.getValue().isEmpty()) {
            throw new IllegalArgumentException("Token value cannot be null or empty");
        }
        if (token.getExpiresAt() == null) {
            throw new IllegalArgumentException("Token expiration date cannot be null");
        }
        return TokenMapper.toDTO(tokenRepository.save(tokenEntity));
    }
    

    public Optional<TokenDTO> getById(Long id) {
        return tokenRepository.findById(id).map(TokenMapper::toDTO);
    }
    
    public Optional<TokenDTO> getByValue(String value) {
        return tokenRepository.findByValue(value).map(TokenMapper::toDTO);
    }
    
    public List<TokenDTO> getAll() {
        return tokenRepository.findAll().stream().map(TokenMapper::toDTO).collect(Collectors.toList());
    }
    
    public TokenDTO update(TokenDTO tokenDetails) {
        Optional<Token> token = tokenRepository.findById(tokenDetails.getId());
        if (token.isEmpty()) {
            throw new RuntimeException("Token not found with id: " + tokenDetails.getId());
        }
        
        Token existingToken = token.get();
        if (tokenDetails.getValue() != null) {
            existingToken.setValue(tokenDetails.getValue());
        }
        if (tokenDetails.getExpiresAt() != null) {
            existingToken.setExpiresAt(tokenDetails.getExpiresAt());
        }
        TokenDTO tokenz = TokenMapper.toDTO(tokenRepository.save(existingToken));
        return tokenz;
    }
    
    public void delete(Long id) {
        if (!tokenRepository.existsById(id)) {
            throw new RuntimeException("Token not found with id: " + id);
        }
        tokenRepository.deleteById(id);
    }
    
    public List<TokenDTO> getExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        return tokenRepository.findAll().stream()
                .filter(token -> token.getExpiresAt().isBefore(now))
                .map(TokenMapper::toDTO)
                .collect(Collectors.toList());
    }
}
