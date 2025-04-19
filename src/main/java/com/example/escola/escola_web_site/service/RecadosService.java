package com.example.escola.escola_web_site.service;

import com.example.escola.escola_web_site.model.RecadosModel;
import com.example.escola.escola_web_site.repository.RecadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecadosService {

    @Autowired
    private RecadosRepository repository;

    @Scheduled(cron = "0 0 0 * * ?") // Executa diariamente à meia-noite
    public void validateExpiredRecados() {
        System.out.println("Running scheduled task to validate expired recados...");
        LocalDate currentDate = LocalDate.now();
        List<RecadosModel> expiredRecados = repository.findByDataValidadeBeforeAndAtivoTrue(currentDate);

        for (RecadosModel recado : expiredRecados) {
            recado.setAtivo(false);
            repository.save(recado);
        }
    }
}
