package com.pec.biosistemico.pec.criatf;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.util.Global;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Atendimento_03 extends Fragment implements OnClickListener {

	private Calendar cal;
	private int minute;
	private int hour;
	private int day;
	private int month;
	private int year;
	private int raca;

	Date previsao_parto;
	int projeto = 0;
	String grupo = "";
	int produtor = 0;
	Spinner ddlRaca;
	Spinner ddlVaca;
	Spinner ddlTouro;
	String _id;
	long id_criatf;
	String status;
	String idAnimais;
	int numero_dias;
	SQLiteDatabase db;
	ListView lstAnimals;
	ListView lstDG;

	EditText txtNDias;
	TextView lblAnimal;
	TextView lblIA ;
	TextView lblPrevisao;

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

		Global global = Global.getInstance();
		projeto = global.getProjeto();
		grupo = global.getUsuario();
		produtor = global.getProdutor();

		final View view = inflater.inflate(R.layout.atendimento_03, null);

		txtNDias = (EditText)view.findViewById(R.id.txtNDias);

		lblAnimal = (TextView) view.findViewById(R.id.lblAnimal);
		lblPrevisao = (TextView) view.findViewById(R.id.lblPrevisao);
		lblIA = (TextView) view.findViewById(R.id.lblIA);


		//final Spinner ddlProdutor = (Spinner) view.findViewById(R.id.ddl1);
		final Spinner ddlDG = (Spinner) view.findViewById(R.id.ddlDG);

		final Button btnSalvar = (Button) view
				.findViewById(R.id.btnSalvarPaper);

		lstAnimals = (ListView) view.findViewById(R.id.lstAnimals);
		lstDG = (ListView) view.findViewById(R.id.lstDG);

		Date data = new Date(System.currentTimeMillis());

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String formatted = format.format(data);

		final TextView txtProjeto = (TextView)view.findViewById(R.id.txtProjeto);
		final TextView txtGrupo = (TextView)view.findViewById(R.id.txtGrupo);
		//final TextView txtRef = (TextView)view.findViewById(R.id.txtReferencia);
		final TextView txtProdutor = (TextView)view.findViewById(R.id.txtProdutor);
		final TextView txtDtColeta = (TextView)view.findViewById(R.id.txtReferencia);


		txtProjeto.setText("Projeto: " + RetornaNomeProjeto(projeto));
		txtGrupo.setText("Grupo: " + RetornaNomeGrupo(Integer.parseInt(grupo)));
		txtProdutor.setText("Produtor: " + RetornaProdutor(produtor));

		txtDtColeta.setText("Referência: " + formatted.toString());
		txtDtColeta.setText(formatted.toString());
		txtDtColeta.setEnabled(false);

		Cursor cursor = db.rawQuery(
				"SELECT rowid _id, id, nome FROM produtor WHERE id = "
						+ produtor + "", null);

		String[] from3 = { "nome", "id", "_id" };
		int[] to3 = { R.id.lblNome, R.id.lblId, R.id.lbl_id_FK };
	//	@SuppressWarnings("deprecation")
	//	SimpleCursorAdapter ad = new SimpleCursorAdapter(getActivity(),	R.layout.spinner, cursor, from3, to3);
	//	ddlProdutor.setAdapter(ad);

		List<String> OP1 = new ArrayList<String>();
		OP1.add("--SELECIONE--");
		OP1.add("IA");
		OP1.add("TOURO");
		OP1.add("VAZIA");
		OP1.add("CANCELAR");

		// OP1.add("PROCESSO IATF NAO FINDADO");

		// PASSANDO O ARRAYLIST LITROS
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, OP1);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		ddlDG.setAdapter(spinnerArrayAdapter);

		Cursor cursor1 = db.rawQuery("SELECT _id,id,nome_vaca FROM criatf WHERE _propriedade = "+ produtor + " AND DG = 'SIM'", null);
		String[] from = { "_id", "id", "nome_vaca" };
		int[] to = { R.id.lblID, R.id.lblBrinco, R.id.lblNome };

		SimpleCursorAdapter ad1 = new SimpleCursorAdapter(getActivity(),R.layout.animais_list, cursor1, from, to);
		lstDG.setAdapter(ad1);

		cursor1 = db.rawQuery("SELECT _id,id,nome_vaca FROM criatf WHERE _propriedade = "+ produtor + " AND FEZ_IA = 'SIM' AND DG IS NULL", null);
		String[] from1 = { "_id", "id", "nome_vaca" };
		int[] to1 = { R.id.lblID, R.id.lblBrinco, R.id.lblNome};

		ad1 = new SimpleCursorAdapter(getActivity(), R.layout.animais_list,	cursor1, from1, to1);
		lstAnimals.setAdapter(ad1);

		lstAnimals.setOnItemClickListener(new OnItemClickListener(){

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,long id) {

				SQLiteDatabase db1 = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

				Cursor cursor = db1
						.rawQuery(
								"SELECT id, _id, data_protocolo, nome_vaca,_id_animais,numeroDias,previsao_parto, processo_iatf,id_criatf FROM criatf WHERE _id = "+ id + "", null);
				try {
					if (cursor != null && cursor.getCount() > 0)
					{
						if (cursor.moveToFirst()) {
							do {

								id_criatf = cursor.getLong(cursor.getColumnIndex("id_criatf"));
								lblIA.setText(cursor.getString(cursor.getColumnIndex("data_protocolo")));
								lblAnimal.setText(cursor.getString(cursor.getColumnIndex("nome_vaca")));
								status = cursor.getString(cursor.getColumnIndex("processo_iatf"));
								idAnimais = cursor.getString(cursor.getColumnIndex("_id_animais"));
								numero_dias = 0;
								lblPrevisao.setText(cursor.getString(cursor.getColumnIndex("previsao_parto")));

								txtNDias.setEnabled(true);
								txtNDias.setText("");




							} while (cursor.moveToNext());
						}
					}

					if (status.equals("NAO")) {

						btnSalvar.setText("REATIVAR VACA");
						ddlDG.setEnabled(false);


					} else {

						ddlDG.setSelection(0);
						lblPrevisao.setText("");
						ddlDG.setEnabled(true);
						btnSalvar.setText("DIAGNOSTICAR");
					}

				}

				catch (Exception ex) {
					Toast.makeText(getActivity(), ex.getMessage(),
							Toast.LENGTH_LONG).show();

				}
			}
		});

		lstDG.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,
									long id) {

				SQLiteDatabase db1 = getActivity().openOrCreateDatabase(
						"IbsPEC.db", Context.MODE_PRIVATE, null);

				Cursor cursor = db1.rawQuery("SELECT id, _id, data_protocolo, nome_vaca,previsao_parto,diagnostico, numeroDias FROM criatf WHERE _id = "+ id + "", null);
				try {
					if (cursor != null && cursor.getCount() > 0) {
						if (cursor.moveToFirst()) {
							do {

								_id = cursor.getString(cursor
										.getColumnIndex("_id"));
								lblIA.setText(cursor.getString(cursor
										.getColumnIndex("data_protocolo")));
								lblAnimal.setText(cursor.getString(cursor
										.getColumnIndex("nome_vaca")));
								lblPrevisao.setText(cursor.getString(cursor
										.getColumnIndex("previsao_parto")));
								txtNDias.setText(cursor.getString(cursor.getColumnIndex("numeroDias")));

								numero_dias = cursor.getInt(cursor.getColumnIndex("numeroDias"));


								String o = cursor.getString(cursor.getColumnIndex("diagnostico"));



								if (o.equals("IA")) {

									ddlDG.setSelection(1);
									txtNDias.setEnabled(true);
								}
								if (o.equals("TOURO")) {

									ddlDG.setSelection(2);
									txtNDias.setEnabled(true);
								}
								if (o.equals("VAZIA")) {

									ddlDG.setSelection(3);
									txtNDias.setEnabled(true);
								}
								if (o.equals("CANCELAR")) {

									ddlDG.setSelection(4);
									txtNDias.setEnabled(true);
								}


							} while (cursor.moveToNext());
						}
					}

					btnSalvar.setText("ATUALIZAR");

				} catch (Exception ex) {
					Toast.makeText(getActivity(), ex.getMessage(),
							Toast.LENGTH_LONG).show();

				}
			}
		});

		txtNDias.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if(event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() || actionId == EditorInfo.IME_ACTION_DONE)
				{
					String o = ddlDG.getSelectedItem().toString();

					if(o.equals("IA") || o.equals("TOURO"))

					{
						txtNDias.setEnabled(true);

						java.util.Date dtParto = null;
						Date dtDG = null;
						String data = lblIA.getText().toString();

						SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
						java.sql.Date data1 = null;

						try {
							data1 = new java.sql.Date(format.parse(data)
									.getTime());
						}
						catch (ParseException e)
						{
							e.printStackTrace();
						}

						if(txtNDias.getText().toString().equals("")){

							numero_dias = 0;
						}
						else
						{
							numero_dias = Integer.parseInt(txtNDias.getText().toString());
						}

						int dias;

						if(ddlDG.getSelectedItemPosition() == 1){

							dias = 280 ;//- numero_dias;
						}
						else
						{
							dias = 280 - numero_dias;
						}

						dtDG = data1;
						dtParto = somaDias(dtDG, dias);

						format = new SimpleDateFormat("dd/MM/yyyy");

						String formatted = format.format(dtParto);
						lblPrevisao.setText(formatted);
					}

				}

				return false;
			}

		}) ;



		ddlDG.setOnItemSelectedListener(new OnItemSelectedListener() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {

				String o = ddlDG.getSelectedItem().toString();

				if (o != "--SELECIONE--")
				{

					if(o.equals("VAZIA"))
					{
						String prenha = RetornaStatusVaca();

						if (prenha.equals("VACA PRENHA") || prenha.equals("PRENHE"))
						{
							MessageBox("Realize o parto no PAPER TOP antes de Fazer o DG como VAZIA no IATF");
							lblPrevisao.setText("-");
							txtNDias.setEnabled(true);
							txtNDias.setText("0");
							btnSalvar.setEnabled(false);
						}
					}
					else {

						if (lblAnimal.getText().toString().equals("")|| lblIA.getText().toString().equals("")) {

							Toast.makeText(getActivity(),"Selecione um animal antes de informar o DG",Toast.LENGTH_LONG).show();
						}
						else
						if (o.equals("IA") || o.equals("TOURO"))

						{
							btnSalvar.setEnabled(true);

							if (txtNDias.getText().toString().equals("") || txtNDias.getText().toString().equals("0"))
							{
								MessageBox("Informe o número de dias");
								ddlDG.setSelection(0);

							} else
							{

								txtNDias.setEnabled(true);

								java.util.Date dtParto = null;
								Date dtDG = null;
								String data = lblIA.getText().toString();

								SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
								java.sql.Date data1 = null;

								try
								{
									data1 = new java.sql.Date(format.parse(data).getTime());
								}
								catch (ParseException e)
								{
									e.printStackTrace();
								}

								//	int numero_dias = Integer.parseInt(txtNDias.getText().toString());

								if (numero_dias == 0) {

									numero_dias = Integer.parseInt(txtNDias.getText().toString());
								}

								int dias;

								if(ddlDG.getSelectedItemPosition() == 1){

									try {
										data1 = new Date(format.parse(data).getTime());
									} catch (ParseException e) {
										e.printStackTrace();
									}

									dias = 280 ;//- numero_dias;
								}
								else
								if(ddlDG.getSelectedItemPosition() == 2){

									Date dt = new Date(System.currentTimeMillis());
									//SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
									String formatted = format.format(dt);

									try
									{
										data1 = new Date(format.parse(formatted).getTime());
									}
									catch (ParseException e)
									{
										e.printStackTrace();
									}

									dias = 280 ;//- numero_dias;
								}
								else
								{
									try {
										data1 = new Date(format.parse(data).getTime());
									} catch (ParseException e) {
										e.printStackTrace();
									}

									dias = 280 - numero_dias;
								}

								dtDG = data1;
								dtParto = somaDias(dtDG, dias);

								format = new SimpleDateFormat("dd/MM/yyyy");

								String formatted = format.format(dtParto);
								lblPrevisao.setText(formatted);
							}

						} else if (o.equals("VAZIA"))
						{
							lblPrevisao.setText("-");
							txtNDias.setEnabled(true);
							txtNDias.setText("0");
						}
						else if(o.equals("CANCELAR"))
						{
							lblPrevisao.setText("-");
							txtNDias.setEnabled(true);
							txtNDias.setText("0");
						}
					}

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		btnSalvar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (btnSalvar.getText().toString().equals("REATIVAR VACA")) {

					if (status.equals("NAO")) {

						//ROOL BACK NOS DADOS PARA VISUALIZAR NO AT01
						db.execSQL("UPDATE PaperPort data_coleta = '"+txtDtColeta.getText().toString()+"', SET  id_criatf = "+id_criatf+", fez_d0 = " + null+ " WHERE IDANIMAIS = " + idAnimais + "");
						db.execSQL("UPDATE criatf SET tipo_atendimento = 'AT02', dg = 'CAN', fez_ia = 'NAO', nova_ia = 'true' WHERE id_criatf = "+ id_criatf + "");

						btnSalvar.setText("SALVAR");
						CleanFiedls();
						RefreshLists();
						ddlDG.setEnabled(true);

						MessageBox("Animal Pronto para fazer o D0");

					}
				}

				else {

					if (ddlDG.getSelectedItem().toString() == "--SELECIONE--")
					{
						MessageBox("INFORME O DG ANTES DE ATUALIZAR");
					}
					else
					{
						if (lblAnimal.getText().toString().equals("")|| lblIA.getText().toString().equals(""))
						{
							MessageBox("SELECIONE UM ANIMAL ANTES DE FAZER O DG");
						}
						else
						if(txtNDias.getText().equals("") || txtNDias.getText().equals(null))
						{
							MessageBox("INFORME O NUMERO DE DIAS");

						}
						else
						{
							try {

								if (btnSalvar.getText().toString().equals("DIAGNOSTICAR"))
								{

									Global x = Global.getInstance();
									StringBuilder sql = new StringBuilder();

									if(ddlDG.getSelectedItem().toString() == "IA")
									{
										if(txtNDias.getText().toString().equals(""))
										{
											MessageBox("Informe o número de dias");
										}
										else {
											sql.append("UPDATE criatf SET  numeroDias =" + txtNDias.getText() + ", tipo_atendimento = 'AT03',enviado = 'NAO', consultor = '" + x.getLogin() + "', diagnostico = '" + ddlDG.getSelectedItem() + "', dg = 'SIM',previsao_parto = '" + lblPrevisao.getText() + "' WHERE id_criatf = " + id_criatf + "");
											db.execSQL(sql.toString());

											//ATUALIZA DADOS NO PAPERTOP
											db.execSQL("UPDATE PAPERPORT SET numeroDias =" + txtNDias.getText() + ",categoria = 'PRENHE', status = 'VACA PRENHA', dataCobertura = '" + lblIA.getText() + "', dataDiagnostico = '" + txtDtColeta.getText() + "', id_criatf = " + id_criatf + ", statusProdutivo = 'PRENHE', previsaoParto = '" + lblPrevisao.getText() + "',ENVIADO = 'NAO', STATUS = 'LA', MODIFICADO = 'SIM' WHERE idAnimais = " + idAnimais + "  ");
										}

									}
									else
									if(ddlDG.getSelectedItem().toString() == "TOURO")
									{
										if(txtNDias.getText().toString().equals(""))
										{
											MessageBox("Informe o número de dias");
										}
										else {
											sql.append("UPDATE criatf SET numeroDias =" + txtNDias.getText() + ", tipo_atendimento = 'AT03',enviado = 'NAO', consultor = '" + x.getLogin() + "', diagnostico = '" + ddlDG.getSelectedItem() + "', dg = 'SIM',previsao_parto = '" + lblPrevisao.getText() + "' WHERE id_criatf = " + id_criatf + "");
											db.execSQL(sql.toString());

											//ATUALIZA DADOS NO PAPERTOP
											db.execSQL("UPDATE PAPERPORT SET numeroDias =" + txtNDias.getText() + ",categoria = 'PRENHE', status = 'VACA PRENHA', dataCobertura = '" + lblIA.getText() + "', dataDiagnostico = '" + txtDtColeta.getText() + "', id_criatf = " + id_criatf + ", statusProdutivo = 'PRENHE', previsaoParto = '" + lblPrevisao.getText() + "',ENVIADO = 'NAO', STATUS = 'LA', MODIFICADO = 'SIM' WHERE idAnimais = " + idAnimais + "  ");

										}
									}
									else
									if(ddlDG.getSelectedItem().toString() == "VAZIA")
									{
										//REMOVENDO ANIMAL DA LISTA DE IA
										sql.append("UPDATE criatf SET  numeroDias = 0, tipo_atendimento = 'AT03',enviado = 'NAO', consultor = '"+x.getLogin()+"', diagnostico = '"+ ddlDG.getSelectedItem()	+ "', dg = 'SIM',previsao_parto = '"+ lblPrevisao.getText()+ "' WHERE id_criatf = " + id_criatf + "");
										db.execSQL(sql.toString());

										//ATUALIZA DADOS NO PAPERTOP
										db.execSQL("UPDATE PAPERPORT SET numeroDias = 0, dataCobertura = '"+lblIA.getText()+"', dataDiagnostico = '"+txtDtColeta.getText()+"', id_criatf = "+id_criatf+", statusProdutivo = 'VAZIA', ENVIADO = 'NAO', MODIFICADO = 'SIM' WHERE idAnimais = "+idAnimais+"  ");

									}
									else
									if(ddlDG.getSelectedItem().toString() == "CANCELAR"){

										//CANCELAR IA
										db.execSQL("UPDATE criatf SET tipo_atendimento = 'AT03', dg = 'CAN', fez_ia = 'NAO', nova_ia = 'true', criatf_pai = 0  WHERE id_criatf = "+ id_criatf + "");

									}

									RefreshLists();
									CleanFiedls();
									MessageBox("DG Inserido com Sucesso!");

								}
								if (btnSalvar.getText().toString().equals("ATUALIZAR")) {

									StringBuilder sql = new StringBuilder();
									Global x = Global.getInstance();

									if(ddlDG.getSelectedItem().toString() == "IA")
									{
										if(txtNDias.getText().toString().equals(""))
										{
											MessageBox("Informe o número de dias");
										}
										else {
											sql.append("UPDATE criatf SET numeroDias = " + txtNDias.getText() + ", tipo_atendimento = 'AT03',enviado = 'NAO', consultor = '" + x.getLogin() + "', diagnostico = '" + ddlDG.getSelectedItem() + "', dg = 'SIM',previsao_parto = '" + lblPrevisao.getText() + "' WHERE _id = " + _id + "");
											db.execSQL(sql.toString());

											//ATUALIZA DADOS NO PAPERTOP
											db.execSQL("UPDATE PAPERPORT SET numeroDias =" + txtNDias.getText() + ", dataCobertura = '" + lblIA.getText() + "', dataDiagnostico = '" + txtDtColeta.getText() + "', id_criatf = " + _id + ",status = 'VACA PRENHA', statusProdutivo = 'PRENHE', previsaoParto = '" + lblPrevisao.getText() + "',ENVIADO = 'NAO', STATUS = 'LA', MODIFICADO = 'SIM' WHERE idAnimais = " + idAnimais + "  ");
										}
									}
									else
									if(ddlDG.getSelectedItem().toString() == "TOURO")
									{
										if(txtNDias.getText().toString().equals(""))
										{
											MessageBox("Informe o número de dias");
										}
										else {
											sql.append("UPDATE criatf SET  numeroDias =" + txtNDias.getText() + ", tipo_atendimento = 'AT03',enviado = 'NAO', consultor = '" + x.getLogin() + "', diagnostico = '" + ddlDG.getSelectedItem() + "', dg = 'SIM',previsao_parto = '" + lblPrevisao.getText() + "' WHERE id_criatf = " + id_criatf + "");
											db.execSQL(sql.toString());

											//ATUALIZA DADOS NO PAPERTOP
											db.execSQL("UPDATE PAPERPORT SET numeroDias =" + txtNDias.getText() + ", status = 'VACA PRENHA', dataCobertura = '" + lblIA.getText() + "', dataDiagnostico = '" + txtDtColeta.getText() + "', id_criatf = " + id_criatf + ", statusProdutivo = 'PRENHE', previsaoParto = '" + lblPrevisao.getText() + "',ENVIADO = 'NAO', STATUS = 'LA', MODIFICADO = 'SIM' WHERE idAnimais = " + idAnimais + "  ");
										}
									}
									else
									if(ddlDG.getSelectedItem().toString() == "VAZIA")
									{
										//REMOVENDO ANIMAL DA LISTA DE IA
										sql.append("UPDATE criatf SET  numeroDias = 0, tipo_atendimento = 'AT03',enviado = 'NAO', consultor = '"+x.getLogin()+"', diagnostico = '"+ ddlDG.getSelectedItem()	+ "', dg = 'SIM',previsao_parto = '"+ lblPrevisao.getText()+ "' WHERE id_criatf = " + id_criatf + "");
										db.execSQL(sql.toString());

										//ATUALIZA DADOS NO PAPERTOP
										db.execSQL("UPDATE PAPERPORT SET numeroDias = 0, dataCobertura = '"+lblIA.getText()+"', dataDiagnostico = '"+txtDtColeta.getText()+"',status = 'VACA VAZIA' id_criatf = "+id_criatf+", statusProdutivo = 'VAZIA', ENVIADO = 'NAO', MODIFICADO = 'SIM' WHERE idAnimais = "+idAnimais+"  ");

									}

									MessageBox("DG atualizado com sucesso!");
									btnSalvar.setText("SALVAR");

									CleanFiedls();
								}

							}

							catch (Exception ex) {

								MessageBox(ex.getMessage());
							}
						}
					}
				}
			}
		});

		return view;
	}

	public void CleanFiedls(){

		lblAnimal.setText("");
		lblIA.setText("");
		lblPrevisao.setText("");
		txtNDias.setText("");
	}

	@SuppressWarnings("deprecation")
	public void RefreshLists(){

		Cursor cursor1 = db.rawQuery("SELECT _id,id,nome_vaca FROM criatf WHERE _propriedade = "+ produtor + " AND DG = 'SIM'", null);
		String[] from = { "_id", "id", "nome_vaca" };
		int[] to = { R.id.lblID, R.id.lblBrinco,R.id.lblNome };

		SimpleCursorAdapter ad1 = new SimpleCursorAdapter(getActivity(),R.layout.animais_list, cursor1,	from, to);
		lstDG.setAdapter(ad1);

		cursor1 = db.rawQuery("SELECT _id,id,nome_vaca FROM criatf WHERE _propriedade = "+ produtor+ " AND FEZ_IA = 'SIM' AND DG IS NULL", null);
		String[] from1 = { "_id", "id", "nome_vaca" };
		int[] to1 = { R.id.lblID, R.id.lblBrinco,R.id.lblNome };

		ad1 = new SimpleCursorAdapter(getActivity(),R.layout.animais_list, cursor1,	from1, to1);
		lstAnimals.setAdapter(ad1);
	}

	public java.util.Date somaDias(Date data, int dias)
	{
		Calendar cal = new GregorianCalendar();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_MONTH, dias);
		return cal.getTime();
	}

	public void setSpinTextByID(Spinner spin, long text) {
		for (int i = 0; i < spin.getAdapter().getCount(); i++) {
			long x = spin.getAdapter().getItemId(i);

			if (spin.getAdapter().getItemId(i) == text) {
				spin.setSelection(i);
			}
		}

	}

	public void setSpinTextByText(Spinner spin, String text) {
		for (int i = 0; i < spin.getAdapter().getCount(); i++) {
			long x = spin.getAdapter().getItemId(i);

			if (spin.getAdapter().getItem(i).toString().contains(text)) {
				spin.setSelection(i);
			}
		}

	}

	public String RetornaProdutor(int _id) {

		String result = "";
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);

		Cursor cursor = db.rawQuery("SELECT NOME FROM PRODUTOR WHERE ID = " + _id + "", null);
		cursor.moveToFirst();
		result = cursor.getString(cursor.getColumnIndex("nome"));

		return result;
	}

	public String RetornaNomeProjeto(int codigo) {
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",
				Context.MODE_PRIVATE, null);
		Cursor cursor = db.rawQuery("SELECT NOME FROM PROJETO WHERE ID = "
				+ codigo + "", null);
		cursor.moveToFirst();

		String x = cursor.getString(cursor.getColumnIndex("nome"));

		return x;
	}

	public String RetornaStatusVaca(){

		String x = "";

		Cursor cursor = db.rawQuery("SELECT categoria FROM PAPERPORT WHERE IDANIMAIS = " + idAnimais, null);
		if(cursor.moveToFirst()){

			x =  cursor.getString(cursor.getColumnIndex("categoria"));

		}

		return  x;
	}

	public String RetornaNomeGrupo(int codigo) {
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",
				Context.MODE_PRIVATE, null);
		Cursor cursor = db.rawQuery("SELECT NOME FROM GRUPO WHERE ID = "
				+ codigo + "", null);
		cursor.moveToFirst();

		String x = cursor.getString(cursor.getColumnIndex("nome"));

		return x;
	}

	public void MessageBox(String msg) {
		AlertDialog.Builder informa = new AlertDialog.Builder(getActivity());
		informa.setTitle("Alerta!").setMessage(msg);
		informa.setNeutralButton("Fechar", null).show();
	}

	public int RetornaLastID() {

		int x = 0;
		try {
			SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",
					Context.MODE_PRIVATE, null);

			Cursor cursor = db.rawQuery("SELECT _id FROM criatf  order by _id desc limit 1",null);
			cursor.moveToFirst();

			x = cursor.getInt(cursor.getColumnIndex("_id"));
			Global b = Global.getInstance();
			b.setLastID(x);

		}

		catch (Exception ex) {

			MessageBox(ex.getMessage());

		}

		return x;
	}

	@Override
	public void onClick(View v) {

	}

}
