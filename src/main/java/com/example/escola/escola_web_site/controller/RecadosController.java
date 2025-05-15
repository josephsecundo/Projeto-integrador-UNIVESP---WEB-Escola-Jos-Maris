package com.example.escola.escola_web_site.controller;

import com.example.escola.escola_web_site.model.RecadosModel;
import com.example.escola.escola_web_site.repository.RecadosRepository;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/api/recados")
public class RecadosController {

    private static final Logger logger = LogManager.getLogger(RecadosController.class);

    @Autowired
    private RecadosRepository repository;

    @GetMapping
    public ResponseEntity<Iterable<RecadosModel>> listarTodos(){
        Iterable<RecadosModel> recados = repository.findAll();
        return ResponseEntity.ok(recados);
    }

    @GetMapping("/ativos")
    public ResponseEntity<Iterable<RecadosModel>> listarAtivos() {
        Iterable<RecadosModel> recadosAtivos = repository.findByAtivoTrue();
        return ResponseEntity.ok(recadosAtivos);
    }

    @GetMapping("/inativos")
    public ResponseEntity<Iterable<RecadosModel>> listarInativos() {
        Iterable<RecadosModel> recadosInativos = repository.findByAtivoFalse();
        return ResponseEntity.ok(recadosInativos);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity consultar(@PathVariable("codigo") Integer id) {
        return repository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/salvar")
    public RecadosModel salvar(@RequestBody RecadosModel recado) {
        if (recado.getdataValidade() == null || recado.getdataPostagem() == null) {
            throw new IllegalArgumentException("Data de postagem e data de validade são obrigatórias.");
        }

        if (recado.getdataValidade().isBefore(recado.getdataPostagem())) {
            throw new IllegalArgumentException("A data de validade não pode ser anterior à data de postagem.");
        }

        if (recado.getdataValidade().isBefore(LocalDate.now())) {
            recado.setAtivo(false);
        }

        return repository.save(recado);
    }

    @PutMapping("/atualizar/{codigo}")
    public ResponseEntity<RecadosModel> atualizar(@PathVariable("codigo") Integer id, @RequestBody RecadosModel recado) {
        logger.info("Atualizando recado com ID {}: {}", id, recado);
        return repository.findById(id)
                .map(record -> {
                    if (recado.getTitulo() != null) record.setTitulo(recado.getTitulo());
                    if (recado.getDescricao() != null) record.setDescricao(recado.getDescricao());
                    if (recado.getdataPostagem() != null) record.setdataPostagem(recado.getdataPostagem());
                    if (recado.getdataValidade() != null) record.setdataValidade(recado.getdataValidade());
                    RecadosModel updated = repository.save(record);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/inativar/{codigo}")
    public ResponseEntity<RecadosModel> inativar(@PathVariable Integer codigo) {
        return repository.findById(codigo)
                .map(record -> {
                    record.setAtivo(false); // Define o recado como inativo
                    RecadosModel atualizado = repository.save(record);
                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/reativar/{codigo}")
    public ResponseEntity<RecadosModel> reativar(@PathVariable Integer codigo) {
        return repository.findById(codigo)
                .map(record -> {
                    record.setAtivo(true); // Define o recado como ativo
                    RecadosModel atualizado = repository.save(record);
                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
