package br.com.rateshare.model;

public class Post {

    private String titulo;

    private String categoria;

    private String rate;

    private String imagem;

    private String descricao;

    private String numAvaliacoes;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNumAvaliacoes() {
        return numAvaliacoes;
    }

    public void setNumAvaliacoes(String numAvaliacoes) {
        this.numAvaliacoes = numAvaliacoes;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}