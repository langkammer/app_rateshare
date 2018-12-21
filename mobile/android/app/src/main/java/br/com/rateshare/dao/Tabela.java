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


    public Tabela getTAbelaPostagem(){
        this.tabela = "POSTAGEM";
        this.script =  new String[]{
                "(",
                "id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,",
                "titulo                TEXT,",
                "id_user_externo       TEXT,",
                "id_externo            TEXT,",
                "id_categoria_externa  TEXT,",
                "nome_categoria        TEXT,",
                "descricao             TEXT,",
                "nota                  REAL,",
                "num_avaliacoes        TEXT,",
                "caminho_foto          TEXT,",
                "data                  TEXT",
                ");"
        };


        ContentValues dados = new ContentValues();
        dados.put("id","long");
        dados.put("titulo","string");
        dados.put("id_user_externo","string");
        dados.put("id_externo","string");
        dados.put("id_categoria_externa","string");
        dados.put("nome_categoria","string");
        dados.put("descricao","string");
        dados.put("nota","double");
        dados.put("num_avaliacoes","string");
        dados.put("caminho_foto","string");
        dados.put("data","string");
        this.contentValues = dados;

        return this;
    }


}
