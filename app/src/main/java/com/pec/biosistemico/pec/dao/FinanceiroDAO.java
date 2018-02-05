package com.pec.biosistemico.pec.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.pec.biosistemico.pec.helpers.DbHelper;
import com.pec.biosistemico.pec.paperTop.financeiro.Produto;

import java.util.ArrayList;
import java.util.List;

public class FinanceiroDAO {

    private SQLiteDatabase database;
    private DbHelper dbHelper;
    private String[] allColumns = {DbHelper.COLUMN_ID, "_projeto", "_grupo", "_propriedade",
                                                        "descricao", "idProduto", "tipo", "unidadeMedida",
                                                        "valorMaximo", "valorMinimo","valorUnitario","quantidade","enviado"};

    public FinanceiroDAO(Context context) {

        dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Produto inserirProduto(Produto produto)
    {
        database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("_projeto",produto.get_projeto());
        values.put("_grupo",produto.get_grupo());
        values.put("_propriedade",produto.get_propriedade());
        values.put("descricao",produto.getDescricao());
        values.put("idProduto",produto.getIdProduto());
        values.put("tipo",produto.getTipo());
        values.put("unidadeMedida",produto.getUnidadeMedida());
        values.put("valorMaximo",produto.getValorMaximo());
        values.put("valorMinimo",produto.getValorMinimo());
        values.put("valorUnitario",produto.getValorUnitario());
        values.put("quantidade",produto.getQuantidade());
        values.put("enviado",produto.getEnviado());

        long insertId = database.insert(DbHelper.TABLE_FINANCEIRO, null,values);
        Cursor cursor = database.query(DbHelper.TABLE_FINANCEIRO,allColumns,DbHelper.COLUMN_ID + " = " + insertId, null,null,null,null);

        cursor.moveToFirst();
        Produto newProduto = cursorToProduto(cursor);

        return newProduto;

    }

    public void deleteProduto(int i) {

        database = dbHelper.getWritableDatabase();
        database.delete(DbHelper.TABLE_FINANCEIRO, "_id" + " = " + i, null);
    }

    public void deleteAllEnviado() {

        database = dbHelper.getWritableDatabase();
        database.delete(DbHelper.TABLE_FINANCEIRO,"enviado <> 'NAO' ", null);
    }


    public List<Produto> getAllProduto(){

        database = dbHelper.getWritableDatabase();

        List<Produto> produtoList = new ArrayList<Produto>();

        Cursor cursor = database.query(DbHelper.TABLE_FINANCEIRO,allColumns, null,null,null,null,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){

            Produto produto = cursorToProduto(cursor);
            produtoList.add(produto);

            cursor.moveToNext();
        }

        cursor.close();
        return  produtoList;
    }


    public List<Produto> getAllProdutoPorPropriedadeTipo(String tipo, int propriedade){

        database = dbHelper.getWritableDatabase();

        List<Produto> produtoList = new ArrayList<Produto>();

        Cursor cursor = database.query(DbHelper.TABLE_FINANCEIRO,allColumns, " _propriedade = "+propriedade+" AND tipo = '"+tipo+"' AND ENVIADO = 'NAO'",null,null,null,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){

            Produto produto = cursorToProduto(cursor);
            produtoList.add(produto);

            cursor.moveToNext();
        }

        cursor.close();
        return  produtoList;
    }

    public int getPosition(int idProduto, int propriedade){
        int  position = 0;

        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT _id FROM FINANCEIRO WHERE idProduto = "+idProduto+" AND _propriedade = "+propriedade+"",null);

        if (cursor.moveToFirst()){

            position = cursor.getInt(cursor.getColumnIndex("_id"));
        }

        cursor.close();
        return  position;
    }


    public List<Produto> getAllProdutoPorID(String id){
        List<Produto> produtoList = new ArrayList<Produto>();

        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.query(DbHelper.TABLE_FINANCEIRO,allColumns, DbHelper.COLUMN_ID + " = " + id,null,null,null,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){

            Produto produto = cursorToProduto(cursor);
            produtoList.add(produto);

            cursor.moveToNext();
        }

        cursor.close();
        return  produtoList;
    }


    public String getDescricaoPorID(long id){
        String  produto = "";

        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT DESCRICAO FROM FINANCEIRO WHERE _ID = "+id+"",null);

        if (cursor.moveToFirst()){

            produto = cursor.getString(cursor.getColumnIndex("descricao"));
        }

        cursor.close();
        return  produto;
    }

    public int getIdProdutoPorID(long id){
        int  produto = 0;

        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT idProduto FROM FINANCEIRO WHERE _ID = "+id+"",null);

        if (cursor.moveToFirst()){

            produto = cursor.getInt(cursor.getColumnIndex("idProduto"));
        }

        cursor.close();
        return  produto;
    }

    public int getIdPorPosicao(int posicao){
        int  produto = 0;

        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT idProduto FROM FINANCEIRO WHERE _ID = "+posicao+"",null);

        if (cursor.moveToFirst()){

            produto = cursor.getInt(cursor.getColumnIndex("idProduto"));
        }

        cursor.close();
        return  produto;
    }

    private Produto cursorToProduto(Cursor cursor)
    {
        Produto produto = new Produto();

        produto.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
        produto.set_projeto(cursor.getInt(cursor.getColumnIndex("_projeto")));
        produto.set_grupo(cursor.getInt(cursor.getColumnIndex("_grupo")));
        produto.set_propriedade(cursor.getInt(cursor.getColumnIndex("_propriedade")));
        produto.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
        produto.setIdProduto(cursor.getInt(cursor.getColumnIndex("idProduto")));
        produto.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
        produto.setUnidadeMedida(cursor.getString(cursor.getColumnIndex("unidadeMedida")));
        produto.setValorMaximo(cursor.getDouble(cursor.getColumnIndex("valorMaximo")));
        produto.setValorMinimo(cursor.getDouble(cursor.getColumnIndex("valorMinimo")));
        produto.setValorUnitario(cursor.getDouble(cursor.getColumnIndex("valorUnitario")));
        produto.setQuantidade(cursor.getInt(cursor.getColumnIndex("quantidade")));
        produto.setEnviado(cursor.getString(cursor.getColumnIndex("enviado")));

        return  produto;
    }

  }




