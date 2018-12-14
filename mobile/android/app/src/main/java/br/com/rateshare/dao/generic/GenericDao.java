package br.com.rateshare.dao.generic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.rateshare.dao.Tabela;

public class GenericDao extends SQLiteOpenHelper implements DaoImplementsGeneric{

    private Tabela tabela;

    public SQLiteDatabase db;

    public GenericDao(Context context,String database,Tabela tabela){
        super(context, database,null,1);
        Log.i("GENERIC_DAO", "ACESSANDO CONSTRUTOR GENERIC DAO ");
        setTabela(tabela);

    }

    public void setTabela(Tabela tabela) {
        this.tabela = tabela;
    }

    public Tabela getTabela(){
        return tabela;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + tabela.tabela;
        for(int i = 0; i < tabela.script.length ; i++){
            sql += tabela.script[i];
        }
        Log.i("GENERIC_DAO", "EXECUTANDO SCRIPT " + sql);

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public ContentValues save(ContentValues dados) {
        db = getWritableDatabase();
        long idv = db.insert(tabela.tabela, null, dados);
        Log.i("GENERIC_DAO","DADO SALVO ID : " + idv);
        if(idv > 0){
            return findById(idv);
        }
        else{
            return null;
        }

    }

    @Override
    public void update(ContentValues dados) {
        db = getWritableDatabase();
        String[] params = {tabela.contentValues.get("id").toString()};
        db.update(tabela.tabela, dados, "id = ?", params);
    }

    @Override
    public void delete(Long id) {
        db = getWritableDatabase();
        String[] params = {id.toString()};
        db.delete(tabela.tabela, "id = ?", params);
    }

    @Override
    public ContentValues findById(Long id) {
        db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+tabela.tabela+" WHERE id = ?", new String[]{id.toString()});
        ContentValues dados;
        Object object = new Object();
        if(c.getColumnCount() > 0){
            dados = new ContentValues();
            while (c.moveToNext()) {
                if(c.isFirst()){
                    for (String key : tabela.contentValues.keySet()) {
                        if(tabela.contentValues.get(key).equals("long"))
                            dados.put(key, c.getLong(c.getColumnIndex(key)));
                        if(tabela.contentValues.get(key).equals("string"))
                            dados.put(key, c.getString(c.getColumnIndex(key)));
                        if(tabela.contentValues.get(key).equals("double"))
                            dados.put(key, c.getDouble(c.getColumnIndex(key)));
                        if(tabela.contentValues.get(key).equals("float"))
                            dados.put(key, c.getFloat(c.getColumnIndex(key)));
                        if(tabela.contentValues.get(key).equals("blob"))
                            dados.put(key, c.getBlob(c.getColumnIndex(key)));
                        if(tabela.contentValues.get(key).equals("int"))
                            dados.put(key, c.getInt(c.getColumnIndex(key)));
                    }
                }
            }
            c.close();
            return dados;
        }
        else{
            c.close();
            return null;
        }
    }

    @Override
    public List<ContentValues> findAll() {
        String sql = "SELECT * FROM "+ tabela.tabela + ";" ;
        db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        ContentValues dados;
        List<ContentValues> listaContents = new ArrayList<ContentValues>();
        while (c.moveToNext()) {
            dados = new ContentValues();
            for (String key : tabela.contentValues.keySet()) {
                if(tabela.contentValues.get(key).equals("long"))
                    dados.put(key, c.getLong(c.getColumnIndex(key)));
                if(tabela.contentValues.get(key).equals("string"))
                    dados.put(key, c.getString(c.getColumnIndex(key)));
                if(tabela.contentValues.get(key).equals("double"))
                    dados.put(key, c.getDouble(c.getColumnIndex(key)));
                if(tabela.contentValues.get(key).equals("float"))
                    dados.put(key, c.getFloat(c.getColumnIndex(key)));
                if(tabela.contentValues.get(key).equals("blob"))
                    dados.put(key, c.getBlob(c.getColumnIndex(key)));
                if(tabela.contentValues.get(key).equals("int"))
                    dados.put(key, c.getInt(c.getColumnIndex(key)));
            }
            listaContents.add(dados);
        }
        c.close();

        return listaContents;

    }

}
