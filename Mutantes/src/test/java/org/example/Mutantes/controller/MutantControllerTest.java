package org.example.Mutantes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Mutantes.dto.DnaRequest;
import org.example.Mutantes.dto.StatsResponse;
import org.example.Mutantes.service.MutantService;
import org.example.Mutantes.service.StatsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MutantController.class)
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;

    @Test
    @DisplayName("POST /mutant/ debe retornar 200 OK para ADN mutante")
    void testCheckMutantReturns200ForMutant() throws Exception {
        String[] mutantDna = {
                "ATGCGA", "CAGTGC", "TTATGT",
                "AGAAGG", "CCCCTA", "TCACTG"
        };
        DnaRequest request = new DnaRequest(mutantDna);

        // Simulamos que el servicio dice que ES mutante
        when(mutantService.isMutant(any(String[].class))).thenReturn(true);

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /mutant/ debe retornar 403 Forbidden para ADN humano")
    void testCheckMutantReturns403ForHuman() throws Exception {
        String[] humanDna = {
                "ATGCGA", "CAGTGC", "TTATTT",
                "AGACGG", "GCGTCA", "TCACTG"
        };
        DnaRequest request = new DnaRequest(humanDna);

        // Simulamos que el servicio dice que NO es mutante
        when(mutantService.isMutant(any(String[].class))).thenReturn(false);

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /mutant/ debe retornar 400 Bad Request para ADN nulo")
    void testCheckMutantReturns400ForNullDna() throws Exception {
        DnaRequest request = new DnaRequest(null); // ADN Nulo

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant/ debe retornar 400 Bad Request para ADN vacío")
    void testCheckMutantReturns400ForEmptyDna() throws Exception {
        DnaRequest request = new DnaRequest(new String[]{}); // Array vacío

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant/ debe rechazar body vacío")
    void testCheckMutantReturns400ForInvalidBody() throws Exception {
        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON))
                // No enviamos .content()
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /stats debe retornar estadísticas correctamente")
    void testGetStatsReturnsCorrectData() throws Exception {
        StatsResponse statsResponse = new StatsResponse(40, 100, 0.4);
        when(statsService.getStats()).thenReturn(statsResponse);

        mockMvc.perform(get("/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(40))
                .andExpect(jsonPath("$.count_human_dna").value(100))
                .andExpect(jsonPath("$.ratio").value(0.4));
    }

    @Test
    @DisplayName("GET /stats debe retornar 200 OK incluso sin datos")
    void testGetStatsReturns200WithNoData() throws Exception {
        StatsResponse statsResponse = new StatsResponse(0, 0, 0.0);
        when(statsService.getStats()).thenReturn(statsResponse);

        mockMvc.perform(get("/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(0))
                .andExpect(jsonPath("$.count_human_dna").value(0));
    }
}
