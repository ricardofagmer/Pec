package com.pec.biosistemico.pec.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.pec.biosistemico.pec.helpers.DbHelper;
import com.pec.biosistemico.pec.model.Criatf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricardo on 16/11/2016.
 */

public class CriatfDAO {

    private SQLiteDatabase database;
    private DbHelper dbHelper;
    private String[] allColumns = {DbHelper.COLUMN_ID,"_projeto","_grupo","_propriedade","id_criatf","criatf_pai","data_protocolo","data_inseminacao","data_d0","vacina",
            "_raca","touro","consultor","enviado","fez_d0","nome_vaca","_id_animais","id_reprodutivo","id_cobertura","processo_d8","processo_iatf","fez_ia","dg",
            "diagnostico","numeroDias","previsao_parto","tipo_atendimento","nova_ia"};

    public CriatfDAO(Context context){

        dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }


    public Criatf inserirCriatf(Criatf criatf){

        database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("_projeto",criatf.get_projeto());
        values.put("_grupo",criatf.getGrupo());
        values.put("_propriedade",criatf.getPropriedade());
        values.put("id_criatf",criatf.getId_criatf());
        values.put("criatf_pai",criatf.getCriatf_pai());
        values.put("data_protocolo",criatf.getData_protocolo());
        values.put("data_inseminacao",criatf.getData_inseminacao());
        values.put("data_d0",criatf.getData_d0());
        values.put("vacina",criatf.getVacina());
        values.put("_raca",criatf.getRaca());
        values.put("touro",criatf.getTouro());
        values.put("consultor",criatf.getConsultor());
        values.put("enviado",criatf.getEnviado());
        values.put("fez_d0",criatf.getFez_d0());
        values.put("nome_vaca",criatf.getNome_vaca());
        values.put("_id_animais",criatf.getId_animais());
        values.put("id_reprodutivo",criatf.getId_reprodutivo());
        values.put("id_cobertura",criatf.getCobertura());
        values.put("processo_d8",criatf.getD8());
        values.put("processo_iatf",criatf.getIatf());
        values.put("fez_ia",criatf.getFez_ia());
        values.put("dg",criatf.getDg());
        values.put("diagnostico",criatf.getDiagnostico());
        values.put("numeroDias",criatf.getNumeroDias());
        values.put("previsao_parto",criatf.getPrevisao_parto());
        values.put("tipo_atendimento",criatf.getTipo_atendimento());
        values.put("nova_ia",criatf.getNova_ia());

        long insertId = database.insert(DbHelper.TABLE_CRIATF, null,values);
        Cursor cursor = database.query(DbHelper.TABLE_CRIATF,allColumns,DbHelper.COLUMN_ID + " = " + insertId, null,null,null,null);

        cursor.moveToFirst();
        Criatf newCriatf = cursorToCriatf(cursor);
        //cursor.close();

        return newCriatf;
    }

    public List<Criatf> getAllCriatf(){
        List<Criatf> criatfList = new ArrayList<Criatf>();

        Cursor cursor = database.query(DbHelper.TABLE_CRIATF,allColumns, null,null,null,null,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Criatf criatf = cursorToCriatf(cursor);
            criatfList.add(criatf);

            cursor.moveToNext();
        }

        cursor.close();
        return  criatfList;
    }


    private Criatf cursorToCriatf(Cursor cursor){

        Criatf criatf = new Criatf();

        criatf.set_projeto(cursor.getInt(0));
        criatf.setGrupo(cursor.getString(1));
        criatf.setPropriedade(cursor.getInt(2));
        criatf.setId_criatf(cursor.getInt(3));
        criatf.setCriatf_pai(cursor.getString(4));
        criatf.setData_protocolo(cursor.getString(5));
        criatf.setData_inseminacao(cursor.getString(6));
        criatf.setData_d0(cursor.getString(7));
        criatf.setVacina(cursor.getString(8));
        criatf.setRaca(cursor.getString(9));
        criatf.setTouro(cursor.getString(10));
        criatf.setConsultor(cursor.getString(11));
        criatf.setEnviado(cursor.getString(12));
        criatf.setFez_d0(cursor.getString(13));
        criatf.setNome_vaca(cursor.getString(14));
        criatf.setId_animais(cursor.getString(15));
        criatf.setId_reprodutivo(cursor.getInt(16));
        criatf.setCobertura(cursor.getInt(17));
        criatf.setD8(cursor.getString(18));
        criatf.setIatf(cursor.getString(19));
        criatf.setFez_ia(cursor.getString(20));
        criatf.setDg(cursor.getString(21));
        criatf.setDiagnostico(cursor.getString(22));
        criatf.setNumeroDias(cursor.getString(23));
        criatf.setPrevisao_parto(cursor.getString(24));
        criatf.setTipo_atendimento(cursor.getString(25));
        criatf.setNova_ia(cursor.getString(26));

        return  criatf;
    }
}
