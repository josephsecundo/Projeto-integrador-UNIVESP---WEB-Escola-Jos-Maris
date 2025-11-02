package com.example.escola.repository;

import com.example.escola.model.Professores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessoresRepository extends JpaRepository<Professores, Integer> {
    List<Professores> findProfessoresBySeriesId(Integer serieId);
}