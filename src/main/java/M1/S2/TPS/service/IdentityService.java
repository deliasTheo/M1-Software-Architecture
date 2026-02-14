package M1.S2.TPS.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import M1.S2.TPS.dto.IdentityDTO;
import M1.S2.TPS.entities.Identity;
import M1.S2.TPS.mapper.IdentityMapper;
import M1.S2.TPS.repository.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service pour gérer la logique métier des identités
 * 
 * @Service : Indique à Spring que c'est un Bean de service
 * @Transactional : Toutes les méthodes sont transactionnelles
 *                  Si une erreur se produit, tout est annulé (rollback)
 * 
 * Ce service :
 * - Reçoit des DTOs du Controller
 * - Valide les données
 * - Convertit DTO ↔ Entity via le Mapper
 * - Interagit avec le Repository
 * - Retourne des DTOs au Controller
 */
@Service
@Transactional
public class IdentityService {
    
    /**
     * Repository pour accéder aux données Identity en base
     */
    @Autowired
    private IdentityRepository identityRepository;
    
    /**
     * Mapper pour convertir entre DTO et Entity
     */
    @Autowired
    private IdentityMapper identityMapper;
    
    /**
     * Créer une nouvelle identité
     * 
     * Étapes :
     * 1. Valider que l'uid n'est pas vide
     * 2. Vérifier que l'uid n'existe pas déjà
     * 3. Convertir DTO → Entity
     * 4. Sauvegarder en base
     * 5. Convertir Entity → DTO et retourner
     * 
     * @param dto Le DTO contenant les données de l'identité à créer
     * @return Le DTO de l'identité créée (avec l'ID généré)
     * @throws IllegalArgumentException Si l'uid est invalide ou existe déjà
     */
    public IdentityDTO create(IdentityDTO dto) {
        // 1. Validation : uid ne doit pas être null ou vide
        if (dto.getUid() == null || dto.getUid().isEmpty()) {
            throw new IllegalArgumentException("UID cannot be null or empty");
        }
        
        // 2. Vérifier que l'uid n'existe pas déjà
        if (identityRepository.existsByUid(dto.getUid())) {
            throw new IllegalArgumentException("UID already exists: " + dto.getUid());
        }
        
        // 3. Convertir DTO → Entity
        Identity entity = identityMapper.toEntity(dto);
        
        // 4. Sauvegarder en base de données
        Identity savedEntity = identityRepository.save(entity);
        
        // 5. Convertir Entity → DTO et retourner
        return identityMapper.toDTO(savedEntity);
    }
    
    /**
     * Récupérer une identité par son ID technique
     * 
     * @param id L'identifiant technique de l'identité
     * @return Optional contenant le DTO si trouvé, vide sinon
     */
    public Optional<IdentityDTO> getById(Long id) {
        return identityRepository.findById(id)
            .map(identityMapper::toDTO);
    }
    
    /**
     * Récupérer une identité par son UID unique
     * 
     * @param uid L'identifiant unique de l'identité
     * @return Optional contenant le DTO si trouvé, vide sinon
     */
    public Optional<IdentityDTO> getByUid(String uid) {
        return identityRepository.findByUid(uid)
            .map(identityMapper::toDTO);
    }
    
    /**
     * Récupérer toutes les identités
     * 
     * @return Liste de tous les DTOs Identity
     */
    public List<IdentityDTO> getAll() {
        return identityRepository.findAll()
            .stream()
            .map(identityMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Mettre à jour une identité existante
     * 
     * Étapes :
     * 1. Vérifier que l'identité existe
     * 2. Vérifier que le nouvel uid n'est pas déjà utilisé
     * 3. Mettre à jour les champs
     * 4. Sauvegarder
     * 5. Retourner le DTO mis à jour
     * 
     * @param dto Le DTO contenant les nouvelles données
     * @return Le DTO de l'identité mise à jour
     * @throws RuntimeException Si l'identité n'existe pas
     * @throws IllegalArgumentException Si le nouvel uid existe déjà
     */
    public IdentityDTO update(IdentityDTO dto) {
        // 1. Vérifier que l'identité existe
        Optional<Identity> existingOpt = identityRepository.findById(dto.getId());
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Identity not found with id: " + dto.getId());
        }
        
        Identity existing = existingOpt.get();
        
        // 2. Mettre à jour le uid si fourni et différent
        if (dto.getUid() != null && !dto.getUid().equals(existing.getUid())) {
            // Vérifier que le nouveau uid n'existe pas déjà
            if (identityRepository.existsByUid(dto.getUid())) {
                throw new IllegalArgumentException("UID already exists: " + dto.getUid());
            }
            existing.setUid(dto.getUid());
        }
        
        // 3. Sauvegarder les modifications
        Identity updated = identityRepository.save(existing);
        
        // 4. Retourner le DTO mis à jour
        return identityMapper.toDTO(updated);
    }
    
    /**
     * Supprimer une identité par son ID
     * 
     * @param id L'identifiant technique de l'identité à supprimer
     * @throws RuntimeException Si l'identité n'existe pas
     */
    public void delete(Long id) {
        // Vérifier que l'identité existe avant de supprimer
        if (!identityRepository.existsById(id)) {
            throw new RuntimeException("Identity not found with id: " + id);
        }
        
        identityRepository.deleteById(id);
    }
}
