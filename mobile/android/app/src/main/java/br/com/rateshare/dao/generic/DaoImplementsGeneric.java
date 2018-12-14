package br.com.rateshare.dao.generic;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public interface DaoImplementsGeneric  {

    ContentValues save(ContentValues dados);

    void update(ContentValues dados);

    void delete(Long id);

    ContentValues findById(Long id);


    List<ContentValues> findAll();

}
