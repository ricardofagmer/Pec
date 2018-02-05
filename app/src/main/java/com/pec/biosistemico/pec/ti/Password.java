package com.pec.biosistemico.pec.ti;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.pec.biosistemico.pec.R;


@SuppressLint("ValidFragment")
public class Password extends DialogFragment implements DialogInterface.OnClickListener {

    private Button btnAcessar;
    private EditText txtSenha;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog);

    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.password, container);

        btnAcessar = (Button)view.findViewById(R.id.btnAcessar);
        txtSenha = (EditText)view.findViewById(R.id.txtPass);

        btnAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtSenha.getText().toString().equals("ibsconn@3026")){

                    Intent i = new Intent(getActivity(),FrmSql.class);
                    startActivity(i);

                }
                else {

                    Util util = new Util();
                    util.MessageBox(getActivity(),"Senha inválida!");
                }
            }
        });



        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


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

    }

}
