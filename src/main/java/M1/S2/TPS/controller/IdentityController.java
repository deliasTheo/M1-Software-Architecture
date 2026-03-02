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

import M1.S2.TPS.dto.IdentityDTO;
import M1.S2.TPS.entities.Identity;
import M1.S2.TPS.mapper.IdentityMapper;
import M1.S2.TPS.service.IdentityService;

/**
 * Controller REST pour gérer les identités
 * 
 * @RestController : Indique que c'est un controller REST (retourne du JSON)
 * @RequestMapping : Toutes les routes commencent par /api/identities
 * 
 * Ce controller :
 * - Reçoit les requêtes HTTP du client
 * - Appelle le Service pour faire le travail
 * - Retourne des ResponseEntity avec le bon code HTTP
 * 
 * Codes HTTP utilisés :
 * - 200 OK : Succès (GET, PUT)
 * - 201 CREATED : Ressource créée (POST)
 * - 204 NO_CONTENT : Suppression réussie (DELETE)
 * - 400 BAD_REQUEST : Données invalides
 * - 404 NOT_FOUND : Ressource non trouvée
 */
@RestController
@RequestMapping("/api/identities")
public class IdentityController {
    
    @Autowired
    private IdentityService identityService;

    @Autowired
    private IdentityMapper identityMapper;

    /**
     * Créer une nouvelle identité
     * 
     * Endpoint : POST /api/identities
     * Body : { "uid": "user123" }
     * 
     * @param dto Le DTO contenant les données de l'identité
     * @return ResponseEntity avec le DTO créé et code 201 CREATED
     *         ou code 400 BAD_REQUEST si données invalides
     */
    @PostMapping
    public ResponseEntity<IdentityDTO> create(@RequestBody IdentityDTO dto) {
        try {
            Identity entity = identityMapper.toEntity(dto);
            Identity created = identityService.create(entity);
            return new ResponseEntity<>(identityMapper.toDTO(created), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    /**
     * Récupérer une identité par son ID technique
     * 
     * Endpoint : GET /api/identities/{id}
     * Exemple : GET /api/identities/1
     * 
     * @param id L'identifiant technique
     * @return ResponseEntity avec le DTO et code 200 OK
     *         ou code 404 NOT_FOUND si non trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<IdentityDTO> getById(@PathVariable Long id) {
        Optional<Identity> entity = identityService.getById(id);
        return entity.map(e -> new ResponseEntity<>(identityMapper.toDTO(e), HttpStatus.OK))
                  .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * Récupérer une identité par son UID unique
     * 
     * Endpoint : GET /api/identities/uid/{uid}
     * Exemple : GET /api/identities/uid/user123
     * 
     * @param uid L'identifiant unique
     * @return ResponseEntity avec le DTO et code 200 OK
     *         ou code 404 NOT_FOUND si non trouvé
     */
    @GetMapping("/uid/{uid}")
    public ResponseEntity<IdentityDTO> getByUid(@PathVariable String uid) {
        Optional<Identity> entity = identityService.getByUid(uid);
        return entity.map(e -> new ResponseEntity<>(identityMapper.toDTO(e), HttpStatus.OK))
                  .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * Récupérer toutes les identités
     * 
     * Endpoint : GET /api/identities
     * 
     * @return ResponseEntity avec la liste des DTOs et code 200 OK
     */
    @GetMapping
    public ResponseEntity<List<IdentityDTO>> getAll() {
        List<Identity> entities = identityService.getAll();
        List<IdentityDTO> dtos = entities.stream().map(identityMapper::toDTO).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
    
    /**
     * Mettre à jour une identité existante
     * 
     * Endpoint : PUT /api/identities/{id}
     * Body : { "uid": "newuser456" }
     * 
     * @param id L'identifiant de l'identité à mettre à jour
     * @param dto Le DTO avec les nouvelles données
     * @return ResponseEntity avec le DTO mis à jour et code 200 OK
     *         ou code 404 NOT_FOUND si non trouvé
     *         ou code 400 BAD_REQUEST si données invalides
     */
    @PutMapping("/{id}")
    public ResponseEntity<IdentityDTO> update(@PathVariable Long id, @RequestBody IdentityDTO dto) {
        try {
            dto.setId(id);
            Identity entity = identityMapper.toEntity(dto);
            Identity updated = identityService.update(entity);
            return new ResponseEntity<>(identityMapper.toDTO(updated), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Supprimer une identité
     * 
     * Endpoint : DELETE /api/identities/{id}
     * 
     * @param id L'identifiant de l'identité à supprimer
     * @return ResponseEntity vide avec code 204 NO_CONTENT
     *         ou code 404 NOT_FOUND si non trouvé
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            identityService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            // Identité non trouvée
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
