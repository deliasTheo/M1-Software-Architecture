package M1.S2.TPS.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "validation_token")
public class ValidationToken extends AbstractToken {

  @Column(nullable = false)
  private boolean used = false;
}