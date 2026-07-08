package com.acme.recipesservice.infrastructure.image;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PexelsImageClient {

    private final RestClient restClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public PexelsImageClient(
            @Value("${pexels.api.key}") String apiKey,
            @Value("${pexels.api.url}") String apiUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", apiKey)
                .build();
    }

    public String searchImage(String query) {
        try {
            String rawResponse = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("query", query)
                            .queryParam("per_page", 1)
                            .queryParam("orientation", "landscape")
                            .build())
                    .retrieve()
                    .body(String.class);

            JsonNode root = mapper.readTree(rawResponse);
            JsonNode photos = root.path("photos");

            if (photos.isArray() && !photos.isEmpty()) {
                return photos.get(0).path("src").path("large").asText();
            }
        } catch (Exception e) {
            // Si falla la búsqueda, no rompemos la generación de la receta
            System.err.println("Error buscando imagen para '" + query + "': " + e.getMessage());
        }
        return ""; // fallback: sin imagen, el frontend puede mostrar un placeholder
    }
}
