package org.example.Mutantes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estructura estándar de respuesta de error")
public class ErrorResponse {

    @Schema(description = "Fecha y hora del error", example = "2024-01-01T12:00:00")
    private String timestamp;

    @Schema(description = "Código de estado HTTP", example = "400")
    private int status;

    @Schema(description = "Tipo de error", example = "Bad Request")
    private String error;

    @Schema(description = "Mensaje detallado", example = "Secuencia de ADN inválida")
    private String message;

    @Schema(description = "Ruta de la solicitud", example = "/mutant/")
    private String path;
}
