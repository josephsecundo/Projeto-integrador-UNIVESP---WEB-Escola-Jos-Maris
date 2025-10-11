package com.example.escola.services;

import com.example.escola.model.Emprestimo;
import com.example.escola.model.Livros;
import com.example.escola.model.Alunos;
import com.example.escola.repository.EmprestimoRepository;
import com.example.escola.repository.LivrosRepository;
import com.example.escola.repository.AlunosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoServices {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private LivrosRepository livrosRepository;

    @Autowired
    private AlunosRepository alunosRepository;

    @Autowired
    private JavaMailSender mailSender;

    public Emprestimo registrarEmprestimoById(String tituloId, Integer alunoId) {
        return null;
    }
}