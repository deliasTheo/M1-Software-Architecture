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

import M1.S2.TPS.entities.Authority;
import M1.S2.TPS.service.AuthorityService;

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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
