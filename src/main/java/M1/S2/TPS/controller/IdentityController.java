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

import M1.S2.TPS.entities.Identity;
import M1.S2.TPS.service.IdentityService;

@RestController
@RequestMapping("/api/identities")
public class IdentityController {
    
    @Autowired
    private IdentityService identityService;
    
    @PostMapping
    public ResponseEntity<Identity> create(@RequestBody Identity identity) {
        try {
            Identity createdIdentity = identityService.create(identity);
            return new ResponseEntity<>(createdIdentity, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Identity> getById(@PathVariable Long id) {
        Optional<Identity> identity = identityService.getById(id);
        return identity.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/uid/{uid}")
    public ResponseEntity<Identity> getByUid(@PathVariable String uid) {
        Optional<Identity> identity = identityService.getByUid(uid);
        return identity.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping
    public ResponseEntity<List<Identity>> getAll() {
        List<Identity> identities = identityService.getAll();
        return new ResponseEntity<>(identities, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Identity> update(@PathVariable Long id, @RequestBody Identity identityDetails) {
        try {
            Identity updatedIdentity = identityService.update(id, identityDetails);
            return new ResponseEntity<>(updatedIdentity, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            identityService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
