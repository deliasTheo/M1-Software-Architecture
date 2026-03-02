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

import M1.S2.TPS.dto.TokenDTO;
import M1.S2.TPS.entities.Token;
import M1.S2.TPS.mapper.TokenMapper;
import M1.S2.TPS.service.TokenService;

@RestController
@RequestMapping("/api/tokens")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenMapper tokenMapper;

    @PostMapping
    public ResponseEntity<TokenDTO> create(@RequestBody TokenDTO token) {
        try {
            Token entity = tokenMapper.toEntity(token);
            Token created = tokenService.create(entity);
            return new ResponseEntity<>(tokenMapper.toDTO(created), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TokenDTO> getById(@PathVariable Long id) {
        Optional<Token> token = tokenService.getById(id);
        return token.map(e -> new ResponseEntity<>(tokenMapper.toDTO(e), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/value/{value}")
    public ResponseEntity<TokenDTO> getByValue(@PathVariable String value) {
        Optional<Token> token = tokenService.getByValue(value);
        return token.map(e -> new ResponseEntity<>(tokenMapper.toDTO(e), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<TokenDTO>> getAll() {
        List<Token> entities = tokenService.getAll();
        List<TokenDTO> dtos = entities.stream().map(tokenMapper::toDTO).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/expired")
    public ResponseEntity<List<TokenDTO>> getExpiredTokens() {
        List<Token> entities = tokenService.getExpiredTokens();
        List<TokenDTO> dtos = entities.stream().map(tokenMapper::toDTO).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TokenDTO> update(@PathVariable Long id, @RequestBody TokenDTO tokenDetails) {
        try {
            tokenDetails.setId(id);
            Token entity = tokenMapper.toEntity(tokenDetails);
            Token updated = tokenService.update(entity);
            return new ResponseEntity<>(tokenMapper.toDTO(updated), HttpStatus.OK);
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
