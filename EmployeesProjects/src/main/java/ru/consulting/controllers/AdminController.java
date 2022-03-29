package ru.consulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.consulting.entitity.security.Role;
import ru.consulting.service.AdminService;

@RequestMapping("admin")
@RestController
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PutMapping("{id}")
    public void changeStatus(@PathVariable Long id, @RequestParam boolean isActive) {
        adminService.changeService(id, isActive);
    }

    @PostMapping("{id}")
    public ResponseEntity<Boolean> addRoleUser(@PathVariable Long id, @RequestParam Role role, Session session) {
        return ResponseEntity.ok(adminService.editUserRole(id, role));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> deleteRoleUser(@PathVariable Long id, @RequestParam Role role) {
        return new ResponseEntity<>(adminService.deleteUserRole(id, role), HttpStatus.NO_CONTENT);
    }
}
