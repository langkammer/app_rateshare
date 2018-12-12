package br.com.rateshare.model;

import com.orm.SugarRecord;

public class Categoria extends SugarRecord<Categoria> {

    public Long id;

    public String  nomeCategoria;

    public Integer  id_externo;

    public String  data_cadastro;


    public Categoria(){
    }

    public Categoria(Long id, String nomeCategoria,Integer id_externo,String data_cadastro){
        this.id = id;
        this.nomeCategoria = nomeCategoria;
        this.id_externo = id_externo;
        this.data_cadastro = data_cadastro;
    }


    public Categoria findById(Long id){
        return Categoria.findById(Categoria.class, id);
    }


    public void save(){
         this.save();
    }

    public Categoria update(){
        Categoria categoria = Categoria.findById(Categoria.class, this.id);
        categoria = this;
        categoria.save(); // updates the previous entry with new values.
        return categoria;
    }

    public void deleta(Long id){
        Categoria categoria = Categoria.findById(Categoria.class, id);
        categoria.delete();
    }



}
