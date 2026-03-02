package M1.S2.TPS.dto;

import lombok.Data;

@Data
public class CredentialDTO {
    
    private Long id;
    
    private String value;
    
    public CredentialDTO() {
    }
    
    public CredentialDTO(String value) {
        this.value = value;
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
}
