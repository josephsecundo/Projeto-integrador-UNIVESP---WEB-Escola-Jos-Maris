package com.example.escola.escola_web_site.repository;

import com.example.escola.escola_web_site.model.UsuarioModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UsuarioRepository extends CrudRepository <UsuarioModel, Integer> {
    List<UsuarioModel> findByAtivoTrue();
    List<UsuarioModel> findByAtivoFalse();
    UsuarioModel findByRegistroToken(String registroToken);

    @Query("SELECT u FROM usuario u WHERE u.login = :login")
    UsuarioModel findByLogin(String login);

}
