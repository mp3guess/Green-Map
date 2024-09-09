package tis.techacademy.green_map.util.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Request for authorization")
public class SignInRequest {

    @Schema(description = "User name", example = "Jon")
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters long")
    @NotBlank(message = "Username should not be blank")
    private String username;

    @Schema(description = "Password", example = "my_secret_password")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters long")
    @NotBlank(message = "Password should not be blank")
    private String password;
}
