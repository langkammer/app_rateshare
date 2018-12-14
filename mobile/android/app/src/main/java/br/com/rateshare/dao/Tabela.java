package br.com.rateshare.dao;

import android.content.ContentValues;
import android.content.Context;

import java.util.List;

import br.com.rateshare.dao.generic.FieldGeneric;

public class Tabela {

    public String tabela;

    public String[] script;

    public ContentValues contentValues;


    public Tabela getTAbelaCategoria(){
        this.tabela = "CATEGORIA";
        this.script =  new String[]{
                "(",
                "id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,",
                "id_externo TEXT,",
                "nome       TEXT,",
                "data       TEXT",
                ");"
        };

        ContentValues dados = new ContentValues();
        dados.put("id","long");
        dados.put("id_externo","string");
        dados.put("nome","string");
        dados.put("data","string");
        this.contentValues = dados;

        return this;
    }
}
