package com.pec.biosistemico.pec.cloud;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.domain.Retorno;
import com.pec.biosistemico.pec.main;
import com.pec.biosistemico.pec.model.CheckList;
import com.pec.biosistemico.pec.model.Criatf;
import com.pec.biosistemico.pec.model.PaperTop;
import com.pec.biosistemico.pec.paperTop.financeiro.LancamentoFinanceiro;
import com.pec.biosistemico.pec.paperTop.financeiro.LancamentoFinanceiroItem;
import com.pec.biosistemico.pec.paperTop.financeiro.Produto;
import com.pec.biosistemico.pec.rest.ApiClientSend;
import com.pec.biosistemico.pec.rest.ApiInterface;
import com.pec.biosistemico.pec.util.Global;
import com.pec.biosistemico.pec.util.Ping;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jibble.simpleftp.SimpleFTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;

public class Service extends android.app.Service {

	public List<Worker> threads = new ArrayList<Worker>();
	public boolean ativo = false;
	private String forms = "";
	private SQLiteDatabase db;
	private  int _id;
	private String consultor_ibs = "";
	private Global mDados = Global.getInstance();
	private String line = "",versao = "", numero = "";
	private String versionName;
	private String pct;
	private ApiInterface apiService;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();

		db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

		Global c = Global.getInstance();
		consultor_ibs = c.getLogin();

		apiService = ApiClientSend.getClient().create(ApiInterface.class);
		
		Log.i("Script", "onCreate()");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){

		db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
		
		if(ativo == false)
		{
			Worker w = new Worker(startId);
			w.start();
			threads.add(w);
			
			ativo = true;
		}
				
		return(super.onStartCommand(intent, flags, startId));
	}	
	
	class Worker extends Thread{

		public int count = 0;
		public int startId;
		public boolean ativo = true;
		
		public Worker(int startId){
			this.startId = startId;
		}
		
		public void run(){

			Random aleatorio = new Random();

			ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			
				if (conn.getNetworkInfo(1).isConnected() == true) 
				{
					Ping ping = new Ping();
					List<String> commands = new ArrayList<String>();
					commands.add("ping");
					commands.add("-c");
					commands.add("1");
					commands.add("www.google.com.br");
				
					try {
						if (ping.doCommand(commands) != 0)
						{
							while (ativo && count <1)
							{
								Thread.sleep(1000);
								BuildPaperTop();
								BuildCheckList();
								BuildCriatf();
								EnviarFinanceiro();

								if(mDados.getAtualizacao() == false)
								{
									getInstrucao();
									NewVersion();
									mDados.setAtualizacao(true);
									startService();
								}
								count++;
							}
						}
						
					} catch (IOException e) 
					{
						e.printStackTrace();
					} 
					
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}

					Log.i("Script", "COUNT: "+count);
			}
			//stopSelf(startId);

			if (forms.equals("")) {

				forms = "Muito bem " + mDados.getLogin() + " Todos os dados ja foram enviados!";
				Notifica(Service.this,"Pec - Ultimo envio feito em: "+getDateTime(), forms,aleatorio.nextInt());
			}
			else {

				Notifica(Service.this, "Pec - Ultimo envio feito em: "+getDateTime(), "Resumo de envios: " + forms, aleatorio.nextInt());
			}
		}
	}

	public String VerficaVersao() throws JSONException {

		HttpClient client = new DefaultHttpClient();

		HttpGet httpGet = new HttpGet("http://179.184.44.116:8086/VersaoApp.aspx?app=Pec");
		//HttpGet httpGet = new HttpGet("http://192.168.25.100:8086/VersaoApp.aspx?app=Pec");
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {

				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));

				if ((line = reader.readLine()) != null)
				{
					JSONArray jArray = new JSONArray(line);

					line = line.replace(":{",":[{");
					line = line.replace("}}","}]}");

					for (int j = 0; j < jArray.length(); j++)
					{
						JSONObject jReal = jArray.getJSONObject(j);

						versao = jReal.getString("nome");
						numero = jReal.getString("numero");
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


		return versao;
	}

	public void installAPP() {

		String filePath = Environment.getExternalStorageDirectory().toString() + "/DCIM/pec.apk";
		Process installProcess = null;
		int installResult = -1337;

		try {
			//installProcess = Runtime.getRuntime().exec("su -c pm install -r " + filePath);
			File file = new File(filePath);

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} catch (Exception e) {
			// Handle IOException the way you like.
		}

		if (installProcess != null) {
			try {
				installResult = installProcess.waitFor();
			} catch (InterruptedException e) {
				// Handle InterruptedException the way you like.
			}

			if (installResult == 0) {
				// Success!
			} else {
				// Failure. :-/
			}
		} else {
			// Failure 2. :-(
		}

	}

	public String getVersionName() {

		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			pct = getPackageManager().getPackageInfo(getPackageName(), 0).packageName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		return  versionName;
	}

	private void addShortcut() {
		//Adding shortcut for MainActivity
		//on Home screen
		Intent shortcutIntent = new Intent(getApplicationContext(), main.class);

		shortcutIntent.setAction(Intent.ACTION_MAIN);

		Intent addIntent = new Intent();
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Ibs - Pec");
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher));

		addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		addIntent.putExtra("duplicate", false);  //may it's already there so don't duplicate
		getApplicationContext().sendBroadcast(addIntent);
	}

	public String NewVersion() throws JSONException {

		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection connection = null;
		String versao_corrente = VerficaVersao();

		if (!versao_corrente.equals("")) {

			if (versao_corrente.equals(getVersionName())) {
				// Toast.makeText(main.this, "?ltima vers?o j? instalada!", Toast.LENGTH_LONG).show();
			} else {
				try {

					// URL url = new URL("http://192.168.25.100:8086/download/pec.apk");
					URL url = new URL("http://179.184.44.116:8086/download/pec.apk");

					connection = (HttpURLConnection) url.openConnection();
					connection.connect();

					// expect HTTP 200 OK, so we don't mistakenly save error report
					// instead of the file
					if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
						return "Server returned HTTP " + connection.getResponseCode()
								+ " " + connection.getResponseMessage();
					}

					// this will be useful to display download percentage
					// might be -1: server did not report the length
					int fileLength = connection.getContentLength();

					// download the file
					input = connection.getInputStream();
					output = new FileOutputStream("/sdcard/DCIM/pec.apk");

					byte data[] = new byte[4096];
					long total = 0;
					int count;
					while ((count = input.read(data)) != -1) {
						// allow canceling with back button
						if (false) {
							input.close();
							return null;
						}
						total += count;
						// publishing the progress....
						if (fileLength > 0) // only if total length is known
							//   publishProgress((int) (total * 100 / fileLength));
							output.write(data, 0, count);
					}
				} catch (Exception e) {
					return e.toString();
				} finally {
					try {
						if (output != null)
							output.close();
						if (input != null)
							input.close();
					} catch (IOException ignored) {
					}

					if (connection != null)
						connection.disconnect();
				}

				installAPP();
				addShortcut();
			}
		}


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

				Notifica(Service.this,"Erro" + resposta, "",44);

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

				Notifica(Service.this,"Erro" + resposta, "",44);

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
				Notifica(Service.this, "Erro:" + resposta, "", 44);
			}
		}

		return retorno[0];
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		
		for(int i = 0, tam = threads.size(); i < tam; i++){
			threads.get(i).ativo = false;
		}

	/*	Intent alarmIntent = new Intent(Service.this, BroadReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(Service.this, 0, alarmIntent, 0);

		AlarmManager alarme = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarme.cancel(pendingIntent);
		*/

	}

	@SuppressLint("SimpleDateFormat")
	private String getDateTime() {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);

	}

	public void enviaEmail(String form, String numero, String consultor, String dado){
	
	  GMailSender send = new GMailSender("mobile@biosistemico.com.br","ibs@6022");
			try {				
					send.sendMail(form + numero + " " + consultor_ibs,consultor + dado,"mobile@biosistemico.com.br","ricardo.fagmer@biosistemico.com.br,douglas.souza@biosistemico.com.br",null);
				}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}

	public void copyDataBase() {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date = new java.util.Date();
		String data_corrente = dateFormat.format(date);

		Log.i("info", "in copy data base at finally");
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "/data/com.pec.biosistemico.pec/databases/IbsPEC.db";
				String backupDBPath = "IbsPEC.db";
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);
				if (currentDB.exists()) {
					@SuppressWarnings("resource")
					FileChannel src = new FileInputStream(currentDB)
							.getChannel();
					@SuppressWarnings("resource")
					FileChannel dst = new FileOutputStream(backupDB)
							.getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
				}
			}
		} catch (Exception e) {
			Log.i("info", "in copy of bata base 10 ");
		}
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

				//Enviar os dados para o WS para salvar o lan?amento da propriedade
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
					Notifica(Service.this,"Erro","Tente novamente",3);
				}
			}
		}

		catch (Exception ex){

			ex.getMessage();
		}

		return true;
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

	public void getInstrucao() throws JSONException {

		String sql= "", result = "";

		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet;

		httpGet = new HttpGet("http://179.184.44.116:8086/Instrucao.aspx?UN=PEC&&ID=FAGMER");


	//	 httpGet = new HttpGet("http://192.168.25.100:8086/Instrucao/Instrucao.aspx?UN=PEC&&ID=FAGMER");

		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {

				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));

				if ((line = reader.readLine()) != null)
				{
					JSONArray jArray = new JSONArray(line);

					line = line.replace(":{",":[{");
					line = line.replace("}}","}]}");

					for (int j = 0; j < jArray.length(); j++)
					{
						JSONObject jReal = jArray.getJSONObject(j);

						sql = jReal.getString("instrucao");
					}

					if (!sql.equals("")){
						db.execSQL(sql);
						result = "Comando executado com sucesso! <br/><i>"+sql+"";
					}
				}
			}

		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
			result = e.getMessage();
		} catch (IOException e)
		{
			e.printStackTrace();
			result = e.getMessage();
		}
		catch(RuntimeException e){
			result = e.getMessage();
		}
		catch (Exception ex) {
			result = ex.getMessage();
		}
		if(!result.equals("")) {
			enviaEmail("INSTRUÇÃO SQL: <br/>",result, mDados.getConsultor(),result);
		}
	}

	public void startService() {

		try {

			ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

			if (conn.getNetworkInfo(1).isConnected() == true) {


				boolean alarmeAtivo = (PendingIntent.getBroadcast(this,0,new Intent("REPLICACAO_IBS"),PendingIntent.FLAG_NO_CREATE) == null);

				if(alarmeAtivo) {

					uploadDB();

					java.util.Date Data = new java.util.Date(System.currentTimeMillis());

					//Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "IbsPEC.db"));
					File file = new File(Environment.getExternalStorageDirectory(),"IbsPEC.db");

					GMailSender send = new GMailSender("mobile@biosistemico.com.br","ibs@6022");
					send.sendMail ("Backup Automático PEC" ,RetornaConsultor().toString() + "<br/>" + Data ,"mobile@biosistemico.com.br", "mobile.fagmer@biosistemico.com.br", file);


					//Intent alarmIntent = new Intent(Service.this, BroadReceiver.class);

					Intent alarmIntent = new Intent("REPLICACAO_IBS");
					PendingIntent pendingIntent = PendingIntent.getBroadcast(Service.this, 0, alarmIntent, 0);

					AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(System.currentTimeMillis());
					calendar.set(Calendar.HOUR_OF_DAY, 07);
					calendar.set(Calendar.MINUTE, 00);

					manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),24 * 60 * 60 * 1000, pendingIntent);
				}
			}
		}
		catch (Exception ex){

			ex.getMessage();
		}
	}

	public String RetornaConsultor() {

		String result = "";

		try {
			Cursor cursor = db.rawQuery("SELECT nome, usuario FROM login", null);
			cursor.moveToFirst();
			result = cursor.getString(cursor.getColumnIndex("nome"));
		} catch (Exception e)
		{
		}

		return result;
	}

	public static void copyDataBase(String name) {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String data_corrente =  dateFormat.format(date);

		Log.i("Info", "in copy data base at finally");
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();
			if (sd.canWrite()) {
				String currentDBPath = "/data/com.pec.biosistemico.pec/databases/IbsPEC.db";
				String backupDBPath = name;
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);
				if (currentDB.exists()) {
					@SuppressWarnings("resource")
					FileChannel src = new FileInputStream(currentDB).getChannel();
					@SuppressWarnings("resource")
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
				}
			}
		} catch (Exception e) {
			Log.i("info", "in copy of bata base 10 ");
		}
	}

	public void uploadDB() {

		try {

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();
			String data_corrente =  dateFormat.format(date);

			String fileName = RetornaConsultor().replace(" ","_") + "_" + data_corrente.replace("/","-").replace(" ","") + ".db";

			copyDataBase(fileName);

			File sd = Environment.getExternalStorageDirectory();

			SimpleFTP ftp = new SimpleFTP();

			ftp.connect("ftp.projecttrace.com.br", 21, "ricardofagmer", "slconn/2101");
			ftp.bin();
			ftp.cwd("bkp_mobile/pec");

			// String currentDBPath = "/data/ibs.com.br.biodiesel/databases/" + fileName;
			// Upload some files.
			ftp.stor(new File(sd + "/" + fileName));
			//ftp.stor(new File("comicbot-latest.png"));
			//You can also upload from an InputStream, e.g.
			//ftp.stor(new FileInputStream(new File("test.png")), "test.png");

			ftp.disconnect();
		}
		catch (IOException e) {

			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

}
