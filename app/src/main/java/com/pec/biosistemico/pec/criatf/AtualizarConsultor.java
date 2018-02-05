package com.pec.biosistemico.pec.criatf;

import com.pec.biosistemico.pec.util.Global;
import com.pec.biosistemico.pec.R;
import  com.pec.biosistemico.pec.main;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AtualizarConsultor extends Activity{

	SQLiteDatabase db;



	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_consultor);
		
		final Global mDados = Global.getInstance();

		db = openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
		
		//  getActionBar().setHomeButtonEnabled(true);
		//	getActionBar().setDisplayHomeAsUpEnabled(true);
			
		Button btnGravar = (Button)findViewById(R.id.btnGravaID);
		final EditText txtID = (EditText)findViewById(R.id.txtIDConsultor);
		final EditText txtNome = (EditText)findViewById(R.id.txtNomeConsultor);
		final EditText txtCREA = (EditText)findViewById(R.id.txtCREAConsultor);

		Cursor c = db.rawQuery("SELECT * FROM LOGIN", null);
		if(c.moveToFirst()){

			txtCREA.setText(c.getString(c.getColumnIndex("crmv")));
			txtID.setText(c.getString(c.getColumnIndex("usuario")));
			txtNome.setText(c.getString(c.getColumnIndex("nome")));

		}

		btnGravar.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("resource")
			@Override
			public void onClick(View v) {
				
			//	mDados.setUsuario(txtID.getText().toString());
				
				db = openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
				
			    Cursor cursor = db.rawQuery("SELECT count(*)as TOTAL FROM login", null);
				cursor.moveToFirst();
				int count1 = cursor.getInt(cursor.getColumnIndex("TOTAL"));
				
				if(txtID.getText().toString().equals("")){
									
					txtID.setError(ALARM_SERVICE.concat("Informe o Login do Consultor"));
					Toast.makeText(getBaseContext(), "Informe o Login do Consultor", Toast.LENGTH_LONG).show();
				}
				else
					if(txtNome.getText().toString().equals(""))					
					{
						//txtID.setError(ALARM_SERVICE.concat("Informe o nome"));
						Toast.makeText(getBaseContext(), "Informe seu nome", Toast.LENGTH_LONG).show();
					}
					else
						if(txtNome.getText().toString().equals(""))					
						{
							//txtID.setError(ALARM_SERVICE.concat("Informe o CRMV"));
							Toast.makeText(getBaseContext(), "Informe o CRMV", Toast.LENGTH_LONG).show();
						}
				else
					if(count1 > 0)
				{
					db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
					
					//String r = "UPDATE LOGIN SET usuario = '"+txtID.getText().toString()+"'";					
					db.execSQL("UPDATE LOGIN SET usuario  = '"+txtID.getText().toString()+"', crmv = '"+txtCREA.getText().toString()+"', nome = '"+txtNome.getText().toString()+"'");
					
					chamaActivity();
					//finish();
					
					
				}
				
					else
						if(count1 == 0)
					{
						db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
						
						//String x = "INSERT INTO LOGIN (usuario) VALUES ('"+txtID.getText().toString();
						
						db.execSQL("INSERT INTO LOGIN (nome, crmv, usuario) VALUES ('"+txtNome.getText().toString()+"','"+txtCREA.getText().toString()+"', '"+txtID.getText().toString()+"')");
						chamaActivity();
						//finish();
						
					}
				
			}
		});
				
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {			
	
			Intent obtnptodos = new Intent(this, main.class);
			startActivity(obtnptodos);
			//finish();	

		
		return false;
		
	}
	
	public void chamaActivity() {
		Intent obtnptodos = new Intent(this, main.class);
		startActivity(obtnptodos);
	}

}
