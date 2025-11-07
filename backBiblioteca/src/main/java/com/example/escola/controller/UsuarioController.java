package com.example.escola.controller;

import com.example.escola.dto.LoginRequest;
import com.example.escola.model.UsuarioModel;
import com.example.escola.repository.UsuarioRepository;
import com.example.escola.services.EmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {

    private static final Logger logger = LogManager.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioRepository repository;

    @PostMapping("/api/usuario/cadastrar")
    public ResponseEntity<String> cadastrarUsuario(@RequestBody UsuarioModel novoUsuario) {
        // Verifica se o login já existe
        if (repository.findByLogin(novoUsuario.getLogin()) != null) {
            return ResponseEntity.status(400).body("Login já está em uso.");
        }

        // Verifica se o e-mail já existe
        if (repository.findByEmail(novoUsuario.getEmail()) != null) {
            return ResponseEntity.status(400).body("E-mail já está em uso.");
        }

        // Define o usuário como ativo e salva no banco
        novoUsuario.setAtivo(true);
        repository.save(novoUsuario);

        return ResponseEntity.status(201).body("Usuário cadastrado com sucesso.");
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
            return ResponseEntity.status(404).body("Usuário não encontrado.");
        }
    }

    @PostMapping("/api/usuario/recuperar-senha")
    public ResponseEntity<String> recuperarSenha(@RequestBody String email) {
        UsuarioModel usuario = repository.findByEmail(email);

        if (usuario == null) {
            return ResponseEntity.status(404).body("E-mail não encontrado.");
        }

        // Exemplo: Gerar um código ou link de redefinição de senha
        String codigoRecuperacao = String.valueOf(new java.security.SecureRandom().nextInt(900000) + 100000); // Gera um código de 6 dígitos //
        String mensagem = "Seu código de recuperação de senha é: " + codigoRecuperacao;

        EmailService emailService = null;
        emailService.enviarEmail(email, "Recuperação de Senha", mensagem);

        return ResponseEntity.ok("E-mail de recuperação enviado com sucesso.");
    }

    @PostMapping("/api/usuario/editar/{id}")
    public ResponseEntity<String> editarUsuario(@PathVariable Integer id, @RequestBody UsuarioModel usuarioAtualizado) {
        // Verifica se o usuário existe
        UsuarioModel usuarioExistente = repository.findById(id).orElse(null);
        if (usuarioExistente == null) {
            return ResponseEntity.status(404).body("Usuário não encontrado.");
        }

        // Atualiza os campos fornecidos
        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setLogin(usuarioAtualizado.getLogin());
        usuarioExistente.setSenha(usuarioAtualizado.getSenha());
        usuarioExistente.setAtivo(usuarioAtualizado.isAtivo());

        // Salva as alterações
        repository.save(usuarioExistente);

        return ResponseEntity.ok("Usuário atualizado com sucesso.");
    }
}