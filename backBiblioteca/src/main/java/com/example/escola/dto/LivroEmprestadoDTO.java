package com.example.escola.dto;

import java.time.LocalDate;

public class LivroEmprestadoDTO {
    private String tituloLivro;
    private String nomeAluno;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private String status;
    private String nomeProfessor;
    private String serieAluno;

    public LivroEmprestadoDTO(String tituloLivro, String nomeAluno, LocalDate dataEmprestimo, LocalDate dataDevolucao, String status, String nomeProfessor, String serieAluno) {
        this.tituloLivro = tituloLivro;
        this.nomeAluno = nomeAluno;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
        this.status = status;
        this.nomeProfessor = nomeProfessor;
        this.serieAluno = serieAluno;
    }

    public String getTituloLivro() {
        return tituloLivro;
    }

    public void setTituloLivro(String tituloLivro) {
        this.tituloLivro = tituloLivro;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNomeProfessor() {
        return nomeProfessor;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

    public String getSerieAluno() {
        return serieAluno;
    }

    public void setSerieAluno(String serieAluno) {
        this.serieAluno = serieAluno;
    }
}