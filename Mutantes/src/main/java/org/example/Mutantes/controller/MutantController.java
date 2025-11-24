package org.example.Mutantes.controller;

import org.example.Mutantes.dto.DnaRequest;
import org.example.Mutantes.dto.StatsResponse;
import org.example.Mutantes.service.MutantService;
import org.example.Mutantes.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mutant Detector", description = "API para detección de mutantes")
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    @PostMapping("/mutant/")
    @Operation(summary = "Verificar si un ADN es mutante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Es mutante"),
            @ApiResponse(responseCode = "403", description = "No es mutante (es humano)"),
            @ApiResponse(responseCode = "400", description = "Secuencia de ADN inválida")
    })
    public ResponseEntity<Void> checkMutant(@Valid @RequestBody DnaRequest request) {
        boolean isMutant = mutantService.isMutant(request.getDna());
        if (isMutant) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "Obtener estadísticas de verificaciones")
    @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas correctamente")
    public ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(statsService.getStats());
    }
}