package br.com.rateshare.model;

import com.google.firebase.auth.FirebaseUser;

import br.com.rateshare.util.DataUtil;

public class User {
    public String uid;
    public String nome;
    public String email;
    public String pathFoto;
    public String data;
    public boolean ativo;
    public boolean adm;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid,
                String nome,
                String email,
                String pathFoto,
                String data,
                boolean ativo,
                boolean adm) {
        this.uid = uid;
        this.nome = nome;
        this.email = email;
        this.pathFoto = pathFoto;
        this.data = data;
        this.ativo = ativo;
        this.adm = adm;
    }

    public User criaPerfilUsuario(FirebaseUser user,String nome){
        this.email = user.getEmail();
        this.adm = false;
        this.ativo = true;
        this.data = DataUtil.data;
        this.uid = user.getUid();
        this.nome = nome;
        return this;
    }


}
