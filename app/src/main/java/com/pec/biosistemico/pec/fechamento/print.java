package com.pec.biosistemico.pec.fechamento;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.main;
import com.pec.biosistemico.pec.util.Global;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class print extends ActionBarActivity {

    private Toolbar mToolbar;
    private Global mDados = Global.getInstance();
    private TextView lblNumero;
    private TextView lblSistema;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(mDados.getProject().toString().equals("PR")){

            setContentView(R.layout.print);
        }
        else
        if(mDados.getProject().toString().equals("ZOETS")){

            setContentView(R.layout.print);
        }
        else
            if(mDados.getProject().toString().equals("MS")){

                setContentView(R.layout.print_sebrae_standard);
            }
            else
            if(mDados.getProject().toString().equals("PLC")){

                setContentView(R.layout.printplc);
            }
        else{
            setContentView(R.layout.print_standard);
        }


		/*android.app.ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ae4d")));
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);*/

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("PEC - Instituto Biosistêmico");
        mToolbar.setTitleTextColor(Color.WHITE);

        //setSupportActionBar(mToolbar);
        setSupportActionBar(mToolbar);

        //setSupportActionBar(mToolbar);

        ImageButton btnPrint = (ImageButton) findViewById(R.id.btnPrintPEC);

       // ImageView img1 = (ImageView)findViewById(R.id.imageView2);
        ImageView img2 = (ImageView)findViewById(R.id.imageView4);

        lblSistema = (TextView)findViewById(R.id.lblSituacaoSistema);
        TextView lblDtAtendimento = (TextView) findViewById(R.id.lblData);
        TextView lblProjeto = (TextView) findViewById(R.id.lblProjeto);
        TextView lblGrupo = (TextView) findViewById(R.id.lblGrupo);
        TextView lblProdutor = (TextView) findViewById(R.id.lblProdutorP);
        TextView lblTipoAtendimento = (TextView) findViewById(R.id.lblTipoAtendimento);
        lblNumero = (TextView)findViewById(R.id.lblNumero);

        TextView lblTitulo = (TextView) findViewById(R.id.lblTitulo);
        TextView lblSubTitulo = (TextView) findViewById(R.id.lblSubtitulo);

        TextView lblSitucao = (TextView) findViewById(R.id.lblSituacao);
        TextView lblRecomendacao = (TextView) findViewById(R.id.lblRecomendacao);


        lblNumero.setText(String.valueOf(mDados.getQtdadeAT()));


        SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        //Date data = new Date(System.currentTimeMillis());
        try {

           Cursor cursor = db.rawQuery("SELECT * FROM CHECKLIST WHERE _id = " + mDados.getLastID() + "", null);

            if (cursor.moveToFirst()) {

                String recomendacao = convertUTF8(cursor.getString(cursor.getColumnIndex("situacaoEncontrada")));

                Global mDados = null;
                mDados = Global.getInstance();

                String data = mDados.getDataRelatorio();

                if(data.equals("") || data.equals(null)) {
                  data = cursor.getString(cursor.getColumnIndex("data"));
                }

                lblDtAtendimento.setText(data);
                lblProjeto.setText(RetornaProjeto(cursor.getInt(cursor.getColumnIndex("_projeto"))));
                TextView lblConsultor = (TextView) findViewById(R.id.lblConsultor);


                int grupo = Integer.parseInt(mDados.getUsuario());
                lblGrupo.setText(RetornaGrupo(cursor.getInt(cursor.getColumnIndex("_grupo"))));

                lblConsultor.setText(mDados.getLogin());

                int produtor = cursor.getInt(cursor.getColumnIndex("_produtor"));

                lblProdutor.setText(RetornaProdutor(cursor.getInt(cursor.getColumnIndex("_produtor"))));
                lblTipoAtendimento.setText(cursor.getString(cursor.getColumnIndex("tipoReprodutivo")));
                lblSitucao.setText(recomendacao);
                lblRecomendacao.setText(cursor.getString(cursor.getColumnIndex("recomendacoes")));

                cursor = db.rawQuery("select count(*) as total from paperPort where modificado = 'SIM' and _propriedade = "+produtor+" and data_coleta = '"+data+"'",null);
                cursor.moveToFirst();
                int totalAT = cursor.getInt(cursor.getColumnIndex("total"));
                cursor = db.rawQuery("select  nome_usual from paperPort where modificado = 'SIM' and _propriedade = " + produtor +" and data_coleta = '"+data+"'limit 15",null);
                String animaisDG = "";

                cursor.moveToFirst();
                while (cursor.moveToNext()){
                    animaisDG += cursor.getString(cursor.getColumnIndex("nome_usual")) + ", ";
                }

                lblSistema.setText("A consultoria avaliou "+totalAT+" animais componentes do rebanho, atualizando o status produção (vaca seca ou lactante, o status reprodutivo (vaca gestante ou vazia), cronograma de secagem de leite, cronograma de nascimentos e ocorrências de rebanho (descarte, venda ou óbito).xml");
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Random gerador = new Random();
                int nome = gerador.nextInt();

                View v1 = getWindow().getDecorView().getRootView();
                v1.setDrawingCacheEnabled(true);
                Bitmap bmp = Bitmap.createBitmap(v1.getDrawingCache());
                v1.setDrawingCacheEnabled(false);
                try {
                    FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory().toString(),
                            nome + ".jpeg"));
                    bmp.compress(CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();

                    Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), nome + ".jpeg"));

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("image/jpeg");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, nome + ".jpg");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(sharingIntent, "Imprimir"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    @SuppressLint("SimpleDateFormat")
    private String getDateTime() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.menu_impressao, menu);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.mnuExit) {
            Intent obtnptodos = new Intent(this, main.class);
            startActivity(obtnptodos);
            finish();
        }


        Random gerador = new Random();
        int nome = gerador.nextInt();


        View v1 = getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);
        try {
            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory().toString(),
                    nome + ".jpeg"));
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), nome + ".jpeg"));

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("image/jpeg");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, nome + ".jpg");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(sharingIntent, "Imprimir"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String RetornaCliente(int _id) {

        String result = "";
        SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db",
                Context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT NOME FROM PRODUTOR WHERE ID = "
                + _id + "", null);
        cursor.moveToFirst();
        result = cursor.getString(cursor.getColumnIndex("nome"));

        return result;
    }

    public String RetornaProjeto(int _id) {

        String result = "";

        try {

            SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db",
                    Context.MODE_PRIVATE, null);

            Cursor cursor = db.rawQuery("SELECT NOME FROM PROJETO WHERE ID = "
                    + _id + "", null);
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("nome"));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


        return result;
    }

    public String RetornaGrupo(int _id) {

        String result = "";
        SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db",
                Context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT NOME FROM GRUPO WHERE ID = " + _id
                + "", null);
        cursor.moveToFirst();
        result = cursor.getString(cursor.getColumnIndex("nome"));

        return result;
    }

    public static String convertUTF8(String str) {
        String ret = null;
        try {
            ret = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return ret;
    }

    public String RetornaProdutor(int _id) {

        String result = "";

        try {
            SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db",
                    Context.MODE_PRIVATE, null);

            Cursor cursor = db.rawQuery("SELECT NOME FROM PRODUTOR WHERE ID = " + _id + "", null);
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("nome"));
        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }


        return result;
    }

}

