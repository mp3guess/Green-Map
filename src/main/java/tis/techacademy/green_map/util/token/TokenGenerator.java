package tis.techacademy.green_map.util.token;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class TokenGenerator {
    public Token generateUUIDToken() {

        return Token.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(generateCreatedAt())
                .build();
    }

    public Instant generateCreatedAt() {
        return Instant.now();
    }
}