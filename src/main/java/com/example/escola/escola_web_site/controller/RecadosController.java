package com.example.escola.escola_web_site.controller;

import com.example.escola.escola_web_site.model.RecadosModel;
import com.example.escola.escola_web_site.repository.RecadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/recados")
public class RecadosController {

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
        LocalDate dataValidade = recado.getdataValidade();
        if (dataValidade.isBefore(LocalDate.now())) {
            recado.setAtivo(false);
        }
        recado.setdataValidade(dataValidade);
    } catch (DateTimeParseException e) {
        throw new IllegalArgumentException("Formato de data inválido. Use o formato dd/MM/yyyy.");
    }
        return repository.save(recado);
    }

    @PutMapping("/atualizar/{codigo}")
    public ResponseEntity<RecadosModel> atualizar(@PathVariable("codigo") Integer id, @RequestBody RecadosModel recado) {
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

}
