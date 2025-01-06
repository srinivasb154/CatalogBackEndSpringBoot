package com.sb.catalog.controllers;

import com.sb.catalog.services.ExportService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/export")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> exportData(
            @RequestParam(required = false) Map<String, String> columnMappings) {
        try {
            // Default columnMappings to an empty map if not provided
            if (columnMappings == null) {
                columnMappings = Map.of();
            }

            Map<String, List<Map<String, Object>>> data = exportService.exportData(columnMappings);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", List.of(Map.of("message", "Internal Server Error")))
            );
        }
    }
}


