package com.pec.biosistemico.pec.popUp;

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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.pec.biosistemico.pec.util.Global;
import com.pec.biosistemico.pec.R;


@SuppressLint("ValidFragment")
public class Recomendacao  extends DialogFragment implements DialogInterface.OnClickListener{

    private int numStyle;
    private int numTheme;
    private int day;
    private int month;
    private int year;
    private Calendar cal;
    private ImageButton btnData;
    private int produtor;


    @SuppressLint("ValidFragment")
    public Recomendacao(int numStyle, int numTheme){
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
        View view = inflater.inflate(R.layout.recomendacao, container);

        Button btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        final EditText txtRecomendacao = (EditText)view.findViewById(R.id.txtRec);
        Button btnCancelar = (Button)view.findViewById(R.id.btnCancelar);
        final ImageButton btnDataImage = (ImageButton)view.findViewById(R.id.imageButton2);

        final Global mDados = Global.getInstance();
        produtor = mDados.getProdutor();
        int x = mDados.getLastID();

        Date data = new Date(System.currentTimeMillis());

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String formatted = format.format(data);
        final TextView txtDt = (TextView)view.findViewById(R.id.lblDtRel);

        txtDt.setText(formatted.toString());

        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT recomendacoes FROM CHECKLIST WHERE _ID = "+x+"",null);
        if(cursor.moveToFirst()){

            txtRecomendacao.setText(cursor.getString(cursor.getColumnIndex("recomendacoes")));
        }


        btnDataImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                                txtDt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                mDados.setDataRelatorio(txtDt.getText().toString());

                            }
                        }, year, month, day);
                dpd.show();


            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtRecomendacao.getText().equals("")) {

                    txtRecomendacao.setText("Favor informar a recomendação");
                    Toast.makeText(getActivity().getBaseContext(), "Favor informar a recomendação" ,Toast.LENGTH_LONG).show();

                }
                else {

                    try {

                        Global mDados = Global.getInstance();
                        int x = mDados.getLastID();
                        mDados.setDataRelatorio(txtDt.getText().toString());

                        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
                        db.execSQL("UPDATE CHECKLIST SET recomendacoes = '"+ txtRecomendacao.getText().toString()+"' WHERE _ID = "+x+"");

                        if(mDados.isEdicao() == false) {
                            Button btnRecomendacao = (Button) getActivity().findViewById(R.id.btnRecomendacao);
                            btnRecomendacao.setTextColor(getResources().getColor(R.color.ORANGE));
                            btnRecomendacao.setText("RECOMENDAÇÃO - OK");
                        }
                       // dismiss();

                        Toast.makeText(getActivity().getBaseContext(),"RECOMENDAÇÃO SALVA",Toast.LENGTH_LONG).show();

                    }

                    catch (Exception ex){

                        Toast.makeText(getActivity().getBaseContext(), ex.getMessage(),Toast.LENGTH_LONG).show();
                    }

                }
            }

        });



        btnCancelar.setOnClickListener(new View.OnClickListener() {
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


