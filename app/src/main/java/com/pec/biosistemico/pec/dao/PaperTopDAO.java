package com.pec.biosistemico.pec.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.pec.biosistemico.pec.helpers.DbHelper;
import com.pec.biosistemico.pec.model.PaperTop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricardo on 16/11/2016.
 */

public class PaperTopDAO {

    private SQLiteDatabase database;
    private DbHelper dbHelper;
    private String[] allColumns = {DbHelper.COLUMN_ID,"categoria","idAnimais","_projeto","_grupo","_propriedade","data_coleta","area_atual","brinco","nome_usual",
            "status","data_secagem","obs","ultimo_parto",
            "ocorrencia","referencia","previsaoSecagem","dataCobertura","dataDiagnostico","dataParto","diasPrenhez","idCobertura","idReprodutivo","numeroFetos","previsaoParto","statusProdutivo","manejo"};

    public PaperTopDAO(Context context){

        dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }


    public PaperTop inserirPaperTop(PaperTop paperTop){

        database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("_projeto",paperTop.getProjeto());
        values.put("_grupo",paperTop.getGrupo());
        values.put("_propriedade", paperTop.getIdPropriedade());
        values.put("categoria", paperTop.getCategoria());
        values.put("idAnimais", paperTop.getIdAnimais());
        values.put("area_atual", paperTop.getArea());
        values.put("brinco", paperTop.getBrinco());
        values.put("nome_usual", paperTop.getNomeVaca());
        values.put("status", paperTop.getStatus());
        values.put("data_secagem", paperTop.getPrevisaoSecagem());
        values.put("obs", paperTop.getObs());
        values.put("ultimo_parto", paperTop.getUltimoparto());
        values.put("ocorrencia", paperTop.getOcorrencia());
        values.put("referencia", paperTop.getReferencia());
        values.put("previsaoSecagem", paperTop.getPrevisaoSecagem());
        values.put("dataCobertura", paperTop.getDataCobertura());
        values.put("dataDiagnostico", paperTop.getDataDiagnostico());
        values.put("dataParto", paperTop.getDataParto());
        values.put("diasPrenhez", paperTop.getDiasPrenhez());
        values.put("idCobertura", paperTop.getIdCobertura());
        values.put("idReprodutivo", paperTop.getIdReprodutivo());
        values.put("numeroFetos", paperTop.getNumeroFetos());
        values.put("previsaoParto", paperTop.getPrevisaoParto());
        values.put("statusProdutivo", paperTop.getStatusProdutivo());
        values.put("data_secagem",paperTop.getDataPesagem());
        values.put("categoria",paperTop.getCategoria());
        values.put("manejo",paperTop.getManejo());

        long insertId = database.insert(DbHelper.TABLE_PAPERTOP, null,values);
        Cursor cursor = database.query(DbHelper.TABLE_PAPERTOP,allColumns,DbHelper.COLUMN_ID + " = " + insertId, null,null,null,null);

        cursor.moveToFirst();
        PaperTop newPaperTop = cursorToPaperTop(cursor);
        //cursor.close();

        return newPaperTop;
    }

    public List<PaperTop> getAllPaperTop(){
        List<PaperTop> paperTopList = new ArrayList<PaperTop>();

        Cursor cursor = database.query(DbHelper.TABLE_PAPERTOP,allColumns, null,null,null,null,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            PaperTop paperTop = cursorToPaperTop(cursor);
            paperTopList.add(paperTop);

            cursor.moveToNext();
        }

        cursor.close();
        return  paperTopList;
    }

    public Boolean deleteDuplicados(){

    StringBuilder sql = new StringBuilder();

        sql.append("DELETE FROM PAPERPORT WHERE IDANIMAIS IN ( ");
        sql.append("SELECT IDANIMAIS FROM PAPERPORT ");
        sql.append("GROUP BY IDANIMAIS ");
        sql.append("HAVING COUNT(IDANIMAIS) > 1 AND IDANIMAIS <> 0) ");
        sql.append("AND NOT _ID IN ");
        sql.append("(SELECT MAX(_ID) FROM PAPERPORT ");
        sql.append("GROUP BY IDANIMAIS HAVING COUNT(IDANIMAIS) > 1); ");

        try
        {
            database.execSQL(sql.toString());
        }
        catch (SQLException ex){

            ex.getMessage();
        }

        return  true;
    }


    private PaperTop cursorToPaperTop(Cursor cursor){

        PaperTop paperTop = new PaperTop();

        paperTop.setCategoria(cursor.getString(0));
        paperTop.setIdAnimais(cursor.getString(1));
        paperTop.setIdPropriedade(cursor.getString(2));
        paperTop.setArea(cursor.getString(3));
        paperTop.setBrinco(cursor.getString(4));
        paperTop.setNomeVaca(cursor.getString(5));
        paperTop.setStatus(cursor.getString(6));
        paperTop.setPrevisaoSecagem(cursor.getString(7));
        paperTop.setObs(cursor.getString(8));
        paperTop.setUltimoparto(cursor.getString(9));
        paperTop.setOcorrencia(cursor.getString(10));
        paperTop.setReferencia(cursor.getString(11));
        paperTop.setPrevisaoSecagem(cursor.getString(12));
        paperTop.setDataCobertura(cursor.getString(13));
        paperTop.setDataDiagnostico(cursor.getString(14));
        paperTop.setDataParto(cursor.getString(15));
        paperTop.setDiasPrenhez(cursor.getString(16));
        paperTop.setIdCobertura(cursor.getString(17));
        paperTop.setIdReprodutivo(cursor.getString(18));
        paperTop.setNumeroFetos(cursor.getString(19));
        paperTop.setPrevisaoParto(cursor.getString(20));
        paperTop.setStatusProdutivo(cursor.getString(21));
        paperTop.setManejo(cursor.getString(22));

        return  paperTop;
    }
}
