package com.pec.biosistemico.pec.fechamento;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.pec.biosistemico.pec.util.Global;
import com.pec.biosistemico.pec.R;

public class FimAtendimento extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.end_atendimento);

		Button btn = (Button) findViewById(R.id.btnFechar);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Global mDados = Global.getInstance();
				mDados.setPosition(0);
				mDados.setRespondido(0);
				mDados.setLastID(0);
				finish();
			}
		});
		
		
		ImageButton btnShare =(ImageButton)findViewById(R.id.btnData);
		btnShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				
				Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "pec.txt"));
				
				String filePath = uri.toString();
				String UTF8 = "utf8";
				int BUFFER_SIZE = 8192;

				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), UTF8),BUFFER_SIZE);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), UTF8),BUFFER_SIZE);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "pec.txt"));
				
				    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
				    sharingIntent.setType("text/plain");
				    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Imprimir");
				    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
				    startActivity(Intent.createChooser(sharingIntent, "Imprimir"));			   
				
			}
		});

	}

	@Override
	public void onClick(View v) {

	}

}