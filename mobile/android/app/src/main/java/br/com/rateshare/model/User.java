package br.com.rateshare.model;

public class User {
    public String username;
    public String nameuser;
    public String email;
    public String pathFoto;
    public String data;
    public boolean ativo;
    public boolean adm;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username,
                String nameuser,
                String email,
                String pathFoto,
                String data,
                boolean ativo,
                boolean adm) {
        this.username = username;
        this.nameuser = nameuser;
        this.email = email;
        this.pathFoto = pathFoto;
        this.data = data;
        this.ativo = ativo;
        this.adm = adm;
    }


}
