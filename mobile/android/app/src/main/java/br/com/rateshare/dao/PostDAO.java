package br.com.rateshare.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.rateshare.model.Postagem;

public class PostDAO extends SQLiteOpenHelper {

    Context context;

    public PostDAO(Context context) {
        super(context, "Postagens", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Postagens (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                " titulo TEXT NOT NULL, " +
                " id_categoria INTEGER NOT NULL, " +
                " imagem TEXT NOT NULL, " +
                " rate REAL, " +
                " descricao TEXT, " +
                " data_cadastro TEXT NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        String sql = "";
//        switch (oldVersion) {
//            case 1:
//                sql = "ALTER TABLE Alunos ADD COLUMN caminhoFoto TEXT";
//                db.execSQL(sql); // indo para versao 2
//        }

    }

    public void insere(Postagem post) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = pegaDadosDoPostagem(post);

        db.insert("Postagens", null, dados);
    }

    @NonNull
    private ContentValues pegaDadosDoPostagem(Postagem post) {
        ContentValues dados = new ContentValues();
        dados.put("id", post.getId());
        dados.put("titulo", post.getTitulo());
        dados.put("id_categoria", post.getCategoria().getId());
        dados.put("imagem", post.getImagem());
        dados.put("rate", post.getRate());
        dados.put("descricao", post.getDescricao());
        dados.put("data_cadastro", post.getData_cadastro());
        return dados;
    }

    public List<Postagem> pegaMinhasPostagens() {

        CategoriaDAO categoriaDAO = new CategoriaDAO(this.context);


        String sql = "SELECT * FROM Postagens;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        List<Postagem> postagens = new ArrayList<Postagem>();
        while (c.moveToNext()) {
            Postagem post = new Postagem();
            post.setId(c.getInt(c.getColumnIndex("id")));
            post.setTitulo(c.getString(c.getColumnIndex("titulo")));
            post.setCategoria(categoriaDAO.getCategoriaByIdExterno(Integer.toString(c.getInt(c.getColumnIndex("id_categoria")))));
            post.setImagem(c.getString(c.getColumnIndex("imagem")));
            post.setRate(c.getDouble(c.getColumnIndex("rate")));
            post.setDescricao(c.getString(c.getColumnIndex("descricao")));
            post.setData_cadastro(c.getString(c.getColumnIndex("data_cadastro")));
            postagens.add(post);
        }
        c.close();

        return postagens;
    }

    public void deleta(Postagem post) {
        SQLiteDatabase db = getWritableDatabase();

        String[] params = {post.getId().toString()};
        db.delete("Postagens", "id = ?", params);
    }

    public void altera(Postagem post) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = pegaDadosDoPostagem(post);

        String[] params = {post.getId().toString()};
        db.update("Postagens", dados, "id = ?", params);
    }

}
