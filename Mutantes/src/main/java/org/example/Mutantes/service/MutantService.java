package org.example.Mutantes.service;

import org.example.Mutantes.entity.DnaRecord;
import org.example.Mutantes.exception.DnaHashCalculationException;
import org.example.Mutantes.repository.DnaRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MutantService {

    private final DnaRecordRepository repository;
    private final MutantDetector mutantDetector;

    @Transactional
    public boolean isMutant(String[] dna) {
        // 1. Calcular Hash para deduplicación
        String dnaHash = calculateDnaHash(dna);

        // 2. Verificar si ya existe en BD (Caché)
        Optional<DnaRecord> existingRecord = repository.findByDnaHash(dnaHash);
        if (existingRecord.isPresent()) {
            return existingRecord.get().isMutant();
        }

        // 3. Ejecutar algoritmo de detección
        boolean isMutant = mutantDetector.isMutant(dna);

        // 4. Guardar resultado
        DnaRecord record = new DnaRecord();
        record.setDnaHash(dnaHash);
        record.setMutant(isMutant);
        repository.save(record);

        return isMutant;
    }

    private String calculateDnaHash(String[] dna) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String fullDna = String.join("", dna);
            byte[] hashBytes = digest.digest(fullDna.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new DnaHashCalculationException("Error al calcular hash del ADN", e);
        }
    }
}