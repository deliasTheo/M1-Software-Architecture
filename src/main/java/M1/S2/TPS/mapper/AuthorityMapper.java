package M1.S2.TPS.mapper;

import org.mapstruct.Mapper;

import M1.S2.TPS.dto.AuthorityDTO;
import M1.S2.TPS.entities.Authority;

@Mapper(componentModel = "spring")
public interface AuthorityMapper {

    AuthorityDTO toDTO(Authority entity);

    Authority toEntity(AuthorityDTO dto);
}