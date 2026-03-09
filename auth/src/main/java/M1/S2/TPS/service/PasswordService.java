package M1.S2.TPS.service;

import org.springframework.stereotype.Service;

import M1.S2.TPS.entities.Identity;

@Service
public class PasswordService {
    /*
        hashPassword(...)

        verifyPassword(...)

        checkPasswordStrength(...)
    */

    // todo : semy
    public String hashPassword(String password) {
        return password;
    }

    // todo : semy
    public boolean verifyPassword(Identity identity, String rawPassword) {
        return true;
    }
}
