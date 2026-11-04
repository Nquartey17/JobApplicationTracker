
package com.quartey.jobapplicationtracker.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Bean
    public SecretKey jwtSecretKey(
            @Value("${app.jwt.secret}") String secret) { //reads the secret from app config

        byte[] keyBytes = Base64.getDecoder().decode(secret); // converts configured secret into bytes

        if (keyBytes.length < 32) {
            throw new IllegalStateException(
                    "JWT secret must contain at least 32 bytes");
        }

        return new SecretKeySpec(keyBytes, "HmacSHA256"); // represents the bytes as HMAC signing key
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey jwtSecretKey) { // Spring component to create signed JWTs
        return new NimbusJwtEncoder(
                new ImmutableSecret<>(jwtSecretKey));
    }
}
