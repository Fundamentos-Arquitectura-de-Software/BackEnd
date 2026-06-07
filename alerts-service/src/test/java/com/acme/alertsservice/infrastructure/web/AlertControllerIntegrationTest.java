package com.acme.alertsservice.infrastructure.web;

import com.acme.alertsservice.application.dto.AlertResponse;
import com.acme.alertsservice.application.service.AlertService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlertsController.class)
@AutoConfigureMockMvc(addFilters = false)
class AlertControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertService alertService;

    @Test
    void getAll_ReturnsOkAndAlertList_WhenAlertsExist() throws Exception {
        // Arrange
        AlertResponse alert = new AlertResponse(
                1L,
                "Alimento por vencer",
                "La leche vencerá pronto",
                "HIGH",
                "INVENTORY",
                "ACTIVE",
                "1h ago"
        );
        when(alertService.getAll()).thenReturn(List.of(alert));

        // Act
        mockMvc.perform(get("/api/alerts")
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Alimento por vencer"));
    }
}