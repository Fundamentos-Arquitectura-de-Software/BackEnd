package com.acme.backendfreshsense.monitoring.infrastructure.web;

import com.acme.backendfreshsense.monitoring.application.dto.DeviceRegistrationResponse;
import com.acme.backendfreshsense.monitoring.application.dto.DeviceResponse;
import com.acme.backendfreshsense.monitoring.application.dto.RegisterDeviceRequest;
import com.acme.backendfreshsense.monitoring.application.service.DeviceService;
import com.acme.backendfreshsense.shared.infrastructure.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Devices", description = "Registro y gestión de dispositivos IoT del usuario")
@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Operation(summary = "Registrar un dispositivo IoT (emparejamiento)",
            description = "Asocia un dispositivo al usuario autenticado y devuelve un CÓDIGO DE EMPAREJAMIENTO temporal " +
                          "(de un solo uso). Ese código se introduce en el Edge, que lo canjea por la clave secreta. " +
                          "La clave nunca se muestra al usuario.")
    @PostMapping
    public ResponseEntity<DeviceRegistrationResponse> register(@Valid @RequestBody RegisterDeviceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceService.register(CurrentUser.id(), request));
    }

    @Operation(summary = "Listar mis dispositivos")
    @GetMapping
    public List<DeviceResponse> list() {
        return deviceService.listByUser(CurrentUser.id());
    }
}
