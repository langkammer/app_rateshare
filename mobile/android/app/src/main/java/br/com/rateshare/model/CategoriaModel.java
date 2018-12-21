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

    public Context context;

    private Long getId(){
        return this.id;
    }

    private void setId(Long id){
        this.id = id;
    }

    public CategoriaModel(Context context) {
        super(context,new Tabela().getTAbelaCategoria());
        this.context = context;
    }

    public ContentValues setBindContetnValues(){
        ContentValues contentValues = new ContentValues();

        contentValues.put("nome",this.nome);

        contentValues.put("id_externo",this.id_externo);

        contentValues.put("data",this.data);

        return contentValues;
    }

    public CategoriaModel getBindContetnValues(ContentValues contentValues){

        CategoriaModel categoriaModel = new CategoriaModel(this.context);

        categoriaModel.nome = contentValues.getAsString("nome");

        categoriaModel.id   = contentValues.getAsLong( "id");

        categoriaModel.id_externo = contentValues.getAsString("id_externo");

        categoriaModel.data = contentValues.getAsString("data");

        return categoriaModel;
    }

    public CategoriaModel salvar(){

        ContentValues content = save(setBindContetnValues());

        CategoriaModel categoriaModel = getBindContetnValues(content);

        return categoriaModel;
    }


    public List<CategoriaModel> listar(){

        List<ContentValues> contentValuesList = findAll();

        List<CategoriaModel> list = new ArrayList<>();

        for(ContentValues contentValues : contentValuesList){
            list.add(getBindContetnValues(contentValues));
        }
        return list;
    }

    public CategoriaModel findId(Long id){

        CategoriaModel categoriaModel = getBindContetnValues(findById(id));

        return categoriaModel;
    }

}
