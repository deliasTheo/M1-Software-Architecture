package M1.S2.TPS.dto;

import lombok.Data;

/**
 * DTO (Data Transfer Object) pour l'entité Identity
 * 
 * Ce DTO est utilisé pour transférer les données entre le Controller et le Service.
 * Il permet de ne pas exposer directement l'entité (Entity) au client.
 * 
 * @Data de Lombok génère automatiquement :
 * - getters pour tous les champs
 * - setters pour tous les champs
 * - toString()
 * - equals() et hashCode()
 */
@Data
public class IdentityDTO {
    
    /**
     * Identifiant technique (clé primaire en base de données)
     */
    private Long id;
    
    /**
     * Identifiant unique de l'identité (ex: "user123", "app-service-001", etc.)
     * Ce champ doit être unique dans tout le système
     */
    private String uid;
    
    /**
     * Constructeur par défaut
     * Nécessaire pour la désérialisation JSON
     */
    public IdentityDTO() {
    }
    
    /**
     * Constructeur avec uid
     * Utile pour créer rapidement un DTO avec juste l'uid
     * 
     * @param uid L'identifiant unique
     */
    public IdentityDTO(String uid) {
        this.uid = uid;
    }
    
    /**
     * Constructeur complet
     * 
     * @param id L'identifiant technique
     * @param uid L'identifiant unique
     */
    public IdentityDTO(Long id, String uid) {
        this.id = id;
        this.uid = uid;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
