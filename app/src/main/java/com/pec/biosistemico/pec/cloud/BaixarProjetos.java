package com.pec.biosistemico.pec.cloud;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.main;
import com.pec.biosistemico.pec.util.Global;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class BaixarProjetos extends Activity implements OnClickListener {

    private ProgressDialog dialogo;
    private String[] carregarDados;
    private ProgressDialog barProgressDialog;
    private String login_project;
    private Global mDados = Global.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service);

        login_project = mDados.getProject();

        new asyntodos().execute();
    }

    public Boolean invocarCliente() {

        String NAMESPACE = "http://projecttrace.com.br/";
        String METHOD_NAME = "ListarCliente";
        String SOAP_ACTION = "http://projecttrace.com.br/ListarCliente";
        String URL = "http://www.projecttrace.com.br:8091/Service1.asmx?WSDL";

        try {

            SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
            db.delete("cliente", null, null);

            SoapObject requisicao = new SoapObject(NAMESPACE, METHOD_NAME);
            requisicao.addProperty("login", login_project);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(requisicao);

            HttpTransportSE http = new HttpTransportSE(URL);
            http.call(SOAP_ACTION, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();
            SoapObject diffgram = (SoapObject) response.getProperty("diffgram");
            SoapObject newdataset = (SoapObject) diffgram
                    .getProperty("NewDataSet");

            carregarDados = new String[newdataset.getPropertyCount() * 2];

            int fila = 0;
            for (int i = 0; i < newdataset.getPropertyCount(); i++) {
                SoapObject dadosXml = (SoapObject) newdataset.getProperty(i);

                carregarDados[fila] = dadosXml.getProperty(0).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(1).toString();

                db.execSQL("INSERT INTO cliente(id,nome) VALUES ('" + dadosXml.getProperty(0).toString() + "','" + dadosXml.getProperty(1).toString() + "')");
                fila += 2;
            }

           // invocarEscritorio();

            invocarProjeto();


            return true;


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }
        catch (ExceptionInInitializerError e){

            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;

        }



    }

    public Boolean invocarEscritorio() {

        String NAMESPACE = "http://projecttrace.com.br/";
        String METHOD_NAME = "ListarEscritorio";
        String SOAP_ACTION = "http://projecttrace.com.br/ListarEscritorio";
        String URL = "http://www.projecttrace.com.br:8091/Service1.asmx?WSDL";

        try {

            SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
            db.delete("escritorio", null, null);

            SoapObject requisicao = new SoapObject(NAMESPACE, METHOD_NAME);
            //requisicao.addProperty("login", login_project);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(requisicao);

            HttpTransportSE http = new HttpTransportSE(URL);
            http.call(SOAP_ACTION, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();
            SoapObject diffgram = (SoapObject) response.getProperty("diffgram");
            SoapObject newdataset = (SoapObject) diffgram
                    .getProperty("NewDataSet");

            carregarDados = new String[newdataset.getPropertyCount() * 2];

            int fila = 0;
            for (int i = 0; i < newdataset.getPropertyCount(); i++) {
                SoapObject dadosXml = (SoapObject) newdataset.getProperty(i);

                carregarDados[fila] = dadosXml.getProperty(0).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(1).toString();
                //carregarDados[fila + 1] = dadosXml.getProperty(2).toString();

                db.execSQL("INSERT INTO escritorio(id,nome) VALUES ('" + dadosXml.getProperty(0).toString() + "','" + dadosXml.getProperty(1).toString() + "')");

                fila += 2;
            }

            invocarProjeto();

            return true;


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }

    }

    public Boolean invocarProjeto() {

        String NAMESPACE = "http://projecttrace.com.br/";
        String METHOD_NAME = "ListarProjetos";
        String SOAP_ACTION = "http://projecttrace.com.br/ListarProjetos";
        String URL = "http://www.projecttrace.com.br:8091/Service1.asmx?WSDL";

        try {

            SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
            db.delete("projeto", null, null);

            SoapObject requisicao = new SoapObject(NAMESPACE, METHOD_NAME);
            requisicao.addProperty("login", login_project);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(requisicao);

            HttpTransportSE http = new HttpTransportSE(URL);
            http.call(SOAP_ACTION, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();
            SoapObject diffgram = (SoapObject) response.getProperty("diffgram");
            SoapObject newdataset = (SoapObject) diffgram
                    .getProperty("NewDataSet");

            carregarDados = new String[newdataset.getPropertyCount() * 4];

            int fila = 0;
            for (int i = 0; i < newdataset.getPropertyCount(); i++) {
                SoapObject dadosXml = (SoapObject) newdataset.getProperty(i);

                carregarDados[fila] = dadosXml.getProperty(0).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(1).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(2).toString();

                db.execSQL("INSERT INTO projeto(id,nome,_escritorio,_cliente) VALUES ('" + dadosXml.getProperty(0).toString() + "','" + dadosXml.getProperty(1).toString() + "','" + dadosXml.getProperty(2).toString() + "','" + dadosXml.getProperty(3).toString() + "')");

                fila += 2;
            }

            invocarGrupos();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }


    }

    public Boolean invocarTouros() {

        String NAMESPACE = "http://projecttrace.com.br/";
        String METHOD_NAME = "ListarTouros";
        String SOAP_ACTION = "http://projecttrace.com.br/ListarTouros";
        String URL = "http://www.projecttrace.com.br:8079/Service1.asmx?WSDL";

        try {

            SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
            db.delete("touro", null, null);

            SoapObject requisicao = new SoapObject(NAMESPACE, METHOD_NAME);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(requisicao);

            HttpTransportSE http = new HttpTransportSE(URL);
            http.call(SOAP_ACTION, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();
            SoapObject diffgram = (SoapObject) response.getProperty("diffgram");
            SoapObject newdataset = (SoapObject) diffgram.getProperty("NewDataSet");

            carregarDados = new String[newdataset.getPropertyCount() * 4];

            int fila = 0;
            for (int i = 0; i < newdataset.getPropertyCount(); i++) {
                SoapObject dadosXml = (SoapObject) newdataset.getProperty(i);

                carregarDados[fila] = dadosXml.getProperty(0).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(1).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(2).toString();

                db.execSQL("INSERT INTO touro(_id,rg,nome,raca) VALUES ('" + dadosXml.getProperty(0).toString() + "','" + dadosXml.getProperty(1).toString() + "','" + dadosXml.getProperty(2).toString() + "','" + dadosXml.getProperty(3).toString() + "')");

                fila += 2;
            }


            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }


    }

    public Boolean invocarGrupos() {

        String NAMESPACE = "http://projecttrace.com.br/";
        String METHOD_NAME = "ListarGrupos";
        String SOAP_ACTION = "http://projecttrace.com.br/ListarGrupos";
        String URL = "http://www.projecttrace.com.br:8091/Service1.asmx?WSDL";

        try {

            SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
            db.delete("grupo", null, null);

            SoapObject requisicao = new SoapObject(NAMESPACE, METHOD_NAME);
            requisicao.addProperty("login", login_project);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(requisicao);

            HttpTransportSE http = new HttpTransportSE(URL);
            http.call(SOAP_ACTION, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();
            SoapObject diffgram = (SoapObject) response.getProperty("diffgram");
            SoapObject newdataset = (SoapObject) diffgram.getProperty("NewDataSet");

            carregarDados = new String[newdataset.getPropertyCount() * 3];

            int fila = 0;
            for (int i = 0; i < newdataset.getPropertyCount(); i++) {
                SoapObject dadosXml = (SoapObject) newdataset.getProperty(i);

                carregarDados[fila] = dadosXml.getProperty(0).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(1).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(2).toString();

                //String x = ("INSERT INTO grupo(id,nome,_projeto) VALUES ("+ dadosXml.getProperty(0).toString() + ",'" + dadosXml.getProperty(1).toString() + "',"+ dadosXml.getProperty(2).toString() +")");

                db.execSQL("INSERT INTO grupo(id,nome,_projeto) VALUES (" + dadosXml.getProperty(0).toString() + ",'" + dadosXml.getProperty(1).toString() + "'," + dadosXml.getProperty(2).toString() + ")");


                fila += 2;
            }

            invocarProdutores();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Boolean invocarProdutores() {

        String NAMESPACE = "http://projecttrace.com.br/";
        String METHOD_NAME = "ListarProdutores";
        String SOAP_ACTION = "http://projecttrace.com.br/ListarProdutores";
        String URL = "http://www.projecttrace.com.br:8091/Service1.asmx?WSDL";

        try {

            SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
            db.delete("produtor", null, null);

            SoapObject requisicao = new SoapObject(NAMESPACE, METHOD_NAME);
            requisicao.addProperty("login", login_project);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(requisicao);

            HttpTransportSE http = new HttpTransportSE(URL);
            http.call(SOAP_ACTION, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();
            SoapObject diffgram = (SoapObject) response.getProperty("diffgram");
            SoapObject newdataset = (SoapObject) diffgram.getProperty("NewDataSet");

            carregarDados = new String[newdataset.getPropertyCount() * 3];

            int fila = 0;
            for (int i = 0; i < newdataset.getPropertyCount(); i++) {
                SoapObject dadosXml = (SoapObject) newdataset.getProperty(i);

                carregarDados[fila    ] = dadosXml.getProperty(0).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(1).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(4).toString();
                //carregarDados[fila + 1] = dadosXml.getProperty(3).toString();
                //carregarDados[fila + 1] = dadosXml.getProperty(4).toString();

                db.execSQL("INSERT INTO produtor (id,nome, longitude, latidude, _grupo) VALUES "
                        + " (" + dadosXml.getProperty(0).toString() + ",'" + dadosXml.getProperty(1).toString() +
                        "','" + "latitude" + "','" + "longitude" + "'," + dadosXml.getProperty(4).toString() + ")");

                fila += 2;
            }

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(BaixarProjetos.this,main.class);
        startActivity(i);
    }

    public Boolean invocarProducao() {

        String NAMESPACE = "http://projecttrace.com.br/";
        String METHOD_NAME = "Producao";
        String SOAP_ACTION = "http://projecttrace.com.br/Producao";
        String URL = "http://www.projecttrace.com.br:8091/Service1.asmx?WSDL";

        try {

            SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
            db.delete("producao", null, null);


            SoapObject requisicao = new SoapObject(NAMESPACE, METHOD_NAME);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(requisicao);

            HttpTransportSE http = new HttpTransportSE(URL);
            http.call(SOAP_ACTION, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();
            SoapObject diffgram = (SoapObject) response.getProperty("diffgram");
            SoapObject newdataset = (SoapObject) diffgram.getProperty("NewDataSet");

            carregarDados = new String[newdataset.getPropertyCount() * 10];

            int fila = 0;
            for (int i = 0; i < newdataset.getPropertyCount(); i++) {
                SoapObject dadosXml = (SoapObject) newdataset.getProperty(i);

                carregarDados[fila] = dadosXml.getProperty(0).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(1).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(2).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(3).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(4).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(5).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(6).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(7).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(8).toString();
                carregarDados[fila + 1] = dadosXml.getProperty(9).toString();


                db.execSQL("INSERT INTO producao (id_projeto,id_grupo_propriedade,id_propriedade,data_referencia, vacas_em_lactacao, " +
                        "dias_producao, producao_media_vaca,producao_mensal,area,producao_media_dia) VALUES " +
                        "  (" + dadosXml.getProperty(0).toString() + ",'" + dadosXml.getProperty(1).toString() +
                        "','" + dadosXml.getProperty(2).toString() + "','" + dadosXml.getProperty(3).toString() +
                        "','" + dadosXml.getProperty(4).toString() + "','" + dadosXml.getProperty(5).toString() +
                        "'," + dadosXml.getProperty(6).toString() + "," + dadosXml.getProperty(7).toString() +
                        ",'" + dadosXml.getProperty(8).toString() + "'," + dadosXml.getProperty(9).toString() + ")");


                fila += 2;
            }


            startActivity(new Intent(getBaseContext(), main.class));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }

    }

    class asyntodos extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            // super.onPreExecute();
            dialogo = new ProgressDialog(BaixarProjetos.this);
            dialogo.setTitle("Now Loading...");
            dialogo.setMessage("Lendo dados do servidor Biosistemico...");
            dialogo.setProgressStyle(barProgressDialog.STYLE_SPINNER);
            dialogo.setProgress(0);
            dialogo.setMax(100);
            dialogo.setIndeterminate(false);
            dialogo.setCancelable(false);
            dialogo.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if (invocarCliente())
            {
                return "ok";
            }
            else
            {
                return "err";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            // super.onPostExecute(result);
            dialogo.dismiss();
            if (result.equals("ok")) {
                startActivity(new Intent(getBaseContext(), main.class));
            } else {

            }
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }


}