package M1.S2.TPS.mapper;

import org.mapstruct.Mapper;

import M1.S2.TPS.dto.IdentityDTO;
import M1.S2.TPS.entities.Identity;

/**
 * Mapper pour convertir entre Identity (Entity) et IdentityDTO
 * 
 * MapStruct génère automatiquement l'implémentation de cette interface à la compilation.
 * L'implémentation générée se trouve dans : target/generated-sources/annotations/
 * 
 * @Mapper(componentModel = "spring") indique à MapStruct de générer un Bean Spring
 * qui pourra être injecté avec @Autowired dans les Services
 */
@Mapper(componentModel = "spring")
public interface IdentityMapper {
    
    /**
     * Convertit une Entity Identity en DTO IdentityDTO
     * 
     * Utilisé pour envoyer les données au client (Controller → Client)
     * 
     * MapStruct mappe automatiquement les champs qui ont le même nom :
     * - entity.id → dto.id
     * - entity.uid → dto.uid
     * 
     * @param entity L'entité Identity à convertir
     * @return Le DTO correspondant
     */
    IdentityDTO toDTO(Identity entity);
    
    /**
     * Convertit un DTO IdentityDTO en Entity Identity
     * 
     * Utilisé pour sauvegarder les données en base (Client → Service → Repository)
     * 
     * MapStruct mappe automatiquement :
     * - dto.id → entity.id
     * - dto.uid → entity.uid
     * 
     * @param dto Le DTO à convertir
     * @return L'entité correspondante
     */
    Identity toEntity(IdentityDTO dto);
}
