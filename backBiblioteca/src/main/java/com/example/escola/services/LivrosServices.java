package com.example.escola.services;

import com.example.escola.model.Alunos;
import com.example.escola.model.Emprestimo;
import com.example.escola.model.LivroEmprestadoDTO;
import com.example.escola.model.Livros;
import com.example.escola.repository.AlunosRepository;
import com.example.escola.repository.EmprestimoRepository;
import com.example.escola.repository.LivrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LivrosServices {

    @Autowired
    private AlunosRepository alunosRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private EmailService emailService;

    public Livros registrarEmprestimo(Integer livroId, String matriculaAluno) throws Exception {
        Livros livro = livrosRepository.findById(livroId)
                .orElseThrow(() -> new Exception("Livro não encontrado"));

        if (livro.getQuantidadeDisponivel() == 0) {
            throw new Exception("O livro não está disponível no momento");
        }

        Alunos aluno = alunosRepository.findByMatricula(matriculaAluno)
                .orElseThrow(() -> new Exception("Aluno não encontrado"));

        if (aluno.getSerie() == null || aluno.getSerie().getProfessor() == null) {
            throw new Exception("Professor associado ao aluno não encontrado");
        }
        if (emprestimoRepository.existsByLivroAndAlunoAndStatus(livro, aluno, "pendente")) {
            throw new Exception("O aluno já possui um empréstimo pendente para este livro");
        }
        livro.setQuantidadeDiponivel(livro.getQuantidadeDisponivel() - 1);
        livrosRepository.save(livro);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setAluno(aluno);
        LocalDate dataEmprestimo = LocalDate.now();
        emprestimo.setDataEmprestimo(dataEmprestimo);
        emprestimo.setDataDevolucao(dataEmprestimo.plusDays(10));
        emprestimo.setStatus("pendente");
        emprestimoRepository.save(emprestimo);

        // Send email to the professor
        String professorEmail = aluno.getSerie().getProfessor().getEmail();
        String professorNome = aluno.getSerie().getProfessor().getNome();
        emailService.sendEmail(professorEmail, "Novo Empréstimo Registrado",
                "Olá " + professorNome + ",\n\nO aluno " + aluno.getNome() +
                " realizou o empréstimo do livro '" + livro.getTitulolivro() + "'.\nData de devolução: " +
                emprestimo.getDataDevolucao() + ".\n\nPor favor, acompanhe o empréstimo.");

        return livro;
    }

    @Scheduled(cron = "0 0 9 * * ?") // Executa todos os dias às 9h
    public void verificarEmprestimos() {
        List<Emprestimo> emprestimos = emprestimoRepository.findAll();
        LocalDate hoje = LocalDate.now();

        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getDataDevolucao().minusDays(2).isEqual(hoje)) { // Notifica 2 dias antes
                Alunos aluno = emprestimo.getAluno();
                if (aluno.getSerie() != null && aluno.getSerie().getProfessor() != null) {
                    String professorEmail = aluno.getSerie().getProfessor().getEmail();
                    enviarEmail(professorEmail, "Aviso de Devolução de Livro",
                            "Olá " + aluno.getSerie().getProfessor().getNome() + ",\n\nO prazo de devolução do livro: " +
                                    emprestimo.getLivro().getTitulolivro() + " emprestado ao aluno " + aluno.getNome() +
                                    " está próximo.\nData de devolução: " + emprestimo.getDataDevolucao() +
                                    ".\n\nPor favor, acompanhe a devolução.");
                }
            }
        }
    }

    @Autowired
    private LivrosRepository livrosRepository;

    public Livros registrarDevolucao(Integer livroId, String matriculaAluno) throws Exception {
        Livros livro = livrosRepository.findById(livroId)
                .orElseThrow(() -> new Exception("Livro não encontrado"));

        Alunos aluno = alunosRepository.findByMatricula(matriculaAluno)
                .orElseThrow(() -> new Exception("Aluno não encontrado"));

        Emprestimo emprestimo = (Emprestimo) emprestimoRepository.findByLivroAndAlunoAndStatus(livro, aluno, "pendente")
                .orElseThrow(() -> new Exception("Não há empréstimo pendente para este livro e aluno"));

        livro.setQuantidadeDiponivel(livro.getQuantidadeDisponivel() + 1);
        livrosRepository.save(livro);

        emprestimo.setStatus("devolvido");
        emprestimoRepository.save(emprestimo);

        String professorEmail = aluno.getSerie().getProfessor().getEmail();
        String professorNome = aluno.getSerie().getProfessor().getNome();
        emailService.sendEmail(professorEmail, "Devolução Registrada",
                "Olá " + professorNome + ",\n\nA devolução do livro '" + livro.getTitulolivro() +
                "' emprestado ao aluno " + aluno.getNome() + " foi registrada com sucesso.\n\nAtenciosamente,\nBiblioteca");

        return livro;
    }

    private void enviarEmail(String email, String avisoDeDevoluçãoDeLivro, String s) {
    }

    public Object listarLivros() {
        return null;
    }

    public List<LivroEmprestadoDTO> listarLivrosEmprestados(){
        Collectors Collector = null;
        return emprestimoRepository.findAll().stream().map(emprestimo -> {
            LivroEmprestadoDTO dto = new LivroEmprestadoDTO();
            dto.setTituloLivro(emprestimo.getLivro().getTitulolivro());
            dto.setNomeAluno(emprestimo.getAluno().getNome());
            dto.setNomeProfessor(emprestimo.getAluno().getSerie().getProfessor().getNome());
            dto.setStatus(emprestimo.getStatus());
            dto.dataEmprestimo(emprestimo.getDataEmprestimo());
            dto.dataDevolucao(emprestimo.getDataDevolucao());
            return dto;
        }).collect(Collector.toList());
    }
}