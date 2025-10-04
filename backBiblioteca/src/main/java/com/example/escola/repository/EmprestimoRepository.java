package com.example.escola.repository;

import com.example.escola.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Integer> {
    boolean existsByLivroId(Integer livroId);
}