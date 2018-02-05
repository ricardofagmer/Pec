package com.pec.biosistemico.pec.paperTop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.dao.PaperTopDAO;
import com.pec.biosistemico.pec.domain.CargaDados;
import com.pec.biosistemico.pec.util.Global;


public class SelectProducers extends Activity {

	private SQLiteDatabase db;
	private Button btnAlternativo;
	private Global mDados = Global.getInstance();
	private PaperTopDAO paperTopDAO;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_producers);

		db =  openOrCreateDatabase("IbsPEC.db",	Context.MODE_PRIVATE, null);
		paperTopDAO = new PaperTopDAO(this);
		paperTopDAO.open();


		Global global = Global.getInstance();
		String grupo = global.getUsuario();

		ListView lstProducers = (ListView) findViewById(R.id.lstProducers);
		final ListView lstResult = (ListView) findViewById(R.id.lstResult);
		final Button btnDownload = (Button)findViewById(R.id.btnDow);
		btnAlternativo = (Button)findViewById(R.id.btnAlternativo);

		Cursor cursor1 = db.rawQuery("SELECT _ID, NOME FROM PRODUTOR WHERE _GRUPO = " + grupo + "",	null);

		String[] from = { "_id", "nome" };

		int[] to = { R.id.lbl_id, R.id.lblNome };

		@SuppressWarnings("deprecation")
		SimpleCursorAdapter ad1 = new SimpleCursorAdapter(this,	R.layout.producer_list, cursor1, from, to);

		lstProducers.setAdapter(ad1);

		lstProducers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,long _id) {

				Cursor cursor = db.rawQuery("SELECT ID, NOME FROM PRODUTOR WHERE _ID = "	+ _id + "", null);
				String nome = "";
				String id = "";

				if (cursor != null && cursor.getCount() > 0) {

					if (cursor.moveToFirst()) {		
						
							id = cursor.getString(cursor.getColumnIndex("id"));
							nome = cursor.getString(cursor.getColumnIndex("nome"));

						if(VerficaSeTem(Integer.parseInt(id)) > 0)
						{
							Toast.makeText(SelectProducers.this,"Produtor já inserido!",Toast.LENGTH_LONG).show();

						}else {

							db.execSQL("INSERT INTO TEMP (ID, NOME) VALUES (" + id + ",'" + nome + "')");
						}
													
					}					
					
				}
				
				RefreshList();
			
			}			
			
		});
		
		
		lstResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,long id) {
				
				db.execSQL("DELETE FROM TEMP WHERE _ID = "+ id +"");
				RefreshList();
				
			}
		});

		btnAlternativo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				db.execSQL("DELETE  FROM PAPERPORT WHERE ENVIADO = 'SIM'");
				db.execSQL("DELETE FROM CRIATF WHERE ENVIADO = 'SIM'");

				mDados.setPaperTop(1);

				CallJason();

			}
		});
		
		
		btnDownload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			//db.delete("paperPort", null, null);
				db.execSQL("DELETE  FROM PAPERPORT WHERE ENVIADO = 'SIM'");
				//db.execSQL("DELETE FROM CRIATF WHERE ENVIADO IS NULL OR ENVIADO = 'SIM'");
				db.execSQL("DELETE FROM CRIATF WHERE ENVIADO = 'SIM'");

				mDados.setPaperTop(0);
				CallJason();

			}
		});
	}

	public int VerficaSeTem(int id){

		int existe = 0;

		Cursor cursor = db.rawQuery("SELECT COUNT(*) as total FROM TEMP  WHERE ID = "+id+"", null);

		if(cursor.moveToFirst())
		{
			existe = cursor.getInt(cursor.getColumnIndex("total"));
		}


		return existe;
	}
	
	public void RefreshList(){
		
		SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db",	Context.MODE_PRIVATE, null);
		
		final ListView lstResult = (ListView) findViewById(R.id.lstResult);

		
		Cursor cursor2 = db.rawQuery("SELECT * FROM TEMP", null);
		String[] from1 = { "id", "nome" };
		int[] to1 = { R.id.lbl_id, R.id.lblNome };
		
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter ad2 = new SimpleCursorAdapter(this,	R.layout.producer_list, cursor2, from1, to1);
		lstResult.setAdapter(ad2);
		
	}
	
	public void MessageBox(String msg) {
		AlertDialog.Builder informa = new AlertDialog.Builder(this);
		informa.setTitle("Alerta!").setMessage(msg);
		informa.setNeutralButton("Fechar", null).show();
	}
	
	public void CallJason(){
		
		Intent obtnptodos = new Intent(this,CargaDados.class);
		startActivity(obtnptodos);
		finish();
		
	}

	
	
	
	
	
}
