package com.pec.biosistemico.pec.checklist;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

//import com.sun.mail.dsn.message_deliverystatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.pec.biosistemico.pec.util.Global;
import com.pec.biosistemico.pec.R;

public class CheckList extends Fragment implements OnClickListener {

	int salvo = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.checkfragment_item_detail, null);

		Button btnSalvar = (Button) view.findViewById(R.id.btnSalvarCheck);

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
		final Spinner ddl2 =  (Spinner) view.findViewById(R.id.ddl2);
		final Spinner ddl3 =  (Spinner) view.findViewById(R.id.ddl3);
		final Spinner ddl4 =  (Spinner) view.findViewById(R.id.ddl4);
		final Spinner ddl5 =  (Spinner) view.findViewById(R.id.ddl5);
		final Spinner ddl6 =  (Spinner) view.findViewById(R.id.ddl6);
		final Spinner ddl7 =  (Spinner) view.findViewById(R.id.ddl7);
		final Spinner ddl8 =  (Spinner) view.findViewById(R.id.ddl8);
		final Spinner ddl9 =  (Spinner) view.findViewById(R.id.ddl9);
		final Spinner ddl10 = (Spinner) view.findViewById(R.id.ddl10);
		final Spinner ddl11 = (Spinner) view.findViewById(R.id.ddl11);
		final Spinner ddl12 = (Spinner) view.findViewById(R.id.ddl12);
		final Spinner ddl13 = (Spinner) view.findViewById(R.id.ddl13);
		final Spinner ddl14 = (Spinner) view.findViewById(R.id.ddl14);
		final Spinner ddl15 = (Spinner) view.findViewById(R.id.ddl15);
		final Spinner ddl16 = (Spinner) view.findViewById(R.id.ddl16);
		final Spinner ddl17 = (Spinner) view.findViewById(R.id.ddl17);
		final Spinner ddl18 = (Spinner) view.findViewById(R.id.ddl18);
		final Spinner ddl19 = (Spinner) view.findViewById(R.id.ddl19);
		final Spinner ddl20 = (Spinner) view.findViewById(R.id.ddl20);
		final Spinner ddl21 = (Spinner) view.findViewById(R.id.ddl21);
		final Spinner ddl22 = (Spinner) view.findViewById(R.id.ddl22);

		ddl1.setAdapter(spinnerArrayAdapter1);
		ddl2.setAdapter(spinnerArrayAdapter1);
		ddl3.setAdapter(spinnerArrayAdapter1);
		ddl4.setAdapter(spinnerArrayAdapter1);
		ddl5.setAdapter(spinnerArrayAdapter1);
		ddl6.setAdapter(spinnerArrayAdapter1);
		ddl7.setAdapter(spinnerArrayAdapter1);
		ddl8.setAdapter(spinnerArrayAdapter1);
		ddl9.setAdapter(spinnerArrayAdapter1);
		ddl10.setAdapter(spinnerArrayAdapter1);
		ddl11.setAdapter(spinnerArrayAdapter1);
		ddl12.setAdapter(spinnerArrayAdapter1);
		ddl13.setAdapter(spinnerArrayAdapter1);
		ddl14.setAdapter(spinnerArrayAdapter1);
		ddl15.setAdapter(spinnerArrayAdapter1);
		ddl16.setAdapter(spinnerArrayAdapter1);
		ddl17.setAdapter(spinnerArrayAdapter1);
		ddl18.setAdapter(spinnerArrayAdapter1);
		ddl19.setAdapter(spinnerArrayAdapter1);
		ddl20.setAdapter(spinnerArrayAdapter1);
		ddl21.setAdapter(spinnerArrayAdapter1);
		ddl22.setAdapter(spinnerArrayAdapter1);

		btnSalvar.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				SQLiteDatabase db = getActivity().openOrCreateDatabase(
						"IbsPEC.db", Context.MODE_PRIVATE, null);

				try {
					if(salvo == 1){
						MessageBox("CheckList já respondido!");
					}
					
					if(salvo !=1){
					
					int i  = 0;
					if (ddl1.getSelectedItem().toString().equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl2.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl3.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl4.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl5.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl6.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl7.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl8.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl9.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl10.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl11.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl12.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl13.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl14.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl15.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl16.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl17.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl18.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl19.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl20.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl21.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					if (ddl22.getSelectedItem().toString()
							.equals("--SELECIONE UMA RESPOSTA--")) {
						i = 1;
					}
					
					if(i == 1){
						MessageBox("VOCÊ DEVER SELECIONAR SIM, NAO OU PARCIAL PARA TODAS AS QUESTÕES!");
					}
					
					else {
						
						Date data = new Date(System.currentTimeMillis());

						Global mDados = Global.getInstance();
						int cliente = mDados.getCliente();
						int projeto = mDados.getProjeto();
						String grupo = mDados.getUsuario();
						int produtor = mDados.getIDProdutor();

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
						Sql.append("controle3,");
						Sql.append("manejoN1,");
						Sql.append("manejoN2,");
						Sql.append("manejoN3,");
						Sql.append("manejoN4,");
						Sql.append("manejoN5,");
						Sql.append("manejoN6,");
						Sql.append("sanidade1,");
						Sql.append("sanidade2,");
						Sql.append("sanidade3,");
						Sql.append("sanidade4,");
						Sql.append("sanidade5,");
						Sql.append("manejoR1,");
						Sql.append("manejoR2,");
						Sql.append("manejoR3,");
						Sql.append("manejoR4,");
						Sql.append("qualidade1,");
						Sql.append("qualidade2,");
						Sql.append("qualidade3,");
						Sql.append("qualidade4) VALUES ( ");

						Sql.append("'NAO',");
						Sql.append("" + data.toString() + ",");
						Sql.append("" + cliente + ",");
						Sql.append("" + projeto + ",");
						Sql.append("" + grupo + ",");
						Sql.append("" + produtor + ",");
						Sql.append("'" + ddl1.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl2.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl3.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl4.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl5.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl6.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl7.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl8.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl9.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl10.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl11.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl12.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl13.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl14.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl15.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl16.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl17.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl18.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl19.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl20.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl21.getSelectedItem().toString()
								+ "',");
						Sql.append("'" + ddl22.getSelectedItem().toString()
								+ "')");

						String sql = Sql.toString();
						db.execSQL(sql);
						mDados.setPosition(1);
						
						salvo = 1;

						MessageBox("CheckList Salvo! Fechamento Liberado para preenchimento.");
					}
					
					}
				} catch (Exception ex) {
					MessageBox("ERRO CONTATE O TI IBS: " + ex.getMessage());
				}
			}
		});

		return view;

	}

	public void MessageBox(String msg) {
		AlertDialog.Builder informa = new AlertDialog.Builder(getActivity());
		informa.setTitle("Alerta!").setMessage(msg);
		informa.setNeutralButton("Fechar", null).show();
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
