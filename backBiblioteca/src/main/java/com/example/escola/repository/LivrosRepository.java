package com.example.escola.repository;

import com.example.escola.model.Livros;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LivrosRepository extends JpaRepository<Livros, Integer> {
    Optional<Livros> findByTitulolivro(String titulolivro);
}
