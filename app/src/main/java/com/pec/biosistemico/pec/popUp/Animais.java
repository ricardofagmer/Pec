package com.pec.biosistemico.pec.popUp;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.pec.biosistemico.pec.paperTop.Vaca;
import com.pec.biosistemico.pec.paperTop.VacaAdapter;
import com.pec.biosistemico.pec.util.Global;
import com.pec.biosistemico.pec.R;

@SuppressLint("ValidFragment")
public class Animais extends DialogFragment implements OnClickListener{
	
	private int numStyle;
	private int numTheme;
	private int day;
	private int month;
	private int year;
	private Calendar cal;
	private EditText txtDtNasc;
	private EditText txtNome;
	private EditText txtNPartos;
	private EditText txtPlantel;
	private EditText txtBrincoN;
	private Spinner ddlSexo;
	private Spinner ddlOrigem;
	private Spinner ddlRaca;
	private Spinner ddlCategoria;
	private Spinner ddlPelagem;
	private int produtor;
	private TextView lblError;
	private ListView lstAnimais;
	private int _id;
	private Spinner ddlStatus;
	private ImageButton btnPlantel;
	private ImageButton btnDataN;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_DeviceDefault_Light_Dialog);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		final View view = inflater.inflate(R.layout.animais, null);
		final Global mDados = Global.getInstance();
		String Grupo = mDados.getUsuario();
		int Projeto = mDados.getProjeto();
		produtor = mDados.getProdutor();
		
		final SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",	Context.MODE_PRIVATE, null);
		
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);

		final TextView lblMsg = (TextView) view.findViewById(R.id.lblmessage);

		final Button btnSalvar = (Button) view.findViewById(R.id.btnSalvarN);
		Button btnCancelar = (Button) view.findViewById(R.id.btnCancelaN);

		txtDtNasc = (EditText) view.findViewById(R.id.txtDtNascN);
		txtNome = (EditText) view.findViewById(R.id.txtNomeN);

		lblError = (TextView)view.findViewById(R.id.textView11);
		//txtNPartos = (EditText) view.findViewById(R.id.txtNpartos);
		//txtPlantel = (EditText) view.findViewById(R.id.txtPlantel);
		txtBrincoN = (EditText) view.findViewById(R.id.txtBrincoN);
		//ddlSexo = (Spinner) view.findViewById(R.id.ddlVacina);
		//ddlOrigem = (Spinner) view.findViewById(R.id.ddlOrigemEntrada);
		ddlRaca = (Spinner) view.findViewById(R.id.ddlRaca);
		//ddlCategoria = (Spinner) view.findViewById(R.id.ddlCategoria);
		//ddlPelagem = (Spinner) view.findViewById(R.id.ddlPelagem);
		btnPlantel = (ImageButton) view.findViewById(R.id.btnPlantel);
		btnDataN = (ImageButton) view.findViewById(R.id.btnDataN);
		//lstAnimais = (ListView)view.findViewById(R.id.lstAnimais);
		txtDtNasc.setEnabled(false);
		//txtPlantel.setEnabled(false);
		
		//populaCategoria();
		populaRaca();

		List<String> status = new ArrayList<String>();
		status.add("SECA");
		status.add("LA");

		List<String> entrada = new ArrayList<String>();
		entrada.add("NASCIMENTO");
		entrada.add("COMPRA");
		entrada.add("TRANSFERENCIA");
		entrada.add("EST. IMPLANT.");
		entrada.add("OUTROS");

		List<String> pelagem = new ArrayList<String>();
		pelagem.add("CARACTERISTICA");
		pelagem.add("BRANCA");
		pelagem.add("CINZA");

		List<String> sexo = new ArrayList<String>();
		sexo.add("FEMEA");
		sexo.add("MACHO");

		Cursor cursor1 = db.rawQuery("SELECT _id,id,nome,brinco FROM ANIMAL WHERE _produtor = "+produtor+" AND ENVIADO = 'NAO'", null);
		String[] from = { "_id","brinco","nome"};
		int[] to = { R.id.lblID,R.id.lblBrinco,R.id.lblNome};

		@SuppressWarnings("deprecation")
		SimpleCursorAdapter ad1 = new SimpleCursorAdapter(getActivity(),R.layout.animais_list, cursor1, from, to);

		btnDataN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Process to get Current Date
				final Calendar c = Calendar.getInstance();
				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH);
				day = c.get(Calendar.DAY_OF_MONTH);

				// Launch Date Picker Dialog
				DatePickerDialog dpd = new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								// Display Selected date in textbox
								txtDtNasc.setText(dayOfMonth + "/"
										+ (monthOfYear + 1) + "/" + year);

							}
						}, year, month, day);
				dpd.show();
			}
		});

		btnSalvar.setOnClickListener(new OnClickListener() {
			@SuppressLint({ "SimpleDateFormat", "ShowToast" })
			@Override
			public void onClick(View v) {
				
				if(txtBrincoN.getText().toString().equals("") && txtNome.getText().toString().equals(""))
				{
					lblError.setVisibility(View.VISIBLE);
					lblError.setText("Ao menos o nome ou brinco devem ser digitados!");
				}				
				else{
					
					if(btnSalvar.getText().toString().equals("SALVAR"))
					{
				
				    Date data = new Date(System.currentTimeMillis());
				
					Global x = Global.getInstance();
					SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
					
					try{
						int n = 0;
						int brinco = 0;
						
						if(txtBrincoN.getText().toString().equals(""))
						{
							brinco = 0;
						}
						else
						{
							brinco = Integer.parseInt(txtBrincoN.getText().toString());
						}

						Random r = new Random();
						r.nextInt();

						Date data_coleta = new Date(System.currentTimeMillis());
						SimpleDateFormat formatBR = new SimpleDateFormat("dd/MM/yyyy");
						final String data_formatada = formatBR.format(data_coleta);

						String id_propriedade = mDados.getProdutor() + "|" +r.nextInt();

						StringBuilder sql = new StringBuilder();
						sql.append("INSERT INTO paperPort(data_coleta,status,categoria,nome_usual,brinco,data_nascimento,cod_mobile,raca,_projeto,_grupo,_propriedade,modificado,enviado, idCobertura,idReprodutivo,idAnimais,diasPrenhez,numeroFetos)");
						sql.append(" VALUES ('"+data_formatada+"','SECA','VACA VAZIA','"+txtNome.getText()+"','"+txtBrincoN.getText()+"','"+txtDtNasc.getText()+"','");
						sql.append(""+id_propriedade+"','"+RetornaIDRaca(ddlRaca.getSelectedItemId())+"','"+mDados.getProjeto()+"','"+mDados.getUsuario()+"','"+mDados.getProdutor()+"','SIM','NAO',0,0,0,0,0);");

						db.execSQL(sql.toString());

						
						RefreshList();
						dismiss();
						
					}
					
					catch(Exception ex)
					{
					}

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

		return view;

	}
	
	public void setSpinTextByID(Spinner spin, long text)
	{
	    for(int i= 0; i < spin.getAdapter().getCount(); i++)
	    {
	    	long x = spin.getAdapter().getItemId(i);
	    	
	        if(spin.getAdapter().getItemId(i) == text)
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
	    	
	        if(spin.getAdapter().getItem(i).toString().contains(text))
	        {
	            spin.setSelection(i);
	        }
	    }

	}
	
	public void CleanFields(){
		
		txtBrincoN.setText("");
		txtDtNasc.setText("");
		txtNome.setText("");
		txtNPartos.setText("");
		txtPlantel.setText("");
	}

	public void RefreshList() {

		int total_animais = 0;

		Global mDados = Global.getInstance();
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
		ArrayList<Vaca> vacas = new ArrayList<Vaca>();

		ListView lv = (ListView) getActivity().findViewById(R.id.listView1);
		TextView lblCountAnimais =  (TextView) getActivity().findViewById(R.id.txtTotalAnimais);
		TextView lblDG = (TextView)getActivity().findViewById(R.id.lblDG);
		TextView lblParto = (TextView)getActivity().findViewById(R.id.lblParto);

		produtor = mDados.getProdutor();

		Cursor cursor1 = db
				.rawQuery(
						"SELECT DISTINCT numeroDias,p.dataParto,p.dataDiagnostico, p._id as _id,p.previsaoParto, p.categoria, p.data_secagem,p.modificado,  "
								+ "p.manejo, p.peso_atual, p.id as id,  proj.nome as _projeto, grup.nome as _grupo, produ.nome as _propriedade, "
								+ "p.data_coleta,p.area_atual,p.brinco,p.nome_usual,p.status,p.data_secagem,p.obs,p.ocorrencia,p.referencia "
								+ "FROM paperPort p, projeto proj, grupo grup, produtor produ "
								+ "WHERE p._projeto = proj.id AND p._grupo = grup.id AND p._propriedade = produ.id AND p._propriedade = "
								+ produtor + "", null);

		while (cursor1.moveToNext()) {

			Vaca vaca = new Vaca();
			vaca.set_id(cursor1.getString(cursor1.getColumnIndex("_id")));
			vaca.setNome_usual(cursor1.getString(cursor1.getColumnIndex("nome_usual")));
			vaca.setBrinco(cursor1.getString(cursor1.getColumnIndex("brinco")));
			vaca.setCat_atual(cursor1.getString(cursor1.getColumnIndex("categoria")));
			vaca.setData_secagem(cursor1.getString(cursor1.getColumnIndex("data_secagem")));
			vaca.setOcorrencia(cursor1.getString(cursor1.getColumnIndex("ocorrencia")));
			vaca.setPrevisao_parto(cursor1.getString(cursor1.getColumnIndex("previsaoParto")));
			vaca.setStatus(cursor1.getString(cursor1.getColumnIndex("status")));
			vaca.setNumero_dias(cursor1.getString(cursor1.getColumnIndex("numeroDias")));
			vaca.setPeso_atual(cursor1.getString(cursor1.getColumnIndex("peso_atual")));
			vaca.setModificado(cursor1.getString(cursor1.getColumnIndex("modificado")));

			total_animais = total_animais + 1;

			vacas.add(vaca);

			//lblParto.setText("Parto feito em: " + txtDtNasc.getText().toString());
			//lblDG.setText("Diagnóstico: " +  cursor1.getString(cursor1.getColumnIndex("dataDiagnostico")) );

		}

		lblCountAnimais.setText("Animais: " + String.valueOf(total_animais));

		final VacaAdapter v;
		v = new VacaAdapter(getActivity(), vacas);
		lv.setAdapter(v);

	}

	public void populaRaca() {
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
		Cursor cursor = db.rawQuery("SELECT DISTINCT * FROM RACA ORDER BY NOME",null);

		String[] from2 = {"nome", "id", "_id" };
		int[] to2 = { R.id.lblNome, R.id.lblId, R.id.lbl_id_FK };
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter ad = new SimpleCursorAdapter(getActivity().getBaseContext(),R.layout.spinner, cursor, from2, to2);
		ddlRaca.setAdapter(ad);
	}
	
	public String RetornaIDRaca(long l) {

		String result = "";
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",	Context.MODE_PRIVATE, null);

		Cursor cursor = db.rawQuery("SELECT id FROM raca WHERE _id = "+ l + "", null);
		cursor.moveToFirst();
		result = cursor.getString(cursor.getColumnIndex("id"));

		return result;
	}
	
	public String RetornaIDCategoria(long l) {

		String result = "";
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);

		Cursor cursor = db.rawQuery("SELECT id FROM categoria WHERE _id = "+ l + "", null);
		cursor.moveToFirst();
		result = cursor.getString(cursor.getColumnIndex("id"));

		return result;
	}
	
	public void RefreshList1(){
		
		SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",	Context.MODE_PRIVATE, null);
		
		final ListView lstResult = (ListView)getActivity().findViewById(R.id.lstResult);

		
		Cursor cursor2 = db.rawQuery("SELECT _id,id, nome, brinco FROM ANIMAL WHERE _produtor = "+produtor+"", null);
		String[] from1 = { "_id", "nome","brinco"};
		int[] to1 = { R.id.lblID, R.id.lblBrinco, R.id.lblNome };
		
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter ad2 = new SimpleCursorAdapter(getActivity(),R.layout.animais_list, cursor2, from1, to1);
		lstResult.setAdapter(ad2);
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View arg0) {

	}

}
