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

import M1.S2.TPS.dto.AuthorityDTO;
import M1.S2.TPS.entities.Authority;
import M1.S2.TPS.mapper.AuthorityMapper;
import M1.S2.TPS.service.AuthorityService;

@RestController
@RequestMapping("/api/authorities")
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private AuthorityMapper authorityMapper;

    @PostMapping
    public ResponseEntity<AuthorityDTO> create(@RequestBody AuthorityDTO authority) {
        try {
            Authority entity = authorityMapper.toEntity(authority);
            Authority created = authorityService.create(entity);
            return new ResponseEntity<>(authorityMapper.toDTO(created), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorityDTO> getById(@PathVariable Long id) {
        Optional<Authority> authority = authorityService.getById(id);
        return authority.map(e -> new ResponseEntity<>(authorityMapper.toDTO(e), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<AuthorityDTO> getByName(@PathVariable String name) {
        Optional<Authority> authority = authorityService.getByName(name);
        return authority.map(e -> new ResponseEntity<>(authorityMapper.toDTO(e), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<AuthorityDTO>> getAll() {
        List<Authority> entities = authorityService.getAll();
        List<AuthorityDTO> dtos = entities.stream().map(authorityMapper::toDTO).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorityDTO> update(@RequestBody AuthorityDTO authorityDetails) {
        try {
            Authority entity = authorityMapper.toEntity(authorityDetails);
            Authority updated = authorityService.update(entity);
            return new ResponseEntity<>(authorityMapper.toDTO(updated), HttpStatus.OK);
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
