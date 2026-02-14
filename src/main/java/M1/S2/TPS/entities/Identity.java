package M1.S2.TPS.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "identities")
public class Identity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String uid;
    
    public Identity() {
    }
    
    public Identity(String uid) {
        this.uid = uid;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUid() {
        return uid;
    }
    
    public void setUid(String uid) {
        this.uid = uid;
    }
}
