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

    public String email;
    public String password;
    
   
}
