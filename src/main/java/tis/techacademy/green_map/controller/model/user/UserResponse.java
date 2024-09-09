package tis.techacademy.green_map.controller.model.user;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserResponse {
    private int id;

    private String username;

    private String password;

    private String email;

    private int yearOfBirth;

    private String role;

    private String token;

    private Instant tokenCreatedAt;

    private String otp;

    private boolean active;
}
