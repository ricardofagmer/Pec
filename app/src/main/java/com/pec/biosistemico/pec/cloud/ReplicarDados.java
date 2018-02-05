package com.pec.biosistemico.pec.cloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pec.biosistemico.pec.domain.Retorno;
import com.pec.biosistemico.pec.model.PaperTop;
import com.pec.biosistemico.pec.model.CheckList;
import com.pec.biosistemico.pec.model.Criatf;
import com.pec.biosistemico.pec.paperTop.financeiro.LancamentoFinanceiro;
import com.pec.biosistemico.pec.paperTop.financeiro.LancamentoFinanceiroItem;
import com.pec.biosistemico.pec.paperTop.financeiro.Produto;
import com.pec.biosistemico.pec.rest.ApiClientSend;
import com.pec.biosistemico.pec.rest.ApiInterface;
import com.pec.biosistemico.pec.util.Global;
import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.main;

import retrofit2.Call;

public class ReplicarDados extends Activity implements OnClickListener {
	
	private ProgressDialog dialogo;
	private SQLiteDatabase db;
	private  int _id;
	private String forms = "";
	private Global mDados = Global.getInstance();
	private ApiInterface apiService;
	private String consultor_ibs = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service);

		db = openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
		mDados.setEnvio(true);

		Global c = Global.getInstance();
		consultor_ibs = c.getLogin();

		apiService = ApiClientSend.getClient().create(ApiInterface.class);

		new asyntodos().execute();
	}
	
	@SuppressWarnings("deprecation")
	public Boolean EnviarAtendimento_01() {

		try {

			SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
			Cursor cursor = db.rawQuery("SELECT * FROM CRIATF WHERE ENVIADO = 'NAO'",null);

			int totalDB = cursor.getCount();
			int totalReplicado = 0;

			while (cursor.moveToNext()) {
				
				StringBuilder str = new StringBuilder();			
				
				_id = cursor.getInt(cursor.getColumnIndex("_id"));		
				str.append("http://www.projecttrace.com.br:8096/Insert.aspx?OP=AT01");
				str.append("&_projeto=");
				str.append(cursor.getString(cursor.getColumnIndex("_projeto")));
				str.append("&_grupo=");
				str.append(cursor.getString(cursor.getColumnIndex("_grupo")));
				str.append("&_propriedade=");
				str.append(cursor.getString(cursor.getColumnIndex("_propriedade")));
				str.append("&data_protocolo=");
				str.append(cursor.getString(cursor.getColumnIndex("data_protocolo")));
				str.append("&data_atendimento=");
				str.append(cursor.getString(cursor.getColumnIndex("data_d0")));
				str.append("&nome_vaca=");
				str.append(cursor.getString(cursor.getColumnIndex("nome_vaca")));			
				str.append("&data_inseminacao=");
				str.append(cursor.getString(cursor.getColumnIndex("data_inseminacao")));
				str.append("&_id_animais=");
				str.append(cursor.getString(cursor.getColumnIndex("_id_animais")));
				str.append("&vacina=");
				str.append(cursor.getString(cursor.getColumnIndex("vacina")));
				str.append("&raca=");
				str.append(cursor.getString(cursor.getColumnIndex("_raca")));
				str.append("&nome_touro=");
				str.append(cursor.getString(cursor.getColumnIndex("touro")));
				str.append("&consultor=");
				str.append(cursor.getString(cursor.getColumnIndex("consultor")));
				str.append("&id_reprodutivo=");
				str.append(cursor.getString(cursor.getColumnIndex("id_reprodutivo")));
				str.append("&id_cobertura=");
				str.append(cursor.getString(cursor.getColumnIndex("id_cobertura")));
				str.append("&fez_d0=");
				str.append(cursor.getString(cursor.getColumnIndex("fez_d0")));
				str.append("&processo_d8=");
				str.append(cursor.getString(cursor.getColumnIndex("processo_d8")));
				str.append("&processo_iatf=");
				str.append(cursor.getString(cursor.getColumnIndex("processo_iatf")));
				str.append("&fez_ia=");
				str.append(cursor.getString(cursor.getColumnIndex("fez_ia")));
				str.append("&dg=");
				str.append(cursor.getString(cursor.getColumnIndex("dg")));
				str.append("&diagnostico=");
				str.append(cursor.getString(cursor.getColumnIndex("diagnostico")));
				str.append("&previsao_parto=");
				str.append(cursor.getString(cursor.getColumnIndex("previsao_parto")));
				str.append("&tipo_atendimento=");
				str.append(cursor.getString(cursor.getColumnIndex("tipo_atendimento")));
				str.append("&nova_ia=");
				str.append(cursor.getString(cursor.getColumnIndex("nova_ia")));
				str.append("&id_criatf=");
				str.append(cursor.getString(cursor.getColumnIndex("id_criatf")));	
				str.append("&criatf_pai=");
				str.append(cursor.getString(cursor.getColumnIndex("criatf_pai")));
							
				String x = str.toString().replace(" ","%20");

				String converter = convertUTF8toISO(x);
				URL url = new URL(converter);
				HttpURLConnection http = (HttpURLConnection) url.openConnection();
				InputStreamReader ips = new InputStreamReader(http.getInputStream());
				BufferedReader line = new BufferedReader(ips);
				
				String o = line.readLine();	
				
				if(o.equals("inserido")){
					
					totalReplicado++;
					db.execSQL("UPDATE criatf SET ENVIADO = 'SIM' WHERE _ID = "+ _id +"");
				}
				else
					{						
						enviaEmail("CRIATF: Erro de sistema ao tentar enviar dados", "404", "Exception Android", o);
					}				
			
			}
			
			if(totalDB != 0){

				forms = forms + "CRIATF: " + totalReplicado + "\n";


			//	Notification nt = null;
		//	NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		/*	if (totalDB == totalReplicado)
			{
				nt = new Notification(R.drawable.ibs, "Status Replicação CRIATF: ",System.currentTimeMillis());
				nt.flags = Notification.FLAG_AUTO_CANCEL;
				PendingIntent p = PendingIntent.getActivity(this, 0,new Intent(this.getApplicationContext(),main.class), 0);
				nt.setLatestEventInfo(this, "Status Replicação CRIATF: ",	"Replicação feita com sucesso, total: "+ totalReplicado, p);

				nt.vibrate = new long[] { 100, 2000, 1000, 2000 };
				notificationManager.notify((int) Math.round(Math.random()), nt);
			}*/
		}

			db.close();
		}
		catch(ClientProtocolException e){
			
			enviaEmail("CRIATF : Erro de sistema ao tentar enviar dados", " 404 ", " Exception Android PEC: ","<br> "+e.getMessage()+"");
			db.execSQL("UPDATE criatf SET ENVIADO = 'NAO' WHERE _ID = " + _id + "");
		}
		
		catch(IOException e){
			
			enviaEmail("CRIATF : Erro de sistema ao tentar enviar dados", " 404 ", " Exception Android PEC: ","<strong  style='color:red;'><b><h2><br>SERVIDOR DESLIGADO : </h2></b> <br> "+e.getMessage()+"");
			db.execSQL("UPDATE criatf SET ENVIADO = 'NAO' WHERE _ID = " + _id + "");
		}
		
		catch(RuntimeException e){
			
			enviaEmail("CRIATF : Erro de sistema ao tentar enviar dados", " 404 ", " Exception Android PEC: ","<br> "+e.getMessage()+"");
			db.execSQL("UPDATE criatf SET ENVIADO = 'NAO' WHERE _ID = " + _id + "");
		}

		catch (Exception ex) {
			
		
			enviaEmail("CRIATF : Erro de sistema ao tentar enviar dados", " 404 ", " Exception Android PEC: ","<br> "+ex.getMessage()+"");

			db.execSQL("UPDATE criatf SET ENVIADO = 'NAO' WHERE _ID = " + _id + "");
			//Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
		}

		return null;

	}

	@SuppressWarnings("deprecation")
	public Boolean EnviarPaperTop() {

		try {

			db = openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
			Cursor cursor = db.rawQuery("SELECT * FROM PAPERPORT WHERE MODIFICADO = 'SIM' AND ENVIADO = 'NAO'",null);

			int totalDB = cursor.getCount();
			int totalReplicado = 0;
			String consultor = "";
			Date data = new Date(System.currentTimeMillis());

			while (cursor.moveToNext()) {

				StringBuilder strPaper = new StringBuilder();
				String nomeVaca = cursor.getString(cursor.getColumnIndex("nome_usual"));
				nomeVaca.replaceAll("\\s+"," ");

				_id = cursor.getInt(cursor.getColumnIndex("_id"));
				consultor = cursor.getString(cursor.getColumnIndex("_propriedade"));
				strPaper.append("http://www.projecttrace.com.br:8098/Insert.aspx?OP=paperTop");
				//strPaper.append(cursor.getString(cursor.getColumnIndex("_id")));
				strPaper.append("&_projeto=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("_projeto")));
				strPaper.append("&_grupo=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("_grupo")));
				strPaper.append("&_propriedade=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("_propriedade")));
				strPaper.append("&data_coleta=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("data_coleta")));
				strPaper.append("&area_atual=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("area_atual")));
				strPaper.append("&brinco=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("brinco")));
				strPaper.append("&id_animal=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("idAnimais")));
				strPaper.append("&nome_usual=");
				strPaper.append(nomeVaca);
				strPaper.append("&manejo=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("manejo")));
				strPaper.append("&peso_atual=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("peso_atual")));
				strPaper.append("&status=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("status")));
				strPaper.append("&data_secagem=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("data_secagem")));
				strPaper.append("&obs=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("obs")));
				strPaper.append("&ultimo_parto=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("ultimo_parto")));
				strPaper.append("&ocorrencia=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("ocorrencia")));
				strPaper.append("&referencia=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("referencia")));
				strPaper.append("&data_importacao=");
				strPaper.append(data);
				strPaper.append("&dataCobertura=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("dataCobertura")));
				strPaper.append("&statusProdutivo=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("statusProdutivo")));
				strPaper.append("&status_reprodutivo=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("statusReprodutivo")));
				strPaper.append("&tipoParto=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("tipoParto")));
				strPaper.append("&numeroDias=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("numeroDias")));
				strPaper.append("&dataDiagnostico=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("dataDiagnostico")));
				strPaper.append("&idReprodutivo=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("idReprodutivo")));
				strPaper.append("&dataParto=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("dataParto")));
				strPaper.append("&dataPesagem=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("dataPesagem")));
				strPaper.append("&diasPrenhez=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("diasPrenhez")));
				strPaper.append("&idCobertura=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("idCobertura")));
				strPaper.append("&numeroFetos=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("numeroFetos")));
				strPaper.append("&previsaoParto=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("previsaoParto")));
				strPaper.append("&previsaoSecagem=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("previsaoSecagem")));
				strPaper.append("&ultimo_peso=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("ultimo_peso")));
				strPaper.append("&id_criatf=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("id_criatf")));
				strPaper.append("&consultor=");
				strPaper.append(mDados.getConsultor());
				strPaper.append("&cod_mobile=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("cod_mobile")));
				strPaper.append("&raca=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("raca")));
				strPaper.append("&data_nascimento=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("data_nascimento")));

				String x = strPaper.toString().replace(" ","%20");

				URL url = new URL(x);
				HttpURLConnection http = (HttpURLConnection) url.openConnection();
				InputStreamReader ips = new InputStreamReader(http.getInputStream());
				BufferedReader line = new BufferedReader(ips);

				String o = line.readLine();

				if(o.equals("inserido")){

					totalReplicado++;
					db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'SIM',peso_atual ='', dataParto = '', dataDiagnostico = '' WHERE _ID = "+ _id +"");
				}
				else
				{
					enviaEmail("FrmPaperTop: ",String.valueOf(_id), consultor,"<br>"+o);
				}

			}

			if(totalDB != 0){

				forms = forms + "Paper Top: " + totalReplicado + "\n";

				Notification nt = null;
				NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	/*		if (totalDB == totalReplicado) {
				nt = new Notification(R.drawable.ibs,
						"Status Replicação ListGson: ",
						System.currentTimeMillis());
				nt.flags = Notification.FLAG_AUTO_CANCEL;
				PendingIntent p = PendingIntent
						.getActivity(this, 0,
								new Intent(this.getApplicationContext(),
										main.class), 0);
				nt.setLatestEventInfo(this, "Status Replicação ListGson: ",
						"Replicação feita com sucesso, total: "
								+ totalReplicado, p);

				nt.vibrate = new long[] { 100, 2000, 1000, 2000 };
				notificationManager.notify((int) Math.round(Math.random()), nt);
			}*/
			}

			db.close();

		}

		catch(ClientProtocolException e){

			enviaEmail("Paper Top: Erro de sistema ao tentar enviar dados", " 404 ", " Exception Android PEC: ","<br>"+e.getMessage()+"");
			db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'NAO' WHERE _ID = " + _id + "");

		}

		catch(IOException e){

			enviaEmail("Paper Top: Erro de sistema ao tentar enviar dados", " 404 ", " Exception Android PEC: ","<strong  style='color:red;'><b><h2><br>SERVIDOR DESLIGADO : </h2></b> <br> "+e.getMessage()+"");
			db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'NAO' WHERE _ID = " + _id + "");

		}

		catch(RuntimeException e){

			enviaEmail("Paper Top: Erro de sistema ao tentar enviar dados", " 404 ", " Exception Android PEC: ","<br>"+e.getMessage()+"");
			db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'NAO' WHERE _ID = " + _id + "");

		}

		catch (Exception ex) {

			enviaEmail("Paper Top: Erro de sistema ao tentar enviar dados", " 404 ", " Exception Android PEC: ","<br>"+ex.getMessage()+"");
			db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'NAO' WHERE _ID = " + _id + "");
		}

		//startActivity(new Intent(getBaseContext(), main.class));

		return null;

	}

	public boolean BuildCriatf() {

		try {

			Cursor cursor = db.rawQuery("SELECT * FROM CRIATF WHERE ENVIADO = 'NAO'",null);

			List<com.pec.biosistemico.pec.model.Criatf> criatfList = new ArrayList<com.pec.biosistemico.pec.model.Criatf>();

			while (cursor.moveToNext()) {

				Criatf criatf = new Criatf();

				_id = cursor.getInt(cursor.getColumnIndex("_id"));
				criatf.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				criatf.set_projeto(cursor.getInt(cursor.getColumnIndex("_projeto")));
				criatf.setGrupo(cursor.getString(cursor.getColumnIndex("_grupo")));
				criatf.setPropriedade(cursor.getInt(cursor.getColumnIndex("_propriedade")));
				criatf.setData_protocolo(cursor.getString(cursor.getColumnIndex("data_protocolo")));
				criatf.setData_d0(cursor.getString(cursor.getColumnIndex("data_d0")));
				criatf.setNome_vaca(cursor.getString(cursor.getColumnIndex("nome_vaca")));
				criatf.setData_inseminacao(cursor.getString(cursor.getColumnIndex("data_inseminacao")));
				criatf.setId_animais(cursor.getString(cursor.getColumnIndex("_id_animais")));
				criatf.setVacina(cursor.getString(cursor.getColumnIndex("vacina")));
				criatf.setRaca(cursor.getString(cursor.getColumnIndex("_raca")));
				criatf.setTouro(cursor.getString(cursor.getColumnIndex("touro")));
				criatf.setConsultor(cursor.getString(cursor.getColumnIndex("consultor")));
				criatf.setId_reprodutivo(cursor.getInt(cursor.getColumnIndex("id_reprodutivo")));
				criatf.setCobertura(cursor.getInt(cursor.getColumnIndex("id_cobertura")));
				criatf.setFez_d0(cursor.getString(cursor.getColumnIndex("fez_d0")));
				criatf.setD8(cursor.getString(cursor.getColumnIndex("processo_d8")));
				criatf.setIatf(cursor.getString(cursor.getColumnIndex("processo_iatf")));
				criatf.setFez_ia(cursor.getString(cursor.getColumnIndex("fez_ia")));
				criatf.setDg(cursor.getString(cursor.getColumnIndex("dg")));
				criatf.setDiagnostico(cursor.getString(cursor.getColumnIndex("diagnostico")));
				criatf.setPrevisao_parto(cursor.getString(cursor.getColumnIndex("previsao_parto")));
				criatf.setTipo_atendimento(cursor.getString(cursor.getColumnIndex("tipo_atendimento")));
				criatf.setNova_ia(cursor.getString(cursor.getColumnIndex("nova_ia")));
				criatf.setId_criatf(cursor.getInt(cursor.getColumnIndex("id_criatf")));
				criatf.setCriatf_pai(cursor.getString(cursor.getColumnIndex("criatf_pai")));
				criatf.setCod_mobile(cursor.getString(cursor.getColumnIndex("cod_mobile")));

				criatfList.add(criatf);
			}

			if(criatfList != null && !criatfList.isEmpty()){

				Gson gson = new Gson();
				String json = gson.toJson(criatfList);

				//String converter = convertUTF8toISO(x);

				SendCriatf(criatfList);
			}
		}
		catch (Exception ex)
		{
			ex.getMessage();
		}

		return true;
	}

	private String SendCriatf(final List<Criatf> criatfList) throws IOException {

		final String retorno = null;

		Call<String> call = apiService.enviarCriatf(criatfList);
		String resposta  = call.execute().body().toString();

		if(resposta.equals("inserido"))
		{
			for (Criatf criatf : criatfList) {

				db.execSQL("UPDATE criatf SET ENVIADO = 'SIM' WHERE _ID = "+ criatf.getId() +"");
			}
		}
		else
		{
			Log.e("Criatf error", "Erro ao enviar dados");
			for (Criatf criatf : criatfList) {

				Notifica(ReplicarDados.this,"Erro" + resposta, "",44);

			}
		}

		return retorno;
	}

	public boolean BuildPaperTop() {

		try {

			Cursor cursor = db.rawQuery("SELECT * FROM PAPERPORT WHERE MODIFICADO = 'SIM' AND ENVIADO = 'NAO'", null);

			List<PaperTop> paperTopList = new ArrayList<PaperTop>();

			while (cursor.moveToNext()) {

				PaperTop paperTop = new PaperTop();

				paperTop.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
				paperTop.setProjeto(cursor.getInt(cursor.getColumnIndex("_projeto")));
				paperTop.setGrupo(cursor.getInt(cursor.getColumnIndex("_grupo")));
				paperTop.setIdPropriedade(cursor.getString(cursor.getColumnIndex("_propriedade")));
				paperTop.setData_coleta(cursor.getString(cursor.getColumnIndex("data_coleta")));
				paperTop.setArea(cursor.getString(cursor.getColumnIndex("area_atual")));
				paperTop.setBrinco(cursor.getString(cursor.getColumnIndex("brinco")));
				paperTop.setIdAnimais(cursor.getString(cursor.getColumnIndex("idAnimais")));
				paperTop.setNomeVaca(cursor.getString(cursor.getColumnIndex("nome_usual")));
				paperTop.setManejo(cursor.getString(cursor.getColumnIndex("manejo")));
				paperTop.setPeso_atual(cursor.getString(cursor.getColumnIndex("peso_atual")));
				paperTop.setStatus(cursor.getString(cursor.getColumnIndex("status")));
				paperTop.setPrevisaoSecagem(cursor.getString(cursor.getColumnIndex("data_secagem")));
				paperTop.setObs(cursor.getString(cursor.getColumnIndex("obs")));
				paperTop.setUltimoparto(cursor.getString(cursor.getColumnIndex("ultimo_parto")));
				paperTop.setOcorrencia(cursor.getString(cursor.getColumnIndex("ocorrencia")));
				paperTop.setReferencia(cursor.getString(cursor.getColumnIndex("referencia")));
				paperTop.setDataCobertura(cursor.getString(cursor.getColumnIndex("dataCobertura")));
				paperTop.setStatusProdutivo(cursor.getString(cursor.getColumnIndex("statusProdutivo")));
				paperTop.setIdReprodutivo(cursor.getString(cursor.getColumnIndex("statusReprodutivo")));
				paperTop.setTipoParto(cursor.getString(cursor.getColumnIndex("tipoParto")));
				paperTop.setNumeroDias(cursor.getInt(cursor.getColumnIndex("numeroDias")));
				paperTop.setDataDiagnostico(cursor.getString(cursor.getColumnIndex("dataDiagnostico")));
				paperTop.setIdReprodutivo(cursor.getString(cursor.getColumnIndex("idReprodutivo")));
				paperTop.setDataParto(cursor.getString(cursor.getColumnIndex("dataParto")));
				paperTop.setDiasPrenhez(cursor.getString(cursor.getColumnIndex("diasPrenhez")));
				paperTop.setIdCobertura(cursor.getString(cursor.getColumnIndex("idCobertura")));
				paperTop.setNumeroFetos(cursor.getString(cursor.getColumnIndex("numeroFetos")));
				paperTop.setPrevisaoParto(cursor.getString(cursor.getColumnIndex("previsaoParto")));
				paperTop.setPrevisaoSecagem(cursor.getString(cursor.getColumnIndex("previsaoSecagem")));
				paperTop.setId_criatf(cursor.getInt(cursor.getColumnIndex("id_criatf")));
				paperTop.setConsultor(RetornaConsultor());
				paperTop.setCod_mobile(cursor.getString(cursor.getColumnIndex("cod_mobile")));
				paperTop.setRaca(cursor.getInt(cursor.getColumnIndex("raca")));
				paperTop.setData_nascimento(cursor.getString(cursor.getColumnIndex("data_nascimento")));

				paperTopList.add(paperTop);
			}

			if(paperTopList != null && !paperTopList.isEmpty()){

				Gson gson = new Gson();
				String json = gson.toJson(paperTopList);

				SendPaperTop(paperTopList);
			}
		}
		catch (Exception ex)
		{
			ex.getMessage();
		}

		return true;
	}

	private String SendPaperTop(final List<PaperTop> listaPaperTop) throws IOException {

		final String retorno = null;
		Call<String> call = apiService.enviarPaperTop(listaPaperTop);
		String resposta  = call.execute().body().toString();

		if(resposta.equals("inserido"))
		{
			for (PaperTop paperTop : listaPaperTop)
			{
				db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'SIM' WHERE _ID = " + paperTop.get_id() + "");
				db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'SIM',peso_atual ='', dataParto = '', dataDiagnostico = '' WHERE _ID = " + _id + "");
			}
		}
		else {
				Log.e("FrmPaperTop error", "Erro ao enviar dados");
				for (PaperTop paperTop : listaPaperTop) {

					Notifica(ReplicarDados.this,"Erro" + resposta, "",44);

				}
				//Toast.makeText(getApplicationContext(), "Error ao enviar dados AtendimentoCopel", Toast.LENGTH_LONG).show();
			}

		/*call.enqueue(new Callback<String>() {
			@SuppressWarnings("WrongConstant")
			@Override
			public void onResponse(Call<String> call, Response<String> response) {

				String resposta = response.body().toString();

				if (resposta == "inserido") {
					Log.i("FrmPaperTop", "success");

					for (PaperTop paperTop : listaPaperTop) {
						db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'SIM' WHERE _ID = " + paperTop.get_id() + "");
						db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'SIM',peso_atual ='', dataParto = '', dataDiagnostico = '' WHERE _ID = "+ _id +"");
					}
				} else {
					Log.e("FrmPaperTop error", "Erro ao enviar dados");
					for (PaperTop cargaPaperTop : listaPaperTop) {

						Notifica(ReplicarDados.this,"Erro", "",44);

					}
					//Toast.makeText(getApplicationContext(), "Error ao enviar dados AtendimentoCopel", Toast.LENGTH_LONG).show();
				}

			}

			@SuppressWarnings("WrongConstant")
			@Override
			public void onFailure(Call<String> call, Throwable t) {

				Log.e("FrmPaperTop error", "Erro ao enviar dados: " + t.getMessage());
				//Toast.makeText(getApplicationContext(), "Error ao enviar dados FrmPaperTop: " + t.getMessage(), Toast.LENGTH_LONG).show();

				Notifica(ReplicarDados.this,"FrmPaperTop: Erro ao tentar enviar formulario FrmPaperTop:", t.getMessage(),0);
			}
		});*/

		return retorno;
	}

	public boolean BuildCheckList() {

		try {

			Cursor cursor = db.rawQuery("SELECT * FROM CHECKLIST WHERE ENVIADO = 'NAO'", null);

			List<com.pec.biosistemico.pec.model.CheckList> checkListList = new ArrayList<com.pec.biosistemico.pec.model.CheckList>();

			while (cursor.moveToNext()) {

				CheckList checkList = new CheckList();

				checkList.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				checkList.set_projeto(cursor.getInt(cursor.getColumnIndex("_projeto")));
				checkList.set_grupo(cursor.getInt(cursor.getColumnIndex("_grupo")));
				checkList.set_propriedade(cursor.getInt(cursor.getColumnIndex("_produtor")));
				checkList.setData_coleta(cursor.getString(cursor.getColumnIndex("data")));
				checkList.setControle1(cursor.getString(cursor.getColumnIndex("controle1")));
				checkList.setControle2(cursor.getString(cursor.getColumnIndex("controle2")));
				checkList.setControle3(cursor.getString(cursor.getColumnIndex("controle3")));
				checkList.setManejon1(cursor.getString(cursor.getColumnIndex("manejoN1")));
				checkList.setManejon2(cursor.getString(cursor.getColumnIndex("manejoN2")));
				checkList.setManejon3(cursor.getString(cursor.getColumnIndex("manejoN3")));
				checkList.setManejon4(cursor.getString(cursor.getColumnIndex("manejoN4")));
				checkList.setManejon5(cursor.getString(cursor.getColumnIndex("manejoN5")));
				checkList.setManejon6(cursor.getString(cursor.getColumnIndex("manejoN6")));
				checkList.setSanidade1(cursor.getString(cursor.getColumnIndex("sanidade1")));
				checkList.setSanidade2(cursor.getString(cursor.getColumnIndex("sanidade2")));
				checkList.setSanidade3(cursor.getString(cursor.getColumnIndex("sanidade3")));
				checkList.setSanidade4(cursor.getString(cursor.getColumnIndex("sanidade4")));
				checkList.setSanidade5(cursor.getString(cursor.getColumnIndex("sanidade5")));
				checkList.setManejor1(cursor.getString(cursor.getColumnIndex("manejoR1")));
				checkList.setManejor2(cursor.getString(cursor.getColumnIndex("manejoR2")));
				checkList.setManejor3(cursor.getString(cursor.getColumnIndex("manejoR3")));
				checkList.setManejor4(cursor.getString(cursor.getColumnIndex("manejoR4")));
				checkList.setQualidade1(cursor.getString(cursor.getColumnIndex("qualidade1")));
				checkList.setQualidade2(cursor.getString(cursor.getColumnIndex("qualidade2")));
				checkList.setQualidade3(cursor.getString(cursor.getColumnIndex("qualidade3")));
				checkList.setQualidade4(cursor.getString(cursor.getColumnIndex("qualidade4")));
				checkList.setTipo_reprodutivo(cursor.getString(cursor.getColumnIndex("tipoReprodutivo")));
				checkList.setConsultor(cursor.getString(cursor.getColumnIndex("consultor")));
				checkList.setSituacao_encontrada(cursor.getString(cursor.getColumnIndex("situacaoEncontrada")));
				checkList.setRecomendacao(cursor.getString(cursor.getColumnIndex("recomendacoes")));

				checkListList.add(checkList);
			}

			if(checkListList != null && !checkListList.isEmpty()){

				Gson gson = new Gson();
				String json = gson.toJson(checkListList);
				String  covnertido = convertUTF8toISO(json);


				SendCheckList(checkListList);
			}
		}
		catch (Exception ex)
		{
			ex.getMessage();
		}

		return true;
	}

	private boolean SendCheckList(final List<CheckList> listaChecklist) throws IOException {

		final boolean[] retorno = new boolean[1];

		Call<String> call = apiService.enviarCheckList(listaChecklist);
		String resposta  = call.execute().body().toString();

		String contentCorrected = "";

		if(resposta.equals("inserido"))
		{
			for (CheckList checkList : listaChecklist)
			{
				db.execSQL("UPDATE CHECKLIST SET ENVIADO = 'SIM' WHERE _ID = " + checkList.getId() + "");
			}
		}
		else {

			Log.e("CHECKLIST error", "Erro ao enviar dados");
			for (CheckList checkList : listaChecklist) {
				Notifica(ReplicarDados.this, "Erro:" + resposta, "", 44);
			}
		}

		return retorno[0];
	}

	public Boolean EnviarFinanceiro() {
		try {

			SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
			List<Produto> listaProdutos = new ArrayList<Produto>();

			List<Integer> ids = new ArrayList<>();

			int totalReplicado = 0;

			Cursor cursor = db.rawQuery("SELECT * FROM FINANCEIRO WHERE ENVIADO = 'NAO'", null);

			HashMap<String, LancamentoFinanceiro> map = new HashMap();

			while (cursor.moveToNext()) {

				ids.add(cursor.getInt(cursor.getColumnIndex("_id")));

				String key = cursor.getString(cursor.getColumnIndex("_propriedade"));

				if (map.containsKey(key)) {

					LancamentoFinanceiroItem item = new LancamentoFinanceiroItem();

					item.setIdLancamento(map.get(key).getId());
					item.setId(map.get(key).getListatens().size() + 1);
					item.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
					item.setIdProduto(cursor.getInt(cursor.getColumnIndex("idProduto")));

					double quantidade = cursor.getDouble(cursor.getColumnIndex("quantidade"));
					item.setQuantidade(quantidade);

					double valorUnitario = cursor.getDouble(cursor.getColumnIndex("valorUnitario"));
					item.setValorUnitario(valorUnitario);

					item.setValorTotal(quantidade * valorUnitario);
					map.get(key).getListatens().add(item);

					totalReplicado++;
				}
				else
				{
					LancamentoFinanceiro l = new LancamentoFinanceiro();

					l.setId(cursor.getInt(cursor.getColumnIndex("_id")));
					l.setDataColeta(getDateTime());
					l.setIdProjeto(cursor.getInt(cursor.getColumnIndex("_projeto")));
					l.setNomePropriedade("");
					l.setIdGrupo(cursor.getInt(cursor.getColumnIndex("_grupo")));
					l.setNomeGrupo("");
					l.setIdPropriedade(cursor.getInt(cursor.getColumnIndex("_propriedade")));
					l.setNomeProjeto("");

					l.setListaItens(new ArrayList<LancamentoFinanceiroItem>());

					LancamentoFinanceiroItem item = new LancamentoFinanceiroItem();

					item.setId(1);
					item.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
					item.setIdProduto(cursor.getInt(cursor.getColumnIndex("idProduto")));
					item.setIdLancamento(cursor.getInt(cursor.getColumnIndex("_id")));

					double quantidade = cursor.getDouble(cursor.getColumnIndex("quantidade"));
					item.setQuantidade(quantidade);

					double valorUnitario = cursor.getDouble(cursor.getColumnIndex("valorUnitario"));
					item.setValorUnitario(valorUnitario);

					item.setValorTotal(quantidade * valorUnitario);

					totalReplicado++;

					l.getListatens().add(item);
					map.put(key, l);
				}
			}

			for (Map.Entry<String, LancamentoFinanceiro> list : map.entrySet())
			{
				Gson gson = new Gson();

				LancamentoFinanceiro lancamento = list.getValue();
				String json = gson.toJson(lancamento);

				String lancamentoJson = json.toString();

				//Enviar os dados para o WS para salvar o lançamento da propriedade
				String retorno = WBFinanceiro(lancamentoJson);

				if(respostaFinanceiro(retorno) == true)
				{
					String codigos = "";
					for(Integer id : ids){

						codigos += codigos.length() == 0 ? id : "," + id;
					}

					if(codigos.length() > 0) {

						forms = forms + "FrmPaperTop Financeiro: " + totalReplicado + "\n";
						db.execSQL("UPDATE FINANCEIRO SET ENVIADO = 'SIM' WHERE _ID IN (" + ids.toString().replace("[","").replace("]","") + ")");
					}
				}
				else
				{
					Notifica(ReplicarDados.this,"Erro","Tente novamente",3);
				}
			}
		}

		catch (Exception ex){

			ex.getMessage();
		}

		return true;
	}

	@SuppressLint("SimpleDateFormat")
	private String getDateTime() {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);

	}

	public static void Notifica(Context context,CharSequence title, CharSequence message, int id){

		Intent intent = new Intent(context, main.class);

		PendingIntent pendingIntent =
				PendingIntent.getActivity(context, 0, intent, 0);
		Notification notification = null;
		Notification.Builder builder =
				new Notification.Builder(context)
						.setContentTitle(title)
						.setContentText(message)
						.setSmallIcon(R.drawable.ibs)
						.setContentIntent(pendingIntent);
		if(Build.VERSION.SDK_INT == 17){
			notification = builder.build();
		}
		else{
			notification = builder.getNotification();
		}
		NotificationManager notificationManager =
				(NotificationManager) context.getSystemService
						(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(id, notification);
	}

	public String WBFinanceiro(String conteudo) {

		String NAMESPACE = "www.projecttrace.com.br";
		String METHOD_NAME = "novo";
		String SOAP_ACTION = "http://www.projecttrace.com.br/novo";
		String URL = "http://www.projecttrace.com.br/projectservice/services/LancamentoWS?wsdl";

		try {

			SoapObject requisicao = new SoapObject(NAMESPACE, METHOD_NAME);

			requisicao.addProperty("atendimento", conteudo);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			// envelope.dotNet = true;
			envelope.setOutputSoapObject(requisicao);

			new MarshalBase64().register(envelope);
			envelope.implicitTypes = true;

			HttpTransportSE http = new HttpTransportSE(URL);
			http.call(SOAP_ACTION, envelope);

			SoapPrimitive s_resposta = (SoapPrimitive) envelope.getResponse();

			return s_resposta.toString();

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());

			return e.getMessage();
		}

	}

	public Boolean respostaFinanceiro(String jsonString) {

		Retorno retorno = new Retorno();
		try {
			JSONObject object = new JSONObject(jsonString);

			retorno.setMensagem(object.getString("mensagem"));
			retorno.setSucesso(object.getBoolean("sucesso"));

			//	enviaEmail("Erro ao tentar enviar financeiro","",mDados.getConsultor(),retorno.getMensagem());

		} catch (JSONException e)
		{
			enviaEmail("Erro ao tentar enviar financeiro","",mDados.getConsultor(),e.getMessage());

		}

		return retorno.isSucesso();
	}

	@SuppressWarnings("deprecation")
	public Boolean EnviarCheckList() {
		try {

			SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
			Cursor cursor = db.rawQuery("SELECT * FROM CHECKLIST WHERE ENVIADO = 'NAO'", null);

			int totalDB = cursor.getCount();
			int totalReplicado = 0;
			Date data = new Date(System.currentTimeMillis());
			String consultor = "";

			while (cursor.moveToNext()) {
				StringBuilder strPaper = new StringBuilder();
				_id = cursor.getInt(cursor.getColumnIndex("_id"));
				consultor = cursor.getString(cursor.getColumnIndex("consultor"));

				
				strPaper.append("http://www.projecttrace.com.br:8096/Insert.aspx?OP=checkList");
				//strPaper.append(cursor.getString(cursor.getColumnIndex("_id")));
				
				strPaper.append("&_projeto=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("_projeto")));
				strPaper.append("&_grupo=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("_grupo")));
				strPaper.append("&_propriedade=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("_produtor")));
				strPaper.append("&data_coleta=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("data")));
				strPaper.append("&controle1=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("controle1")));
				strPaper.append("&controle2=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("controle2")));
				strPaper.append("&controle3=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("controle3")));
				strPaper.append("&manejoN1=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("manejoN1")));
				strPaper.append("&manejoN2=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("manejoN2")));
				strPaper.append("&manejoN3=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("manejoN3")));
				strPaper.append("&manejoN4=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("manejoN4")));
				strPaper.append("&manejoN5=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("manejoN5")));
				strPaper.append("&manejoN6=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("manejoN6")));
				strPaper.append("&sanidade1=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("sanidade1")));
				strPaper.append("&sanidade2=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("sanidade2")));
				strPaper.append("&sanidade3=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("sanidade3")));
				strPaper.append("&sanidade4=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("sanidade4")));
				strPaper.append("&sanidade5=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("sanidade5")));
				strPaper.append("&manejoR1=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("manejoR1")));
				strPaper.append("&manejoR2=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("manejoR2")));
				strPaper.append("&manejoR3=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("manejoR3")));
				strPaper.append("&manejoR4=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("manejoR4")));
				strPaper.append("&qualidade1=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("qualidade1")));
				strPaper.append("&qualidade2=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("qualidade2")));
				strPaper.append("&qualidade3=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("qualidade3")));
				strPaper.append("&qualidade4=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("qualidade4")));				
				strPaper.append("&tipoReprodutivo=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("tipoReprodutivo")));						
				strPaper.append("&consultor=");
				strPaper.append(consultor_ibs);	
				strPaper.append("&situacaoEncontrada=");
				strPaper.append("");				
				strPaper.append("&recomendacoes=");
				strPaper.append("");		
				
				String x = strPaper.toString().replace(" ","%20");

				String converter = convertUTF8toISO(x);
				URL url = new URL(converter);
				HttpURLConnection http = (HttpURLConnection) url.openConnection();
				InputStreamReader ips = new InputStreamReader(http.getInputStream());
				BufferedReader line = new BufferedReader(ips);
				
				String o = line.readLine();	
				
				if(o.equals("inserido")){
					
					totalReplicado++;
					db.execSQL("UPDATE CHECKLIST SET ENVIADO = 'SIM' WHERE _ID = "+ _id + "");
					//enviaEmail("CheckList",String.valueOf(_id), consultor , "CheckList");

				}
				else
					{						
					enviaEmail("CheckList: ",String.valueOf(_id), consultor,"<br>"+o);
					}			
			   }

			
			if(totalDB != 0){

				forms = forms + "Relatório: " + totalReplicado + "\n";


			//	Notification nt = null;
		//	NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		/*	if (totalDB == totalReplicado) {
				nt = new Notification(R.drawable.ibs,
						"Status Replicação CheckList: ",
						System.currentTimeMillis());
				nt.flags = Notification.FLAG_AUTO_CANCEL;
				PendingIntent p = PendingIntent
						.getActivity(this, 1,
								new Intent(this.getApplicationContext(),
										main.class), 1);
				nt.setLatestEventInfo(this, "Status Replicação CheckList: ",
						"Replicação feita com sucesso, total: "
								+ totalReplicado, p);

				nt.vibrate = new long[] { 100, 2000, 1000, 2000 };
				notificationManager.notify((int) Math.round(Math.random()), nt);
			}*/
			}

			db.close();
			

		}
		
	catch(ClientProtocolException e){
			
		enviaEmail("CheckList: Erro de sistema ao tentar enviar dados", " 404 ", " Exception Android PEC: ","<br> "+e.getMessage()+"");
		db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'NAO' WHERE _ID = " + _id + "");

		}
		
		catch(IOException e){
			
			db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'NAO' WHERE _ID = " + _id + "");
			enviaEmail("CheckList: Erro de sistema ao tentar enviar dados", " 404 ", " Exception Android PEC: ","<strong  style='color:red;'><b><h2><br>SERVIDOR DESLIGADO : </h2></b> <br> "+e.getMessage()+"");
		}
		
		catch(RuntimeException e){
			
			db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'NAO' WHERE _ID = " + _id + "");
			enviaEmail("CheckList: Erro de sistema ao tentar enviar dados", " 404 ", " Exception Android PEC: ","<br> "+e.getMessage()+"");
		}

		catch (Exception e) {

			db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'NAO' WHERE _ID = " + _id + "");
			enviaEmail("CheckList: Erro de sistema ao tentar enviar dados", " 404 ", " Exception Android PEC: ","<br> "+e.getMessage()+"");
		}

		return null;

	}

	@SuppressWarnings("deprecation")
	public Boolean EnviarRelatorio() {
		try {

			SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
			Cursor cursor = db.rawQuery("SELECT * FROM REPORT WHERE ENVIADO = 'NAO'", null);

			int totalDB = cursor.getCount();
			int totalReplicado = 0;
			Date data = new Date(System.currentTimeMillis());

			while (cursor.moveToNext()) {
				StringBuilder strPaper = new StringBuilder();
				_id = cursor.getInt(cursor.getColumnIndex("_id"));
				strPaper.append("http://www.projecttrace.com.br:8096/Insert.aspx?OP=report&_id=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("_id")));
				strPaper.append("&_projeto=");
				strPaper.append(cursor.getString(cursor
						.getColumnIndex("_projeto")));
				strPaper.append("&_grupo=");
				strPaper.append(cursor.getString(cursor
						.getColumnIndex("_grupo")));
				strPaper.append("&_propriedade=");
				strPaper.append(cursor.getString(cursor
						.getColumnIndex("_produtor")));
				strPaper.append("&data_coleta=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("data")));
				strPaper.append("&data_replicacao=");
				strPaper.append(data);
				strPaper.append("&tipoReprodutivo=");
				strPaper.append(cursor.getString(cursor
						.getColumnIndex("tipoReprodutivo")));
				
				strPaper.append("&caminho_imagem=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("caminho_imagem")));
				strPaper.append("&consultor=");
				strPaper.append(consultor_ibs);	
     			strPaper.append("&recomendacoes=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("recomedacoes")));
				
				strPaper.append("&situacaoEncontrada=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("situacaoEncontrada")));
				
				String x = strPaper.toString().replace(" ","%20");		

				URL url = new URL(x);
				HttpURLConnection http = (HttpURLConnection) url.openConnection();
				InputStreamReader ips = new InputStreamReader(http.getInputStream());
				BufferedReader line = new BufferedReader(ips);
				
				String o = line.readLine();
				
				if(o.equals("inserido")){
					
					totalReplicado++;
					db.execSQL("UPDATE REPORT SET ENVIADO = 'SIM' WHERE _ID = "+ _id +"");
				}
				else
					{						
						MessageBox("Relatório: "+_id+" NAO inserido!");
					}	
			}

			Notification nt = null;
		/*	NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			if (totalDB == totalReplicado) {
				nt = new Notification(R.drawable.ibs,
						"Status Replicação Relatório: ",
						System.currentTimeMillis());
				nt.flags = Notification.FLAG_AUTO_CANCEL;
				PendingIntent p = PendingIntent
						.getActivity(this, 2,
								new Intent(this.getApplicationContext(),
										main.class), 2);
				nt.setLatestEventInfo(this, "Status Replicação Relatório: ",
						"Replicação feita com sucesso, total: "
								+ totalReplicado, p);

				nt.vibrate = new long[] { 100, 2000, 1000, 2000 };
				notificationManager.notify((int) Math.round(Math.random()), nt);
			}*/

			db.close();

		}
		
	catch(ClientProtocolException e){
			
			e.printStackTrace();
		}
		
		catch(IOException e){
			
			e.printStackTrace();
		}
		
		catch(RuntimeException e){
			
			e.printStackTrace();
		}

		catch (Exception ex) {

			 db.rawQuery("UPDATE PAPERPORT SET ENVIADO = 'NAO' WHERE _ID = " + _id + "", null);
			Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

		startActivity(new Intent(getBaseContext(), main.class));

		return null;

	}
		
	@SuppressWarnings("deprecation")
	public Boolean EnviarAnimais() {
		try {

			SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
			Cursor cursor = db.rawQuery("SELECT * FROM ANIMAL WHERE ENVIADO = 'NAO'", null);

			int totalDB = cursor.getCount();
			int totalReplicado = 0;
			Date data = new Date(System.currentTimeMillis());
			String _produtor = "";
			String consultor = "";

			while (cursor.moveToNext()) {
				StringBuilder strPaper = new StringBuilder();
				_id = cursor.getInt(cursor.getColumnIndex("_id"));				
				_produtor = cursor.getString(cursor.getColumnIndex("_produtor"));

				strPaper.append("http://www.projecttrace.com.br:8096/Insert.aspx?OP=animal&_id=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("_id")));
				strPaper.append("&_projeto=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("_projeto")));
				strPaper.append("&_grupo=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("_grupo")));
				strPaper.append("&_propriedade=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("_produtor")));
				strPaper.append("&data_coleta=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("data")));
				strPaper.append("&data_replicacao=");
				strPaper.append(data);			
				strPaper.append("&brinco=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("brinco")));
				strPaper.append("&nome=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("nome")));
				strPaper.append("&numero_partos=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("numero_partos")));
				strPaper.append("&sexo=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("sexo")));
				strPaper.append("&origem_entrada=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("origem_entrada")));
				strPaper.append("&raca=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("raca")));
				strPaper.append("&categoria=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("categoria")));
				strPaper.append("&pelagem=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("pelagem")));
				strPaper.append("&entrada_plantel=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("entrada_plantel")));
				strPaper.append("&ultimo_parto=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("ultimo_parto")));
				strPaper.append("&data_nascimento=");
				strPaper.append(cursor.getString(cursor.getColumnIndex("data_nascimento")));
				//consultor = (cursor.getString(cursor.getColumnIndex("consultor")));
				strPaper.append("&consultor=");
				strPaper.append(consultor_ibs);	
				
				//String query = URLEncoder.encode(strPaper.toString(),"utf-8");				
				//String x = "http://www.projecttrace.com.br:8096/Insert.aspx?OP=animal&_id=" + query;
				
				String x = strPaper.toString().replace(" ","%20");
				strPaper.toString().replace(" ","%20" );

				String converter = convertUTF8toISO(x);
				URL url = new URL(converter);
				HttpURLConnection http = (HttpURLConnection) url.openConnection();
				InputStreamReader ips = new InputStreamReader(http.getInputStream());
				BufferedReader line = new BufferedReader(ips);				
				
				//################################################ VALIDAR RESULTADO WEB#############################
				/*HttpClient cliente = new DefaultHttpClient();
				HttpPost post = new HttpPost(x);
				HttpResponse resp = cliente.execute(post);
				HttpEntity ent = resp.getEntity();				
				String text = EntityUtils.toString(ent);			
				String j = text;*/
				//####################################################################################################
				
				String o = line.readLine();	
				
				if(o.equals("inserido")){
					
					//enviaEmail("Animal",String.valueOf(_id), _produtor , "Animal");

					totalReplicado++;
					db.execSQL("UPDATE ANIMAL SET ENVIADO = 'SIM'  WHERE _ID = "+ _id +"");

				}
				else
					{						
					enviaEmail("Novos animais: ",String.valueOf(_id), consultor,"<br>"+o);
					}
				
			}
			
			if(totalDB != 0){

			Notification nt = null;
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			/*if (totalDB == totalReplicado) {
				nt = new Notification(R.drawable.ibs,
						"Status Replicação Animal: ",
						System.currentTimeMillis());
				nt.flags = Notification.FLAG_AUTO_CANCEL;
				PendingIntent p = PendingIntent
						.getActivity(this, 2,
								new Intent(this.getApplicationContext(),
										main.class), 2);
				nt.setLatestEventInfo(this, "Status Replicaçaão Animal: ",
						"Replicação feita com sucesso, total: "
								+ totalReplicado, p);

				nt.vibrate = new long[] { 100, 2000, 1000, 2000 };
				notificationManager.notify((int) Math.round(Math.random()), nt);
			}*/
			}
			db.close();

		}
		
		catch(ClientProtocolException e){
			
			enviaEmail("Novos Animais: Erro de sistema ao tentar enviar dados", " 404 ", " Exception Android PEC: ","<br> "+e.getMessage()+"");
			db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'NAO' WHERE _ID = " + _id + "");

		}
		
		catch(IOException e){
			
			enviaEmail("Novos Animais: Erro de sistema ao tentar enviar dados", " 404 ", " Exception Android PEC: ","<strong  style='color:red;'><b><h2><br>SERVIDOR DESLIGADO : </h2></b> <br> "+e.getMessage()+"");
			db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'NAO' WHERE _ID = " + _id + "");

		}
		
		catch(RuntimeException e){
			
			db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'NAO' WHERE _ID = " + _id + "");
			enviaEmail("Novos Animais: Erro de sistema ao tentar enviar dados", " 404 ", " Exception Android PEC: ","<br> "+e.getMessage()+"");
		}


		catch (Exception e) {

			db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'NAO' WHERE _ID = " + _id + "");
			enviaEmail("Novos Animais: Erro de sistema ao tentar enviar dados", " 404 ", " Exception Android PEC: ","<br> "+e.getMessage()+"");
		}

		startActivity(new Intent(getBaseContext(), main.class));

		return null;

	}

	public void MessageBox(String msg) {
		AlertDialog.Builder informa = new AlertDialog.Builder(getBaseContext());
		informa.setTitle("Alerta!").setMessage(msg);
		informa.setNeutralButton("Fechar", null).show();
	}
	
	public void enviaEmail(String form, String numero, String consultor, String dado){
		
   GMailSender send = new GMailSender("mobile@biosistemico.com.br","ibs@6022");
		try {				
				send.sendMail(form + numero + " " + consultor_ibs,consultor + dado,"mobile@biosistemico.com.br","ricardo.fagmer@biosistemico.com.br,concelina@biosistemico.com.br,douglas.souza@biosistemico.com.br",null);
			}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	@Override
	public void onClick(View v) {

	}
		public String RetornaConsultor(){

			String consultor ="";

			SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

			Cursor cursor = db.rawQuery("select nome from login",null);

			if(cursor.moveToFirst()) {
				consultor = cursor.getString(cursor.getColumnIndex("nome"));
			}
			return consultor;
		}


	public static String convertUTF8toISO(String str) {
		String ret = null;
		try {
			ret = new String(str.getBytes("UTF-8"), "ISO-8859-1");
		}
		catch (java.io.UnsupportedEncodingException e) {
			return null;
		}
		return ret;
	}

	class asyntodos extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			dialogo = new ProgressDialog(ReplicarDados.this);
			dialogo.setTitle("Now Loading...");
			dialogo.setMessage("Enviando dados ao IBS...");
			dialogo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialogo.setProgress(0);
			dialogo.setMax(100);
			dialogo.setIndeterminate(false);
			dialogo.setCancelable(false);
			dialogo.show();
		}

		@Override
		protected String doInBackground(String... params) {

			BuildPaperTop();
			BuildCheckList();
			BuildCriatf();
			EnviarFinanceiro();
			//EnviarCheckList();
			//EnviarPaperTop();
			//EnviarAtendimento_01();

			Random aleatorio = new Random();

			if (forms.equals("")) {

				forms = "Muito bem " + mDados.getLogin() + " Todos os dados já foram enviados!";
				Notifica(ReplicarDados.this,"Pec - último envio feito em: "+getDateTime(), forms,aleatorio.nextInt());
			}
			else {

				Notifica(ReplicarDados.this, "Pec -  Último envio feito em: "+getDateTime(), "Resumo de envios: " + forms, aleatorio.nextInt());
			}
		
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			dialogo.dismiss();
			startActivity(new Intent(getBaseContext(), main.class));

		}

	}

}
