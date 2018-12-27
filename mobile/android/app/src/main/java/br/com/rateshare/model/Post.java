package br.com.rateshare.model;

import java.util.HashMap;
import java.util.Map;

public class Post {

    public  String titulo;

    public  String uid;

    public  Categoria categoria;

    public  String descricao;

    public  String  data;

    public  Integer  numAvaliacoes;

    public  String  pathFotoAndroid;

    public  Boolean aprovado;

    public Map<String, Integer> stars = new HashMap<>();

    private String key;

    public String getKey(){
        return key;
    }

    public void setKey(String key){
        this.key = key;
    }



}
