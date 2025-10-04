package com.example.escola.services;

import com.example.escola.model.Emprestimo;
import com.example.escola.model.Livros;
import com.example.escola.model.Alunos;
import com.example.escola.repository.EmprestimoRepository;
import com.example.escola.repository.LivrosRepository;
import com.example.escola.repository.AlunosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoServices {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private LivrosRepository livrosRepository;

    @Autowired
    private AlunosRepository alunosRepository;

    @Autowired
    private JavaMailSender mailSender;

    public Livros registrarEmprestimo(Integer livroId, String matriculaAluno) throws Exception {
        Livros livro = livrosRepository.findById(livroId)
                .orElseThrow(() -> new Exception("Livro não encontrado"));

        // Verificar inconsistência
        boolean emprestimoExiste = emprestimoRepository.existsByLivroId(livroId);
        if (!livro.getDisponivel() && !emprestimoExiste) {
            livro.setDisponivel(true);
            livrosRepository.save(livro);
        }

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

        return livro;
    }

    private void enviarEmail(String destinatario, String assunto, String mensagem) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(destinatario);
        email.setSubject(assunto);
        email.setText(mensagem);
        mailSender.send(email);
    }

    @Scheduled(cron = "0 0 9 * * ?") // Executa todos os dias às 9h
    public void verificarEmprestimos() {
        List<Emprestimo> emprestimos = emprestimoRepository.findAll();
        LocalDate hoje = LocalDate.now();

        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getDataDevolucao().minusDays(2).isEqual(hoje)) { // Aviso 2 dias antes
                Alunos aluno = emprestimo.getAluno();
                enviarEmail(aluno.getEmail(), "Aviso de Devolução de Livro",
                        "Olá " + aluno.getNome() + ",\n\nO prazo de devolução do livro: " +
                                emprestimo.getLivro().getTitulolivro() + " está próximo.\nData de devolução: " +
                                emprestimo.getDataDevolucao() + ".\n\nPor favor, devolva o livro no prazo.");
            }
        }
    }

    public Emprestimo registrarEmprestimoById(String tituloId, Integer alunoId) {
        return null;
    }
}