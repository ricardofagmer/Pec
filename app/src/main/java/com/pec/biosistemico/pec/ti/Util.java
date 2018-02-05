package com.pec.biosistemico.pec.ti;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Ricardo on 14/06/2017.
 */

public class Util extends Activity {


    public void MessageBox(Context context, String msg) {

        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }



    public void alert(String msg, final Integer tipo, Context context){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg).setPositiveButton("Sim", dialogClickListener)
                .setNegativeButton("Não", dialogClickListener).show();
    }
}
