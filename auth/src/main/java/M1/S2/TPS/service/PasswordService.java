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

    public String hashPassword(String password) {
        return password;
    }

    public boolean verifyPassword(Identity identity, String rawPassword) {
        return true; // todo
    }
}
