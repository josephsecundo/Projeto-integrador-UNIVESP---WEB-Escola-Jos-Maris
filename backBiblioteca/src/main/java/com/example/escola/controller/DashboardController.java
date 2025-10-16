package com.example.escola.controller;

import com.example.escola.dto.LivrosMaisEmprestadosDTO;
import com.example.escola.repository.EmprestimoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @GetMapping("/livros-mais-emprestados")
    public ResponseEntity<List<LivrosMaisEmprestadosDTO>> livrosMaisEmprestados(
            @RequestParam(defaultValue = "5") int top) {
        List<LivrosMaisEmprestadosDTO> lista = emprestimoRepository.findTopLivrosMaisEmprestados(PageRequest.of(0, top));
        return ResponseEntity.ok(lista);
    }
}