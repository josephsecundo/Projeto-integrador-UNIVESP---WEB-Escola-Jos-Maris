package com.example.escola.escola_web_site.controller;

import com.example.escola.escola_web_site.model.UsuarioModel;
import com.example.escola.escola_web_site.repository.UsuarioRepository;
import com.example.escola.escola_web_site.service.EmailService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);


    @GetMapping(path = "/api/usuario")
    public ResponseEntity<Iterable<UsuarioModel>> listarTodos() {
        Iterable<UsuarioModel> usuarios = repository.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping(path = "/api/usuario/ativos")
    public ResponseEntity<Iterable<UsuarioModel>> listarAtivos() {
        Iterable<UsuarioModel> usuariosAtivos = repository.findByAtivoTrue();
        return ResponseEntity.ok(usuariosAtivos);
    }

    @GetMapping(path = "/api/usuario/inativos")
    public ResponseEntity<Iterable<UsuarioModel>> listarInativos() {
        Iterable<UsuarioModel> usuariosInativos = repository.findByAtivoFalse();
        return ResponseEntity.ok(usuariosInativos);
    }



    @Autowired
    private EmailService emailService;

    @PostMapping(path = "/api/usuario/salvar")
    public ResponseEntity<String> salvar(@RequestBody UsuarioModel usuario) {
        String token = UUID.randomUUID().toString();
        usuario.setRegistroToken(token);
        usuario.setTokenExpiration(LocalDateTime.now().plusHours(24));
        usuario.setAtivo(false);
        repository.save(usuario);

        String mensagem = "Olá " + usuario.getNome() + ",\n\n" +
                "Por favor, confirme seu cadastro clicando no link abaixo:\n" +
                "http://localhost:8081/api/usuario/validar-token?token=" + token;
        emailService.enviarEmail(usuario.getEmail(), "Confirmação de Cadastro", mensagem);

        return ResponseEntity.ok("Usuário cadastrado com sucesso! Verifique seu e-mail para confirmar o registro.");
    }

    @PutMapping(path = "/api/usuario/atualizar/{codigo}")
    public ResponseEntity<UsuarioModel> atualizar(@PathVariable("codigo") Integer codigo, @RequestBody UsuarioModel usuario) {
        try {
            return repository.findById(codigo)
                    .map(record -> {
                        logger.info("Atualizando usuário com ID: {}", codigo);
                        if (usuario.getNome() != null) record.setNome(usuario.getNome());
                        if (usuario.getEmail() != null) record.setEmail(usuario.getEmail());
                        if (usuario.getLogin() != null) record.setLogin(usuario.getLogin());
                        if (usuario.getSenha() != null) record.setSenha(usuario.getSenha());
                        UsuarioModel updated = repository.save(record);
                        logger.info("Usuário atualizado com sucesso: {}", updated);
                        return ResponseEntity.ok(updated);
                    })
                    .orElseGet(() -> {
                        logger.warn("Usuário com código {} não existe", codigo);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuário {}: {}", codigo, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping(path = "/api/usuario/desativar/{codigo}")
    @Transactional
    public ResponseEntity<Object> desativar(@PathVariable("codigo") Integer codigo) {
        try {
            return repository.findById(codigo)
                    .map(record -> {
                        record.setAtivo(false); // Set the 'ativo' field to false
                        repository.save(record); // Save the updated record
                        logger.info("Usuário com ID {} desativado com sucesso", codigo);
                        return ResponseEntity.ok().build();
                    })
                    .orElseGet(() -> {
                        logger.warn("Usuário com código {} não existe", codigo);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("Erro ao desativar usuário {}: {}", codigo, e.getMessage(), e);
            return ResponseEntity.status(500).body("Erro ao desativar usuário: " + e.getMessage());
        }
    }

    @PostMapping("/api/usuario/validar-token")
    public ResponseEntity<String> validarToken(@RequestParam String token) {
        logger.info("Recebendo token: {}", token);
        UsuarioModel usuario = repository.findByRegistroToken(token);
        if (usuario != null) {
            logger.info("Usuário encontrado: {}", usuario.getNome());
            if (usuario.getTokenExpiration().isAfter(LocalDateTime.now())) {
                usuario.setAtivo(true);
                usuario.setRegistroToken(null);
                usuario.setTokenExpiration(null);
                repository.save(usuario);
                logger.info("Registro confirmado com sucesso para o usuário: {}", usuario.getNome());
                return ResponseEntity.ok("Registro confirmado com sucesso!");
            } else {
                logger.warn("Token expirado para o usuário: {}", usuario.getNome());
                return ResponseEntity.badRequest().body("Token inválido ou expirado.");
            }
        } else {
            logger.warn("Token não encontrado: {}", token);
            return ResponseEntity.badRequest().body("Token inválido ou expirado.");
        }
    }

    @GetMapping("/api/usuario/validar-token")
    public ResponseEntity<String> validarTokenGet(@RequestParam String token) {
        return validarToken(token);
    }


    @PostMapping("/api/usuario/complete-registro")
    public ResponseEntity<String> completeRegistro(@RequestParam String token) {
        UsuarioModel usuario = repository.findByRegistroToken(token);
        if (usuario != null && usuario.getTokenExpiration().isAfter(LocalDateTime.now())) {
            return ResponseEntity.ok("Registro já confirmado.");
        } else {
            return ResponseEntity.badRequest().body("Token inválido ou expirado.");
        }
    }

    @PostMapping("/api/usuario/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Tentativa de login com login: {}", loginRequest.getLogin());
        UsuarioModel usuario = repository.findByLogin(loginRequest.getLogin());

        if (usuario != null && usuario.isAtivo()) {
            if (usuario.getSenha().equals(loginRequest.getSenha())) {
                logger.info("Login bem-sucedido para o usuário: {}", usuario.getNome());
                return ResponseEntity.ok("Login efetuado com sucesso!");
            } else {
                logger.warn("Senha incorreta para o usuário: {}", usuario.getNome());
                return ResponseEntity.status(401).body("Senha incorreta.");
            }
        } else {
            logger.warn("Usuário não encontrado ou inativo: {}", loginRequest.getLogin());
            return ResponseEntity.status(404).body("Usuário não encontrado ou inativo.");
        }
    }
}

