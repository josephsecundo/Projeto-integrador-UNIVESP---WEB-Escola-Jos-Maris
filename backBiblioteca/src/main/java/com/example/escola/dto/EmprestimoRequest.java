package com.example.escola.dto;

import java.time.LocalDate;

public class EmprestimoRequest {
    private Integer livroId;
    private Integer matriculaAluno;
    private Integer professorId;
    private LocalDate dataEmprestimo; // Novo campo

    // Getters e Setters
    public Integer getLivroId() {
        return livroId;
    }

    public void setLivroId(Integer livroId) {
        this.livroId = livroId;
    }

    public String getMatriculaAluno() {
        return matriculaAluno != null ? matriculaAluno.toString() : null;
    }

    public void setMatriculaAluno(Integer matriculaAluno) {
        this.matriculaAluno = matriculaAluno;
    }

    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }
}