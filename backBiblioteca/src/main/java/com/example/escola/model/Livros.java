package com.example.escola.model;

import jakarta.persistence.*;

@Entity
public class Livros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String titulolivro;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false, name = "quantidade_disponivel")
    private Integer quantidadeDisponivel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulolivro() {
        return titulolivro;
    }

    public void setTitulolivro(String titulolivro) {
        this.titulolivro = titulolivro;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(Integer quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }
}