package M1.S2.TPS.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TokenDTO {
    private Long id;
    
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
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
