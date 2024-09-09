package tis.techacademy.green_map.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tis.techacademy.green_map.security.AuthenticationService;
import tis.techacademy.green_map.service.user.UserService;
import tis.techacademy.green_map.util.authentication.JwtAuthenticationResponse;
import tis.techacademy.green_map.util.authentication.SignInRequest;
import tis.techacademy.green_map.util.authentication.SignUpRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    @Operation(summary = "User registration")
    @PostMapping("/register")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        log.info("User registration requested for email: {}", request.getEmail());
        return authenticationService.signUp(request);
    }

    @Operation(summary = "User Authorization")
    @PostMapping("/authenticate")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        log.info("User sign-in requested for username: {}", request.getUsername());
        return authenticationService.signIn(request);
    }

    @Operation(summary = "Forgot password")
    @PutMapping("/forgot-password/{email}")
    public ResponseEntity<String> forgotPassword(@PathVariable String email) {
        log.info("Password reset requested for email: {}", email);
        return ResponseEntity.ok(userService.forgotPassword(email));
    }

    @Operation(summary = "Verify account")
    @PutMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String email,
                                                @RequestParam String otp) {
        return ResponseEntity.ok(userService.verifyAccount(email, otp));
    }

    @Operation(summary = "Set new password")
    @PutMapping("/set-password/{email}/{token}")
    public ResponseEntity<String> setPassword(@PathVariable String email, @RequestParam String password, @PathVariable String token) {
        log.info("Setting new password for email: {}", email);
        return ResponseEntity.ok(userService.setPassword(email, password, token));
    }
}
