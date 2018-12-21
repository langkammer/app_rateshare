package br.com.rateshare.model;

import java.util.HashMap;
import java.util.Map;

public class Post {

    public String uid;
    public String author;
    public String title;
    public String descricao;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Post(String uid, String author, String title, String descricao) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.descricao = descricao;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", descricao);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }


}
