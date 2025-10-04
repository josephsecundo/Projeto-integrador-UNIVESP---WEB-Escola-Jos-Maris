package com.example.escola.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livros livro;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Alunos aluno;

    @Column(nullable = false)
    private LocalDate dataEmprestimo;

    @Column(nullable = true)
    private LocalDate dataDevolucao;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Livros getLivro() {
        return livro;
    }

    public void setLivro(Livros livro) {
        this.livro = livro;
    }

    public Alunos getAluno() {
        return aluno;
    }

    public void setAluno(Alunos aluno) {
        this.aluno = aluno;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }
}