package backend.zelkova.admin.controller;

import backend.zelkova.admin.dto.request.GrantRoleRequest;
import backend.zelkova.admin.dto.request.RemoveRoleRequest;
import backend.zelkova.admin.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyRole('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/roles")
public class AdminController {

    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<Void> grantRole(@RequestBody @Valid GrantRoleRequest grantRoleRequest) {
        adminService.grantRole(grantRoleRequest.getAccountId(), grantRoleRequest.getRole());

        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeRole(@RequestBody @Valid RemoveRoleRequest removeRoleRequest) {
        adminService.removeRole(removeRoleRequest.getAccountId(), removeRoleRequest.getRole());

        return ResponseEntity.noContent()
                .build();
    }
}
