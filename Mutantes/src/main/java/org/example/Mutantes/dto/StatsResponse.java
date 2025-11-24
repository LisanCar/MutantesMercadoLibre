package org.example.Mutantes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estadísticas de verificaciones de ADN")
public class StatsResponse {

    @JsonProperty("count_mutant_dna")
    @Schema(description = "Cantidad de ADN mutantes verificados", example = "40")
    private long countMutantDna;

    @JsonProperty("count_human_dna")
    @Schema(description = "Cantidad de ADN humanos verificados", example = "100")
    private long countHumanDna;

    @Schema(description = "Ratio: mutantes / humanos", example = "0.4")
    private double ratio;
}