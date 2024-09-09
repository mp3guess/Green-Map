package tis.techacademy.green_map.util.token;


import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Token {
    private String token;

    private Instant createdAt;
}
