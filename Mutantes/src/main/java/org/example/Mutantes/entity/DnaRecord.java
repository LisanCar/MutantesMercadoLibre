package org.example.Mutantes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dna_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DnaRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dna_hash", unique = true, nullable = false)
    private String dnaHash;

    @Column(name = "is_mutant", nullable = false)
    private boolean isMutant;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Asigna la fecha automáticamente antes de guardar en la BD
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
