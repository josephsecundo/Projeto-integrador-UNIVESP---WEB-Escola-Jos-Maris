package com.example.escola.repository;

import com.example.escola.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {
    UsuarioModel findByLogin(String login);

    UsuarioModel findByEmail(String email);
}