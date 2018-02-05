package com.pec.biosistemico.pec.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pec.biosistemico.pec.dao.interfaces.SQLiteGenericDAO;
import com.pec.biosistemico.pec.helpers.DbHelper;
import com.pec.biosistemico.pec.model.PaperTop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricardo on 08/05/2017.
 */

public class CargaPaperTopGenericDAO implements SQLiteGenericDAO<PaperTop> {
    private SQLiteDatabase database;
    private String table_name = "paperTop";

    public  CargaPaperTopGenericDAO(Context context){
        DbHelper helper = new DbHelper(context);

        database = helper.getWritableDatabase();

    }

    @Override
    public long salvar(PaperTop paperTop) throws Exception {
        ContentValues values = new ContentValues();
        values.put("",paperTop.get_id());

        return  database.insert(table_name,null,values);
    }

    @Override
    public void editar(PaperTop paperTop) throws Exception {
        ContentValues values = new ContentValues();
        values.put("",paperTop.get_id());

        String where  = "codigo = ?,?,?";
        String[] args = {String.valueOf(paperTop.getNome())};

        database.update(table_name,values,where,args);

    }

    @Override
    public void deletar(PaperTop paperTop) throws Exception {

        String where  = "codigo = ?,?,?";
        String[] args = {String.valueOf(paperTop.get_id())};

        database.delete(table_name,where,args);
    }

    @Override
    public PaperTop buscar(long codigo) {

        PaperTop paperTop;

        String[] columns = {"codigo","nome"}; //campos da clase
        String where = "codigo = ?";
        String[] args = {String.valueOf(codigo)};

       Cursor cursor = database.query(table_name,columns,where,args,"group by",null,"order by");

        if(cursor.moveToFirst()){

            paperTop = new PaperTop();
            paperTop.setNome(cursor.getString(cursor.getColumnIndex("nome_usual")));

            return  paperTop;
        }

        return null;
    }

    @Override
    public List<PaperTop> bucarTodos() {

        List<PaperTop> lista_paperTop = new ArrayList<>();
        String[] columns = {"codigo","nome"}; //campos da clase
        String where = "codigo = ?";

        Cursor cursor = database.query(table_name,columns,where,null,"group by",null,"order by");

        if(cursor.moveToFirst()){

            do{
                PaperTop paperTop =  new PaperTop();
                paperTop.setNome(cursor.getString(cursor.getColumnIndex("nome_usual")));

                lista_paperTop.add(paperTop);

            }while (cursor.moveToNext());

            return lista_paperTop;

        }
        return null;
    }
}
