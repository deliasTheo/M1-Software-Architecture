package M1.S2.TPS.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TokenDTO {
    
    private String value;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime expiresAt;
    
    public TokenDTO() {
    }
    
    public TokenDTO(String value, LocalDateTime expiresAt) {
        this.value = value;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = expiresAt;
    }
    
   
}
