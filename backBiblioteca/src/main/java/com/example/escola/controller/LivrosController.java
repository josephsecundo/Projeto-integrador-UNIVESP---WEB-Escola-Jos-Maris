package com.example.escola.controller;

import com.example.escola.dto.DevolucaoRequest;
import com.example.escola.dto.EmprestimoRequest;
import com.example.escola.model.*;
import com.example.escola.repository.AlunosRepository;
import com.example.escola.repository.EmprestimoRepository;
import com.example.escola.repository.LivrosRepository;
import com.example.escola.services.LivrosServices;
import com.example.escola.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/livros")
public class LivrosController {

    @Autowired
    private LivrosServices livrosServices;

    @Autowired
    private LivrosRepository livrosRepository;

    @Autowired
    private AlunosRepository alunosRepository;
    @Autowired
    private EmprestimoRepository emprestimoRepository;
    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<?> cadastrarLivro(@RequestBody Livros livro) {
        try {
            if (livro.getQuantidade() == null || livro.getQuantidade() < 0) {
                return ResponseEntity.badRequest().body("O campo 'quantidade' deve ser informado e maior ou igual a zero.");
            }
            livro.setQuantidadeDisponivel(livro.getQuantidade()); // Inicializa quantidade_disponivel
            Livros novoLivro = livrosRepository.save(livro);
            return ResponseEntity.ok(Map.of(
                    "mensagem", "Livro cadastrado com sucesso",
                    "livro", novoLivro
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao cadastrar o livro: " + e.getMessage());
        }
    }

    @PostMapping("/emprestimo")
    public ResponseEntity<?> registrarEmprestimo(@RequestBody EmprestimoRequest emprestimoRequest) {
        try {
            Livros livro = livrosRepository.findById(emprestimoRequest.getLivroId())
                    .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado."));

            Alunos aluno = alunosRepository.findByMatricula(emprestimoRequest.getMatriculaAluno())
                    .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

            // Checar se o aluno tem empréstimo do livro pendente para devolução
            boolean hasPendingLoan = emprestimoRepository.existsByLivroAndAlunoAndStatus(livro, aluno, "pendente");
            if (hasPendingLoan) {
                return ResponseEntity.badRequest().body("O aluno já possui um empréstimo pendente para este livro.");
            }

            if (livro.getQuantidadeDisponivel() <= 0) {
                return ResponseEntity.badRequest().body("Livro indisponível para empréstimo.");
            }

            Series serie = aluno.getSerie();
            if (serie == null || serie.getProfessores().isEmpty()) {
                return ResponseEntity.badRequest().body("Série ou professores associados não encontrados.");
            }

            Professores professor = serie.getProfessores().iterator().next();
            if (professor == null) {
                return ResponseEntity.badRequest().body("Professor associado à série não encontrado.");
            }

            Emprestimo emprestimo = new Emprestimo();
            emprestimo.setLivro(livro);
            emprestimo.setAluno(aluno);
            emprestimo.setDataEmprestimo(LocalDate.now());
            emprestimo.setDataDevolucao(LocalDate.now().plusDays(10));
            emprestimo.setStatus("pendente");

            emprestimoRepository.save(emprestimo);

            livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);
            livrosRepository.save(livro);

            String emailMessage = String.format(
                    "Prezado(a) %s,\n\nO aluno %s realizou o empréstimo do livro '%s'.\nData do empréstimo: %s\nData de devolução: %s\n\nAtenciosamente,\nBiblioteca",
                    professor.getNome(),
                    aluno.getNome(),
                    livro.getTitulolivro(),
                    emprestimo.getDataEmprestimo(),
                    emprestimo.getDataDevolucao()
            );
            emailService.sendEmail(professor.getEmail(), "Confirmação de Empréstimo", emailMessage);

            return ResponseEntity.ok(Map.of(
                    "mensagem", "Empréstimo registrado com sucesso e e-mail enviado ao professor.",
                    "emprestimo", emprestimo
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "erro", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "erro", "Erro interno ao registrar empréstimo: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/devolucao")
    public ResponseEntity<?> registrarDevolucao(@RequestBody DevolucaoRequest devolucaoRequest) {
        try {
            Livros livro = livrosServices.registrarDevolucao(
                    devolucaoRequest.getLivroId(),
                    devolucaoRequest.getMatriculaAluno()
            );
            return ResponseEntity.ok(Map.of(
                    "mensagem", "Devolução registrada com sucesso",
                    "livro", livro,
                    "dataDevolucao", LocalDate.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registrar devolução: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarLivros() {
        try {
            return ResponseEntity.ok(livrosServices.listarLivros());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao listar livros: " + e.getMessage());
        }
    }

    @GetMapping("/livros-emprestados")
    public ResponseEntity<?> listarLivrosEmprestados() {
        try {
            List<LivroEmprestadoDTO> livrosEmprestados = livrosServices.listarLivrosEmprestados();
            return ResponseEntity.ok(livrosEmprestados);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao listar livros emprestados: " + e.getMessage());
        }
    }
}