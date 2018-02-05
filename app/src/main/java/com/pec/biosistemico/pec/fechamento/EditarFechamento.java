package com.pec.biosistemico.pec.fechamento;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.popUp.Recomendacao;
import com.pec.biosistemico.pec.popUp.SituacaoEncontrada;
import com.pec.biosistemico.pec.util.Global;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.sql.Date;

public class EditarFechamento extends DialogFragment implements DialogInterface.OnClickListener {

    String localDaFoto;
    Uri localFotoUri;
    ImageView img;
    Bitmap selected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_DeviceDefault_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.edite_relatorio, null);
        final Intent i;
        final int cons = 0;

        final EditText txtConsultor = (EditText) view.findViewById(R.id.txtConsultor);
        final Button btnSituacao = (Button) view.findViewById(R.id.btnSituacao);
        final Button btnRecomendacao = (Button) view.findViewById(R.id.btnRecomendacao);

        txtConsultor.setEnabled(false);

        try {
            SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("SELECT NOME FROM LOGIN", null);
            cursor.moveToFirst();
            txtConsultor.setText(cursor.getString(cursor.getColumnIndex("nome")).toUpperCase());
        } catch (Exception e) {
            MessageBox(e.getMessage());
        }


        Global mDados = Global.getInstance();
        if (mDados.getStart() == 0) {
            MessageBox("Antes de preencher o fechamento responda o CHECKLIST");
        }

        Button btnSalvar = (Button) view.findViewById(R.id.btnSalvarFechamento);

        btnRecomendacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    popUpRecomendacao(view);
                } catch (Exception e) {
                    MessageBox(e.getMessage());
                }
            }
        });

        btnSituacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    popUpSituacao(view);
                } catch (Exception e) {
                    MessageBox(e.getMessage());
                }

            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        return view;
    }

    public void popUpRecomendacao(View view) {
        try {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Recomendacao cdf = new Recomendacao(1, 1);
            cdf.show(ft, "DG");
        } catch (Exception e) {
            MessageBox(e.getMessage());
        }
    }

    public void popUpSituacao(View view) {

        try {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            SituacaoEncontrada cdf = new SituacaoEncontrada(1, 1);
            cdf.show(ft, "DG");
        } catch (Exception e) {
            MessageBox(e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }



    public String RetornaCliente(int _id) {
        String result = "";
        try {


            SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",
                    Context.MODE_PRIVATE, null);

            Cursor cursor = db.rawQuery("SELECT NOME FROM PRODUTOR WHERE ID = "
                    + _id + "", null);
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("nome"));
        } catch (Exception e) {
            MessageBox(e.getMessage());
        }

        return result;
    }

    public String RetornaProjeto(int _id) {

        String result = "";

        try {
            SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",
                    Context.MODE_PRIVATE, null);

            Cursor cursor = db.rawQuery("SELECT NOME FROM PROJETO WHERE ID = "
                    + _id + "", null);
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("nome"));
        } catch (Exception e) {
            MessageBox(e.getMessage());
        }

        return result;
    }

    public String RetornaGrupo(int _id) {

        String result = "";
        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",
                Context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT NOME FROM GRUPO WHERE ID = " + _id
                + "", null);
        cursor.moveToFirst();
        result = cursor.getString(cursor.getColumnIndex("nome"));

        return result;
    }

    public String RetornaProdutor(int _id) {

        String result = "";

        try {
            SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",
                    Context.MODE_PRIVATE, null);

            Cursor cursor = db.rawQuery("SELECT NOME FROM PRODUTOR WHERE ID = " + _id + "", null);
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("nome"));
        } catch (Exception e) {
            MessageBox(e.getMessage());
        }

        return result;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

    }


    public void copyFile() {
        Log.i("info", "in copy file at finally");
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "/data/com.pec.biosistemico/files/pec.txt";
                String backupDBPath = "pec.txt";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            Log.i("info", "in file ");
        }
    }

    public void MessageBox(String msg) {
        AlertDialog.Builder informa = new AlertDialog.Builder(getActivity());
        informa.setTitle("Alerta!").setMessage(msg);
        informa.setNeutralButton("Fechar", null).show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
