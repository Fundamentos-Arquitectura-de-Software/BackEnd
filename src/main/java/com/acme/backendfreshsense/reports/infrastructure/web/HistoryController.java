package com.acme.backendfreshsense.reports.infrastructure.web;

import com.acme.backendfreshsense.reports.application.dto.HistoryCreateRequestDto;
import com.acme.backendfreshsense.reports.application.dto.HistoryResponseDto;
import com.acme.backendfreshsense.reports.application.service.HistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = "https://front-end-fresh-sense.vercel.app") // o pon aquí tu dominio de Vercel
public class HistoryController {

    private final HistoryService service;

    public HistoryController(HistoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<HistoryResponseDto> getAll() {
        return service.getAll();
    }

    @PostMapping
    public HistoryResponseDto create(@RequestBody HistoryCreateRequestDto dto) {
        return service.create(dto);
    }
}