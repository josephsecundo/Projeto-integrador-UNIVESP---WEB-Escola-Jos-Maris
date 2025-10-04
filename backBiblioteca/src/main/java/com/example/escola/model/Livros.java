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
    private String autorlivro;

    @Column(nullable = false)
    private String editora;

    @Column(nullable = false)
    private Integer anopublicacao;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = true)
    private String localizacao;

    @Column(nullable = false)
    private Boolean disponivel;

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

    public String getAutorlivro() {
        return autorlivro;
    }

    public void setAutorlivro(String autorlivro) {
        this.autorlivro = autorlivro;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public Integer getAnopublicacao() {
        return anopublicacao;
    }

    public void setAnopublicacao(Integer anopublicacao) {
        this.anopublicacao = anopublicacao;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
}