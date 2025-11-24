package org.example.Mutantes.service;

import org.example.Mutantes.entity.DnaRecord;
import org.example.Mutantes.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private MutantService mutantService;

    // Datos de prueba
    private final String[] mutantDna = {"AAAA", "CCCC", "GGGG", "TTTT"};
    private final String[] humanDna = {"AAAT", "CCCG", "GGGC", "TTTA"};

    @Test
    @DisplayName("Debe analizar ADN mutante y guardarlo en DB")
    void testIsMutantAndSave() {
        // ARRANGE
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty()); // No está en caché
        when(mutantDetector.isMutant(mutantDna)).thenReturn(true); // Es mutante

        // ACT
        boolean result = mutantService.isMutant(mutantDna);

        // ASSERT
        assertTrue(result);

        // VERIFY: Se llamó al detector y se guardó en BD
        verify(mutantDetector, times(1)).isMutant(mutantDna);
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe analizar ADN humano y guardarlo en DB")
    void testIsHumanAndSave() {
        // ARRANGE
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(humanDna)).thenReturn(false); // Es humano

        // ACT
        boolean result = mutantService.isMutant(humanDna);

        // ASSERT
        assertFalse(result);

        // VERIFY
        verify(mutantDetector, times(1)).isMutant(humanDna);
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe retornar resultado cacheado si el ADN ya fue analizado")
    void testReturnCachedResultForAnalyzedDna() {
        // ARRANGE
        DnaRecord cachedRecord = new DnaRecord();
        cachedRecord.setMutant(true);

        // Simulamos que YA existe en base de datos
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.of(cachedRecord));

        // ACT
        boolean result = mutantService.isMutant(mutantDna);

        // ASSERT
        assertTrue(result);

        // VERIFY: NO se debe llamar al detector ni guardar de nuevo (ahorro de recursos)
        verify(mutantDetector, never()).isMutant(any());
        verify(dnaRecordRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe generar hash consistente para el mismo ADN")
    void testConsistentHashGeneration() {
        // ARRANGE
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(any())).thenReturn(true);

        // ACT: Llamamos dos veces con el mismo ADN
        mutantService.isMutant(mutantDna);
        mutantService.isMutant(mutantDna);

        // VERIFY: Debe buscar por hash 2 veces (implica que el hash se generó)
        verify(dnaRecordRepository, times(2)).findByDnaHash(anyString());
    }

    @Test
    @DisplayName("Debe guardar registro con hash correcto (SHA-256)")
    void testSavesRecordWithCorrectHash() {
        // ARRANGE
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(mutantDna)).thenReturn(true);

        // ACT
        mutantService.isMutant(mutantDna);

        // VERIFY: Verificamos que lo que se pasa al save cumpla condiciones
        verify(dnaRecordRepository).save(argThat(record ->
                record.getDnaHash() != null &&
                        record.getDnaHash().length() == 64 && // Longitud de SHA-256 en Hex
                        record.isMutant()
        ));
    }
}
