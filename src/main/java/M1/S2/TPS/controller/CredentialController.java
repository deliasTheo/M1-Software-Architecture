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

import M1.S2.TPS.entities.Credential;
import M1.S2.TPS.service.CredentialService;

@RestController
@RequestMapping("/api/credentials")
public class CredentialController {
    
    @Autowired
    private CredentialService credentialService;
    
    @PostMapping
    public ResponseEntity<Credential> create(@RequestBody Credential credential) {
        try {
            Credential createdCredential = credentialService.create(credential);
            return new ResponseEntity<>(createdCredential, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Credential> getById(@PathVariable Long id) {
        Optional<Credential> credential = credentialService.getById(id);
        return credential.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping
    public ResponseEntity<List<Credential>> getAll() {
        List<Credential> credentials = credentialService.getAll();
        return new ResponseEntity<>(credentials, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Credential> update(@PathVariable Long id, @RequestBody Credential credentialDetails) {
        try {
            Credential updatedCredential = credentialService.update(id, credentialDetails);
            return new ResponseEntity<>(updatedCredential, HttpStatus.OK);
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
