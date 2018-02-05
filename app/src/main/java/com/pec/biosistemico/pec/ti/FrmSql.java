package com.pec.biosistemico.pec.ti;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.main;

public class FrmSql extends AppCompatActivity {

    private Button btnSql;
    private EditText txtSql;
    private Util util;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_sql);

        btnSql = (Button)findViewById(R.id.btnSql);
        txtSql = (EditText)findViewById(R.id.txtSql);
        db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);


        btnSql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtSql.getText().toString().equals("")){

                    Toast.makeText(FrmSql.this,"Não seja idiota antes informe o comando SQL",Toast.LENGTH_LONG).show();
                }
                else {

                    String sql = txtSql.getText().toString();
                    if(sql.contains("drop") || sql.contains("DROP"))
                    {
                        Toast.makeText(FrmSql.this,"Drop não são permitidos.",Toast.LENGTH_LONG).show();
                    }
                    else {

                        alert("Confirma a execução do comando SQL?", 0, sql);
                    }
                }

            }
        });
    }

    public void alert(String msg, final Integer tipo, final String cmd){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        try {
                            db.execSQL(cmd);
                            Toast.makeText(FrmSql.this,"Comando executado com sucesso.", Toast.LENGTH_LONG).show();

                        } catch (SQLException ex) {

                            Toast.makeText(FrmSql.this,ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                }
            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg).setPositiveButton("Sim", dialogClickListener)
                .setNegativeButton("Não", dialogClickListener).show();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(FrmSql.this, main.class);
        startActivity(intent);

        super.onBackPressed();
    }
}
