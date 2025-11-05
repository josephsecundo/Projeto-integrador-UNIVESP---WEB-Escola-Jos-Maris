package com.example.escola.controller;

import com.example.escola.dto.CadastrarProfessorRequest;
import com.example.escola.model.Professores;
import com.example.escola.repository.ProfessoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/professores")
public class ProfessoresController {

    @Autowired
    private ProfessoresRepository professoresRepository;

    @PostMapping
    public ResponseEntity<?> cadastrarProfessor(@RequestBody CadastrarProfessorRequest request) {
        try {
            if (request.getNome() == null || request.getNome().isEmpty()) {
                return ResponseEntity.badRequest().body("O campo 'nome' é obrigatório.");
            }
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body("O campo 'email' é obrigatório.");
            }

            Professores professor = new Professores();
            professor.setNome(request.getNome());
            professor.setEmail(request.getEmail());
            Professores novoProfessor = professoresRepository.save(professor);

            return ResponseEntity.ok(Map.of(
                    "mensagem", "Professor cadastrado com sucesso!",
                    "professor", novoProfessor
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao cadastrar professor: " + e.getMessage());
        }
    }

    @GetMapping("/listarProfessores")
    public ResponseEntity<?> listarProfessores() {
        try {
            var professores = professoresRepository.findAll();
            return ResponseEntity.ok(professores);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao listar professores: " + e.getMessage());
        }
    }

    @GetMapping("/listarPorSerie")
    public ResponseEntity<?> listarProfessoresPorSerie(@RequestParam Integer serieId) {
        try {
            if (serieId == null) {
                return ResponseEntity.badRequest().body("O parâmetro 'serieId' é obrigatório.");
            }

            var professores = professoresRepository.findProfessoresBySeriesId(serieId);
            return ResponseEntity.ok(professores);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao listar professores: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProfessor(@PathVariable Integer id, @RequestBody CadastrarProfessorRequest request) {
        try {
            if (request.getNome() == null || request.getNome().isEmpty()) {
                return ResponseEntity.badRequest().body("O campo 'nome' é obrigatório.");
            }
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body("O campo 'email' é obrigatório.");
            }

            Professores professor = professoresRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado com o ID fornecido."));

            professor.setNome(request.getNome());
            professor.setEmail(request.getEmail());
            Professores professorAtualizado = professoresRepository.save(professor);

            return ResponseEntity.ok(Map.of(
                    "mensagem", "Professor atualizado com sucesso!",
                    "professor", professorAtualizado
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao atualizar professor: " + e.getMessage());
        }
    }

    
}