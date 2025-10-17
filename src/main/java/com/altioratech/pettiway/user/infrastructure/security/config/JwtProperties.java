package com.altioratech.pettiway.user.infrastructure.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    /**
     * Clave secreta HMAC para firmar tokens JWT.
     */
    private String secret;

    /**
     * Duración del access token en milisegundos.
     */
    private long expiration;

    /**
     * Duración del refresh token en milisegundos.
     */
    private long refreshExpiration;

    // Getters y Setters
    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    public long getExpiration() { return expiration; }
    public void setExpiration(long expiration) { this.expiration = expiration; }

    public long getRefreshExpiration() { return refreshExpiration; }
    public void setRefreshExpiration(long refreshExpiration) { this.refreshExpiration = refreshExpiration; }
}
