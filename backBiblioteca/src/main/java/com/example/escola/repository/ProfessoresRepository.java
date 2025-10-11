package com.example.escola.repository;

import com.example.escola.model.Professores;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessoresRepository extends JpaRepository<Professores, Integer> {
}