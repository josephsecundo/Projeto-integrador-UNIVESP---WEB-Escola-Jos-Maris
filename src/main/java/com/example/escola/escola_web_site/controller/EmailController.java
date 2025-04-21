package com.example.escola.escola_web_site.controller;


import com.example.escola.escola_web_site.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/enviar")
    public String enviarEmail(String destinatario, String assunto, String mensagem) {
        emailService.enviarEmail(destinatario, assunto, mensagem);
        return "Email enviado com sucesso!";
    }
}
