package br.com.rateshare.model;


import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.rateshare.dao.Tabela;
import br.com.rateshare.dao.generic.GenericDao;


public class PostModel extends GenericDao {

    private Long   id;

    public  String titulo;

    public  String id_user_externo;

    public  String id_externo;

    public  String id_categoria_externa;

    public  String nome_categoria;

    public  String descricao;

    public  Double nota;

    public  String  data;

    public  String  num_avaliacoes;

    public  String  caminho_foto;

    public  Integer flagEnvio;

    public Context context;

    public Long getId(){
        return this.id;
    }

    private void setId(Long id){
        this.id = id;
    }


    public PostModel(Context context) {
        super(context,new Tabela().getTAbelaPostagem());
        this.context = context;
    }

    public ContentValues setBindContetnValues(){
        ContentValues contentValues = new ContentValues();

        contentValues.put("titulo",this.titulo);

        contentValues.put("id_user_externo",this.id_user_externo);

        contentValues.put("id_externo",this.id_externo);

        contentValues.put("id_categoria_externa",this.id_categoria_externa);

        contentValues.put("nome_categoria",this.nome_categoria);

        contentValues.put("descricao",this.descricao);

        contentValues.put("nota",this.nota);

        contentValues.put("data",this.data);

        contentValues.put("caminho_foto",this.caminho_foto);

        contentValues.put("flagEnvio",this.flagEnvio);

        return contentValues;
    }

    public PostModel getBindContetnValues(ContentValues contentValues){

        PostModel postModel = new PostModel(this.context);

        postModel.id   = contentValues.getAsLong( "id");

        postModel.titulo   = contentValues.getAsString( "titulo");

        postModel.id_user_externo   = contentValues.getAsString( "id_user_externo");

        postModel.id_externo   = contentValues.getAsString( "id_externo");

        postModel.id_categoria_externa   = contentValues.getAsString( "id_categoria_externa");

        postModel.nome_categoria   = contentValues.getAsString( "nome_categoria");

        postModel.descricao   = contentValues.getAsString( "descricao");

        postModel.nota   = contentValues.getAsDouble( "nota");

        postModel.data   = contentValues.getAsString( "data");

        postModel.caminho_foto   = contentValues.getAsString( "caminho_foto");

        postModel.flagEnvio   = contentValues.getAsInteger( "flagEnvio");

        return postModel;
    }

    public PostModel salvar(){

        ContentValues content = save(setBindContetnValues());

        PostModel categoriaModel = getBindContetnValues(content);

        return categoriaModel;
    }


    public List<PostModel> listar(){

        List<ContentValues> contentValuesList = findAll();

        List<PostModel> list = new ArrayList<>();

        for(ContentValues contentValues : contentValuesList){
            list.add(getBindContetnValues(contentValues));
        }
        return list;
    }
}
