package M1.S2.TPS.controller;

import M1.S2.TPS.entities.Identity;
import M1.S2.TPS.service.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
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
