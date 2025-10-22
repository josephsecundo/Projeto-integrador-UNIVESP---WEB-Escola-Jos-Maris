package com.example.escola.controller;

import com.example.escola.dto.AlunosDTO;
import com.example.escola.dto.ProfessorDTO;
import com.example.escola.dto.SerieDTO;
import com.example.escola.model.Alunos;
import com.example.escola.model.Professores;
import com.example.escola.model.Series;
import com.example.escola.repository.AlunosRepository;
import jakarta.validation.Valid;
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
    public ResponseEntity<Alunos> cadastrarAluno(@Valid @RequestBody AlunosDTO alunosDTO) {
        try {
            // Validação e conversão do DTO para a entidade
            Alunos aluno = new Alunos();
            aluno.setNome(alunosDTO.getNome());
            aluno.setMatricula(alunosDTO.getMatricula());
            if (alunosDTO.getSerie() != null) {
                Series serie = new Series();
                serie.setId(alunosDTO.getSerie().getId());
                aluno.setSerie(serie);
            }
            if (alunosDTO.getProfessor() != null) {
                Professores professor = new Professores();
                professor.setId(alunosDTO.getProfessor().getId());
            }

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
                aluno.getSerie() != null ? new SerieDTO(
                    aluno.getSerie().getId(),
                    aluno.getSerie().getNome(),
                    aluno.getSerie().getAno()
                ) : null,
                aluno.getProfessor() != null ? new ProfessorDTO(
                    aluno.getProfessor().getId(),
                    aluno.getProfessor().getNome()
                ) : null
            ))
            .toList();
        return ResponseEntity.ok(alunosDTO);
    }
}

