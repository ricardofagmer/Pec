package com.pec.biosistemico.pec.popUp;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.pec.biosistemico.pec.util.Global;
import com.pec.biosistemico.pec.R;

@SuppressLint("ValidFragment")
public class SituacaoEncontrada  extends DialogFragment implements DialogInterface.OnClickListener{


    private int numStyle;
    private int numTheme;
    private int day;
    private int month;
    private int year;
    private int x;
    private Calendar cal;
    private ImageButton btnData;
    private int produtor;
    private Spinner ddlTipoParto;
    private EditText txtSituacao;

    @SuppressLint("ValidFragment")
    public SituacaoEncontrada(int numStyle, int numTheme){
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
            case 1 : style = DialogFragment.STYLE_NO_TITLE;break;
            case 2 : style = DialogFragment.STYLE_NO_INPUT;break;
            case 3 : style = DialogFragment.STYLE_NO_FRAME;break;
            default: style = DialogFragment.STYLE_NORMAL;break;

        }

        switch(numTheme){
            case 1 : theme = android.R.style.Theme_DeviceDefault_Light_Dialog;break;
            case 2 : theme = android.R.style.Theme_Holo_Dialog;break;
            default: theme = android.R.style.Theme_Holo_Light_DarkActionBar;break;
        }

        setStyle(style, theme);
        setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.situacao_encontrada, container);

        Button btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        txtSituacao = (EditText)view.findViewById(R.id.txtSituacaoEncontrada);
        Button btnCancelar = (Button)view.findViewById(R.id.btnCancelar);
        Button btnLoad = (Button)view.findViewById(R.id.btnLoadLast);

        ddlTipoParto = (Spinner)view.findViewById(R.id.ddlSituacao);
        final Button btnClean = (Button)view.findViewById(R.id.btnClean);

        Global mDados = Global.getInstance();
        produtor = mDados.getProdutor();
        x = mDados.getLastID();

        List<String> OP = new ArrayList<String>();
        OP.add("REPRODUTIVO");
        OP.add("PRODUTIVO");
        OP.add("SANITARIO");
        OP.add("CRIATF");
        OP.add("FIV");
        OP.add("TE");

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, OP);
        ArrayAdapter<String> spinnerArrayAdapter1 = arrayAdapter1;
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        ddlTipoParto.setAdapter(spinnerArrayAdapter1);
        ddlTipoParto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {


                if(ddlTipoParto.getSelectedItemPosition() == 0)
                {
                    String sql = "Na propriedade em questão as vacas encontram-se com escore corporal médio de...  (1/ 1,5/2/2,5/3/3,5/4/4,5/5). A mineralização é feita de forma .... (correta/ incorreta) a ração concentrada.... (está/ nao está) sendo fornecidas as vacas de forma adequada." +
                            "Foram avaliadas......(quantidade de vacas avaliadas) dentre estas .....(quantidade em número) estão gestantes e .....( quantidade em número) estão nao gestantes.";

                    txtSituacao.setText(sql);
                }
                else
                if(ddlTipoParto.getSelectedItemPosition() == 2)
                {
                    String sql = "Na propriedade em questão foi realizado a analises do leite e coletado amostras para exames de CCS e CBT. Abaixo segue os números encontrados nas analises in Loco:"+
                            "Dornic ......(número) \n"+
                            "Gordura .... (número em %)\n"+
                            "Proteína ....(número em %)\n"+
                            "EST..........(número)\n"+
                            "ESD .........(número)\n";

                    txtSituacao.setText(sql);
                }

                else
                if(ddlTipoParto.getSelectedItemPosition() == 3){

                    String sql = "Na propriedade em questão as vacas foram avaliadas para serem inseminadas e estas se encontram com escore corporal.....(bom/médio/ruim). A mineralização destes animais....(estão/ nao estão) de acordo com as necessidades. Para este trabalho foram protocoladas....(números) vacas para Inseminação e o D8.....(foi/ nao foi) realizado de forma satisfatória.";

                    txtSituacao.setText(sql);
                }
                else
                    txtSituacao.setText("");

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        btnClean.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                txtSituacao.setText("");
            }
        });

        btnLoad.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                LoadSituation();

            }
        });



        btnSalvar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtSituacao.getText().equals("")) {

                    txtSituacao.setText("Favor informar a situação encontrada");
                    Toast.makeText(getActivity().getBaseContext(), "Favor informar a situação encontrada" ,Toast.LENGTH_LONG).show();

                } else {

                    try {

                        Global mDados = Global.getInstance();
                        int x = mDados.getLastID();

                        String situacao =  convertUTF8toISO(txtSituacao.getText().toString());

                        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
                        db.execSQL("UPDATE CHECKLIST SET situacaoEncontrada = '"+ situacao +"', tipoReprodutivo = '"+ddlTipoParto.getSelectedItem().toString()+"' WHERE _ID = "+x+"");

                        if (mDados.isEdicao() == false) {

                            Button btnSituacao = (Button) getActivity().findViewById(R.id.btnSituacao);
                            btnSituacao.setTextColor(getResources().getColor(R.color.ORANGE));
                            btnSituacao.setText("SITUAÇÃO ENCONTRADA - OK");
                        }

                        //dismiss();
                        Toast.makeText(getActivity().getBaseContext(), "SITUAÇÃO SALVA",Toast.LENGTH_LONG).show();

                    }

                    catch (Exception ex){

                        Toast.makeText(getActivity().getBaseContext(), ex.getMessage(),Toast.LENGTH_LONG).show();
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

    public void LoadSituation(){

        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT situacaoEncontrada, tipoReprodutivo FROM CHECKLIST WHERE _ID = "+x+"",null);
        if(cursor.moveToFirst()){

            txtSituacao.setText(cursor.getString(cursor.getColumnIndex("situacaoEncontrada")));

            String o = cursor.getString(cursor.getColumnIndex("tipoReprodutivo"));

            if(o != null) {

                if (o.equals("REPRODUTIVO")) {

                    ddlTipoParto.setSelection(0);
                }
                if (o.equals("PRODUTIVO")) {
                    ddlTipoParto.setSelection(1);
                }
                if (o.equals("SANITARIO")) {
                    ddlTipoParto.setSelection(2);
                }
                if (o.equals("CRIATF")) {
                    ddlTipoParto.setSelection(3);
                }
                if (o.equals("FIV")) {
                    ddlTipoParto.setSelection(4);
                }
                if (o.equals("TE")) {
                    ddlTipoParto.setSelection(5);
                }
            }

        }
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

    public static String convertUTF8toISO(String str) {
        String ret = null;
        try {
            ret = new String(str.getBytes("UTF-8"), "ISO-8859-1");
        }
        catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return ret;
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

