package org.example.Mutantes.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {

    // Regex precompilado para mejorar performance (evita recompilar en cada petición)
    private static final Pattern VALID_DNA_PATTERN = Pattern.compile("^[ATCG]+$");
    private static final int MIN_SIZE = 4;

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        // 1. Validar que no sea nulo o vacío
        if (dna == null || dna.length == 0) {
            return false;
        }

        int n = dna.length;

        // 2. Validar tamaño mínimo (debe ser al menos 4x4 para tener secuencias)
        if (n < MIN_SIZE) {
            return false;
        }

        for (String row : dna) {
            // 3. Validar que la fila no sea nula
            if (row == null) {
                return false;
            }

            // 4. Validar que la matriz sea cuadrada (NxN)
            if (row.length() != n) {
                return false;
            }

            // 5. Validar caracteres permitidos (A, T, C, G)
            if (!VALID_DNA_PATTERN.matcher(row).matches()) {
                return false;
            }
        }

        return true;
    }
}
