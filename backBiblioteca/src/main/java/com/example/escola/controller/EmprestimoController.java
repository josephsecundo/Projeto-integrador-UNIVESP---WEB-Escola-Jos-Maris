package com.example.escola.controller;

import com.example.escola.model.Emprestimo;
import com.example.escola.services.EmprestimoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoServices emprestimoServices;

    @PostMapping
    public ResponseEntity<Emprestimo> registrarEmprestimo(
            @RequestParam String tituloId,
            @RequestParam Integer alunoId) {
        try {
            Emprestimo emprestimo = emprestimoServices.registrarEmprestimoById(tituloId, alunoId);
            return ResponseEntity.ok(emprestimo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}