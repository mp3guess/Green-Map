package tis.techacademy.green_map.service.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
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
