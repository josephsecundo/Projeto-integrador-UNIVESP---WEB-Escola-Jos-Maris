package com.example.escola.dto;

public class LivrosMaisEmprestadosDTO {
    private String titulo;
    private Long emprestimos;

    public LivrosMaisEmprestadosDTO(String titulo, Long emprestimos) {
        this.titulo = titulo;
        this.emprestimos = emprestimos;
    }

    public String getTitulo() {
        return titulo;
    }

    public Long getEmprestimos() {
        return emprestimos;
    }
}