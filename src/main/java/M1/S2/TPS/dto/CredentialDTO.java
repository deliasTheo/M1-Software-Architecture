package M1.S2.TPS.dto;

import lombok.Data;

@Data
public class CredentialDTO {
    
    
    private String value;
    
    public CredentialDTO() {
    }
    
    public CredentialDTO(String value) {
        this.value = value;
    }
       
}
