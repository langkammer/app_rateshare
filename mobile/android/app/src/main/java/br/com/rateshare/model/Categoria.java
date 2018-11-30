package br.com.rateshare.model;

public class Categoria {

    private Integer id;

    private String  nomeCategoria;

    private Integer  id_externo;

    private String  data_cadastro;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public Integer getId_externo() {
        return id_externo;
    }

    public void setId_externo(Integer id_externo) {
        this.id_externo = id_externo;
    }

    public String getData_cadastro() {
        return data_cadastro;
    }

    public void setData_cadastro(String data_cadastro) {
        this.data_cadastro = data_cadastro;
    }
}
