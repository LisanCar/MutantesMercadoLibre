package org.example.Mutantes.service;

import org.example.Mutantes.dto.StatsResponse;
import org.example.Mutantes.repository.DnaRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final DnaRecordRepository repository;

    public StatsResponse getStats() {
        long countMutant = repository.countByIsMutant(true);
        long countHuman = repository.countByIsMutant(false);

        // Calcular ratio manejando división por cero si no hay humanos
        double ratio = countHuman == 0 ?
                (countMutant > 0 ? countMutant : 0.0) :
                (double) countMutant / countHuman;

        return new StatsResponse(countMutant, countHuman, ratio);
    }
}
