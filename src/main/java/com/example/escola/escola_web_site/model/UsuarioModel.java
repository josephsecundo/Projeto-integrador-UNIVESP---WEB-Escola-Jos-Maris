package com.example.escola.escola_web_site.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity(name = "usuario")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer codigo;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 50, message = "Nome deve ter no máximo 50 caracteres")
    @Column(nullable = false, length = 50)
    public String nome;

    @NotBlank(message = "Login é obrigatório")
    @Size(max = 15, message = "Login deve ter no máximo 15 caracteres")
    @Column(nullable = false, length = 15)
    public String login;


    @NotBlank(message = "Senha é obrigatória")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "A senha deve conter pelo menos 8 caracteres, incluindo letras, números e um caractere especial"
    )
    @Column(nullable = false, length = 20)
    public String senha;

    @Column(nullable = false)
    private boolean ativo = true;

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}