package M1.S2.TPS.notification.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MockUserRegisteredRequest(
        @NotBlank String userId,
        @NotBlank @Email String email
) {
}
