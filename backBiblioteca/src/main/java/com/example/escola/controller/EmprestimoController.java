package com.example.escola.controller;

import com.example.escola.repository.AlunosRepository;
import com.example.escola.repository.EmprestimoRepository;
import com.example.escola.repository.LivrosRepository;
import com.example.escola.services.EmprestimoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoServices emprestimoServices;

    @Autowired
    private LivrosRepository livrosRepository;

    @Autowired
    private AlunosRepository alunosRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

}