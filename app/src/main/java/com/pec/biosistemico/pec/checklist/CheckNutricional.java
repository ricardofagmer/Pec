package com.pec.biosistemico.pec.checklist;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.pec.biosistemico.pec.util.Global;
import com.pec.biosistemico.pec.R;

@SuppressLint("ValidFragment")
public class CheckNutricional extends DialogFragment implements DialogInterface.OnClickListener{
	
	private int numStyle;
	private int numTheme;	
	private TextView lblMsg;
	
	public CheckNutricional(int numStyle,int numTheme){
		this.numStyle = numStyle;
		this.numTheme = numTheme;				
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
		
	int style;
	int theme;
	
	switch(numStyle){
	case 1 : style = DialogFragment.STYLE_NO_FRAME;break;	
	default: style = DialogFragment.STYLE_NORMAL;break; 
	
	}
	
	switch(numTheme){
	case 1 : theme = android.R.style.Theme_DeviceDefault_Light_Dialog;break;	
	default: theme = android.R.style.Theme_Holo_Light_DarkActionBar;break;
	}
	
	setStyle(style, theme);
	setCancelable(false);		
	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
	super.onCreateView(inflater,container,savedInstanceState);
	
	
	View view = inflater.inflate(R.layout.sanidade, container);
	Button btnSalvar = (Button) view.findViewById(R.id.btnSalvaSanidade);
    Button btnCancelar = (Button)view.findViewById(R.id.btnCanSanidade);		
	lblMsg = (TextView)view.findViewById(R.id.lblMsg);
		
	List<String> OP = new ArrayList<String>();
	OP.add("--SELECIONE UMA RESPOSTA--");
	OP.add("SIM");
	OP.add("NAO");
	OP.add("PARCIAL");

	// PASSANDO O ARRAYLIST LITROS
	ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, OP);
	ArrayAdapter<String> spinnerArrayAdapter1 = arrayAdapter1;
	spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	
	final Spinner ddl10 =  (Spinner) view.findViewById(R.id.ddl10);
	ddl10.setAdapter(spinnerArrayAdapter1);
	
	final Spinner ddl11 =  (Spinner) view.findViewById(R.id.ddl11);
	ddl11.setAdapter(spinnerArrayAdapter1);
	
	final Spinner ddl12 =  (Spinner) view.findViewById(R.id.ddl12);
	ddl12.setAdapter(spinnerArrayAdapter1);
	
	final Spinner ddl13 =  (Spinner) view.findViewById(R.id.ddl13);
	ddl13.setAdapter(spinnerArrayAdapter1);
	
	final Spinner ddl14 =  (Spinner) view.findViewById(R.id.ddl14);
	ddl14.setAdapter(spinnerArrayAdapter1);
	
	
	btnSalvar.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			
		
			
			if (ddl10.getSelectedItem().toString().equals("--SELECIONE UMA RESPOSTA--") ||
			    ddl11.getSelectedItem().toString().equals("--SELECIONE UMA RESPOSTA--") ||
			    ddl12.getSelectedItem().toString().equals("--SELECIONE UMA RESPOSTA--") ||
			    ddl13.getSelectedItem().toString().equals("--SELECIONE UMA RESPOSTA--") ||
			    ddl14.getSelectedItem().toString().equals("--SELECIONE UMA RESPOSTA--") )
		{
			   
					lblMsg.setVisibility(View.VISIBLE);
				
			    }
			else{
				
				try{
			
			SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
			
			Date data = new Date(System.currentTimeMillis());

			Global mDados = Global.getInstance();
			int x = mDados.getLastID();
			
			StringBuilder sql = new StringBuilder();
			
			sql.append("UPDATE CHECKLIST SET sanidade1 = ");
			sql.append("'" + ddl10.getSelectedItem().toString()+ "',");
			sql.append("sanidade2 = ");
			sql.append("'" + ddl11.getSelectedItem().toString()+ "',");
			sql.append("sanidade3 = ");
			sql.append("'" + ddl12.getSelectedItem().toString()+ "',");
			sql.append("sanidade4 = ");
			sql.append("'" + ddl13.getSelectedItem().toString()+ "',");
			sql.append("sanidade5 = ");
			sql.append("'" + ddl14.getSelectedItem().toString()+ "'");				
			sql.append("WHERE _ID = "+x+"");
								
			db.execSQL(sql.toString());
			
			Button btnControl = (Button)getActivity().findViewById(R.id.btnIII);
			btnControl.setTextColor(getResources().getColor(R.color.ORANGE));			
			dismiss();
			
			
			
				}
				catch(Exception ex)
				{
					lblMsg.setVisibility(View.VISIBLE);
					lblMsg.setText(ex.getMessage());
				}
			
			}
			
		}
	});

	
	
	btnCancelar.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();		
			
		}
	});
	
	return(view);
	
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}
	

	@Override
	public void onCancel(DialogInterface dialog){
		super.onCancel(dialog);
	}
	
	
	
	@Override
	public void onDestroyView(){
		super.onDestroyView();
		
	}
	
	@Override
	public void onDismiss(DialogInterface dialog){
		super.onDismiss(dialog);		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);		
	}
	
	@Override
	public void onStart(){
		super.onStart();		
	}
	
	@Override
	public void onStop(){
		super.onStop();		
	}


	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	

}
