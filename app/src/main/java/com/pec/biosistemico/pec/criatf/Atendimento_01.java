package com.pec.biosistemico.pec.criatf;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.util.Global;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Atendimento_01 extends Fragment implements OnClickListener {

    int projeto = 0;
    String grupo = "";
    int produtor = 0;
    Spinner ddlRaca;
    Spinner ddlTouro;
    Spinner ddlVaca;
    int idVaca;
    String id_pai;
    String idCobertura;
    String idReprodutivo;
    String nomeVaca;
    String idAnimais;
    String idRaca;
    String cod_mobile;
    private Calendar cal;
    private int minute;
    private int hour;
    private int day;
    private int month;
    private int year;
    private String raca;
    private TextView txtRef;
    private EditText txtDtPeriodo;;
    private EditText txtDtProtocolo;
    private EditText txtDtInseminacao;
    private Button btnDataD0;
    private Global mDados = Global.getInstance();

    @SuppressLint("SimpleDateFormat")
    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        Global global = Global.getInstance();
        projeto = global.getProjeto();
        grupo = global.getUsuario();
        produtor = global.getProdutor();

        final View view = inflater.inflate(R.layout.atendimento_01, null);

        final TextView txtProjeto = (TextView) view.findViewById(R.id.txtProjeto);
        final TextView txtGrupo = (TextView) view.findViewById(R.id.txtGrupo);
        txtRef = (TextView) view.findViewById(R.id.txtReferencia);
        final TextView txtProdutor = (TextView) view.findViewById(R.id.txtProdutor);

        txtProjeto.setText("Projeto: " + RetornaNomeProjeto(projeto));
        txtGrupo.setText("Grupo: " + RetornaNomeGrupo(Integer.parseInt(grupo)));
        txtProdutor.setText("Produtor: " + RetornaProdutor(produtor));

         txtDtPeriodo = (EditText) view.findViewById(R.id.txtDtAtendimento);
         txtDtInseminacao = (EditText) view.findViewById(R.id.txtDtInseminacao);
         txtDtProtocolo = (EditText) view.findViewById(R.id.txtDtProtocolo);

        //final Spinner ddlProdutor = (Spinner)view.findViewById(R.id.ddl1);
        ddlTouro = (Spinner) view.findViewById(R.id.ddlTouro);
        ddlRaca = (Spinner) view.findViewById(R.id.ddlRacas);
        ddlVaca = (Spinner) view.findViewById(R.id.ddlVaca);

        final Spinner ddlVacina = (Spinner) view.findViewById(R.id.ddlVacina);

       // final ImageButton btnData = (ImageButton) view.findViewById(R.id.btnShare);
        final Button btnSalvar = (Button) view.findViewById(R.id.btnSalvarPaper);
        final Button btnLoad = (Button) view.findViewById(R.id.btnLoadLast);

        btnDataD0 = (Button) view.findViewById(R.id.btnData);

        Date data = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String formatted = format.format(data);

        txtRef.setText("Referência: " + formatted.toString());
        txtDtPeriodo.setText(formatted.toString());

        data.setDate(data.getDate() + 8);
        formatted = format.format(data);
        txtDtProtocolo.setText(formatted.toString());


        data.setDate(data.getDate() + 2);
        formatted = format.format(data);
        txtDtInseminacao.setText(formatted.toString());

        //#POPULANDO OS SPINNERS
        populaRaca();
        populaVacas();

		/*Cursor cursor = db.rawQuery("SELECT rowid _id, id, nome FROM produtor WHERE id = "+ produtor + "", null);
        String[] from3 = { "nome", "id", "_id" };
		int[] to3 = { R.id.lblNome, R.id.lblId, R.id.lbl_id_FK };
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter ad = new SimpleCursorAdapter(getActivity(),	R.layout.spinner, cursor, from3, to3);
		ddlProdutor.setAdapter(ad);*/

        txtDtPeriodo.setEnabled(false);
        txtDtInseminacao.setEnabled(false);
        txtDtProtocolo.setEnabled(false);

        List<String> OP1 = new ArrayList<String>();
        OP1.add("SIM");
        OP1.add("NAO");

        // PASSANDO O ARRAYLIST LITROS
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, OP1);
        ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        ddlVacina.setAdapter(spinnerArrayAdapter);


        ddlVaca.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView lblcod = (TextView) view.findViewById(R.id.lblId);
                idVaca = Integer.parseInt(lblcod.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSalvar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (ddlVaca.getSelectedItem() == null) {

                    MessageBox("Favor selecionar um animal");
                } else {

                try {

                        if (btnSalvar.getText().toString().equals("SALVAR")) {

                            Global x = Global.getInstance();

                            String codeVaca = RetornaIDAnimais(idVaca);
                            String nomeVaca = RetornaNomeVaca(ddlVaca.getSelectedItemId());
                            String idTouro = RetornaID_Touro(ddlTouro.getSelectedItemId());
                            int id_animais = 0;

                            if (cod_mobile.isEmpty() || cod_mobile.equals("null") || cod_mobile.equals("")) {

                                id_animais = 0;
                            } else {
                                id_animais = Integer.parseInt(codeVaca);
                            }

                            if (ajustaEstoque(Integer.parseInt(idTouro)) > 0) {

                                StringBuilder sql = new StringBuilder();
                                sql.append("INSERT INTO criatf (_projeto,_grupo,_propriedade, nome_vaca, _id_animais, ");
                                sql.append("data_protocolo,data_inseminacao,vacina,_raca,touro,consultor,id_reprodutivo, id_cobertura, enviado, fez_d0, criatf_pai, data_d0,tipo_atendimento,cod_mobile) VALUES (");
                                sql.append("" + projeto + ",");
                                sql.append("" + grupo + ",");
                                sql.append("" + produtor + ",");
                                sql.append("'" + nomeVaca + "',");
                                sql.append("" + codeVaca + ",");
                                sql.append("'" + txtDtProtocolo.getText() + "',");
                                sql.append("'" + txtDtInseminacao.getText() + "',");
                                sql.append("'" + ddlVacina.getSelectedItem() + "',");
                                sql.append("'" + idRaca + "',");
                                sql.append("'" + idTouro + "',");
                                sql.append("'" + x.getLogin() + "',");
                                sql.append("'" + idReprodutivo + "',");
                                sql.append("'" + idCobertura + "',");
                                sql.append("'NAO',");
                                sql.append("'SIM',");
                                sql.append("" + id_pai + ",");
                                sql.append("'" + txtDtPeriodo.getText() + "',");
                                sql.append("'AT01',");
                                sql.append("'" + cod_mobile + "'");
                                sql.append(");");

                                db.execSQL(sql.toString());

                                if (codeVaca.isEmpty() || codeVaca == null || codeVaca.equals("0")) {

                                    db.execSQL("UPDATE PAPERPORT SET fez_d0 = 'SIM' WHERE cod_mobile = '" + cod_mobile + "'");
                                } else {
                                    db.execSQL("UPDATE PAPERPORT SET fez_d0 = 'SIM' WHERE IDANIMAIS = " + codeVaca + "");
                                }

                                populaVacas();

                                MessageBox("Atendimento inserido com sucesso!");

                            }

                            else
                            {

                                MessageBox("SALDO INSUFICIENTE PARA ESTE ANIMAL.");
                            }

                        }


                        //if (btnSalvar.getText().toString().equals("ATUALIZAR")) {
						
						/*Global x = Global.getInstance();
						
						int id = x.getLastID();
						
						StringBuilder sql = new StringBuilder();
						sql.append("UPDATE atendimento_01 SET data_atendimento ='"+txtDtPeriodo.getText()+"', nome_vaca = '"+ddlVaca.getSelectedItemId()+"', ");
						sql.append("data_protocolo = '"+txtDtProtocolo.getText()+"',data_inseminacao = '"+txtDtInseminacao.getText()+"', ");
						sql.append("vacina = '"+ddlVacina.getSelectedItem()+"',raca = '"+ddlRaca.getSelectedItemId()+"',nome_touro = '"+ddlTouro.getSelectedItemId()+"' WHERE _ID = "+id+"");		
						
						db.execSQL(sql.toString());							
						
						MessageBox("Atendimento Atualizado com Sucesso!");
										
						btnSalvar.setText("SALVAR");*/

                       // }
                    } catch(Exception ex){

                        MessageBox(ex.getMessage());
                    }
                }

            }
        });


        ddlRaca.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                SQLiteCursor dados = (SQLiteCursor) ddlRaca.getAdapter().getItem(ddlRaca.getSelectedItemPosition());
                raca = RetornaNomeRaca(ddlRaca.getSelectedItemId());

                if(mDados.getProject().equals("SEBRAERN")
                        || mDados.getProject().equals("SEBRAEPB")
                        || mDados.getProject().equals("DEMO")){

                    populaTouro("RN");
                }
                else
                if(mDados.getProject().equals("SEBRAEMS") || mDados.getProject().equals("SEBRAEGO") ||
                   mDados.getProject().equals("FIBRIA") || mDados.getProject().equals("COPLAF")){
                    populaTouro("MS");
                }
                else {
                    populaTouro("PR");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnLoad.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SQLiteDatabase db1 = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

                Cursor cursor = db1.rawQuery("SELECT * FROM atendimento_01 ORDER BY _ID DESC LIMIT 1", null);

                try {
                    if (cursor != null && cursor.getCount() > 0) {
                        if (cursor.moveToFirst())
                            do {

                                txtDtPeriodo.setText(cursor.getString(cursor.getColumnIndex("data_atendimento")));
                                txtDtInseminacao.setText(cursor.getString(cursor.getColumnIndex("data_inseminacao")));
                                txtRef.setText(cursor.getString(cursor.getColumnIndex("data_atendimento")));
                                txtDtProtocolo.setText(cursor.getString(cursor.getColumnIndex("data_protocolo")));

                                String o = "";
                                o = cursor.getString(cursor.getColumnIndex("vacina"));

                                if (o.toString().equals("SIM")) {
                                    ddlVacina.setSelection(0);
                                }
                                if (o.toString().equals("NAO")) {
                                    ddlVacina.setSelection(1);
                                }

                                long x = cursor.getLong(cursor.getColumnIndex("raca"));
                                setSpinTextByID(ddlRaca, x);

                                x = cursor.getLong(cursor.getColumnIndex("nome_vaca"));
                                setSpinTextByID(ddlVaca, x);

                                String y = cursor.getString(cursor.getColumnIndex("nome_touro"));
                                setSpinTextByText(ddlTouro, y);

                                btnSalvar.setText("ATUALIZAR");

                            }

                            while (cursor.moveToNext());

                    }
                } catch (Exception ex) {

                    MessageBox(ex.getMessage());

                }
            }
        });

        btnDataD0.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                txtDtPeriodo.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year + "/" + hour + ":" + minute);
                                try {
                                    AtualizaData();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, year, month, day);
                dpd.show();


            }
        });

        return view;
    }

    public void AtualizaData() throws ParseException {

        String data = txtDtPeriodo.getText().toString();

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        java.util.Date data_modificada = df.parse(data);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String formatted = format.format(data_modificada);


       // txtRef.setText("Referência: " + data_modificada.toString());
        data_modificada.setDate(data_modificada.getDate());
        formatted = format.format(data_modificada);
        txtDtPeriodo.setText(formatted.toString());


        data_modificada.setDate(data_modificada.getDate() + 8);
        formatted = format.format(data_modificada);
        txtDtProtocolo.setText(formatted.toString());


        data_modificada.setDate(data_modificada.getDate() + 2);
        formatted = format.format(data_modificada);
        txtDtInseminacao.setText(formatted.toString());

    }

    public void setSpinTextByID(Spinner spin, long text) {
        for (int i = 0; i < spin.getAdapter().getCount(); i++) {
            long x = spin.getAdapter().getItemId(i);

            if (x == text) {
                spin.setSelection(i);
            }
        }

    }

    public void setSpinTextByText(Spinner spin, String text) {
        for (int i = 0; i < spin.getAdapter().getCount(); i++) {
            long x = spin.getAdapter().getItemId(i);

            if (spin.getAdapter().getItem(i).toString().contains(text)) {
                spin.setSelection(i);
            }
        }
    }

    public String RetornaNomeVaca(long id) {

        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT NOME_USUAL FROM PAPERPORT WHERE _ID = " + id + "", null);
        cursor.moveToFirst();

        String x = cursor.getString(cursor.getColumnIndex("nome_usual"));

        return x;
    }

    public String RetornaIDAnimais(long id) {

        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT ID_CRIATF, IDANIMAIS,COD_MOBILE,IDCOBERTURA,IDREPRODUTIVO FROM PAPERPORT WHERE _ID = " + id + "", null);
        cursor.moveToFirst();

	/*	String valor = "";
		valor = cursor.getString(cursor.getColumnIndex("idAnimais"));

		if(valor.equals("null") || valor.equals(""))
		{
			valor = "0";
		}*/

        id_pai = cursor.getString(cursor.getColumnIndex("id_criatf"));
        cod_mobile = cursor.getString(cursor.getColumnIndex("cod_mobile"));
        if (cod_mobile == null) {
            cod_mobile = "null";
        }
        idCobertura = cursor.getString(cursor.getColumnIndex("idCobertura"));
        idReprodutivo = cursor.getString(cursor.getColumnIndex("idReprodutivo"));
        idAnimais = cursor.getString(cursor.getColumnIndex("idAnimais"));

        if (idAnimais == null) {

            idAnimais = "0";
        }

        return idAnimais;

    }

    public String RetornaNomeProjeto(int codigo) {

        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT NOME FROM PROJETO WHERE ID = " + codigo + "", null);
        cursor.moveToFirst();

        String x = cursor.getString(cursor.getColumnIndex("nome"));

        return x;
    }

    public String RetornaNomeGrupo(int codigo) {

        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT NOME FROM GRUPO WHERE ID = " + codigo + "", null);
        cursor.moveToFirst();

        String x = cursor.getString(cursor.getColumnIndex("nome"));

        return x;
    }

    public void MessageBox(String msg) {
        AlertDialog.Builder informa = new AlertDialog.Builder(getActivity());
        informa.setTitle("Alerta!").setMessage(msg);
        informa.setNeutralButton("Fechar", null).show();
    }

    public void populaVacas() {

        Global mDados = Global.getInstance();
        int id = mDados.getProdutor();

        String sql = "SELECT DISTINCT p.IDANIMAIS, (p.brinco || ' - ' || p.nome_usual)as vaca, p._id , p.id  FROM PaperPort p LEFT JOIN  CRIATF " +
                "c ON p.IDANIMAIS = c._id_animais  WHERE   p._propriedade = " + id + " AND p.categoria  = 'VAZIA_IATF' " +
                "AND (p.fez_d0 is null)  " +
                "ORDER BY nome_usual ";

				/*"SELECT DISTINCT p.IDANIMAIS, p.nome_usual, p._id , p.id  FROM PaperPort p "+
					 "LEFT JOIN  CRIATF c ON p.IDANIMAIS = c._id_animais "+
					 "AND ( fez_ia is null OR  fez_ia = '')	AND (dg is null OR dg = '') "+
					 "WHERE p._propriedade = "+ id +" AND (p.fez_d0 is null  OR p.fez_d0 = '') "+
					 "AND c._id_animais is null ORDER BY nome_usual ";*/

        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery(sql, null);

        String[] from2 = {"vaca", "_id", "id"};
        int[] to2 = {R.id.lblNome, R.id.lblId, R.id.lbl_id_FK};
        @SuppressWarnings("deprecation")
        SimpleCursorAdapter ad = new SimpleCursorAdapter(getActivity().getBaseContext(), R.layout.spinner, cursor, from2, to2);
        ddlVaca.setAdapter(ad);
    }

    public String RetornaNomeRaca(long id) {

        String x = "";

        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT _id, RACA FROM TOURO WHERE _ID = " + id + "", null);

        if (cursor.moveToFirst()) {

            x = cursor.getString(cursor.getColumnIndex("raca"));
        }

        return x;

    }

    public String RetornaID_Touro(long id) {

        String x = "";

        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT _id,id, id_raca FROM TOURO WHERE _ID = " + id + "", null);

        if (cursor.moveToFirst()) {

            x = cursor.getString(cursor.getColumnIndex("id"));
            idRaca = cursor.getString(cursor.getColumnIndex("id_raca"));
        }

        return x;

    }

    public String Retorna_id_pai(long id) {

        String x = "";

        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT _id, id_criatf FROM paperport WHERE _ID = " + id + "", null);

        if (cursor.moveToFirst()) {

            x = cursor.getString(cursor.getColumnIndex("id_criatf"));
        }

        return x;

    }

    public String RetornaProdutor(int _id) {

        String result = "";
        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT NOME FROM PRODUTOR WHERE ID = " + _id + "", null);
        cursor.moveToFirst();
        result = cursor.getString(cursor.getColumnIndex("nome"));

        return result;
    }

    public void populaRaca() {

        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select distinct  _id, raca  from touro group by raca order by raca", null);

        String[] from2 = {"raca", "_id"};
        int[] to2 = {R.id.lblNome, R.id.lbl_id_FK};
        @SuppressWarnings("deprecation")
        SimpleCursorAdapter ad = new SimpleCursorAdapter(getActivity().getBaseContext(), R.layout.spinner, cursor, from2, to2);
        ddlRaca.setAdapter(ad);
    }

    public void populaTouro(String regiao) {

        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT DISTINCT id,_id, (nome || ' - SÊMEM DISPONIVEL: ' || sememDisponivel)as nome FROM TOURO WHERE raca = '" + raca + "' AND nome like '%"+regiao+"' ORDER BY NOME", null);

        String[] from2 = {"nome", "id", "_id"};
        int[] to2 = {R.id.lblNome, R.id.lblId, R.id.lbl_id_FK};
        @SuppressWarnings("deprecation")
        SimpleCursorAdapter ad = new SimpleCursorAdapter(getActivity().getBaseContext(), R.layout.spinner, cursor, from2, to2);
        ddlTouro.setAdapter(ad);
    }

    public void RetornaLastID() {

        try {
            SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

            Cursor cursor = db.rawQuery("SELECT _id FROM criatf  order by _id desc limit 1", null);
            cursor.moveToFirst();

            int x = cursor.getInt(cursor.getColumnIndex("_id"));
            Global b = Global.getInstance();
            b.setLastID(x);

        } catch (Exception ex) {

            MessageBox(ex.getMessage());

        }

    }

    @Override
    public void onClick(View v) {

    }

    public int ajustaEstoque(long id){

        try {

            int qtd = 0;

            SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("SELECT sememDisponivel FROM TOURO WHERE ID = " + id + " ", null);

            if (cursor.moveToFirst()) {

                int total;
                qtd = cursor.getInt(cursor.getColumnIndex("sememDisponivel"));

                if(qtd > 0) {

                    total = qtd - 1;
                    db.execSQL("UPDATE TOURO SET sememDisponivel = " + total + " WHERE ID = " + id + "");
                }
            }

            if(mDados.getProject().equals("SEBRAERN")
                    || mDados.getProject().equals("SEBRAEPB")
                    || mDados.getProject().equals("DEMO")){

                populaTouro("RN");
            }
            else
            if(mDados.getProject().equals("SEBRAEMS") || mDados.getProject().equals("SEBRAEGO") ||
                    mDados.getProject().equals("FIBRIA") || mDados.getProject().equals("COPLAF")){
                populaTouro("MS");
            }
            else {
                populaTouro("PR");
            }
            return qtd;
        }
        catch (Exception ex){

            return  0;
        }
    }




}
