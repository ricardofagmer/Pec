package com.pec.biosistemico.pec.fechamento;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.impressao.PrinterActivity;
import com.pec.biosistemico.pec.popUp.Recomendacao;
import com.pec.biosistemico.pec.popUp.SituacaoEncontrada;
import com.pec.biosistemico.pec.util.Global;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.sql.Date;

public class ReportFinaly extends Fragment implements OnClickListener, SeekBar.OnSeekBarChangeListener {

    String localDaFoto;
    Uri localFotoUri;
    ImageView img;
    Bitmap selected;
    private Button btnSelect;
    private CheckBox chkImpressao;
    private SeekBar bar;
    private TextView lblNumero;
    private Global mDados = Global.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fechamentofragment_item_detail, null);
        final Intent i;
        final int cons = 0;

        final EditText txtConsultor = (EditText) view.findViewById(R.id.txtConsultor);

        final Button btnSituacao = (Button) view.findViewById(R.id.btnSituacao);
        final Button btnRecomendacao = (Button) view.findViewById(R.id.btnRecomendacao);
        final Button btnSelect = (Button) view.findViewById(R.id.btnSelect);
        chkImpressao = (CheckBox) view.findViewById(R.id.checkImpressora);
        lblNumero = (TextView) view.findViewById(R.id.lblNumero);

        mDados.setEdicao(false);

        bar = (SeekBar) view.findViewById(R.id.seekBar);
        bar.setProgress(2);

        lblNumero.setText("Horas de atendimento: 02");




        txtConsultor.setEnabled(false);

        try {
            SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("SELECT NOME FROM LOGIN", null);
            cursor.moveToFirst();
            txtConsultor.setText(cursor.getString(cursor.getColumnIndex("nome")).toUpperCase());
        } catch (Exception e) {
            MessageBox(e.getMessage());
        }

        String print_name = "";
        String nome = txtConsultor.getText().toString().toUpperCase();
        int contador = nome.length();

        for (int p = 0; p < contador; p++) {

            if (nome.substring(p, p + 1).equals(" ")) {
                int posicao = p + 1;
                print_name = nome.substring(0, posicao);
            }
        }


        Global mDados = Global.getInstance();
        if (mDados.getStart() == 0) {
            MessageBox(print_name + "antes de preencher o fechamento responda o CHECKLIST");
            btnRecomendacao.setEnabled(false);
            btnSituacao.setEnabled(false);
        }
        else{

            btnRecomendacao.setEnabled(true);
            btnSituacao.setEnabled(true);
        }

        img = (ImageView) view.findViewById(R.id.imagem);

        if (mDados.getCount_animais() != mDados.getAnimais_verificado()) {

            btnRecomendacao.setEnabled(false);
            btnSituacao.setEnabled(false);
            MessageBox(print_name + " antes de realizar o fechamento do atendimento é necessário conferir todo o rebanho");
        }

        if (mDados.getVerfica_parto() != 0) {
            MessageBox(print_name + " existem animais que o parto deve ser verificado, retorne ao paper top e verfique as linhas em amarelo");
            btnRecomendacao.setEnabled(false);
            btnSituacao.setEnabled(false);
        }

        Button btnSalvar = (Button) view.findViewById(R.id.btnSalvarFechamento);
        //Button btnCapturar = (Button) view.findViewById(R.id.btnCaptura);

		/*btnCapturar.setOnClickListener(new OnClickListener() {
            @Override
			public void onClick(View v) {

				try {

					localDaFoto = Environment.getExternalStorageDirectory()
							+ "/" + System.currentTimeMillis() + ".jpg";

					File arquivo = new File(localDaFoto);
					localFotoUri = Uri.fromFile(arquivo);

					Intent irParaCamera = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);

					irParaCamera
							.putExtra(MediaStore.EXTRA_OUTPUT, localFotoUri);
					startActivityForResult(irParaCamera, 123456);

				}

				catch (Exception ex) {
					Toast.makeText(getActivity(), ex.getMessage(),
							Toast.LENGTH_LONG).show();
				}

				// Intent i = new
				// Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				// startActivityForResult(i,cons);
			}
		});*/

        bar.setOnSeekBarChangeListener(this);

        btnRecomendacao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    popUpRecomendacao(view);
                } catch (Exception e) {
                    MessageBox(e.getMessage());
                }
            }
        });

        btnSituacao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    popUpSituacao(view);
                } catch (Exception e) {
                    MessageBox(e.getMessage());
                }

            }
        });


        btnSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent = new Intent(Intent.ACTION_PICK, Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    MessageBox(e.getMessage());
                }


            }
        });

        btnSalvar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
                try {


                   /* if (localDaFoto == null) {
                        MessageBox("FOTO OBRIGATÓRIO!");
                    }*/
                    if (btnRecomendacao.getText().toString().equals("RECOMENDAÇÃO")) {

                        MessageBox("Recomendação obrigatório");
                    }

                    if (btnSituacao.getText().toString().equals("SITUAÇÃO ENCONTRADA")) {

                        MessageBox("Situação encontrada é necessário");
                    } else {

                        Date data = new Date(System.currentTimeMillis());

                        Global mDados = Global.getInstance();
                        int cliente = mDados.getCliente();
                        int projeto = mDados.getProjeto();
                        String grupo = mDados.getUsuario();
                        int produtor = mDados.getProdutor();

                        int x = mDados.getLastID();

                        StringBuilder Sql = new StringBuilder();
                        Sql.append("UPDATE CHECKLIST ");
                        Sql.append("SET consultor = '" + txtConsultor.getText() + "' WHERE _ID = " + x + "");

                        String sql = Sql.toString();
                        db.execSQL(sql);

                        MessageBox("Atendimento Salvo e Finalizado!");

                        mDados.setStart(0);
                        mDados.setRespondido(0);

                        if (chkImpressao.isChecked()) {
                            Intent obtnptodos = new Intent(getActivity(), PrinterActivity.class);
                            startActivity(obtnptodos);
                        } else {
                            Intent obtnptodos = new Intent(getActivity(), print.class);
                            startActivity(obtnptodos);
                        }

                    }
                } catch (Exception ex) {
                    MessageBox("ERRO CONTATE O TI IBS: " + ex.getMessage());
                }
            }
        });

        return view;
    }

    public void popUpRecomendacao(View view) {
        try {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Recomendacao cdf = new Recomendacao(1, 1);
            cdf.show(ft, "DG");
        } catch (Exception e) {
            MessageBox(e.getMessage());
        }
    }

    public void popUpSituacao(View view) {

        try {

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            SituacaoEncontrada cdf = new SituacaoEncontrada(1, 1);
            cdf.show(ft, "DG");
        } catch (Exception e) {
            MessageBox(e.getMessage());
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {

        lblNumero.setText("Horas de atendimento: " + progress);
        Global mDados = Global.getInstance();
        mDados.setQtdadeAT(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        seekBar.setProgress(2);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {

            switch (requestCode) {
                case 1:
                    if (resultCode == getActivity().RESULT_OK) {

                        localDaFoto = Environment.DIRECTORY_PICTURES + "/"
                                + System.currentTimeMillis() + ".jpg";

                        Uri uri = data.getData();
                        String[] proejection = {Images.Media.DATA};

                        Cursor c = getActivity().getContentResolver().query(uri,
                                proejection, null, null, null);
                        c.moveToFirst();

                        int columnIndex = c.getColumnIndex(proejection[0]);
                        String filePath = c.getString(columnIndex);
                        c.close();

                        selected = BitmapFactory.decodeFile(filePath);
                        @SuppressWarnings("deprecation")
                        Drawable d = new BitmapDrawable(selected);

                        if (uri != null) {

                            img.setImageURI(uri);
                        }


                        Date dataAtual = new Date(System.currentTimeMillis());
                        Global x = Global.getInstance();

                        String z = RetornaProdutor(x.getProdutor());

                        uri = Uri.fromFile(new File(filePath));

                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("image/*");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, dataAtual.toString() + "_" + z + ".jpg");
                        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(Intent.createChooser(sharingIntent, "Salvar"));
                    }

                    break;

                case 123456:

                    Bitmap bmp;
                    ImageView img = (ImageView) getView().findViewById(R.id.imagem);

                    super.onActivityResult(requestCode, resultCode, data);
                    // if(requestCode == Activity.RESULT_OK){

                    // Bundle ext = data.getExtras();
                    // bmp = (Bitmap)ext.get("data");

                    if (localFotoUri != null) {

                        img.setImageURI(localFotoUri);
                    }

                default:
                    break;
            }
        } catch (Exception ex) {

            MessageBox(ex.getMessage());
        }

    }


    public void verifyField() {

        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
    }


    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public String BrokeLine(String s) {

        int l = s.length();
        String y = "";
        for (int i = 0; i < l; i += 70) {
            int z = 70;
            if (s.length() < z) {
                z = s.length();
            }
            y = y + s.substring(0, z) + "\n";
            s = s.substring(z);
        }

        return y;
    }

    public String RetornaCliente(int _id) {
        String result = "";
        try {


            SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",
                    Context.MODE_PRIVATE, null);

            Cursor cursor = db.rawQuery("SELECT NOME FROM PRODUTOR WHERE ID = "
                    + _id + "", null);
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("nome"));
        } catch (Exception e) {
            MessageBox(e.getMessage());
        }

        return result;
    }

    public String RetornaProjeto(int _id) {

        String result = "";

        try {
            SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",
                    Context.MODE_PRIVATE, null);

            Cursor cursor = db.rawQuery("SELECT NOME FROM PROJETO WHERE ID = "
                    + _id + "", null);
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("nome"));
        } catch (Exception e) {
            MessageBox(e.getMessage());
        }

        return result;
    }

    public String RetornaGrupo(int _id) {

        String result = "";
        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",
                Context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT NOME FROM GRUPO WHERE ID = " + _id
                + "", null);
        cursor.moveToFirst();
        result = cursor.getString(cursor.getColumnIndex("nome"));

        return result;
    }

    public String RetornaProdutor(int _id) {

        String result = "";

        try {
            SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",
                    Context.MODE_PRIVATE, null);

            Cursor cursor = db.rawQuery("SELECT NOME FROM PRODUTOR WHERE ID = " + _id + "", null);
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("nome"));
        } catch (Exception e) {
            MessageBox(e.getMessage());
        }

        return result;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

    }

    @Override
    public void onClick(View v) {
    }

    public void copyFile() {
        Log.i("info", "in copy file at finally");
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "/data/com.pec.biosistemico/files/pec.txt";
                String backupDBPath = "pec.txt";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            Log.i("info", "in file ");
        }
    }

    public void MessageBox(String msg) {
        AlertDialog.Builder informa = new AlertDialog.Builder(getActivity());
        informa.setTitle("Alerta!").setMessage(msg);
        informa.setNeutralButton("Fechar", null).show();
    }

}
