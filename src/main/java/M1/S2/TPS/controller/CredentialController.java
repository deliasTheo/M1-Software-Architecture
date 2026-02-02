package M1.S2.TPS.controller;

import M1.S2.TPS.entities.Credential;
import M1.S2.TPS.service.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
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
