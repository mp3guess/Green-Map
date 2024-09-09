package tis.techacademy.green_map.controller.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserRequest {

    private Long id;

    @NotEmpty(message = "Username is required")
    private String username;

    @NotEmpty(message = "Password is required")
    private String password;

    @Email
    @NotEmpty(message = "Email is required")
    private String email;

    @NotNull(message = "Year of birth is required")
    @Min(1900)
    @Max(2024)
    private int yearOfBirth;

    private String role;

    private String token;

    private Instant tokenCreatedAt;

    private String otp;

    private boolean active;
}
