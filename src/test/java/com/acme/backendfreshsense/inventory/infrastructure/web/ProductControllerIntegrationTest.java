package com.acme.backendfreshsense.inventory.infrastructure.web;

import com.acme.backendfreshsense.inventory.application.dto.ProductResponse;
import com.acme.backendfreshsense.inventory.application.service.ProductService;
import com.acme.backendfreshsense.shared.infrastructure.exceptions.ResourceNotFoundException;
import com.acme.backendfreshsense.shared.infrastructure.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void update_ReturnsOk_WhenProductExistsAndDataIsValid() throws Exception {
        // Arrange
        Long productId = 1L;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("quantity", 15);

        ProductResponse response = new ProductResponse(productId, "Leche", "1 litro", "Lácteos", 15, null);

        when(productService.update(eq(productId), any())).thenReturn(response);

        // Act
        mockMvc.perform(patch("/api/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.quantity").value(15));
    }

    @Test
    void update_ReturnsNotFound_WhenProductDoesNotExist() throws Exception {
        // Arrange
        Long productId = 99L;
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("quantity", 5);

        when(productService.update(eq(productId), any()))
                .thenThrow(new ResourceNotFoundException("Producto no encontrado con id: " + productId));

        // Act
        mockMvc.perform(patch("/api/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                // Assert
                .andExpect(status().isNotFound());
    }
}