package M1.S2.TPS.controller;

import M1.S2.TPS.entities.Authority;
import M1.S2.TPS.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authorities")
public class AuthorityController {
    
    @Autowired
    private AuthorityService authorityService;
    
    @PostMapping
    public ResponseEntity<Authority> create(@RequestBody Authority authority) {
        try {
            Authority createdAuthority = authorityService.create(authority);
            return new ResponseEntity<>(createdAuthority, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Authority> getById(@PathVariable Long id) {
        Optional<Authority> authority = authorityService.getById(id);
        return authority.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<Authority> getByName(@PathVariable String name) {
        Optional<Authority> authority = authorityService.getByName(name);
        return authority.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping
    public ResponseEntity<List<Authority>> getAll() {
        List<Authority> authorities = authorityService.getAll();
        return new ResponseEntity<>(authorities, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Authority> update(@PathVariable Long id, @RequestBody Authority authorityDetails) {
        try {
            Authority updatedAuthority = authorityService.update(id, authorityDetails);
            return new ResponseEntity<>(updatedAuthority, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            authorityService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
