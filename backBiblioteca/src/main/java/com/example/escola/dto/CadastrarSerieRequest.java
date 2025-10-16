package com.example.escola.dto;

import java.util.Set;

public class CadastrarSerieRequest {

    private String nome;
    private Integer ano;
    private Set<Integer> professoresIds; // IDs of associated professors

    public String getNome() {
        return nome;
    }

    public Integer getAno() {
        return ano;
    }

    public Set<Integer> getProfessoresIds() {
        return professoresIds;
    }
}