package M1.S2.TPS.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import M1.S2.TPS.dto.CreateUserRequest;
import M1.S2.TPS.dto.DeleteUserRequest;
import M1.S2.TPS.dto.ModifyRoleRequest;
import M1.S2.TPS.service.AdminService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody CreateUserRequest request) {
        adminService.addUser(authorizationHeader, request);
        return ResponseEntity.ok("Utilisateur créé avec succès. Un email de validation a été envoyé.");
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody DeleteUserRequest request) {
        adminService.deleteUser(authorizationHeader, request);
        return ResponseEntity.ok("Utilisateur supprimé avec succès");
    }

    @PutMapping("/modifyRole")
    public ResponseEntity<String> modifyRole(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody ModifyRoleRequest request) {
        adminService.modifyRole(authorizationHeader, request);
        return ResponseEntity.ok("Rôle modifié avec succès");
    }
}
