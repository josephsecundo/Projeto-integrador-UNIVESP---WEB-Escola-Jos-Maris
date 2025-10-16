package com.example.escola.controller;

import com.example.escola.dto.AlunosDTO;
import com.example.escola.dto.ProfessorDTO;
import com.example.escola.dto.SerieDTO;
import com.example.escola.model.Alunos;
import com.example.escola.repository.AlunosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/listar")
    public ResponseEntity<List<AlunosDTO>> listarAlunos() {
        List<Alunos> alunos = alunosRepository.findAll();
        List<AlunosDTO> alunosDTO = alunos.stream()
            .map(aluno -> new AlunosDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getMatricula(),
                new SerieDTO(
                    aluno.getSerie().getId(),
                    aluno.getSerie().getNome(),
                    aluno.getSerie().getAno()
                ),
                new ProfessorDTO(
                    aluno.getProfessor().getId(),
                    aluno.getProfessor().getNome()
                )
            ))
            .toList();
        return ResponseEntity.ok(alunosDTO);
    }
}

