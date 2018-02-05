package com.pec.biosistemico.pec.criatf;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.util.Global;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Atendimento_02 extends Fragment implements OnClickListener {
		
	private Calendar cal;
	private int minute;
	private int hour;
	private int day;
	private int month;
	private int year;
	private String raca;
	private TextView lblRaca;
	private TextView lblTouro;
	private int animalSelecionado;
	private Global mDados = Global.getInstance();

	private int posicaoRaca;
	private int posicaoTouro;

	int projeto = 0;
	String grupo = "";
	int produtor = 0;
	Spinner ddlRaca;
	Spinner ddlVaca;
	Spinner ddlTouro;
	
	String idCobertura;
	String idReprodutivo;
	String nomeVaca;	
	String idAnimais;
	long id_criatf;
	String idRaca;
	int raca_position;
	int touro_position;
	private Button btnDataD0;
	private String regiao;
	private int count = 0;


	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		   Bundle savedInstanceState) {
		
		final SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
		
		Global global = Global.getInstance();
		projeto = global.getProjeto();
		grupo = global.getUsuario();
		produtor = global.getProdutor();		
		
		final View view = inflater.inflate(R.layout.atendimento_02,null);

		final TextView txtProjeto = (TextView)view.findViewById(R.id.txtProjeto);
		final TextView txtGrupo = (TextView)view.findViewById(R.id.txtGrupo);
		final TextView txtRef = (TextView)view.findViewById(R.id.txtReferencia);
		final TextView txtProdutor = (TextView)view.findViewById(R.id.txtProdutor);
		final EditText txtDtColeta = (EditText)view.findViewById(R.id.txtDtAtendimento);
		
		//final Spinner ddlProdutor = (Spinner)view.findViewById(R.id.ddl1);

		btnDataD0 = (Button) view.findViewById(R.id.btnData);

		ddlVaca = (Spinner)view.findViewById(R.id.Spinner01);
		final Spinner ddlD8 = (Spinner)view.findViewById(R.id.ddlD8);
		ddlRaca = (Spinner)view.findViewById(R.id.ddlRacas);
		ddlTouro = (Spinner)view.findViewById(R.id.ddlTouro);
		final Spinner ddlIATF = (Spinner)view.findViewById(R.id.ddlIATF);

		//final ImageButton btnData = (ImageButton)view.findViewById(R.id.btnShare);
		final Button btnSalvar = (Button)view.findViewById(R.id.btnSalvarPaper);
		final Button btnLoad = (Button)view.findViewById(R.id.btnLoadLast);
		final ImageButton btnSemem = (ImageButton)view.findViewById(R.id.btnSemem);
		
		Date data = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String formatted = format.format(data);

		txtProjeto.setText("Projeto: " + RetornaNomeProjeto(projeto));
		txtGrupo.setText("Grupo: " + RetornaNomeGrupo(Integer.parseInt(grupo)));
		txtProdutor.setText("Produtor: " + RetornaProdutor(produtor));

		txtRef.setText("Referência: " + formatted.toString());
		txtDtColeta.setText(formatted.toString());
		txtDtColeta.setEnabled(false);

		populaRaca();
		populaVaca();

		//txtProjeto.setText(RetornaNomeProjeto(projeto));
		//txtGrupo.setText(RetornaNomeGrupo(Integer.parseInt(grupo)));
						
		//Cursor cursor = db.rawQuery("SELECT rowid _id, id, nome FROM produtor WHERE id = "+ produtor + "", null);

		//String[] from3 = { "nome", "id", "_id" };
		//int[] to3 = { R.id.lblNome, R.id.lblId, R.id.lbl_id_FK };
		//@SuppressWarnings("deprecation")
		//SimpleCursorAdapter ad = new SimpleCursorAdapter(getActivity(),	R.layout.spinner, cursor, from3, to3);
		//ddlProdutor.setAdapter(ad);
		
		List<String> OP1 = new ArrayList<String>();
		OP1.add("SIM");
		OP1.add("NAO");
		
		// PASSANDO O ARRAYLIST LITROS
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, OP1);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		ddlD8.setAdapter(spinnerArrayAdapter);
		ddlIATF.setAdapter(spinnerArrayAdapter);


		ddlRaca.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {


				if(count == 0) {

					alerta("Consultor, a mudança de animal implicara na BAIXA de estoque" +
							" de um animal que NÃO foi planejado para este produtor, você deverá notificar" +
							"o administrativo sobre a mudança, CONFIRMA ESTA ALTERAÇÃO?");

					count++;
				}

				return false;
			}
		});

		ddlTouro.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if(count == 0) {

					alerta("Consultor, a mudança de animal implicara na BAIXA de estoque" +
							" de um animal que NÃO foi planejado para este produtor, você deverá notificar" +
							"o administrativo sobre a mudança, CONFIRMA ESTA ALTERAÇÃO?");

					count++;
				}

				return false;
			}
		});




		ddlTouro.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


				count = 0;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		
		
		btnSemem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				openDialogFragment(v);
				
				
			}
		});
		
		
		btnSalvar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(ddlVaca.getSelectedItemId() == 1){
					
					MessageBox("FAVOR SELECIONAR UMA ANIMAL ANTES DE SALVAR");
				}
				else{
			
				try{	
					
					if(btnSalvar.getText().toString().equals("SALVAR")){
						
						Global x = Global.getInstance();
						
						//String nome_animal = ReturnCowName((int) ddlVaca.getSelectedItemId());
						id_criatf = ReturnCowID(ddlVaca.getSelectedItemId());
						
						if(id_criatf == 0){
							
							MessageBox("Animal fora do período de Inseminação");
						}
						else
						{
							
						String idTouro = RetornaID_Touro(ddlTouro.getSelectedItemId());
																
						StringBuilder sql = new StringBuilder();
						sql.append("UPDATE CRIATF SET criatf_pai = 0, tipo_atendimento = 'AT02', processo_d8 = '"+ddlD8.getSelectedItem()+"',processo_iatf = '"+ddlIATF.getSelectedItem()+"',_raca = "+idRaca);
						sql.append(",touro ='"+idTouro+"',consultor = '"+x.getLogin()+"',id_reprodutivo = "+idReprodutivo+",id_cobertura = "+idCobertura+",fez_ia = 'SIM',enviado = 'NAO'");
						sql.append("WHERE id_criatf = "+id_criatf);						
						
						db.execSQL(sql.toString());		
						
						if(ddlIATF.getSelectedItem().toString().equals("SIM"))
						{
							db.execSQL("UPDATE criatf SET fez_ia = 'SIM' WHERE id_criatf = "+id_criatf+"");
						}
						else
							if(ddlIATF.getSelectedItem().toString().equals("NAO"))
							{
								//db.execSQL("UPDATE criatf SET fez_ia = 'SIM', dg = 'CAN' WHERE id_criatf = "+id_criatf+"");								
								db.execSQL("UPDATE PaperPort SET id_criatf = "+id_criatf+", fez_d0 = " + null+ " WHERE IDANIMAIS = " + idAnimais + "");
								db.execSQL("UPDATE criatf SET tipo_atendimento = 'AT02', dg = 'CAN', fez_ia = 'NAO', nova_ia = 'true', criatf_pai = 0  WHERE id_criatf = "+ id_criatf + "");

								MessageBox("Animal pronto para protocolar");
							}
						
						MessageBox("Atendimento IA Inserido com Sucesso!");
						ddlVaca.setSelection(1);
						populaVaca();
						}
					}
					if(btnSalvar.getText().toString().equals("ATUALIZAR")){
						
						/*Global x = Global.getInstance();
						
						int id = x.getLastID();
						
						StringBuilder sql = new StringBuilder();
						
						sql.append("UPDATE atendimento_02 SET processo_d8 = '"+ddlD8.getSelectedItem().toString()+"',processo_iatf = '"+ddlIATF.getSelectedItem().toString()+"',");
						sql.append("raca = '"+ddlRaca.getSelectedItemId()+"',nome_touro='"+ddlTouro.getSelectedItemId()+"'");
						db.execSQL(sql.toString());							
						
						MessageBox("Atendimento IA Atualizado com Sucesso!");
						
						btnSalvar.setText("SALVAR");
						ddlVaca.setSelection(1);*/
						
					}
									
				}
				
				catch(Exception ex){
					
					MessageBox(ex.getMessage());
				}			
			}
			}
		});
		
		btnLoad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				SQLiteDatabase db1 = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

				Cursor cursor = db1.rawQuery("SELECT * FROM atendimento_02 ORDER BY _ID DESC LIMIT 1", null);
				
				try {
					if (cursor != null && cursor.getCount() > 0) {
						if (cursor.moveToFirst()) 
							do {							
							
								txtDtColeta.setText(cursor.getString(cursor.getColumnIndex("data_atendimento")));							
								
								String o = "";
								o = cursor.getString(cursor.getColumnIndex("processo_iatf"));
								
								if (o.toString().equals("SIM")) 
								{
									ddlIATF.setSelection(0);									
								}
								if (o.toString().equals("NAO")) 
								{
									ddlIATF.setSelection(1);									
								}
								
								o = cursor.getString(cursor.getColumnIndex("processo_d8"));
								
								if (o.toString().equals("SIM")) 
								{
									ddlD8.setSelection(0);									
								}
								if (o.toString().equals("NAO")) 
								{
									ddlD8.setSelection(1);									
								}
																
								
								long x = cursor.getLong(cursor.getColumnIndex("touro"));								
								setSpinTextByIDTouro(ddlTouro, x);									
								
								//long x = cursor.getLong(cursor.getColumnIndex("raca"));								
								//setSpinTextByIDRaca(ddlRaca, x);
								
							    String y = cursor.getString(cursor.getColumnIndex("nome_vaca"));
								setSpinTextByText(ddlVaca, y);
											
						
								btnSalvar.setText("ATUALIZAR");										
							
							}
						
						while(cursor.moveToNext());
						
					}
				}
						
						catch(Exception ex){
							
							MessageBox(ex.getMessage());
							
						}					
			}
		});

		ddlRaca.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				count = 0;

				SQLiteCursor dados = (SQLiteCursor) ddlRaca.getAdapter().getItem(ddlRaca.getSelectedItemPosition());
				raca = RetornaNomeRaca(ddlRaca.getSelectedItemId());


				try {


					if(mDados.getProject().equals("SEBRAERN")
							|| mDados.getProject().equals("SEBRAEPB")
							|| mDados.getProject().equals("DEMO")){
						populaTouro("RN");
						regiao = "RN";
					}
					else
					if(mDados.getProject().equals("SEBRAEMS") || mDados.getProject().equals("SEBRAEGO") ||
							mDados.getProject().equals("FIBRIA") || mDados.getProject().equals("COPLAF")){
						populaTouro("MS");
						regiao = "MS";
					}
					else {
						populaTouro("PR");
						regiao = "PR";
					}


					ddlTouro.setSelection(posicaoTouro);


				}
				catch (Exception ex){

				}


			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		btnDataD0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Process to get Current Date
				final Calendar c = Calendar.getInstance();
				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH);
				day = c.get(Calendar.DAY_OF_MONTH);
				hour = c.get(Calendar.HOUR_OF_DAY);
				minute = c.get(Calendar.MINUTE);

				// Launch Date Picker Dialog
				DatePickerDialog dpd = new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
												  int monthOfYear, int dayOfMonth) {
								// Display Selected date in textbox
								txtDtColeta.setText(dayOfMonth + "/"
										+ (monthOfYear + 1) + "/" + year + "/" + hour + ":" + minute);


							}
						}, year, month, day);
				dpd.show();


			}
		});
		
		ddlVaca.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				
				SQLiteDatabase db1 = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
				
				SQLiteCursor dados = (SQLiteCursor) ddlVaca.getAdapter().getItem(ddlVaca.getSelectedItemPosition());
				int vaca = dados.getInt(1);

				int count = 0;

				Cursor cursor = db1.rawQuery("SELECT * FROM CRIATF WHERE _ID = "+id+"", null);
				Cursor cursorRaca = db.rawQuery("select distinct  _id, raca, id_raca  from touro group by raca order by raca",null);

				long x;
				count = 0;

				try {
					if (cursor != null && cursor.getCount() > 0) {
						if (cursor.moveToFirst())
							do {

								x = cursor.getLong(cursor.getColumnIndex("_raca"));
								while (cursorRaca.moveToNext()) {

									long oo = cursorRaca.getLong(cursorRaca.getColumnIndex("id_raca"));

									if (x == oo) {
										posicaoRaca = count;
										ddlRaca.setSelection(posicaoRaca);
										//nomeRaca = cursor.getString(cursor.getColumnIndex("raca"));
									}
									count++;
								}


								x = cursor.getLong(cursor.getColumnIndex("_raca"));
									Cursor cursorTouro = db1.rawQuery("SELECT DISTINCT * FROM TOURO WHERE id_raca = "+x+" AND nome like '%"+regiao+"' ORDER BY NOME", null);
								x = cursor.getLong(cursor.getColumnIndex("touro"));

								count = 0;
								while (cursorTouro.moveToNext()){

									if(x == cursorTouro.getLong(cursorTouro.getColumnIndex("id")))
									{
										posicaoTouro = count;
										//ddlTouro.setSelection(posicaoTouro);
										animalSelecionado = posicaoTouro;
									}
									count++;
								}
							}
							while(cursor.moveToNext());
					}
				}
						catch(Exception ex){

							MessageBox(ex.getMessage());
						}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			
			}
		});
		return view;	
	}
	
	
	public void openDialogFragment(View view) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ListaSemes cdf = new ListaSemes(1,1);
		cdf.show(ft, "Semem");
	}

	
	public void setSpinTextByIDTouro(Spinner spin, long text)
	{		
	    for(int i= 0; i < spin.getAdapter().getCount(); i++)
	    {
	    	//long x = spin.getAdapter().getItemId(i);	    	
	    	long position = ReturnPositionID_Touro(text);
	    	
	        if(spin.getAdapter().getItemId(i) == position)
	        {
	            spin.setSelection(i);
	        }
	    }

	}
	
	
	public void setSpinTextByIDRaca(Spinner spin, long text)
	{
	    for(int i= 0; i < spin.getAdapter().getCount(); i++)
	    {
	    	//long x = spin.getAdapter().getItemId(i);
	    	long position = ReturnPositionID_Raca(text);

	        if(spin.getAdapter().getItemId(i) == position)
	        {
	            spin.setSelection(i);
	        }
	    }

	}


	public void setSpinTextByText(Spinner spin, String text)
	{
	    for(int i= 0; i < spin.getAdapter().getCount(); i++)
	    {
	    	long x = spin.getAdapter().getItemId(i);
	    	
	        long id = ReturnIDLastAtendimento02(text);
	    	
	        if(spin.getAdapter().getItemId(i) == id)
	        {
	            spin.setSelection(i);
	        }
	    }

	}

	public void alerta(String msg){

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {


				}
			};

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setIcon(R.drawable.caution);
		builder.setTitle("ATENÇÃO!");

		builder.setMessage(msg).setPositiveButton("SIM", dialogClickListener)
		.setNegativeButton("NÃO", dialogClickListener).show();
	}



	public String RetornaNomeProjeto(int codigo) {
		
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",	Context.MODE_PRIVATE, null);
		Cursor cursor = db.rawQuery("SELECT NOME FROM PROJETO WHERE ID = "+ codigo + "", null);
		cursor.moveToFirst();

		String x = cursor.getString(cursor.getColumnIndex("nome"));

		return x;
	}
	
	public int ReturnPositionID_Touro(long codigo) {
		
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",	Context.MODE_PRIVATE, null);
		Cursor cursor = db.rawQuery("SELECT _ID FROM TOURO WHERE ID = "+ codigo + "", null);
		
		if(cursor.moveToFirst()){

			touro_position = cursor.getInt(cursor.getColumnIndex("_id"));
		}

		return touro_position;
		
	}
	
	public int ReturnPositionID_Raca(long codigo) {
		
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",	Context.MODE_PRIVATE, null);
		Cursor cursor = db.rawQuery("SELECT _ID FROM TOURO WHERE ID_RACA = "+ codigo + "", null);
		
		if(cursor.moveToFirst()){

			raca_position = cursor.getInt(cursor.getColumnIndex("_id"));
		}

		return raca_position;
		
	}
	
	public String ReturnCowName(int codigo) {
		
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",	Context.MODE_PRIVATE, null);
		Cursor cursor = db.rawQuery("SELECT _ID,nome_vaca FROM CRIATF WHERE _ID = "+ codigo + "", null);
		cursor.moveToFirst();

		String x = cursor.getString(cursor.getColumnIndex("nome_vaca"));

		return x;
	}
	
	public long ReturnCowID(long l){
		
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
		Cursor cursor = db.rawQuery("SELECT id_criatf,_id_animais, ID_COBERTURA,ID_REPRODUTIVO, _id_animais FROM CRIATF WHERE _ID = "+l+"",null);
		cursor.moveToFirst();
			
		long x = cursor.getLong(cursor.getColumnIndex("id_criatf"));	
		
		idCobertura = cursor.getString(cursor.getColumnIndex("id_cobertura"));
		idReprodutivo = cursor.getString(cursor.getColumnIndex("id_reprodutivo"));
		idAnimais = cursor.getString(cursor.getColumnIndex("_id_animais"));
		
		return x;
		
	}

	public String RetornaNomeRaca(long id){
		
		String x = "";
		
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
		
		Cursor cursor = db.rawQuery("SELECT _id, RACA FROM TOURO WHERE _ID = "+ id + "", null);
		
		if(cursor.moveToFirst()){

			x = cursor.getString(cursor.getColumnIndex("raca"));
		}

		return x;		
	}

	public String RetornaNomeGrupo(int codigo) {
		
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",	Context.MODE_PRIVATE, null);
		Cursor cursor = db.rawQuery("SELECT NOME FROM GRUPO WHERE ID = "+ codigo + "", null);
		cursor.moveToFirst();

		String x = cursor.getString(cursor.getColumnIndex("nome"));

		return x;
	}
	
	public long ReturnIDLastAtendimento02(String nome) {
		
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",	Context.MODE_PRIVATE, null);
		
		Cursor cursor = db.rawQuery("SELECT _id FROM CRIATF WHERE nome_vaca = '"+ nome + "'", null);
		cursor.moveToFirst();

		long x = cursor.getLong(cursor.getColumnIndex("_id"));

		return x;
	}
	
	public void MessageBox(String msg) {
		AlertDialog.Builder informa = new AlertDialog.Builder(getActivity());
		informa.setTitle("Alerta!").setMessage(msg);
		informa.setNeutralButton("Fechar", null).show();
	}
	
	public void populaRaca() {
		
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
		Cursor cursor = db.rawQuery("select distinct  _id, raca  from touro group by raca order by raca",null);
	
		String[] from2 = {"raca","_id"};
		int[] to2 = { R.id.lblNome,R.id.lbl_id_FK};
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter ad = new SimpleCursorAdapter(getActivity().getBaseContext(),R.layout.spinner, cursor, from2, to2);
		ddlRaca.setAdapter(ad);
	}

	public void populaTouro(String regiao) {
		
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
		//Cursor cursor = db.rawQuery("SELECT DISTINCT id,_id, nome FROM TOURO WHERE raca = '"+raca+"' ORDER BY NOME",null);
		Cursor cursor = db.rawQuery("SELECT DISTINCT id,_id, (nome || ' - SÊMEM DISPONIVEL: ' || sememDisponivel)as nome FROM TOURO WHERE raca = '" + raca + "' AND nome like '%"+regiao+"' ORDER BY NOME", null);

		String[] from2 = {"nome", "id", "_id" };
		int[] to2 = { R.id.lblNome, R.id.lblId, R.id.lbl_id_FK };
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter ad = new SimpleCursorAdapter(getActivity().getBaseContext(),R.layout.spinner, cursor, from2, to2);
		ddlTouro.setAdapter(ad);
	}
	
	public void populaVaca() {
		
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
		Global x = Global.getInstance();
		int id = x.getProdutor();

		Cursor cursor = db.rawQuery("SELECT _propriedade, _id_animais, _id, nome_vaca FROM CRIATF WHERE    (_propriedade = "+produtor+" AND fez_ia = '' ) or (fez_ia is null)   ORDER BY nome_vaca ",null);
	
		String[] from2 = {"nome_vaca", "_id_animais", "_id" };
		int[] to2 = { R.id.lblNome, R.id.lblId, R.id.lbl_id_FK };
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter ad = new SimpleCursorAdapter(getActivity().getBaseContext(),R.layout.spinner, cursor, from2, to2);
		ddlVaca.setAdapter(ad);
	}

	public String RetornaID_Touro(long id){
		
		String x = "";
		
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
		
		Cursor cursor = db.rawQuery("SELECT _id,id, id_raca FROM TOURO WHERE _ID = "+ id + "", null);
		
		if(cursor.moveToFirst()){

			x = cursor.getString(cursor.getColumnIndex("id"));
			idRaca = cursor.getString(cursor.getColumnIndex("id_raca"));
		}

		return x;
		
	}

	public String RetornaProdutor(int _id) {

		String result = "";
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);

		Cursor cursor = db.rawQuery("SELECT NOME FROM PRODUTOR WHERE ID = " + _id + "", null);
		cursor.moveToFirst();
		result = cursor.getString(cursor.getColumnIndex("nome"));

		return result;
	}

	public void RetornaLastID() {
		
		try{
			SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",	Context.MODE_PRIVATE, null);
			
			Cursor cursor = db.rawQuery("SELECT _id FROM atendimento_01  order by _id desc limit 1", null);
			cursor.moveToFirst();

			int x = cursor.getInt(cursor.getColumnIndex("_id"));
			Global b = Global.getInstance();
			b.setLastID(x);
						 
		}
		
		catch(Exception ex){
			
			MessageBox(ex.getMessage());
			 	
		  }
		
			
			
		}

	@Override
	public void onClick(View v) {
		
	}

	public int ajustaEstoque(long id){

		try {

			int qtd = 0;

			SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
			Cursor cursor = db.rawQuery("SELECT SEMENDISPONIVEL FROM TOURO WHERE _ID = " + id + " ", null);

			if (cursor.moveToFirst()) {

				qtd = cursor.getInt(cursor.getColumnIndex("semendisponivel"));
			}

			return qtd - 1;
		}
		catch (Exception ex){

			return  0;
		}
	}

}
