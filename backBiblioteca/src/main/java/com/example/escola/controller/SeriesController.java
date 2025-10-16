package com.example.escola.controller;

import com.example.escola.dto.CadastrarSerieRequest;
import com.example.escola.dto.SeriesProfessoresRequest;
import com.example.escola.model.Professores;
import com.example.escola.model.Series;
import com.example.escola.repository.ProfessoresRepository;
import com.example.escola.repository.SeriesRepository;
import com.example.escola.services.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/series")
public class SeriesController {

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private ProfessoresRepository professoresRepository;

    @PostMapping
    public ResponseEntity<?> cadastrarSerie(@RequestBody CadastrarSerieRequest request) {
        try {
            Series serie = seriesService.cadastrarSerie(request);
            return ResponseEntity.ok(Map.of(
                    "mensagem", "Série cadastrada com sucesso",
                    "serie", serie
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Erro interno: " + e.getMessage()));
        }
    }

    @PostMapping("/associar-professores")
    public ResponseEntity<?> associarSeriesProfessores(@RequestBody SeriesProfessoresRequest request) {
        try {
            if (request.getSerieId() == null || request.getProfessoresIds() == null || request.getProfessoresIds().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("erro", "ID da série ou lista de IDs de professores não pode ser nulo ou vazio."));
            }

            System.out.println("ID da Série: " + request.getSerieId());
            System.out.println("IDs dos Professores: " + request.getProfessoresIds());

            Series serie = seriesRepository.findById(request.getSerieId())
                    .orElseThrow(() -> new IllegalArgumentException("Série não encontrada."));

            Set<Professores> professores = new HashSet<>(professoresRepository.findAllById(request.getProfessoresIds()));
            if (professores.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("erro", "Nenhum professor encontrado com os IDs fornecidos."));
            }

            serie.setProfessores(professores);
            seriesRepository.save(serie);

            return ResponseEntity.ok("Professores associados à série com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("erro", "Erro interno ao associar professores: " + (e.getMessage() != null ? e.getMessage() : "Exceção sem mensagem")));
        }
    }
}
