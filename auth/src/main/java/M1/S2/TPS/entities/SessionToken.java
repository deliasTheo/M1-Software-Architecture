package M1.S2.TPS.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "session_token")
public class SessionToken extends AbstractToken {
}