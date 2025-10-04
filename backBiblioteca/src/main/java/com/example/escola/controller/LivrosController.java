package com.example.escola.controller;

import com.example.escola.dto.EmprestimoRequest;
import com.example.escola.model.Livros;
import com.example.escola.repository.LivrosRepository;
import com.example.escola.services.LivrosServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/livros")
public class LivrosController {

    @Autowired
    private LivrosServices livrosServices;

    @Autowired
    private LivrosRepository livrosRepository;

    @PostMapping
    public ResponseEntity<Livros> cadastrarLivro(@RequestBody Livros livro) {
        Livros novoLivro = livrosRepository.save(livro);
        return ResponseEntity.ok(novoLivro);
    }

    @PostMapping("/emprestimo")
    public ResponseEntity<?> registrarEmprestimo(@RequestBody EmprestimoRequest emprestimoRequest) {
        try {
            Livros livro = livrosServices.registrarEmprestimo(
                    emprestimoRequest.getLivroId(),
                    emprestimoRequest.getMatriculaAluno()
            );
            return ResponseEntity.ok(livro);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarLivros() {
        return ResponseEntity.ok(livrosServices.listarLivros());
    }
}