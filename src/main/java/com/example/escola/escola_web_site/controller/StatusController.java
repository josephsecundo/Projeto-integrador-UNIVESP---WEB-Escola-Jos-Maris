package com.example.escola.escola_web_site.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @GetMapping(path ="/api/status")
    public String check() {
        return "API conectada com sucesso!";
    }

}
