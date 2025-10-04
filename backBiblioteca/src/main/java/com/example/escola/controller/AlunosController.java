package com.example.escola.controller;

import com.example.escola.model.Alunos;
import com.example.escola.repository.AlunosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/alunos")
public class AlunosController {

    @Autowired
    private AlunosRepository alunosRepository;

    @PostMapping
    public ResponseEntity<Alunos> cadastrarAluno(@RequestBody Alunos aluno) {
        try {
            Alunos novoAluno = alunosRepository.save(aluno);
            return ResponseEntity.ok(novoAluno);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}