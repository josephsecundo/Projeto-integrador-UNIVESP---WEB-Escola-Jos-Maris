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
    private Integer quantidade_disponivel;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = true)
    private String localizacao;

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

    public Integer getQuantidadeDisponivel() {
        return quantidade_disponivel;
    }

    public void setQuantidadeDiponivel(Integer quantidade_disponivel) {
        this.quantidade_disponivel = quantidade_disponivel;
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
}