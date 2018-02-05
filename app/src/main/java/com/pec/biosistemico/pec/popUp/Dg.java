package com.pec.biosistemico.pec.popUp;


import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Exchanger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.paperTop.Vaca;
import com.pec.biosistemico.pec.paperTop.VacaAdapter;
import com.pec.biosistemico.pec.util.Global;

@SuppressLint("ValidFragment")
public class Dg extends DialogFragment implements DialogInterface.OnClickListener {

    private int numStyle;
    private int numTheme;
    private int day;
    private int month;
    private int year;
    private Calendar cal;
    private ImageButton btnData;
    private int produtor;

    @SuppressLint("ValidFragment")
    public Dg(int numStyle, int numTheme) {
        this.numStyle = numStyle;
        this.numTheme = numTheme;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        int style;
        int theme;

        switch (numStyle) {

            case 1:
                style = DialogFragment.STYLE_NO_TITLE;
                break;
            case 2:
                style = DialogFragment.STYLE_NO_INPUT;
                break;
            case 3:
                style = DialogFragment.STYLE_NO_FRAME;
                break;
            default:
                style = DialogFragment.STYLE_NORMAL;
                break;

        }

        switch (numTheme) {
            case 1:
                theme = android.R.style.Theme_DeviceDefault_Light_Dialog;
                break;
            case 2:
                theme = android.R.style.Theme_Holo_Dialog;
                break;
            default:
                theme = android.R.style.Theme_Holo_Light_DarkActionBar;
                break;
        }

        setStyle(style, theme);
        setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.dg, container);
        Button btnSalvarDG = (Button) view.findViewById(R.id.btnSalvarN);
        final EditText txtDtDG = (EditText) view.findViewById(R.id.txtNomeN);

        txtDtDG.setEnabled(false);

        Button btnCancelar = (Button) view.findViewById(R.id.btnCancelaN);
        final Spinner ddlStaus = (Spinner) view.findViewById(R.id.ddlStatusProduvitoDG);
        final EditText txtNDias = (EditText) view.findViewById(R.id.txtDtNascN);

        btnData = (ImageButton) view.findViewById(R.id.btnDataCalendar);
        btnData.setVisibility(View.INVISIBLE);

        Date data_coleta = new Date(System.currentTimeMillis());
        SimpleDateFormat formatBR = new SimpleDateFormat("dd/MM/yyyy");
        String dFormated = formatBR.format(data_coleta);

        txtDtDG.setText(dFormated);

        Global mDados = Global.getInstance();
        produtor = mDados.getProdutor();

        //txtDtDG.addTextChangedListener(Mask.insert("##/##/####",txtDtDG));

        List<String> OP1 = new ArrayList<String>();
        OP1.add("PRENHE");
        OP1.add("VAZIA");
        OP1.add("VAZIA_IATF");
        OP1.add("RT");

        // PASSANDO O ARRAYLIST LITROS
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, OP1);
        ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        final Spinner ddlStatus = (Spinner) view.findViewById(R.id.ddlStatusProduvitoDG);
        final TextView lblMsg = (TextView) view.findViewById(R.id.lblmessageDG);
        ddlStatus.setAdapter(spinnerArrayAdapter);

        btnData.setOnClickListener(new OnClickListener() {

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
                                txtDtDG.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, year, month, day);
                dpd.show();
            }
        });

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ddlStatus.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                String status = ddlStaus.getSelectedItem().toString();

                if (status == "PRENHE") {

                    txtNDias.setText("");
                    txtNDias.setEnabled(true);
                }

                if (status == "VAZIA" || status == "RT" || status == "VAZIA_IATF") {

                    txtNDias.setText("0");
                    txtNDias.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        btnSalvarDG.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                    format1.setLenient(false);
                    Date dataDG = null;
                    int i = 0;

                    if (txtNDias.getText().toString().equals("")) {

                        lblMsg.setText("Número de dias é necessário!");
                    } else {

                        int dias = Integer.parseInt(txtNDias.getText().toString());

                        if (dias < 28 && ddlStatus.getSelectedItem().toString().equals("PRENHE")) {

                            lblMsg.setText("Número de dias inválido, minímo de 28 dias");
                        } else {


                            try {
                                dataDG = new Date(format1.parse(txtDtDG.getText().toString()).getTime());
                                i = 1;
                            } catch (ParseException e) {
                                lblMsg.setText("Data do DG inválida!");
                                i = 0;
                            }

                            if (txtNDias.getText().toString().equals("") || txtDtDG.getText().toString().equals("")) {

                                lblMsg.setText("*Número de dias e data do DG são necessários");
                            } else if (i == 0) {
                                lblMsg.setText("Data do DG inválida!");
                            } else {

                                Global x = Global.getInstance();
                                SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

                                //String xx = "UPDATE PAPERPORT SET dataDiagnostico ='"+txtDtDG.getText()+"', statusProdutivo = '"+ddlStaus.getSelectedItem().toString()+"', numeroDias = '"+txtNDias.getText()+"' WHERE _ID = "+x.getIDProdutor();

                                java.util.Date dtParto = null;
                                Date dtDG = null;
                                String data = txtDtDG.getText().toString();


                                try {

                                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                                    Date data1 = new Date(format.parse(data).getTime());

                                    dtDG = data1;
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                                int result = 280 - Integer.parseInt(txtNDias.getText().toString());
                                dtParto = somaDias(dtDG, result);

                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                String formatted = format.format(dtParto);

                                Date data_coleta = new Date(System.currentTimeMillis());

                                SimpleDateFormat formatBR = new SimpleDateFormat("dd/MM/yyyy");
                                String dFormated = formatBR.format(data_coleta);

                                String status = ddlStaus.getSelectedItem().toString();

                                if (status == "VACA PRENHA" || status == "PRENHE") {

                                    db.execSQL("UPDATE PAPERPORT SET verifica = 'OK',  data_coleta = '" + dFormated.toString() + "', previsaoParto = '" + formatted.toString() + "', dataDiagnostico ='" + txtDtDG.getText() + "', categoria = '" + ddlStaus.getSelectedItem().toString() + "', statusProdutivo = '" + ddlStaus.getSelectedItem().toString() + "', numeroDias = " + txtNDias.getText() + ", MODIFICADO = 'SIM', ENVIADO = 'NAO' WHERE _ID = " + x.getIdPaper() + "");
                                }

                                if (status == "VAZIA" || status == "RT" || status == "VAZIA_IATF") {

                                    if(status.equals("VAZIA_IATF")){

                                        status = "VAZIA_IATF";
                                    }

                                    db.execSQL("UPDATE PAPERPORT SET verifica = 'OK', data_coleta = '" + dFormated.toString() + "',previsaoParto = '', dataDiagnostico ='" + txtDtDG.getText() + "', categoria = '" + status + "', statusProdutivo = '" + ddlStaus.getSelectedItem().toString() + "', numeroDias = " + txtNDias.getText() + ", MODIFICADO = 'SIM', ENVIADO = 'NAO' WHERE _ID = " + x.getIdPaper() + "");
                                }

                                RefreshList();

                                dismiss();
                            }

                        }
                    }

                }
                catch (Exception ex){
                    lblMsg.setText(ex.getMessage());
                }



            }
        });

	
	/*btnDtParto.setOnClickListener(new View.OnClickListener() {	

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
							EditText txtDtNasc = (EditText)view.findViewById(R.id.txtNascimento);
							// Display Selected date in textbox
							txtDtNasc.setText(dayOfMonth + "/"+ (monthOfYear + 1) + "/" + year);

						}
					}, year, month, day);
			dpd.show();			
		}
	});	*/

        btnCancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

        return (view);

    }

    public void RefreshList() {


        Global mDados = Global.getInstance();
        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
        ArrayList<Vaca> vacas = new ArrayList<Vaca>();
        ListView lv = (ListView) getActivity().findViewById(R.id.listView1);

        TextView lblDG = (TextView)getActivity().findViewById(R.id.lblDG);
        TextView lblParto = (TextView)getActivity().findViewById(R.id.lblParto);

        produtor = mDados.getProdutor();

        Cursor cursor1 = db
                .rawQuery(
                        "SELECT DISTINCT numeroDias,p.verifica,p.dataParto, p.dataDiagnostico, p._id as _id,p.previsaoParto, p.categoria, p.data_secagem,p.modificado,  "
                                + "p.manejo, p.peso_atual, p.id as id,  proj.nome as _projeto, grup.nome as _grupo, produ.nome as _propriedade, "
                                + "p.data_coleta,p.area_atual,p.brinco,p.nome_usual,p.status,p.data_secagem,p.obs,p.ocorrencia,p.referencia "
                                + "FROM paperPort p, projeto proj, grupo grup, produtor produ "
                                + "WHERE p._projeto = proj.id AND p._grupo = grup.id AND p._propriedade = produ.id AND p._propriedade = "
                                + produtor + " ORDER BY p.nome_usual", null);

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
            vaca.setVerifica(cursor1.getString(cursor1.getColumnIndex("verifica")));

            vacas.add(vaca);

            mDados.setCount_animais(mDados.getCount_animais() + 1);


            lblParto.setText("Parto feito em: " + cursor1.getString(cursor1.getColumnIndex("dataParto")));
            lblDG.setText("Diagnóstico: " +  cursor1.getString(cursor1.getColumnIndex("dataDiagnostico")) );
        }

        final VacaAdapter v;
        v = new VacaAdapter(getActivity(), vacas);
        lv.setAdapter(v);

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
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
	
	/*@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		super.onCreateDialog(savedInstanceState);
		
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity()).setTitle("Data do Parto")
				.setIcon(R.drawable.top)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Toast.makeText(getActivity(), "Ok pressed", Toast.LENGTH_LONG).show();
					}
				}).setNegativeButton("Sair",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					dismiss();
						
					}
				});
		
		
		return(alert.show());
	}*/

    public java.util.Date somaDias(Date data, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(data);
        cal.add(Calendar.DAY_OF_MONTH, dias);
        return cal.getTime();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
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
    public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub

    }


}
