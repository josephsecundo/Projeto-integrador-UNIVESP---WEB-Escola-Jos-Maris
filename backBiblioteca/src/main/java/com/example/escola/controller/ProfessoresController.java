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
}