package com.pec.biosistemico.pec.checklist;

import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.util.Global;

public class MainCheckList extends Fragment {

    Button btnControles;
    Button btnManejo;
    Button btnSanidade;
    Button btnReprodutivo;
    Button btnQualidade;
    TextView lblLegenda;

    int lastID;
    int respondido;
    Global g = null;
    Global h = null;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.check_list_main, null);

        Global x = Global.getInstance();

        lblLegenda = (TextView) view.findViewById(R.id.lblLegenda);

        int produtor = x.getProdutor();

        lblLegenda.setText("CHECKLIST: " + RetornaProdutor(produtor));

        if (savedInstanceState != null) {

            btnControles.setText(savedInstanceState.getChar("btnControles"));
            btnManejo.setText(savedInstanceState.getChar("btnManejo"));
            btnSanidade.setText(savedInstanceState.getChar("btnSanidade"));
            btnReprodutivo.setText(savedInstanceState.getChar("btnReprodutivo"));
            btnQualidade.setText(savedInstanceState.getChar("btnQualidade"));

        }

        btnControles = (Button) view.findViewById(R.id.btnI);
        btnControles.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                h = Global.getInstance();
                respondido = h.getRespondido();

                if (respondido == 1) {

                    MessageBox("Grupo de perguntas já respondido!");
                } else {
                    openDialogControles(view);

                }
            }
        });

        btnManejo = (Button) view.findViewById(R.id.btnII);
        btnManejo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                g = g.getInstance();
                lastID = g.getLastID();

                if (lastID == 0) {

                    MessageBox("Para começarmos é necessário responder primeiro o Controles Zootécnicos");
                } else {
                    openDialogManejo(view);

                }
            }
        });

        btnSanidade = (Button) view.findViewById(R.id.btnIII);
        btnSanidade.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                g = g.getInstance();
                lastID = g.getLastID();
                if (lastID == 0) {

                    MessageBox("Para começarmos é necessário responder primeiro o Controles Zootécnicos");
                } else {
                    openDialogSanidade(view);

                }
            }
        });


        btnReprodutivo = (Button) view.findViewById(R.id.btnIV);
        btnReprodutivo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                g = g.getInstance();
                lastID = g.getLastID();
                if (lastID == 0) {

                    MessageBox("Para começarmos é necessário responder primeiro o Controles Zootécnicos");

                } else {
                    openDialogReprodutivo(view);
                }
            }
        });

        btnQualidade = (Button) view.findViewById(R.id.btnV);
        btnQualidade.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                g = g.getInstance();
                lastID = g.getLastID();
                if (lastID == 0) {

                    MessageBox("Para começarmos é necessário responder primeiro o Controles Zootécnicos");

                } else {
                    openDialogQualidade(view);
                }
            }
        });


        return view;

    }

    public void MessageBox(String msg) {
        Builder informa = new Builder(getActivity());
        informa.setTitle("Alerta!").setMessage(msg);
        informa.setNeutralButton("Fechar", null).show();
    }

    public void openDialogControles(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        CheckControles cdf = new CheckControles(1, 1);
        cdf.show(ft, "CH");
    }

    public void openDialogManejo(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        CheckManejo cdf = new CheckManejo(1, 1);
        cdf.show(ft, "CH");
    }

    public void openDialogSanidade(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        CheckSanidade cdf = new CheckSanidade(1, 1);
        cdf.show(ft, "CH");
    }

    public void openDialogReprodutivo(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        CheckReprodutivo cdf = new CheckReprodutivo(1, 1);
        cdf.show(ft, "CH");
    }

    public void openDialogQualidade(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        CheckQualidade cdf = new CheckQualidade(1, 1);
        cdf.show(ft, "CH");
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
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("btnControles", btnControles.getText().toString());
        outState.putString("btnManejo", btnManejo.getText().toString());
        outState.putString("btnSanidade", btnSanidade.getText().toString());
        outState.putString("btnReprodutivo", btnReprodutivo.getText().toString());
        outState.putString("btnQualidade", btnQualidade.getText().toString());
    }

}
