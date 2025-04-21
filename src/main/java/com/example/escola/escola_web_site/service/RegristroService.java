package com.example.escola.escola_web_site.service;

import com.example.escola.escola_web_site.model.UsuarioModel;
import com.example.escola.escola_web_site.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;

public class RegristroService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private EmailService emailService;

    public String gerarRegistroToken (UsuarioModel usuario) {
        String token = UUID.randomUUID().toString();
        usuario.setRegistroToken(token);
        usuario.setTokenExpiration(LocalDateTime.now().plusHours(24)); // Token válido por 24 horas
        repository.save(usuario);
        return token;
    }

    public void enviarEmailRegistro(UsuarioModel usuario, String token) {
        String subject = "Confirmação de Registro";
        String body = "Clique no link para confirmar seu registro: " +
                "http://localhost:8080/api/usuario/confirmar/" + token;
        emailService.enviarEmail(usuario.getEmail(), subject, body);
    }
}
