package com.example.escola.repository;

import com.example.escola.model.Alunos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlunosRepository extends JpaRepository<Alunos, Integer> {
    Optional<Alunos> findByMatricula(String matricula);
}