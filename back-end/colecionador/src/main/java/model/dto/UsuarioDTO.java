package model.dto;

import java.time.LocalDate;

public class UsuarioDTO {
    private int idUsuario;
    private String nome;
    private String email;
    private String login;
    private LocalDate dataCadastro;
    private LocalDate dataExpiracao;

    // Construtor completo para conversões fáceis entre UsuarioVO e UsuarioDTO
    public UsuarioDTO(int idUsuario, String nome, String email, String login, LocalDate dataCadastro, LocalDate dataExpiracao) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.dataCadastro = dataCadastro;
        this.dataExpiracao = dataExpiracao;
    }

    // Getters e Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }
}