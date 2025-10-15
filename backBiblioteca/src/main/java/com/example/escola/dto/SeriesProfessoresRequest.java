package com.example.escola.dto;

import java.util.Set;

public class SeriesProfessoresRequest {
    private Integer serieId;
    private Set<Integer> professoresIds;

    public Integer getSerieId() {
        return serieId;
    }

    public Set<Integer> getProfessoresIds() {
        return professoresIds;
    }
}