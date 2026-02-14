package M1.S2.TPS.mapper;

import org.mapstruct.Mapper;

import M1.S2.TPS.dto.TokenDTO;
import M1.S2.TPS.entities.Token;

@Mapper(componentModel = "spring")
public interface TokenMapper {

    TokenDTO toDTO(Token entity);

    Token toEntity(TokenDTO dto);
}
