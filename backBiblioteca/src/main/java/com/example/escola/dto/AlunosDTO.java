package com.example.escola.dto;

public class AlunosDTO {
    private Integer id;
    private String nome;
    private String matricula;
    private SerieDTO serie;
    private ProfessorDTO professor;

    public AlunosDTO(Integer id, String nome, String matricula, SerieDTO serie, ProfessorDTO professor) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
        this.serie = serie;
        this.professor = professor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public SerieDTO getSerie() {
        return serie;
    }

    public void setSerie(SerieDTO serie) {
        this.serie = serie;
    }

    public ProfessorDTO getProfessor() {
        return professor;
    }

    public void setProfessor(ProfessorDTO professor) {
        this.professor = professor;
    }
}