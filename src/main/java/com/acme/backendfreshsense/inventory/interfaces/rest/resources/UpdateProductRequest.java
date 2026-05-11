package com.acme.backendfreshsense.inventory.interfaces.rest.resources;

import jakarta.validation.constraints.Min;

public record UpdateProductRequest(
        @Min(value = 0, message = "La cantidad no puede ser negativa") Integer quantity
) {}
