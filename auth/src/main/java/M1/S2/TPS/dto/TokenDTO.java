package M1.S2.TPS.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenDTO {
    public String token;

    public TokenDTO(String token) {
        this.token = token;
    }
}
