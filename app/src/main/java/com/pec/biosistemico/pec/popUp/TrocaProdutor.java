package com.pec.biosistemico.pec.popUp;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.pec.biosistemico.pec.paperTop.Vaca;
import com.pec.biosistemico.pec.paperTop.VacaAdapter;
import com.pec.biosistemico.pec.util.Global;
import com.pec.biosistemico.pec.R;

@SuppressLint("ValidFragment")
public class TrocaProdutor extends DialogFragment implements DialogInterface.OnClickListener {

    private int numStyle;
    private int numTheme;
    private int day;
    private int month;
    private int year;
    private Calendar cal;
    private ImageButton btnData;
    private int produtor;
    private Spinner ddlProdutor;
    private String grupo;

    @SuppressLint("ValidFragment")
    public TrocaProdutor(int numStyle, int numTheme) {
        this.numStyle = numStyle;
        this.numTheme = numTheme;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);


        int style;
        int theme;

        switch (numStyle) {
            case 1:
                style = DialogFragment.STYLE_NO_TITLE;
                break;
            case 2:
                style = DialogFragment.STYLE_NO_INPUT;
                break;
            case 3:
                style = DialogFragment.STYLE_NO_FRAME;
                break;
            default:
                style = DialogFragment.STYLE_NORMAL;
                break;

        }

        switch (numTheme) {
            case 1:
                theme = android.R.style.Theme_DeviceDefault_Light_Dialog;
                break;
            case 2:
                theme = android.R.style.Theme_Holo_Dialog;
                break;
            default:
                theme = android.R.style.Theme_Holo_Light_DarkActionBar;
                break;
        }

        setStyle(style, theme);
        setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.troca_produtor, container);

        Button btnSalvar = (Button) view.findViewById(R.id.btnMudar);
        Button btnCancelar = (Button) view.findViewById(R.id.btnCancelaN);
        ddlProdutor = (Spinner) view.findViewById(R.id.ddlProdutorChange);

        final Global mDados = Global.getInstance();
        produtor = mDados.getProdutor();
        grupo = mDados.getUsuario();

        populaProdutor();

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

                SQLiteCursor dados = (SQLiteCursor) ddlProdutor.getAdapter().getItem(ddlProdutor.getSelectedItemPosition());
                mDados.setProdutor(dados.getInt(1));

                produtor = dados.getInt(1);
                mDados.setProdutor(produtor);

                TextView lblProdutor = (TextView) getActivity().findViewById(R.id.txtProdutor);
                lblProdutor.setText("Produtor: " + RetornaProdutor(produtor));

                RefreshList();
                dismiss();

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ddlProdutor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        return (view);

    }

    public void RefreshList() {

        Global mDados = Global.getInstance();
        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
        ArrayList<Vaca> vacas = new ArrayList<Vaca>();
        ListView lv = (ListView) getActivity().findViewById(R.id.listView1);
        produtor = mDados.getProdutor();

        Cursor cursor1 = db
                .rawQuery(
                        "SELECT DISTINCT numeroDias, p._id as _id,p.previsaoParto, p.categoria, p.data_secagem,p.modificado,  "
                                + "p.manejo, p.peso_atual, p.id as id,  proj.nome as _projeto, grup.nome as _grupo, produ.nome as _propriedade, "
                                + "p.data_coleta,p.area_atual,p.brinco,p.nome_usual,p.status,p.data_secagem,p.obs,p.ocorrencia,p.referencia "
                                + "FROM paperPort p, projeto proj, grupo grup, produtor produ "
                                + "WHERE p._projeto = proj.id AND p._grupo = grup.id AND p._propriedade = produ.id AND p._propriedade = "
                                + produtor + "", null);

        while (cursor1.moveToNext()) {

            Vaca vaca = new Vaca();
            vaca.set_id(cursor1.getString(cursor1.getColumnIndex("_id")));
            vaca.setNome_usual(cursor1.getString(cursor1.getColumnIndex("nome_usual")));
            vaca.setBrinco(cursor1.getString(cursor1.getColumnIndex("brinco")));
            vaca.setCat_atual(cursor1.getString(cursor1.getColumnIndex("categoria")));
            vaca.setData_secagem(cursor1.getString(cursor1.getColumnIndex("data_secagem")));
            vaca.setOcorrencia(cursor1.getString(cursor1.getColumnIndex("ocorrencia")));
            vaca.setPrevisao_parto(cursor1.getString(cursor1.getColumnIndex("previsaoParto")));
            vaca.setStatus(cursor1.getString(cursor1.getColumnIndex("status")));
            vaca.setNumero_dias(cursor1.getString(cursor1.getColumnIndex("numeroDias")));
            vaca.setPeso_atual(cursor1.getString(cursor1.getColumnIndex("peso_atual")));
            vaca.setModificado(cursor1.getString(cursor1.getColumnIndex("modificado")));
            vacas.add(vaca);
        }

        final VacaAdapter v;
        v = new VacaAdapter(getActivity(), vacas);
        lv.setAdapter(v);

    }

    public void populaProdutor() {

        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT rowid _id, id, nome, _grupo FROM PRODUTOR WHERE _grupo = "
                + grupo + " OR NOME = '-SELECIONE O PRODUTOR-' ORDER BY NOME", null);

        String[] from = {"nome", "_id", "_grupo"};
        int[] to = {R.id.lblNome, R.id.lblId, R.id.lbl_id_FK};

        @SuppressWarnings("deprecation")
        SimpleCursorAdapter ad1 = new SimpleCursorAdapter(getActivity(),
                R.layout.spinner, cursor, from, to);
        ddlProdutor.setAdapter(ad1);
        db.close();
    }

    public String RetornaProdutor(int _id) {

        String result = "";
        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT NOME FROM PRODUTOR WHERE ID = " + _id + "", null);
        cursor.moveToFirst();
        result = cursor.getString(cursor.getColumnIndex("nome"));

        return result;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub

    }

}
