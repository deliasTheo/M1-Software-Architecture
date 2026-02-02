package M1.S2.TPS.dto;

import lombok.Data;

@Data
public class IdentityDTO {
    
    private String uid;
    
    public IdentityDTO() {
    }
    
    public IdentityDTO(String uid) {
        this.uid = uid;
    }
    
}
