package br.com.rateshare.model;


import android.content.ContentValues;
import android.content.Context;


import java.util.ArrayList;
import java.util.List;

import br.com.rateshare.dao.generic.GenericDao;
import br.com.rateshare.dao.Tabela;


public class CategoriaModel extends GenericDao {

    private Long id;

    public String  nome;

    public String id_externo;

    public String  data;

    private Long getId(){
        return this.id;
    }

    private void setId(Long id){
        this.id = id;
    }

    public CategoriaModel(Context context,String databaseName) {
        super(context,databaseName,new Tabela().getTAbelaCategoria());
    }

    public ContentValues setBindContetnValues(){
        ContentValues contentValues = new ContentValues();

        contentValues.put("nome",this.nome);

        contentValues.put("id_externo",this.id_externo);

        contentValues.put("data",this.data);

        return contentValues;
    }

    public CategoriaModel getBindContetnValues(ContentValues contentValues){

        this.nome = contentValues.getAsString("nome");

        this.id   = contentValues.getAsLong( "id");

        this.id_externo = contentValues.getAsString("id_externo");

        this.data = contentValues.getAsString("data");

        return this;
    }

    public CategoriaModel salvar(){

        ContentValues content = save(setBindContetnValues());

        CategoriaModel categoriaModel = getBindContetnValues(content);

        return categoriaModel;
    }


    public List<CategoriaModel> listar(){

        List<ContentValues> contentValuesList = findAll();

        List<CategoriaModel> list = new ArrayList<CategoriaModel>();

        CategoriaModel categoriaModel;

        for(ContentValues contentValues : contentValuesList){
            categoriaModel = getBindContetnValues(contentValues);
            list.add(categoriaModel);
        }

        return list;
    }
}
