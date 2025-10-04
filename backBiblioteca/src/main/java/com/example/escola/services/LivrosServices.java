package com.example.escola.services;

import com.example.escola.model.Alunos;
import com.example.escola.model.Emprestimo;
import com.example.escola.model.Livros;
import com.example.escola.repository.AlunosRepository;
import com.example.escola.repository.EmprestimoRepository;
import com.example.escola.repository.LivrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LivrosServices {

    @Autowired
    private LivrosRepository livrosRepository;

    @Autowired
    private AlunosRepository alunosRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private EmailService emailService;

    public Livros registrarEmprestimo(Integer livroId, String matriculaAluno) throws Exception {
        Livros livro = livrosRepository.findById(livroId)
                .orElseThrow(() -> new Exception("Livro não encontrado"));

        if (!livro.getDisponivel()) {
            throw new Exception("O livro já está emprestado");
        }

        Alunos aluno = alunosRepository.findByMatricula(matriculaAluno)
                .orElseThrow(() -> new Exception("Aluno não encontrado"));

        livro.setDisponivel(false);
        livrosRepository.save(livro);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setAluno(aluno);
        LocalDate dataEmprestimo = LocalDate.now();
        emprestimo.setDataEmprestimo(dataEmprestimo);
        emprestimo.setDataDevolucao(dataEmprestimo.plusDays(10));
        emprestimoRepository.save(emprestimo);

        // Envia email de notificação
        emailService.sendEmail(aluno.getEmail(), "Empréstimo Registrado",
                "O livro '" + livro.getTitulolivro() + "' foi emprestado com sucesso. Data de devolução: " + emprestimo.getDataDevolucao() + " Não esqueça de devolver o livro na data correta.");

        return livro;
    }

    @Scheduled(cron = "0 0 9 * * ?") // Runs every day at 9 AM
    public void verificarEmprestimos() {
        List<Emprestimo> emprestimos = emprestimoRepository.findAll();
        LocalDate hoje = LocalDate.now();

        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getDataDevolucao().minusDays(2).isEqual(hoje)) {
                Alunos aluno = emprestimo.getAluno();
                enviarEmail(aluno.getEmail(), "Aviso de Devolução de Livro",
                        "Olá " + aluno.getNome() + ",\n\nO prazo de devolução do livro: " +
                                emprestimo.getLivro().getTitulolivro() + " está próximo.\nData de devolução: " +
                                emprestimo.getDataDevolucao() + ".\n\nPor favor, devolva o livro no prazo.");
            }
        }
    }

    private void enviarEmail(String email, String avisoDeDevoluçãoDeLivro, String s) {
    }

    public Object listarLivros() {
        return null;
    }
}