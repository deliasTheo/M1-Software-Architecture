package M1.S2.TPS.mapper;

import org.mapstruct.Mapper;

import M1.S2.TPS.dto.CredentialDTO;
import M1.S2.TPS.entities.Credential;

@Mapper(componentModel = "spring")
public interface CredentialMapper {

    CredentialDTO toDTO(Credential entity);

    Credential toEntity(CredentialDTO dto);
}