package com.pec.biosistemico.pec.checklist;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
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
public class CheckControles extends DialogFragment implements DialogInterface.OnClickListener{
	
	private int numStyle;
	private int numTheme;
	private int day;
	private int month;
	private int year;	
	private Calendar cal;
	private TextView lblMsg;
	
	public CheckControles(int numStyle, int numTheme){
		this.numStyle = numStyle;
		this.numTheme = numTheme;		
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	
	
	
	cal = Calendar.getInstance();
	day = cal.get(Calendar.DAY_OF_MONTH);
	month = cal.get(Calendar.MONTH);
	year = cal.get(Calendar.YEAR);
		
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
	
	
	View view = inflater.inflate(R.layout.controles, container);
	Button btnSalvar = (Button) view.findViewById(R.id.btnSalvaControl);
    Button btnCancelar = (Button)view.findViewById(R.id.btnCanControl);		
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
	
	final Spinner ddl1 =  (Spinner) view.findViewById(R.id.ddl1);
	ddl1.setAdapter(spinnerArrayAdapter1);
	
	final Spinner ddl2 =  (Spinner) view.findViewById(R.id.ddl2);
	ddl2.setAdapter(spinnerArrayAdapter1);
	
	final Spinner ddl3 =  (Spinner) view.findViewById(R.id.ddl3);
	ddl3.setAdapter(spinnerArrayAdapter1);

	
	btnSalvar.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			
		
			
			if (ddl1.getSelectedItem().toString().equals("--SELECIONE UMA RESPOSTA--") ||
			    ddl2.getSelectedItem().toString().equals("--SELECIONE UMA RESPOSTA--") ||
			    ddl3.getSelectedItem().toString().equals("--SELECIONE UMA RESPOSTA--")){
			   
					lblMsg.setVisibility(View.VISIBLE);
				
			    }
			else{
				
				try{
			
			SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
			
			Date data = new Date(System.currentTimeMillis());
			SimpleDateFormat formatBR = new SimpleDateFormat("dd/MM/yyyy");
			String dFormated = formatBR.format(data);

			Global mDados = Global.getInstance();
			int cliente = mDados.getCliente();
			int projeto = mDados.getProjeto();
			String grupo = mDados.getUsuario();
			int produtor = mDados.getProdutor();
			
			StringBuilder Sql = new StringBuilder();
			Sql.append("INSERT INTO checklist (");
			Sql.append("enviado,");
			Sql.append("data,");
			Sql.append("_cliente,");
			Sql.append("_projeto,");
			Sql.append("_grupo,");
			Sql.append("_produtor,");
			Sql.append("controle1,");						
			Sql.append("controle2,");
			Sql.append("controle3) VALUES (");
			
			Sql.append("'NAO',");
			Sql.append("'" + dFormated.toString() + "',");
			Sql.append("" + cliente + ",");
			Sql.append("" + projeto + ",");
			Sql.append("" + grupo   + ",");
			Sql.append("" + produtor+ ",");
			Sql.append("'" + ddl1.getSelectedItem().toString()+ "',");
			Sql.append("'" + ddl2.getSelectedItem().toString()+ "',");
			Sql.append("'" + ddl3.getSelectedItem().toString()+ "')");
			
			db.execSQL(Sql.toString());
			
			Button btnControl = (Button)getActivity().findViewById(R.id.btnI);
			btnControl.setTextColor(getResources().getColor(R.color.ORANGE));	
			RetornaID();
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
	
		
	
	public void RetornaID() {
		
		try{
			SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
			
			Cursor cursor = db.rawQuery("SELECT _id, id  FROM CHECKLIST  order by _id desc limit 1", null);
			cursor.moveToFirst();

			int x = cursor.getInt(cursor.getColumnIndex("_id"));
			Global b = null;
			b = b.getInstance();
			b.setLastID(x);
			b.setRespondido(1);
			 
		}
		
		
	
		catch(Exception ex){
			 	
		  }
		
			
			
		}
	
	

}
