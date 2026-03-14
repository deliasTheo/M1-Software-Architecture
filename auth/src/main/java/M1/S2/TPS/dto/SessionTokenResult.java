package M1.S2.TPS.dto;

import M1.S2.TPS.entities.SessionToken;

public record SessionTokenResult(String rawToken, SessionToken sessionToken) {
}
