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
    
    /**
     * Service contenant la logique métier
     */
    @Autowired
    private IdentityService identityService;
    
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
            IdentityDTO created = identityService.create(dto);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // uid invalide ou existe déjà
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
        Optional<IdentityDTO> dto = identityService.getById(id);
        return dto.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
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
        Optional<IdentityDTO> dto = identityService.getByUid(uid);
        return dto.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
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
        List<IdentityDTO> identities = identityService.getAll();
        return new ResponseEntity<>(identities, HttpStatus.OK);
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
            // S'assurer que l'ID dans le DTO correspond à l'ID dans l'URL
            dto.setId(id);
            IdentityDTO updated = identityService.update(dto);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // uid invalide (existe déjà)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RuntimeException e) {
            // Identité non trouvée
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
