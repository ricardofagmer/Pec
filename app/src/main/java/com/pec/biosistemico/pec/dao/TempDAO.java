package com.pec.biosistemico.pec.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.pec.biosistemico.pec.domain.Temp;
import com.pec.biosistemico.pec.helpers.DbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricardo on 19/11/2016.
 */

public class TempDAO {

    private SQLiteDatabase database;
    private DbHelper dbHelper;
    private String[] allColumns = {DbHelper.COLUMN_ID, "id", "nome"};


    public TempDAO(Context context){

        dbHelper = new DbHelper(context);
    }

    public TempDAO() {

    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public void deleteTemp(int i) {

        database = dbHelper.getWritableDatabase();

        long id = i;
        System.out.println("Comment deleted with id: " + id);
        database.delete(DbHelper.TABLE_TEMP, "id" + " = " + id, null);
    }

    public List<Temp> getAllTemp(){
        List<Temp> tempList = new ArrayList<Temp>();

        Cursor cursor = database.query(DbHelper.TABLE_TEMP,allColumns, null,null,null,null,null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            Temp temp = cursorTemp(cursor);
            tempList.add(temp);

            cursor.moveToNext();
        }

        cursor.close();
        return  tempList;
    }


    private Temp cursorTemp(Cursor cursor){

        Temp temp = new Temp();

        temp.setId(cursor.getInt(0));
        temp.setNome(cursor.getString(1));

        return  temp;
    }

}
