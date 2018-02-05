package com.pec.biosistemico.pec.paperTop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;

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

import com.pec.biosistemico.pec.util.Global;
import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.main;

//com.google.gson.Gson;

public class FormCargaPaperTop extends Activity {
	JSONObject json = null;
	private ProgressDialog dialogo;
	private String[] carregarDados;
	private ProgressDialog barProgressDialog;
	String input = null;
	String Grupo;
	int Projeto;
	String produtor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.service);

		Global mDados = Global.getInstance();
		Grupo = mDados.getUsuario();
		Projeto = mDados.getProjeto();
					
		new asyntodos().execute();

		try {
			JSONObject json = new JSONObject(input);
			// Log.i(FormCargaPaperTop.class.getName(), jsonObject.toString());
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String readBugzilla() throws JSONException {

		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"http://www.projecttrace.com.br/projectservice/papertop/listaJson?idGrupo="	+ Grupo + "&idProp="+produtor+"");
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
			CallMain();

		} catch (IOException e)
		{
			e.printStackTrace();
			CallMain();
		}			
		catch(RuntimeException e){

			CallMain();
			
		}

		catch (Exception ex) {
			CallMain();
		}
		return null;
	}
	
	public String Touros() throws JSONException {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
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
			CallMain();
		} catch (IOException e) {
			e.printStackTrace();
			CallMain();
		}
		catch (RuntimeException x){
			x.printStackTrace();
			CallMain();
		}
		return builder.toString();
	}
	
	public String IA() throws JSONException {
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
			CallMain();
		} catch (IOException e) {
			e.printStackTrace();
			CallMain();
		}
		return builder.toString();
	}
	
	class asyntodos extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// super.onPreExecute();
			dialogo = new ProgressDialog(FormCargaPaperTop.this);
			dialogo.setTitle("Now Loading...");
			dialogo.setMessage("Carregando PaperTops do Grupo: "+ RetornaNomeGrupo(Integer.parseInt(Grupo)) + "");
			dialogo.setProgressStyle(barProgressDialog.THEME_DEVICE_DEFAULT_DARK);
			dialogo.setProgress(0);
			dialogo.setMax(100);
			dialogo.setIndeterminate(false);
			dialogo.setCancelable(false);
			dialogo.show();
		}

		@Override
		protected String doInBackground(String... params) {
			try {				
				 SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db",	Context.MODE_PRIVATE, null);

				 Cursor cursor = db.rawQuery("SELECT * FROM TEMP", null);
					String id = "";
					
					if (cursor != null && cursor.getCount() > 0) {
						if (cursor.moveToFirst()) {		
							do{
								id = cursor.getString((cursor.getColumnIndex("id")));
								
								produtor =  id;
								
								IA();
								Touros();
								//input = readBugzilla();
							
							  }
							while(cursor.moveToNext()== true);
														
						}	
											
					}						
				
			}

			catch (JSONException e) {

				e.printStackTrace();

			}
		
			CallMain();
			return input;

		}

		@Override
		protected void onPostExecute(String result) {
			dialogo.dismiss();

			startActivity(new Intent(getBaseContext(), main.class));

		}

	}
	
	public void CallMain(){

		Intent intent = new Intent(FormCargaPaperTop.this,main.class);
		startActivity(intent);
	}

	public String RetornaNomeGrupo(int codigo) {
		SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
		Cursor cursor = db.rawQuery("SELECT NOME FROM GRUPO WHERE ID = "	+ codigo + "", null);
		cursor.moveToFirst();

		String x = cursor.getString(cursor.getColumnIndex("nome"));

		return x;
	}

}
