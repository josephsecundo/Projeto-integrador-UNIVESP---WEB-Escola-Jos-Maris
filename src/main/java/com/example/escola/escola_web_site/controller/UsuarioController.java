package com.example.escola.escola_web_site.controller;

import com.example.escola.escola_web_site.model.UsuarioModel;
import com.example.escola.escola_web_site.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping(path = "/api/usuario/{codigo}")
    public ResponseEntity consultar(@PathVariable("codigo") Integer codigo) {
        return repository.findById(codigo)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/api/usuario/salvar")
    public UsuarioModel salvar(@RequestBody UsuarioModel usuario) {
        return repository.save(usuario);
    }

    @PutMapping(path = "/api/usuario/atualizar/{codigo}")
    public ResponseEntity<UsuarioModel> atualizar(@PathVariable("codigo") Integer codigo, @RequestBody UsuarioModel usuario) {
        try {
            return repository.findById(codigo)
                    .map(record -> {
                        logger.info("Atualizando usuário com ID: {}", codigo);
                        if (usuario.getNome() != null) record.setNome(usuario.getNome());
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
}

