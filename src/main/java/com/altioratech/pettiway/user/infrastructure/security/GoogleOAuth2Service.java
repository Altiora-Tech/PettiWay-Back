package com.altioratech.pettiway.user.infrastructure.security;

import com.altioratech.pettiway.user.application.dto.response.GoogleUserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GoogleOAuth2Service {

    private final String googleTokenInfoUrl = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=";

    public GoogleUserInfo authenticateWithGoogle(String idToken) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(googleTokenInfoUrl + idToken, Map.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Token de Google inválido o expirado.");
        }

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("email")) {
            throw new RuntimeException("No se pudo obtener información del usuario desde Google.");
        }

        String email = (String) body.get("email");
        String name = (String) body.getOrDefault("name", "Usuario Google");
        String sub = (String) body.get("sub");

        return new GoogleUserInfo(email, name, sub);
    }
}
