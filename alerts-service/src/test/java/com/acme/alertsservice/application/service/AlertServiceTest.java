package com.acme.alertsservice.application.service;

import com.acme.alertsservice.application.dto.AlertRequest;
import com.acme.alertsservice.application.dto.AlertResponse;
import com.acme.alertsservice.domain.model.Alert;
import com.acme.alertsservice.domain.repository.AlertRepository;
import com.acme.alertsservice.infrastructure.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @InjectMocks
    private AlertService alertService;

    @Test
    void getAll() {
        // Arrange
        Alert alert = new Alert();
        alert.setId(1L);
        alert.setTitle("Alimento por vencer");
        alert.setMessage("La leche vencerá pronto");
        when(alertRepository.findAll()).thenReturn(List.of(alert));

        // Act
        List<AlertResponse> responses = alertService.getAll();

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Alimento por vencer", responses.get(0).title());
        verify(alertRepository, times(1)).findAll();
    }

    @Test
    void create() {
        // Arrange
        AlertRequest request = new AlertRequest(
                "Alimento por vencer",
                "La leche vencerá pronto",
                "HIGH",
                "INVENTORY",
                "ACTIVE",
                "1h ago"
        );
        Alert savedAlert = new Alert();
        savedAlert.setId(1L);
        savedAlert.setTitle("Alimento por vencer");
        savedAlert.setMessage("La leche vencerá pronto");
        savedAlert.setSeverity("HIGH");
        savedAlert.setSource("INVENTORY");
        savedAlert.setState("ACTIVE");
        savedAlert.setTimeAgo("1h ago");
        when(alertRepository.save(any(Alert.class))).thenReturn(savedAlert);

        // Act
        AlertResponse response = alertService.create(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Alimento por vencer", response.title());
        verify(alertRepository, times(1)).save(any(Alert.class));
    }

    @Test
    void update_Success() {
        // Arrange
        Long alertId = 1L;
        AlertRequest request = new AlertRequest(
                "Alimento crítico",
                "Revisar de inmediato",
                "CRITICAL",
                null,
                "RESOLVED",
                null
        );
        Alert existingAlert = new Alert();
        existingAlert.setId(alertId);
        existingAlert.setTitle("Alimento por vencer");

        Alert updatedAlert = new Alert();
        updatedAlert.setId(alertId);
        updatedAlert.setTitle("Alimento crítico");
        updatedAlert.setMessage("Revisar de inmediato");
        updatedAlert.setSeverity("CRITICAL");
        updatedAlert.setState("RESOLVED");

        when(alertRepository.findById(alertId)).thenReturn(Optional.of(existingAlert));
        when(alertRepository.save(existingAlert)).thenReturn(updatedAlert);

        // Act
        AlertResponse response = alertService.update(alertId, request);

        // Assert
        assertNotNull(response);
        assertEquals("Alimento crítico", response.title());
        assertEquals("RESOLVED", response.state());
        verify(alertRepository, times(1)).findById(alertId);
        verify(alertRepository, times(1)).save(existingAlert);
    }

    @Test
    void update_ThrowsResourceNotFoundException() {
        // Arrange
        Long alertId = 99L;
        AlertRequest request = new AlertRequest(null, null, null, null, null, null);
        when(alertRepository.findById(alertId)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            alertService.update(alertId, request);
        });

        // Assert
        assertNotNull(exception);
        verify(alertRepository, times(1)).findById(alertId);
        verify(alertRepository, never()).save(any(Alert.class));
    }
}