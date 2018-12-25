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

    public  String  pathFoto;

    public  Boolean aprovado;

    public Map<String, Integer> stars = new HashMap<>();


//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("uid", uid);
//        result.put("author", author);
//        result.put("title", title);
//        result.put("body", descricao);
//        result.put("starCount", starCount);
//        result.put("stars", stars);
//
//        return result;
//    }


}
