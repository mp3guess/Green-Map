package tis.techacademy.green_map.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tis.techacademy.green_map.controller.model.user.UserRequest;
import tis.techacademy.green_map.model.user.UserEntity;
import tis.techacademy.green_map.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user by email")
    @GetMapping("/{email}")
    public ResponseEntity<UserEntity> getUserByEmail(@PathVariable String email) {
        log.info("Get user by email requested for email: {}", email);
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @Operation(summary = "Get current user info")
    @GetMapping("/user-info")
    public ResponseEntity<UserDetails> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.info("Get current user info requested for email: {}", userDetails.getUsername());
        return ResponseEntity.ok(userDetails);
    }

    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        log.info("Get all users requested");
        return ResponseEntity.ok(userService.listAllUsers());
    }

    @Operation(summary = "Delete user by email")
    @DeleteMapping("/{email}")
    public void deleteUser(@PathVariable String email) {
        log.info("Delete user by email requested for email: {}", email);
        userService.deleteUser(email);
    }

    @Operation(summary = "Update user by email")
    @PutMapping
    public ResponseEntity<UserEntity> updateUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("Update user by email requested for email: {}", userRequest.getEmail());
        userService.deleteUser(userRequest.getEmail());
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    @Operation(summary = "Create new user")
    @PostMapping
    public ResponseEntity<UserEntity> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("Create new user requested for email: {}", userRequest.getEmail());
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    @Operation(summary = "Change user role to admin")
    @PutMapping("/make-admin/{email}")
    public ResponseEntity<UserEntity> changeRole(@PathVariable String email) {
        log.info("Change user role to admin requested for email: {}", email);
        return ResponseEntity.ok(userService.makeAdmin(email));
    }
}
