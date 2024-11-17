package model.vo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MoedaVO {
    private int idMoeda;
    private UsuarioVO usuario;
    private String nome;
    private String pais;
    private int ano;
    private BigDecimal valor;
    private String detalhes;
    private LocalDate dataCadastro;
    private byte[] imagem;

    public MoedaVO(int idMoeda, UsuarioVO usuario, int idUsuario, String nome, String pais, int ano, BigDecimal valor, String detalhes, LocalDate dataCadastro, byte[] imagem) {
        super();
        this.idMoeda = idMoeda;
        this.usuario = usuario;
        this.nome = nome;
        this.pais = pais;
        this.ano = ano;
        this.valor = valor;
        this.detalhes = detalhes;
        this.dataCadastro = dataCadastro;
        this.imagem = imagem;
    }

    public MoedaVO() {
        super();
    }

    public int getIdMoeda() {
        return idMoeda;
    }

    public void setIdMoeda(int idMoeda) {
        this.idMoeda = idMoeda;
    }

    public UsuarioVO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioVO usuario) {
        this.usuario = usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro= dataCadastro;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }
}