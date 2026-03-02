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

import M1.S2.TPS.dto.CredentialDTO;
import M1.S2.TPS.entities.Credential;
import M1.S2.TPS.mapper.CredentialMapper;
import M1.S2.TPS.service.CredentialService;

@RestController
@RequestMapping("/api/credentials")
public class CredentialController {

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private CredentialMapper credentialMapper;

    @PostMapping
    public ResponseEntity<CredentialDTO> create(@RequestBody CredentialDTO credential) {
        try {
            Credential entity = credentialMapper.toEntity(credential);
            Credential created = credentialService.create(entity);
            return new ResponseEntity<>(credentialMapper.toDTO(created), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CredentialDTO> getById(@PathVariable Long id) {
        Optional<Credential> credential = credentialService.getById(id);
        return credential.map(e -> new ResponseEntity<>(credentialMapper.toDTO(e), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<CredentialDTO>> getAll() {
        List<Credential> entities = credentialService.getAll();
        List<CredentialDTO> dtos = entities.stream().map(credentialMapper::toDTO).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CredentialDTO> update(@PathVariable Long id, @RequestBody CredentialDTO credentialDetails) {
        try {
            credentialDetails.setId(id);
            Credential entity = credentialMapper.toEntity(credentialDetails);
            Credential updated = credentialService.update(entity);
            return new ResponseEntity<>(credentialMapper.toDTO(updated), HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            credentialService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
