package com.pec.biosistemico.pec.domain;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.cloud.GMailSender;
import com.pec.biosistemico.pec.dao.FinanceiroDAO;
import com.pec.biosistemico.pec.dao.PaperTopDAO;
import com.pec.biosistemico.pec.dao.TempDAO;
import com.pec.biosistemico.pec.main;
import com.pec.biosistemico.pec.model.PaperTop;
import com.pec.biosistemico.pec.paperTop.financeiro.Produto;
import com.pec.biosistemico.pec.rest.ApiClient;
import com.pec.biosistemico.pec.rest.ApiInterface;
import com.pec.biosistemico.pec.util.Global;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import static android.content.ContentValues.TAG;

public class CargaDados extends Activity {

    private final String END_POINT = "http://www.projecttrace.com.br";
    Global mDados =  Global.getInstance();
    private PaperTopDAO paperTopDAO;
    private FinanceiroDAO produtoDAO;
    private TempDAO tempDAO;
    private PaperTop paperTop;
    private Produto produto;
    private ProgressDialog dialogo;
    private ProgressDialog barProgressDialog;
    private int produtor = 0;
    private int erros = 0;
    private int sucesso = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service);

        mDados.setEnvio(false);

        paperTopDAO = new PaperTopDAO(this);
        paperTopDAO.open();

        tempDAO = new TempDAO(this);
        tempDAO.open();

        produtoDAO = new FinanceiroDAO(this);
        produtoDAO.open();


        String Grupo = mDados.getUsuario();

        new ChamaJsonIBS().execute();
    }

    @Override
    public void onResume() {

        paperTopDAO.open();
        tempDAO.open();
        produtoDAO.open();

        super.onResume();
    }

    @Override
    public void onPause() {

        paperTopDAO.close();
        tempDAO.close();
        produtoDAO.close();

        super.onPause();
    }

    public void getPaperTopJsonElement(){

        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiInterface service = retrofit.create(ApiInterface.class);
            Call<ListGson> requestPaperTop = service.listPaperTop(mDados.getUsuario(), produtor);

            requestPaperTop.enqueue(new Callback<ListGson>() {
                @Override
                public void onResponse(Call<ListGson> call, Response<ListGson> response) {
                    if (!response.isSuccessful()) {
                        Log.i("TAG", "Erro:" + response.code());
                    } else {
                        //sucesso
                        ListGson paperTop = response.body();

                        mDados.setStatus_conexao(response.message());

                        //#### DELETANDO O PRODUTOR DA TEMP TABLE###################


                        if(paperTop == null){
                            erros++;
                            CargaDados.this.paperTop = new PaperTop();
                            enviaEmail("Falha! <br>Propriedade: " + CargaDados.this.paperTop.getIdPropriedade() + "<br>" + "ID Animal: " + CargaDados.this.paperTop.getIdAnimais() + "<br>" + "Brinco:" + CargaDados.this.paperTop.getBrinco(), "Consultor: " + mDados.getConsultor());
                        }

                       if(paperTop != null) {

                        for (PaperTop p : paperTop.papertop) {

                            sucesso++;

                            CargaDados.this.paperTop = new PaperTop();

                            CargaDados.this.paperTop.setProjeto(mDados.getProjeto());
                            CargaDados.this.paperTop.setGrupo(Integer.parseInt(mDados.getUsuario()));
                            CargaDados.this.paperTop.setIdPropriedade(p.getIdPropriedade());
                            CargaDados.this.paperTop.setObs(p.getObs());
                            CargaDados.this.paperTop.setIdCobertura(p.getIdCobertura());
                            CargaDados.this.paperTop.setDataParto(p.getDataParto());
                            CargaDados.this.paperTop.setArea(p.getArea());
                            CargaDados.this.paperTop.setDataCobertura(p.getDataCobertura());
                            CargaDados.this.paperTop.setDataDiagnostico(p.getDataDiagnostico());
                            CargaDados.this.paperTop.setBrinco(p.getBrinco());
                           //  paperTop.setDataPesagem(p.getDataPesagem());
                            CargaDados.this.paperTop.setDiasPrenhez(p.getDiasPrenhez());
                            CargaDados.this.paperTop.setIdReprodutivo(p.getIdReprodutivo());
                            CargaDados.this.paperTop.setUltimoparto(p.getUltimoparto());
                            CargaDados.this.paperTop.setStatus(p.getStatus());
                            CargaDados.this.paperTop.setReferencia(p.getReferencia());
                            CargaDados.this.paperTop.setPrevisaoSecagem(p.getPrevisaoSecagem());
                            CargaDados.this.paperTop.setCategoria(p.getCategoria());
                            CargaDados.this.paperTop.setIdAnimais(p.getIdAnimais());
                            CargaDados.this.paperTop.setNomeVaca(p.getNomeVaca());
                            CargaDados.this.paperTop.setStatusProdutivo(p.getStatusProdutivo());
                            CargaDados.this.paperTop.setOcorrencia(p.getOcorrencia());
                            CargaDados.this.paperTop.setManejo(p.getManejo());
                            CargaDados.this.paperTop.setNumeroFetos(p.getNumeroFetos());
                            CargaDados.this.paperTop.setPrevisaoParto(p.getPrevisaoParto());

                            CargaDados.this.paperTop = paperTopDAO.inserirPaperTop(CargaDados.this.paperTop);
                        }
                    }
                    }
                }

                @Override
                public void onFailure(Call<ListGson> call, Throwable t) {

                    erros++;
                    mDados.setStatus_conexao(t.getMessage());

                    paperTop = new PaperTop();

                    enviaEmail("Falha! <br>Propriedade: " + paperTop.getIdPropriedade() + "<br>" + "ID Animal: " + paperTop.getIdAnimais() + "<br>" + "Brinco:" + paperTop.getBrinco(), "Consultor: " + mDados.getConsultor());
                    startActivity(new Intent(getBaseContext(), main.class));

                    Log.e(TAG, "Erro: " + t.getMessage());
                }
            });
        }

        catch (Exception ex){

            ex.getMessage();
        }
    }

    public void getCriatfJsonElement(){

        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiInterface service = retrofit.create(ApiInterface.class);
            Call<ListGson> requestPaperTop = service.listPaperTop(mDados.getUsuario(), produtor);

            requestPaperTop.enqueue(new Callback<ListGson>() {
                @Override
                public void onResponse(Call<ListGson> call, Response<ListGson> response) {
                    if (!response.isSuccessful()) {
                        Log.i("TAG", "Erro:" + response.code());
                    } else {
                        //sucesso
                        ListGson paperTop = response.body();

                        mDados.setStatus_conexao(response.message());

                        //#### DELETANDO O PRODUTOR DA TEMP TABLE###################


                        if(paperTop == null){
                            erros++;
                            CargaDados.this.paperTop = new PaperTop();
                            enviaEmail("Falha! <br>Propriedade: " + CargaDados.this.paperTop.getIdPropriedade() + "<br>" + "ID Animal: " + CargaDados.this.paperTop.getIdAnimais() + "<br>" + "Brinco:" + CargaDados.this.paperTop.getBrinco(), "Consultor: " + mDados.getConsultor());
                        }

                        if(paperTop != null) {

                            for (PaperTop p : paperTop.papertop) {

                                sucesso++;

                                CargaDados.this.paperTop = new PaperTop();

                                CargaDados.this.paperTop.setProjeto(mDados.getProjeto());
                                CargaDados.this.paperTop.setGrupo(Integer.parseInt(mDados.getUsuario()));
                                CargaDados.this.paperTop.setIdPropriedade(p.getIdPropriedade());
                                CargaDados.this.paperTop.setObs(p.getObs());
                                CargaDados.this.paperTop.setIdCobertura(p.getIdCobertura());
                                CargaDados.this.paperTop.setDataParto(p.getDataParto());
                                CargaDados.this.paperTop.setArea(p.getArea());
                                CargaDados.this.paperTop.setDataCobertura(p.getDataCobertura());
                                CargaDados.this.paperTop.setDataDiagnostico(p.getDataDiagnostico());
                                CargaDados.this.paperTop.setBrinco(p.getBrinco());
                                CargaDados.this.paperTop.setDataPesagem(p.getDataPesagem());
                                CargaDados.this.paperTop.setDiasPrenhez(p.getDiasPrenhez());
                                CargaDados.this.paperTop.setIdReprodutivo(p.getIdReprodutivo());
                                CargaDados.this.paperTop.setUltimoparto(p.getUltimoparto());
                                CargaDados.this.paperTop.setStatus(p.getStatus());
                                CargaDados.this.paperTop.setReferencia(p.getReferencia());
                                CargaDados.this.paperTop.setPrevisaoSecagem(p.getPrevisaoSecagem());
                                CargaDados.this.paperTop.setCategoria(p.getCategoria());
                                CargaDados.this.paperTop.setIdAnimais(p.getIdAnimais());
                                CargaDados.this.paperTop.setNomeVaca(p.getNomeVaca());
                                CargaDados.this.paperTop.setStatusProdutivo(p.getStatusProdutivo());
                                CargaDados.this.paperTop.setOcorrencia(p.getOcorrencia());
                                CargaDados.this.paperTop.setManejo(p.getManejo());
                                CargaDados.this.paperTop.setNumeroFetos(p.getNumeroFetos());
                                CargaDados.this.paperTop.setPrevisaoParto(p.getPrevisaoParto());

                                CargaDados.this.paperTop = paperTopDAO.inserirPaperTop(CargaDados.this.paperTop);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ListGson> call, Throwable t) {

                    erros++;
                    mDados.setStatus_conexao(t.getMessage());

                    paperTop = new PaperTop();

                    enviaEmail("Falha! <br>Propriedade: " + paperTop.getIdPropriedade() + "<br>" + "ID Animal: " + paperTop.getIdAnimais() + "<br>" + "Brinco:" + paperTop.getBrinco(), "Consultor: " + mDados.getConsultor());
                    startActivity(new Intent(getBaseContext(), main.class));

                    Log.e(TAG, "Erro: " + t.getMessage());
                }
            });
        }

        catch (Exception ex){

            ex.getMessage();
        }
    }

    public void getProdutoJsonElement(){

        try {

            produtoDAO.deleteAllEnviado();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiInterface service = retrofit.create(ApiInterface.class);
            Call<ListGson> requestProduto = service.listProduto();

            requestProduto.enqueue(new Callback<ListGson>() {
                @Override
                public void onResponse(Call<ListGson> call, Response<ListGson> response) {
                    if (!response.isSuccessful()) {
                        Log.i("TAG", "Erro:" + response.code());
                    } else {
                        //sucesso
                        ListGson produtos = response.body();

                        mDados.setStatus_conexao(response.message());

                        if(produtos == null){
                            erros++;
                            produto = new Produto();
                            enviaEmail("Falha! <br>Propriedade: " + produto.get_propriedade() + "<br>", "Consultor: " + mDados.getConsultor());
                        }

                        if(produtos != null) {

                            for (Produto p : produtos.produto) {

                                sucesso++;

                                produto = new Produto();

                                produto.set_projeto(mDados.getProjeto());
                                produto.set_grupo(Integer.parseInt(mDados.getUsuario()));
                                produto.set_propriedade(mDados.getProdutor());
                                produto.setValorUnitario(p.getValorUnitario());
                                produto.setValorMaximo(p.getValorMaximo());
                                produto.setValorMinimo(p.getValorMinimo());
                                produto.setIdProduto(p.getIdProduto());
                                produto.setUnidadeMedida(p.getUnidadeMedida());
                                produto.setTipo(p.getTipo());
                                produto.setDescricao(p.getDescricao());

                                produto = produtoDAO.inserirProduto(produto);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ListGson> call, Throwable t) {

                    erros++;
                    mDados.setStatus_conexao(t.getMessage());

                    produto = new Produto();

                    enviaEmail("Falha! <br>Propriedade: " + produto.get_propriedade() + "<br>", "Consultor: " + mDados.getConsultor());
                    startActivity(new Intent(getBaseContext(), main.class));

                    Log.e(TAG, "Erro: " + t.getMessage());
                }
            });
        }

        catch (Exception ex){

            ex.getMessage();
        }
    }

    public String Touros() throws JSONException {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        JSONObject json = null;

        HttpGet httpGet = new HttpGet("http://www.projecttrace.com.br/projectservice/touros/listaTodos");
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {

                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;

                int i = 0;

                String sememDisponivel = "";
                String brinco = "";
                String idTouro = "";
                String nome = "";
                String idRaca = "";
                String nomeRaca = "";

                int xx = 0;

                Global mDados = Global.getInstance();
                String Grupo = mDados.getUsuario();
                int Projeto = mDados.getProjeto();

                SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

                //#### DELETANDO O PRODUTOR DA TEMP TABLE###################
                db.execSQL("DELETE FROM Touro ");

                if ((line = reader.readLine()) != null) {
                    // builder.append(line);

                    json = new JSONObject(line);


                    JSONArray jArray = json.getJSONArray("touro");

                    for (int j = 0; j < jArray.length(); j++) {
                        JSONObject jReal = jArray.getJSONObject(j);

                        xx = j;

                        brinco = jReal.getString("brinco");
                        idTouro = jReal.getString("idTouro");
                        nome = jReal.getString("nome");
                        idRaca = jReal.getString("idRaca");
                        nomeRaca = jReal.getString("nomeRaca");
                        sememDisponivel = jReal.getString("sememDisponivel");

                        String x = "INSERT INTO touro(id,brinco,nome,id_raca,raca,sememDisponivel) VALUES ("+idTouro+",'"+brinco+"','"+nome+"',"+idRaca+",'"+nomeRaca+"',"+sememDisponivel+")";


                        db.execSQL(x);

                    }
                }

            }


        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (RuntimeException x){
            x.printStackTrace();
        }
        return builder.toString();
    }

    public String LoadIA() throws JSONException {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(
                "http://www.projecttrace.com.br:8092/Default.aspx?id="+produtor+"&&op=D0");
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {

                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;

                int i = 0;
                String nome_animal = "";
                String data_protocolo= "";
                String data_inseminacao = "";
                String vacina = "";
                String raca = "";
                String id_animais = "";
                String propriedade = "";
                String touro = "";
                String id_criatf = "";
                String d8 = "";
                String iatf = "";
                String fez_d0 = "";
                String fez_ia = "";
                String id_reprodutivo = "";
                String id_cobertua = "";

                int xx = 0;

                Global mDados = Global.getInstance();
                String Grupo = mDados.getUsuario();
                int Projeto = mDados.getProjeto();

                SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);


                if ((line = reader.readLine()) != null) {

                    JSONArray json = new JSONArray(line);

                    for (int j = 0; j < json.length(); j++) {
                        JSONObject jReal = json.getJSONObject(j);

                        xx = j;

                        nome_animal = jReal.getString("nome_animal");
                        data_protocolo = jReal.getString("data_protocolo");
                        data_inseminacao = jReal.getString("data_inseminacao");
                        vacina = jReal.getString("vacina");
                        raca = jReal.getString("raca");
                        id_animais = jReal.getString("id_animais");
                        propriedade = jReal.getString("propriedade");
                        touro = jReal.getString("touro");
                        id_criatf = jReal.getString("id_criatf");
                        d8 = jReal.getString("d8");
                        iatf = jReal.getString("iatf");
                        fez_d0 = jReal.getString("fez_d0");
                        fez_ia = jReal.getString("fez_ia");
                        id_reprodutivo = jReal.getString("id_reprodutivo");
                        id_cobertua = jReal.getString("id_cobertura");

                        StringBuilder sql = new StringBuilder();

                        if(id_animais.toString().equals("")){
                            id_animais = null;
                        }
                        if(id_cobertua.toString().equals("")){

                            id_cobertua = null;
                        }
                        if(id_reprodutivo.toString().equals("")){

                            id_reprodutivo = null;
                        }

                        sql.append("INSERT INTO CRIATF (_projeto,_grupo,_propriedade,id_criatf, data_protocolo,data_inseminacao,vacina,_raca,touro,");
                        sql.append("fez_d0,fez_ia, nome_vaca, _id_animais, id_reprodutivo, id_cobertura,processo_d8, processo_iatf)  ");
                        sql.append(" VALUES (");
                        sql.append(""+Projeto+","+Grupo+","+produtor+","+id_criatf+",'"+data_protocolo+"','"+data_inseminacao+"','"+vacina+"',"+raca+",'"+touro+"','");
                        sql.append(""+fez_d0+"','"+fez_ia+"','"+nome_animal+"',"+id_animais+","+id_reprodutivo+","+id_cobertua+",'"+d8+"','"+iatf+"'");
                        sql.append(" );");

                        db.execSQL(sql.toString());

                    }
                }
            }


        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public void enviaEmail(String corpo, String consultor){

        GMailSender send = new GMailSender("mobile@biosistemico.com.br","ibs@9647");
        try {
            send.sendMail(corpo,consultor,"mobile@biosistemico.com.br","ricardo.fagmer@biosistemico.com.br,douglas.souza@biosistemico.com.br",null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void PlaySound(){

        MediaPlayer mp = MediaPlayer.create(CargaDados.this, R.raw.som);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {

                mp.release();
            }

        });
        mp.start();
    }

    public String readBugzilla() throws JSONException {

        JSONObject json = null;

        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(
                "http://www.projecttrace.com.br/projectservice/papertop/listaJson?idGrupo="	+ mDados.getUsuario() + "&idProp="+produtor+"");
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {

                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;

                int i = 0;
                String dataCobertura = "";
                String dataDiagnostico = "";
                String dataParto = "";
                String diasPrenhez = "";
                String idAnimais = "";
                String idCobertura = "";
                String idReprodutivo = "";
                String numeroFetos = "";
                String previsaoParto = "";
                String StatusProdutivo = "";

                String brinco = "";
                String categoria = "";
                String dataPesagem = "";

                String idPropriedade = "";
                //String kgLeite = "";
                String manejo = "";
                String nomeVaca = "";
                String previsaoSecagem = "";
                String status = "";
                String ocorrencia = "";
                String obs = "";
                String ultimoparto = "";
                String area = "";
                String referencia = "";

                int xx = 0;

                Global mDados = Global.getInstance();
                String Grupo = mDados.getUsuario();
                int Projeto = mDados.getProjeto();

                SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

                //#### DELETANDO O PRODUTOR DA TEMP TABLE###################
                db.execSQL("DELETE FROM TEMP WHERE ID = "+ produtor +"");

                if ((line = reader.readLine()) != null) {

                    line = line.replace(":{",":[{");
                    line = line.replace("}}","}]}");


                    json = new JSONObject(line);


                    JSONArray jArray = json.getJSONArray("papertop");

                    for (int j = 0; j < jArray.length(); j++) {
                        JSONObject jReal = jArray.getJSONObject(j);

                        xx = j;

                        brinco = jReal.getString("brinco");
                        categoria = jReal.getString("categoria");
                        dataPesagem = jReal.getString("dataPesagem");
                        idAnimais = jReal.getString("idAnimais");
                        idPropriedade = jReal.getString("idPropriedade");
                        manejo = jReal.getString("manejo");
                        nomeVaca = jReal.getString("nome");
                        previsaoSecagem = jReal.getString("previsaoSecagem");
                        status = jReal.getString("status");
                        area = jReal.getString("areaUtilizada");
                        dataCobertura = jReal.getString("dataCobertura");
                        dataDiagnostico = jReal.getString("dataDiagnostico");
                        dataParto = jReal.getString("dataParto");
                        diasPrenhez = jReal.getString("diasPrenhez");
                        idAnimais = jReal.getString("idAnimais");
                        idCobertura = jReal.getString("idCobertura");
                        idReprodutivo = jReal.getString("idReprodutivo");
                        numeroFetos = jReal.getString("numeroFetos");
                        previsaoParto = jReal.getString("previsaoParto");
                        StatusProdutivo = jReal.getString("statusProdutivo");

                        String x = "INSERT INTO paperPort(categoria,idAnimais,_projeto,_grupo,_propriedade,data_coleta,area_atual,brinco,nome_usual,status,data_secagem,obs,ultimo_parto,ocorrencia,referencia, "
                                + "previsaoSecagem, dataCobertura,dataDiagnostico,dataParto,diasPrenhez,idCobertura,idReprodutivo,numeroFetos,previsaoParto,statusProdutivo)"
                                + "VALUES('"
                                + categoria
                                + "','"
                                + idAnimais
                                + "','"
                                + Projeto
                                + "','"
                                + Grupo		+ "','"
                                + idPropriedade
                                + "','"
                                + dataPesagem
                                + "','"
                                + area
                                + "','"
                                + brinco
                                + "','"
                                + nomeVaca
                                + "','"
                                + status
                                + "','"
                                + previsaoSecagem
                                + "','"
                                + obs
                                + "','"
                                + ultimoparto
                                + "','"
                                + ocorrencia
                                + "','"
                                + referencia
                                + "','"
                                + previsaoSecagem
                                + "','"
                                + dataCobertura
                                + "','"
                                + dataDiagnostico
                                + "','"
                                + dataParto
                                + "','"
                                + diasPrenhez
                                + "','"
                                + idCobertura
                                + "','"
                                + idReprodutivo
                                + "','"
                                + numeroFetos
                                + "','"
                                + previsaoParto
                                + "','"
                                + StatusProdutivo
                                + "')";

                        db.execSQL(x);


                    }
                }
            }

        } catch (ClientProtocolException e)
        {
            e.printStackTrace();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        catch(RuntimeException e){


        }

        catch (Exception ex) {
        }
        return null;
    }


    class ChamaJsonIBS extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            // super.onPreExecute();
            dialogo = new ProgressDialog(CargaDados.this);
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

            try {

                SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

                Cursor cursor = db.rawQuery("SELECT * FROM TEMP", null);
                int id = 0;

                if (cursor != null && cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            id = cursor.getInt(cursor.getColumnIndex("id"));
                            produtor = id;
                            tempDAO.deleteTemp(produtor);
                            PlaySound();

                            if(mDados.getPaperTop() == 0) {
                                getPaperTopJsonElement();
                            }
                            if(mDados.getPaperTop() == 1) {
                                readBugzilla();
                            }
                            //getCriatfJsonElement();
                            LoadIA();
                            Touros();
                            paperTopDAO.deleteDuplicados();

                        }
                        while (cursor.moveToNext() == true);
                    }

                     getProdutoJsonElement();
                }
            }
            catch (Exception ex){

                return  "Erro";
            }

            return "ok";
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            // super.onPostExecute(result);

            dialogo.dismiss();
            if (result.equals("ok"))
            {
                mDados.setResumo_paperTop("Sucesso: " + sucesso + "/ Falhas: " + erros);
                startActivity(new Intent(getBaseContext(), main.class));

            }
            else {

                mDados.setResumo_paperTop("Sucesso: " + sucesso + "/ Falhas: " + erros);
                startActivity(new Intent(getBaseContext(), main.class));
            }
        }

    }

}
