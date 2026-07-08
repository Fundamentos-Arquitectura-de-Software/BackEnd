package com.acme.recipesservice.infrastructure.ia;

import com.acme.recipesservice.application.dto.CreateRecipeRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OpenAiRecipeClient {

    private final RestClient restClient;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String model;

    public OpenAiRecipeClient(
            @Value("${openai.api.key}") String apiKey,
            @Value("${openai.api.url}") String apiUrl,
            @Value("${openai.model}") String model) {
        this.model = model;
        this.restClient = RestClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Genera 'count' recetas para el 'type' dado (Breakfast, Meals, Snacks).
     * Recibe los títulos ya existentes en el catálogo para que la IA no los repita.
     * Devuelve pares receta + query de búsqueda de imagen (en inglés).
     */
    public List<GeneratedRecipe> generateBatch(String type, int count, List<String> existingTitles) {

        String avoidList = existingTitles.isEmpty()
                ? "(ninguno)"
                : String.join("; ", existingTitles);

        String systemPrompt = """
            Eres un chef experto. Genera %d recetas de cocina ORIGINALES y DIFERENTES entre sí,
            en español, del tipo "%s" (Breakfast = desayuno, Meals = almuerzo/comida, Snacks = snacks/postres).

            Reglas:
            - No repitas el mismo plato ni ideas muy similares entre las %d recetas.
            - PROHIBIDO generar platos que ya existen en el catálogo (ni con otro nombre parecido):
              %s
            - Varía los niveles de dificultad entre Easy, Intermediate y Advanced.
            - En "ingredients" usa nombres CORTOS y COMUNES en español, un alimento por entrada,
              sin marcas, cantidades ni adjetivos largos (ej: "Huevos", "Leche", "Espinacas",
              "Pechuga de pollo", "Arroz", "Queso"). Así se pueden cruzar con el inventario del usuario.
            - Para cada receta agrega un campo "imageQuery": 2-4 palabras EN INGLÉS,
              simples y genéricas, que describan visualmente el plato para buscarlo
              en un banco de imágenes de stock (ej: "pancakes berries", "chicken rice bowl").

            Responde ÚNICAMENTE con un JSON válido con esta estructura exacta:
            {
              "recipes": [
                {
                  "title": "string",
                  "description": "string",
                  "rating": number entre 1 y 5,
                  "level": "Easy" | "Intermediate" | "Advanced",
                  "type": "%s",
                  "time": "string, ej: '30 min'",
                  "ingredients": ["string", ...],
                  "steps": ["string", ...],
                  "imageQuery": "string en inglés"
                }
              ]
            }
            No incluyas explicaciones ni markdown, solo el JSON.
            """.formatted(count, type, count, avoidList, type);

        Map<String, Object> body = Map.of(
                "model", model,
                "response_format", Map.of("type", "json_object"),
                "temperature", 0.9,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", "Genera las " + count + " recetas de tipo " + type + ".")
                )
        );

        String rawResponse = restClient.post().body(body).retrieve().body(String.class);

        try {
            JsonNode root = mapper.readTree(rawResponse);
            String content = root.path("choices").get(0).path("message").path("content").asText();
            JsonNode parsed = mapper.readTree(content);
            JsonNode recipesArray = parsed.path("recipes");

            List<GeneratedRecipe> result = new ArrayList<>();
            for (JsonNode node : recipesArray) {
                CreateRecipeRequest request = CreateRecipeRequest.builder()
                        .title(node.path("title").asText())
                        .description(node.path("description").asText())
                        .image("") // se llena después con Pexels
                        .rating(node.path("rating").asInt(4))
                        .level(node.path("level").asText("Easy"))
                        .type(node.path("type").asText(type))
                        .time(node.path("time").asText(""))
                        .ingredients(toList(node.path("ingredients")))
                        .steps(toList(node.path("steps")))
                        .build();

                String imageQuery = node.path("imageQuery").asText(request.getTitle());
                result.add(new GeneratedRecipe(request, imageQuery));
            }
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la respuesta de OpenAI: " + e.getMessage(), e);
        }
    }

    private List<String> toList(JsonNode arrayNode) {
        List<String> result = new ArrayList<>();
        arrayNode.forEach(n -> result.add(n.asText()));
        return result;
    }

    public record GeneratedRecipe(CreateRecipeRequest recipe, String imageQuery) {}
}
