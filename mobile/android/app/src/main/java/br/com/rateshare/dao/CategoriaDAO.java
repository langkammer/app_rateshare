package br.com.rateshare.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.rateshare.model.Categoria;

public class CategoriaDAO extends SQLiteOpenHelper {

    public CategoriaDAO(Context context) {
        super(context, "Categoria", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Categoria (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                " id_externo INTEGER, " +
                " nomeCategoria TEXT NOT NULL, " +
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

    @NonNull
    private ContentValues pegaDadosCategoria(Categoria categoria) {
        ContentValues dados = new ContentValues();
        dados.put("titulo", categoria.getId());
        dados.put("id_externo", categoria.getId_externo());
        dados.put("nomeCategoria", categoria.getNomeCategoria());
        dados.put("data_cadastro", categoria.getData_cadastro());
        return dados;
    }


    public List<Categoria> listaCategorias() {
        String sql = "SELECT * FROM Categoria;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        List<Categoria> categorias = new ArrayList<Categoria>();
        while (c.moveToNext()) {
            Categoria categoria = new Categoria();
            categoria.setId(c.getInt(c.getColumnIndex("id")));
            categoria.setId_externo(c.getInt(c.getColumnIndex("id_externo")));
            categoria.setNomeCategoria(c.getString(c.getColumnIndex("nomeCategoria")));
            categoria.setData_cadastro(c.getString(c.getColumnIndex("data_cadastro")));
            categorias.add(categoria);
        }
        c.close();

        return categorias;
    }

    public Categoria getCategoriaById(String id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Categoria WHERE id = ?", new String[]{id});
        if(c.getCount() > 0){

            Categoria categoria = new Categoria();

            categoria.setId(c.getInt(c.getColumnIndex("id")));

            categoria.setId_externo(c.getInt(c.getColumnIndex("id_externo")));

            categoria.setNomeCategoria(c.getString(c.getColumnIndex("nomeCategoria")));

            categoria.setData_cadastro(c.getString(c.getColumnIndex("data_cadastro")));

            return categoria;
        }
        else{
            return null;
        }
    }

    public Categoria getCategoriaByIdExterno(String  id_externo){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Categoria WHERE id_externo = ?", new String[]{id_externo});
        if(c.getCount() > 0){

            Categoria categoria = new Categoria();

            categoria.setId(c.getInt(c.getColumnIndex("id")));

            categoria.setId_externo(c.getInt(c.getColumnIndex("id_externo")));

            categoria.setNomeCategoria(c.getString(c.getColumnIndex("nomeCategoria")));

            categoria.setData_cadastro(c.getString(c.getColumnIndex("data_cadastro")));

            return categoria;
        }
        else{
            return null;
        }
    }



}
