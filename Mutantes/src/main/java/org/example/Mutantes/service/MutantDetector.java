package org.example.Mutantes.service;

import org.springframework.stereotype.Component;

@Component
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;
    private static final int MUTANT_SEQUENCE_THRESHOLD = 1;

    public boolean isMutant(String[] dna) {
        if (dna == null || dna.length == 0) return false;

        int n = dna.length;
        int sequenceCount = 0;

        char[][] matrix = new char[n][n];


        for (int i = 0; i < n; i++) {
            if (dna[i] == null || dna[i].length() != n) return false; // Validación fila nula o tamaño incorrecto

            matrix[i] = dna[i].toCharArray();

            // Validación de caracteres válidos
            for (char c : matrix[i]) {
                if (c != 'A' && c != 'T' && c != 'C' && c != 'G') {
                    return false;
                }
            }
        }

        // Algoritmo de Búsqueda
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                // 1. Horizontal (->)
                if (col <= n - SEQUENCE_LENGTH) {
                    if (checkDirection(matrix, row, col, 0, 1)) {
                        sequenceCount++;
                        if (sequenceCount > MUTANT_SEQUENCE_THRESHOLD) return true;
                    }
                }

                // 2. Vertical (v)
                if (row <= n - SEQUENCE_LENGTH) {
                    if (checkDirection(matrix, row, col, 1, 0)) {
                        sequenceCount++;
                        if (sequenceCount > MUTANT_SEQUENCE_THRESHOLD) return true;
                    }
                }

                // 3. Diagonal Descendente (\)
                if (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
                    if (checkDirection(matrix, row, col, 1, 1)) {
                        sequenceCount++;
                        if (sequenceCount > MUTANT_SEQUENCE_THRESHOLD) return true;
                    }
                }

                // 4. Diagonal Ascendente (/)
                if (row >= SEQUENCE_LENGTH - 1 && col <= n - SEQUENCE_LENGTH) {
                    if (checkDirection(matrix, row, col, -1, 1)) {
                        sequenceCount++;
                        if (sequenceCount > MUTANT_SEQUENCE_THRESHOLD) return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean checkDirection(char[][] matrix, int row, int col, int dRow, int dCol) {
        char first = matrix[row][col];
        return first == matrix[row + dRow][col + dCol] &&
                first == matrix[row + 2 * dRow][col + 2 * dCol] &&
                first == matrix[row + 3 * dRow][col + 3 * dCol];
    }
}