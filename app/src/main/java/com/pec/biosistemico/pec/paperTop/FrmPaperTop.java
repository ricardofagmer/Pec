package com.pec.biosistemico.pec.paperTop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.main;
import com.pec.biosistemico.pec.paperTop.financeiro.FrmFinanceiro;
import com.pec.biosistemico.pec.popUp.Animais;
import com.pec.biosistemico.pec.popUp.Dg;
import com.pec.biosistemico.pec.popUp.Parto;
import com.pec.biosistemico.pec.popUp.TrocaProdutor;
import com.pec.biosistemico.pec.util.Global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FrmPaperTop extends Fragment implements OnClickListener {

    private Calendar cal;
    private int day;
    private int month;
    private int year;
    private EditText UltimoParto;
    private EditText media;
    private TextView txtDtColeta;
    private EditText Brinco;
    private TextView DataColeta;
    private EditText DataSecagem;
    private TextView Referencia;
    private EditText AreaAtual;
    private Spinner Manejo;
    private EditText NomeUsual;
    private TextView txtReferencia;
    private EditText txtUltimPeso;
    private EditText PesoAtual;
    private Spinner ddlStatus;
    private Spinner ddlprodutor;
    private Spinner ddlOcorrencia;
    private ImageButton btnData;
    private ImageButton btnData2;
    private Button btnChamaDG;
    private Button btnNovo;
    private Button btnChamaNovo;
    private Button btnChamaParto;
    private String _id;
    public int positionList = 0;
    private int produtor;
    private int stPosition;
    private SQLiteDatabase db;
    private TextView lblProdutor;
    private EditText txtMedia;
    private String status;
    private FloatingActionMenu fab;
    private VacaAdapter adapter;
    private boolean ativo;
    private boolean ativo1;
    private TextView lblDG;
    private TextView lblparto;
    private String prenha = "";
    private String brinco = "", nome = "", _status = "", ocorrencia = "";
    private Button btnVerificarParto;
    private Button btnOk;
    private CheckBox chkVerifica;
    private int totalAnimais = 0;
    private TextView lblTotalAnimais;

    Global mDados = Global.getInstance();

    ListView lv;
    int linhas;
    private int[] colors = new int[]{0x30FF0000, 0x300000FF};

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.paperfragment_item_detail, null);
        db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        ativo = false;
        ativo1 = false;

        String Grupo = mDados.getUsuario();
        int Projeto = mDados.getProjeto();
        produtor = mDados.getProdutor();

        DataSecagem = (EditText) view.findViewById(R.id.txtDataSecagem);
        DataSecagem.setEnabled(true);

        //UltimoParto = (EditText) view.findViewById(R.id.txtUltimoParto);
        //UltimoParto.setEnabled(false);

        Date data = new Date(System.currentTimeMillis());

        //txtUltimPeso = (EditText) view.findViewById(R.id.txtUltimoPeso);

        Date data_coleta = new Date(System.currentTimeMillis());
        SimpleDateFormat formatBR = new SimpleDateFormat("dd/MM/yyyy");
        final String data_formatada = formatBR.format(data_coleta);

        txtDtColeta = (TextView) view.findViewById(R.id.txtDtColeta);
        txtDtColeta.setEnabled(true);
        txtDtColeta.setText("Data Coleta:  " + data_formatada.toString());

        txtReferencia = (TextView) view.findViewById(R.id.txtReferencia);
        txtReferencia.setEnabled(false);
        txtReferencia.setText("Referência: " + data_formatada.toString());

        txtMedia = (EditText) view.findViewById(R.id.txtMedia);

        lblDG = (TextView)view.findViewById(R.id.lblDG);
        lblparto = (TextView)view.findViewById(R.id.lblParto);
        lblTotalAnimais = (TextView)view.findViewById(R.id.txtTotalAnimais);

        lblProdutor = (TextView) view.findViewById(R.id.txtProdutor);
        lblProdutor.setText("Produtor: " + RetornaProdutor(produtor));

        ddlOcorrencia = (Spinner) view.findViewById(R.id.ddlOcorrencia);
        Referencia = (TextView) view.findViewById(R.id.txtReferencia);
        //AreaAtual = (EditText) view.findViewById(R.id.txtAreaAtual);
        Brinco = (EditText) view.findViewById(R.id.txtBrinco);

        btnVerificarParto = (Button)view.findViewById(R.id.btnVerificarParto);
        btnOk = (Button)view.findViewById(R.id.btnOk);

        NomeUsual = (EditText) view.findViewById(R.id.txtConsultor);
        PesoAtual = (EditText) view.findViewById(R.id.txtPesoAtual);

        media = (EditText) view.findViewById(R.id.txtMedia);

        lv = (ListView) view.findViewById(R.id.listView1);
        lv.setTextFilterEnabled(true);
        lv.setSelected(true);

        //ddlprodutor = (Spinner) view.findViewById(R.id.ddl1);
        ddlStatus = (Spinner) view.findViewById(R.id.ddlVacina);
        btnData = (ImageButton) view.findViewById(R.id.btnData);
        btnData.setEnabled(true);
        btnData2 = (ImageButton) view.findViewById(R.id.btnData2);
        btnChamaDG = (Button) view.findViewById(R.id.btnDg);
        //btnChamaNovo = (Button) view.findViewById(R.id.btnNovoAnimal);
        btnChamaParto = (Button) view.findViewById(R.id.btnParto);
        btnNovo = (Button)view.findViewById(R.id.btnNovo);

        chkVerifica = (CheckBox)view.findViewById(R.id.chkVerifica);

        chkVerifica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(chkVerifica.isChecked())
                {
                    RefreshListVerifica();
                }
                else{
                    RefreshList();
                }
            }
        });

        //txtUltimPeso.setEnabled(false);

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        List<String> OP1 = new ArrayList<String>();
        OP1.add("NENHUMA");
        OP1.add("MORTE");
        OP1.add("DESCARTE");
        OP1.add("VENDA");

        // PASSANDO O ARRAYLIST LITROS
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, OP1);
        ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        final Spinner ddlOcorrencia = (Spinner) view.findViewById(R.id.ddlOcorrencia);
        ddlOcorrencia.setAdapter(spinnerArrayAdapter);

        List<String> OP = new ArrayList<String>();
        OP.add("LA");
        OP.add("SECA");
        // OP.add("REPRODUTIVO");

        // PASSANDO O ARRAYLIST LITROS
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, OP);
        ArrayAdapter<String> spinnerArrayAdapter1 = arrayAdapter1;
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        ddlStatus.setAdapter(spinnerArrayAdapter1);

        ddlOcorrencia.setPrompt("Ocorrência");
        ddlStatus.setPrompt("Status");

        RefreshList();

        TextView grupo = (TextView) view.findViewById(R.id.txtGrupo);
        TextView projeto = (TextView) view.findViewById(R.id.txtProjeto);

        grupo.setText("Grupo: " + RetornaNomeGrupo(Integer.parseInt(Grupo)));
        projeto.setText("Projeto: " + RetornaNomeProjeto(Projeto));

        if (savedInstanceState != null) {

            lv.setSelection(savedInstanceState.getInt("lv", 0));
        }

        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                Builder informa = new Builder(getActivity());
                informa.setTitle("ATENÇÃO!" + mDados.getConsultor().toUpperCase()).setMessage("Confirma a existência de todos os animais no rebanho não modificados por você no sistema?");
                informa.setCancelable(true)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                    db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

                                    db.execSQL("UPDATE PAPERPORT SET VERIFICA = 'OK' WHERE _propriedade = "+ produtor +"");
                                    RefreshList();
                                    Toast.makeText(getActivity(),"Rebanho verificado e confirmado",Toast.LENGTH_LONG).show();

                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = informa.create();
                alert.show();
            }

        });

        btnVerificarParto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                MessageBox("Consultor favor verificar os animais em amarelo pois há incoerências nesta propriedade");
                RefreshListVerificaParto();
            }
        });

        btnNovo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialogFragmentAnimais(view);

            }
        });


        btnChamaDG.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (NomeUsual.getText().toString().equals("") && Brinco.getText().toString().equals(""))
                {
                    MessageBox("Selecione um animal antes de fazer o DG");
                }
                else
                if (prenha.equals("VACA PRENHA") || prenha.equals("PRENHE"))
                {
                    MessageBox("Realize o parto antes de Fazer o DG");
                }
                else
                {
                    openDialogFragmentDG(view);
                }
            }
        });


        txtMedia.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() || actionId == EditorInfo.IME_ACTION_DONE
                        || KeyEvent.KEYCODE_ENTER  == 66) {

                    try {

                        if (media.getText().toString().equals("")) {

                            MessageBox("Informe a litragem total para calcular a media");
                        } else {

                            String sql = "";

                            db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

                            float litros = Float.parseFloat(media.getText().toString());
                            float vacas = CountCow(produtor);

                            float total = litros / vacas;
                            DecimalFormat ff = new DecimalFormat("0.00");

                            String formatado = ff.format(total).replace(",", ".");

                            sql = "UPDATE PAPERPORT SET data_coleta= '" + data_formatada + "', PESO_ATUAL = '" + formatado + "', ENVIADO = 'NAO', MODIFICADO = 'SIM' WHERE status = 'LA' AND ocorrencia is null AND _propriedade = " + produtor + "";

                            PesoAtual.setText(String.valueOf(total));

                            db.execSQL(sql);
                            RefreshList();

                            MessageBox("Media de produção de leite atualizada: " + total + "");

                            hideKeyboard(txtMedia);
                        }

                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), ex.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }

                return false;
            }
        });


        NomeUsual.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() || actionId == EditorInfo.IME_ACTION_DONE || KeyEvent.KEYCODE_ENTER  == 66) {

                    try {

                        if(!NomeUsual.getText().toString().equals(nome)) {

                            db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
                            String sql = "";

                            sql = "UPDATE PAPERPORT SET data_coleta= '" + data_formatada + "', nome_usual ='" + NomeUsual.getText() + "', MODIFICADO = 'SIM', ENVIADO = 'NAO' WHERE _ID = " + _id + "";

                            db.execSQL(sql);

                            RefreshList();

                            Toast.makeText(getActivity(), "FrmPaperTop salvo com sucesso!", Toast.LENGTH_LONG).show();

                            hideKeyboard(NomeUsual);

                            db.close();

                        }

                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), ex.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }

                return false;
            }
        });


        PesoAtual.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() || actionId == EditorInfo.IME_ACTION_DONE || KeyEvent.KEYCODE_ENTER  == 66) {

                    try {

                        if (status == "SECA") {

                            MessageBox("O ANIMAL DEVE ESTAR LACTANTE PARA INSERIR PESAGEM");
                        }
                        else {

                            db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
                            String sql = "";

                            sql = "UPDATE PAPERPORT SET data_coleta= '" + data_formatada + "', PESO_ATUAL ='"
                                    + PesoAtual.getText()
                                    + "', MODIFICADO = 'SIM' , ENVIADO = 'NAO' WHERE _ID = " + _id
                                    + "";


                            db.execSQL(sql);

                            RefreshList();


                            Toast.makeText(getActivity(), "FrmPaperTop salvo com sucesso!", Toast.LENGTH_LONG).show();

                            hideKeyboard(PesoAtual);
                        }

                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), ex.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }


                return false;
            }
        });


        DataSecagem.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() || actionId == EditorInfo.IME_ACTION_DONE || KeyEvent.KEYCODE_ENTER  == 66) {

                    try {
                        db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
                        String sql = "";

                        if (DataSecagem.getText().toString().equals("")) {

                            sql = "UPDATE PAPERPORT SET data_coleta= '" + data_formatada + "', PESO_ATUAL ='"
                                    + DataSecagem.getText()
                                    + "', MODIFICADO = 'SIM' , ENVIADO = 'NAO' WHERE _ID = " + _id
                                    + "";
                        }

                        db.execSQL(sql);
                        db.close();

                        RefreshList();


                        Toast.makeText(getActivity(), "FrmPaperTop salvo com sucesso!", Toast.LENGTH_LONG).show();
                        hideKeyboard(DataSecagem);

                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), ex.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }
                return false;

            }
        });


        Brinco.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() || actionId == EditorInfo.IME_ACTION_DONE || KeyEvent.KEYCODE_ENTER  == 66) {

                    try {

                        if(!Brinco.getText().toString().equals(brinco)) {

                            db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
                            String sql = "";

                            sql = "UPDATE PAPERPORT SET data_coleta= '" + data_formatada + "', brinco ='" + Brinco.getText() + "', MODIFICADO = 'SIM', ENVIADO = 'NAO' WHERE _ID = " + _id + "";


                            db.execSQL(sql);

                            RefreshList();

                            Toast.makeText(getActivity(), "FrmPaperTop salvo com sucesso!", Toast.LENGTH_LONG).show();

                            hideKeyboard(Brinco);

                            db.close();
                        }

                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), ex.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }
                return false;

            }
        });


        ddlOcorrencia.setOnItemSelectedListener(new OnItemSelectedListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if(ativo != false) {

                    try {

                        if (NomeUsual.getText().toString().equals("") || NomeUsual.getText().equals(null)) {
                            Toast.makeText(getActivity(), "Selecione um animal antes de salvar", Toast.LENGTH_LONG).show();
                        }

                            if (!ddlOcorrencia.getSelectedItem().toString().equals(ocorrencia)) {

                                if(ddlOcorrencia.getSelectedItem().toString().equals("NENHUMA")){

                                    db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
                                    String sql = "";

                                    sql = "UPDATE PAPERPORT SET data_coleta= '" + data_formatada +
                                            "', ocorrencia ='', MODIFICADO = 'SIM', ENVIADO = 'NAO' WHERE _ID = " + _id
                                            + "";

                                    db.execSQL(sql);

                                    RefreshList();

                                    Toast.makeText(getActivity(), "FrmPaperTop salvo com sucesso!", Toast.LENGTH_LONG).show();


                                }
                                else {

                                    db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
                                    String sql = "";

                                    sql = "UPDATE PAPERPORT SET data_coleta= '" + data_formatada + "', ocorrencia ='"
                                            + ddlOcorrencia.getSelectedItem()
                                            + "', MODIFICADO = 'SIM', ENVIADO = 'NAO' WHERE _ID = " + _id
                                            + "";

                                    db.execSQL(sql);

                                    RefreshList();

                                    Toast.makeText(getActivity(), "FrmPaperTop salvo com sucesso!", Toast.LENGTH_LONG).show();
                                }
                            }

                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), ex.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }

                ativo = true;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


        btnChamaParto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Global mDados = Global.getInstance();

                if (NomeUsual.getText().toString().equals("")
                        && Brinco.getText().toString().equals("")) {
                    MessageBox("Selecione um animal antes de Parir");
                }
               /* else
              if (mDados.getStatus().toString().equals("LA")) {

                    btnData.setEnabled(true);
                    MessageBox("Para parir uma animal antes é necesssário informar a data de secagem!");
                }*/

                else if (mDados.getStatusProdutivo().toString().equals("VACA PRENHA")
                        || mDados.getStatusProdutivo().toString().equals("PRENHE")
                        || mDados.getStatusProdutivo().toString().equals("NOVILHA PRENHA")
                        || mDados.getStatusProdutivo().toString().equals("VACA COBERTA"))

                {
                    openDialogFragment(view);
                } else {
                    MessageBox("Para parir uma Vaca o STATUS PRODUTIVO  deve estar: VACA PRENHE");
                }
            }
        });


        btnData.setOnClickListener(new View.OnClickListener() {

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
                                DataSecagem.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);


                                try {
                                    SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
                                    String sql = "";

                                    sql = "UPDATE PAPERPORT SET data_coleta= '" + data_formatada + "',data_secagem ='"
                                            + DataSecagem.getText()
                                            + "',STATUS ='"
                                            + ddlStatus.getSelectedItem()
                                            + "', MODIFICADO = 'SIM',  ENVIADO = 'NAO'  WHERE _ID = " + _id + "";


                                    db.execSQL(sql);

                                    RefreshList();
                                    Toast.makeText(getActivity(), "FrmPaperTop salvo com sucesso!", Toast.LENGTH_LONG).show();

                                } catch (Exception ex) {
                                    Toast.makeText(getActivity(), ex.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                        }, year, month, day);
                dpd.show();


            }
        });

        btnData2.setOnClickListener(new View.OnClickListener() {

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
                                //	UltimoParto.setText(dayOfMonth + "/"	+ (monthOfYear + 1) + "/" + year);

                            }
                        }, year, month, day);
                dpd.show();
            }
        });


        ddlStatus.setOnItemSelectedListener(new OnItemSelectedListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (ativo1 != false) {

                    if (!_status.equals(ddlStatus.getSelectedItem().toString())) {

                        try {

                            if (NomeUsual.getText().toString().equals("") || NomeUsual.getText().equals(null)) {

                                Toast.makeText(getActivity().getBaseContext(), "Selecione um animal antes de salvar", Toast.LENGTH_SHORT).show();

                            } else {

                                if(!ddlStatus.getSelectedItem().toString().equals(_status)) {

                                    db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
                                    String sql = "";

                                    sql = "UPDATE PAPERPORT SET data_coleta= '" + data_formatada + "', STATUS ='"
                                            + ddlStatus.getSelectedItem()
                                            + "', MODIFICADO = 'SIM', ENVIADO = 'NAO' WHERE _ID = " + _id
                                            + "";

                                    db.execSQL(sql);

                                    RefreshList();

                                    Toast.makeText(getActivity(), "FrmPaperTop salvo com sucesso!", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception ex) {
                            Toast.makeText(getActivity(), ex.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                }

                ativo1 = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        fab = (FloatingActionMenu) view.findViewById(R.id.menu);
        fab.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {

            }
        });

        FloatingActionButton fabHome = (FloatingActionButton) view.findViewById(R.id.fbhome);

        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ReturnHome();

            }
        });

        FloatingActionButton fabDialog = (FloatingActionButton) view.findViewById(R.id.fbChange);
        fabDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                trocaProdutor(view);

            }
        });

        FloatingActionButton fabFinanceiro = (FloatingActionButton) view.findViewById(R.id.fbFinanceiro);
        fabFinanceiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openDialogFinanceiro(view);
            }
        });


        FloatingActionButton fabBkp = (FloatingActionButton) view.findViewById(R.id.fbBkp);
        fabBkp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String consultor = mDados.getLogin();

                copyDataBase();
                java.util.Date dataAtual = new java.util.Date(System.currentTimeMillis());

                Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "IbsPEC.db"));
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/*");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "bkp_" + consultor + "_" + dataAtual.toString() + ".db");
                sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(sharingIntent, "Salvar Copia do Banco de dados"));

            }
        });


        return view;
    }

    public void RefreshList() {

        ArrayList<Vaca> vacas = new ArrayList<Vaca>();

        mDados.setCount_animais(0);
        mDados.setAnimais_verificado(0);

        produtor = mDados.getProdutor();

        Cursor cursor1 = db
                .rawQuery(
                        "SELECT DISTINCT numeroDias,p.verifica, p._id as _id,p.dataParto, p.previsaoParto, p.categoria, p.data_secagem,p.modificado,  "
                                + "p.manejo, p.peso_atual, p.id as id,p.diasPrenhez,  proj.nome as _projeto, grup.nome as _grupo, produ.nome as _propriedade, "
                                + "p.data_coleta,p.area_atual,p.brinco,p.nome_usual,p.status,p.data_secagem,p.obs,p.ocorrencia,p.referencia, p.ultimo_parto "
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
            vaca.setUltimo_parto(cursor1.getString(cursor1.getColumnIndex("ultimo_parto")));
            vaca.setManejo(cursor1.getString(cursor1.getColumnIndex("manejo")));
            vaca.setDaisPrenhez(cursor1.getString(cursor1.getColumnIndex("diasPrenhez")));
            vaca.setVerifica(cursor1.getString(cursor1.getColumnIndex("verifica")));

            totalAnimais = totalAnimais+1;

            vacas.add(vaca);

            mDados.setCount_animais(mDados.getCount_animais() + 1);

            String verifica = vaca.getVerifica();
            String alterado = vaca.getModificado();

            if(verifica != null || alterado != null)
            {
                mDados.setAnimais_verificado(mDados.getAnimais_verificado() + 1);
            }
        }

        lblTotalAnimais.setText("Animais: "+ String.valueOf(totalAnimais));


        final VacaAdapter v ;
        v = new VacaAdapter(getActivity(),vacas);

        lv.setAdapter(v);

        lv.setVerticalScrollbarPosition(positionList);
        lv.smoothScrollToPosition(positionList);
        lv.setSelection(positionList);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

              //  Spinner ddlOcorrencia = (Spinner) view.findViewById(R.id.ddlOcorrencia);
                ddlStatus.setEnabled(true);

                TextView lbl_id = (TextView) view.findViewById(R.id.lbl_id);
                String idSelecionado =   lbl_id.getText().toString();

                _id = idSelecionado;

                positionList = i;

                SQLiteDatabase db1 = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

                Cursor cursor = db1
                        .rawQuery(
                                "SELECT p._id as _id,p.dataParto,p.enviado, p.dataDiagnostico, p.previsaoParto,p.ultimo_peso,p.manejo,p.categoria, p.data_secagem,p.peso_atual, p.id as id, proj.nome as _projeto, grup.nome as _grupo, produ.nome as _propriedade,p.data_coleta,p.area_atual,p.brinco,p.nome_usual,p.status,p.data_secagem,p.obs,p.ultimo_parto,p.ocorrencia,p.referencia "
                                        + "FROM paperPort p, projeto proj, grupo grup, produtor produ "
                                        + "WHERE p._projeto = proj.id AND p._grupo = grup.id AND p._propriedade = produ.id AND p._id = "
                                        + idSelecionado + "", null);
                try {
                    if (cursor != null && cursor.getCount() > 0) {
                        if (cursor.moveToFirst()) {
                            do {

                                brinco = cursor.getString(cursor.getColumnIndex("brinco"));
                                nome = cursor.getString(cursor.getColumnIndex("nome_usual"));
                                _status = cursor.getString(cursor.getColumnIndex("status"));
                                ocorrencia = cursor.getString(cursor.getColumnIndex("ocorrencia"));

                                if(ocorrencia == null){

                                    ocorrencia = "NENHUMA";
                                }

                                Global mDados = Global.getInstance();

                                prenha = cursor.getString(cursor.getColumnIndex("categoria"));

                                //verfica status para passagem no listner
                                stPosition = 0;

                                Brinco.setText(cursor.getString(cursor
                                        .getColumnIndex("brinco")));
                                //AreaAtual.setText(cursor.getString(cursor.getColumnIndex("area_atual")));
                                NomeUsual.setText(cursor.getString(cursor
                                        .getColumnIndex("nome_usual")));
                                DataSecagem.setText(cursor.getString(cursor
                                        .getColumnIndex("data_secagem")));
                                //UltimoParto.setText(cursor.getString(cursor.getColumnIndex("ultimo_parto")));

                                PesoAtual.setText(cursor.getString(cursor
                                        .getColumnIndex("peso_atual")));
                                //txtUltimPeso.setText(cursor.getString(cursor.getColumnIndex("ultimo_peso")));
                                mDados.setStatusProdutivo(cursor.getString(cursor
                                        .getColumnIndex("categoria")));
                                mDados.setStatus(cursor.getString(cursor
                                        .getColumnIndex("status")));

                                lblparto.setText("Parto feito em: " + cursor.getString(cursor.getColumnIndex("dataParto")));
                                lblDG.setText("Diagnóstico: " +  cursor.getString(cursor.getColumnIndex("dataDiagnostico")) );

                                mDados.setIdPaper(Integer.parseInt(idSelecionado));

                                String o = "";
                                o = cursor.getString(cursor
                                        .getColumnIndex("status"));

                                if (o.toString().equals("LA")) {
                                    ddlStatus.setSelection(0);
                                    status = "LA";
                                    _status = "LA";
                                    btnData.setEnabled(true);
                                }
                                if (o.toString().equals("SECA")) {
                                    ddlStatus.setSelection(1);
                                    status = "SECA";
                                    _status = "SECA";
                                    btnData.setEnabled(true);
                                }
                                if (o.toString().equals("VAZIA")) {
                                    ddlStatus.setSelection(2);
                                    status = "VAZIA";
                                    _status = "VAZIA";
                                    btnData.setEnabled(true);
                                }
                                o = cursor.getString(cursor
                                        .getColumnIndex("ocorrencia"));

                                if (o == null) {
                                    ddlOcorrencia.setSelection(0);
                                }
                                else
                                {

                                    if (o.toString().equals("NENHUMA") || o.toString().equals("") || o.toString().equals(null)) {
                                        ddlOcorrencia.setSelection(0);
                                    }
                                    if (o.toString().equals("MORTE")) {
                                        ddlOcorrencia.setSelection(1);
                                    }
                                    if (o.toString().equals("DESCARTE")) {
                                        ddlOcorrencia.setSelection(2);
                                    }
                                    if (o.toString().equals("VENDA")) {
                                        ddlOcorrencia.setSelection(3);
                                    }

                                   // Toast.makeText(getActivity(),cursor.getString(cursor.getColumnIndex("enviado")).toString(),Toast.LENGTH_LONG).show();
                                }

                            } while (cursor.moveToNext());
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });


      /* String[] from = {"_id", "brinco", "nome_usual",
                "status", "peso_atual", "data_secagem",
                "categoria", "previsaoParto", "ocorrencia", "numeroDias"};

        int[] to = {R.id.lbl_id, R.id.lbl_Brinco, R.id.nomeusual,
                R.id.lbl_Status, R.id.PesoAtual, R.id.lblDataSecagem,
                R.id.catAtual, R.id.lblPrevisao, R.id.manejo, R.id.nDias};

        @SuppressWarnings({})
        SimpleCursorAdapter ad1 = new SimpleCursorAdapter(getActivity(), R.layout.model_listar, cursor1, from, to);
        lv.setAdapter(ad1);*/
    }

    public void RefreshListVerifica() {

        totalAnimais = 0;

        ArrayList<Vaca> vacas = new ArrayList<Vaca>();

        mDados.setCount_animais(0);
        mDados.setAnimais_verificado(0);
        mDados.setVerfica_parto(0);

        produtor = mDados.getProdutor();

        Cursor cursor1 = db
                .rawQuery(
                        "SELECT DISTINCT numeroDias,p.verifica, p._id as _id,p.dataParto, p.previsaoParto, p.categoria, p.data_secagem,p.modificado,  "
                                + "p.manejo, p.peso_atual, p.id as id,p.diasPrenhez,  proj.nome as _projeto, grup.nome as _grupo, produ.nome as _propriedade, "
                                + "p.data_coleta,p.area_atual,p.brinco,p.nome_usual,p.status,p.data_secagem,p.obs,p.ocorrencia,p.referencia, p.ultimo_parto "
                                + "FROM paperPort p, projeto proj, grupo grup, produtor produ "
                                + "WHERE p._projeto = proj.id AND p._grupo = grup.id AND p._propriedade = produ.id AND p._propriedade = "
                                + produtor + " AND verifica is null AND p.modificado IS NULL  ORDER BY p.nome_usual", null);

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
            vaca.setUltimo_parto(cursor1.getString(cursor1.getColumnIndex("ultimo_parto")));
            vaca.setManejo(cursor1.getString(cursor1.getColumnIndex("manejo")));
            vaca.setDaisPrenhez(cursor1.getString(cursor1.getColumnIndex("diasPrenhez")));
            vaca.setVerifica(cursor1.getString(cursor1.getColumnIndex("verifica")));

            totalAnimais = totalAnimais + 1;

            vacas.add(vaca);

            mDados.setCount_animais(mDados.getCount_animais() + 1);

            String verifica = vaca.getVerifica();
            String alterado = vaca.getModificado();
            String ver_parto = vaca.getManejo();

            if(verifica != null || alterado != null)
            {
                mDados.setAnimais_verificado(mDados.getAnimais_verificado() + 1);
            }

            if(ver_parto != null) {

                if (ver_parto.equals("VERIFICAR PARTO. ")) {

                    mDados.setVerfica_parto(1);
                }
            }
        }

        lblTotalAnimais.setText("Animais: "+ String.valueOf(totalAnimais));


        final VacaAdapter v ;
        v = new VacaAdapter(getActivity(),vacas);

        lv.setAdapter(v);

        lv.setVerticalScrollbarPosition(positionList);
        lv.smoothScrollToPosition(positionList);
        lv.setSelection(positionList);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

                //  Spinner ddlOcorrencia = (Spinner) view.findViewById(R.id.ddlOcorrencia);
                ddlStatus.setEnabled(true);

                TextView lbl_id = (TextView) view.findViewById(R.id.lbl_id);
                String idSelecionado =   lbl_id.getText().toString();

                _id = idSelecionado;

                positionList = i;

                SQLiteDatabase db1 = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

                Cursor cursor = db1
                        .rawQuery(
                                "SELECT p._id as _id,p.dataParto,p.enviado, p.dataDiagnostico, p.previsaoParto,p.ultimo_peso,p.manejo,p.categoria, p.data_secagem,p.peso_atual, p.id as id, proj.nome as _projeto, grup.nome as _grupo, produ.nome as _propriedade,p.data_coleta,p.area_atual,p.brinco,p.nome_usual,p.status,p.data_secagem,p.obs,p.ultimo_parto,p.ocorrencia,p.referencia "
                                        + "FROM paperPort p, projeto proj, grupo grup, produtor produ "
                                        + "WHERE p._projeto = proj.id AND p._grupo = grup.id AND p._propriedade = produ.id AND p._id = "
                                        + idSelecionado + "", null);
                try {
                    if (cursor != null && cursor.getCount() > 0) {
                        if (cursor.moveToFirst()) {
                            do {

                                brinco = cursor.getString(cursor.getColumnIndex("brinco"));
                                nome = cursor.getString(cursor.getColumnIndex("nome_usual"));
                                _status = cursor.getString(cursor.getColumnIndex("status"));
                                ocorrencia = cursor.getString(cursor.getColumnIndex("ocorrencia"));

                                if(ocorrencia == null){

                                    ocorrencia = "NENHUMA";
                                }

                                Global mDados = Global.getInstance();

                                prenha = cursor.getString(cursor.getColumnIndex("categoria"));

                                //verfica status para passagem no listner
                                stPosition = 0;

                                Brinco.setText(cursor.getString(cursor
                                        .getColumnIndex("brinco")));
                                //AreaAtual.setText(cursor.getString(cursor.getColumnIndex("area_atual")));
                                NomeUsual.setText(cursor.getString(cursor
                                        .getColumnIndex("nome_usual")));
                                DataSecagem.setText(cursor.getString(cursor
                                        .getColumnIndex("data_secagem")));
                                //UltimoParto.setText(cursor.getString(cursor.getColumnIndex("ultimo_parto")));

                                PesoAtual.setText(cursor.getString(cursor
                                        .getColumnIndex("peso_atual")));
                                //txtUltimPeso.setText(cursor.getString(cursor.getColumnIndex("ultimo_peso")));
                                mDados.setStatusProdutivo(cursor.getString(cursor
                                        .getColumnIndex("categoria")));
                                mDados.setStatus(cursor.getString(cursor
                                        .getColumnIndex("status")));

                                lblparto.setText("Parto feito em: " + cursor.getString(cursor.getColumnIndex("dataParto")));
                                lblDG.setText("Diagnóstico: " +  cursor.getString(cursor.getColumnIndex("dataDiagnostico")) );

                                mDados.setIdPaper(Integer.parseInt(idSelecionado));

                                String o = "";
                                o = cursor.getString(cursor
                                        .getColumnIndex("status"));

                                if (o.toString().equals("LA")) {
                                    ddlStatus.setSelection(0);
                                    status = "LA";
                                    _status = "LA";
                                    btnData.setEnabled(true);
                                }
                                if (o.toString().equals("SECA")) {
                                    ddlStatus.setSelection(1);
                                    status = "SECA";
                                    _status = "SECA";
                                    btnData.setEnabled(true);
                                }
                                if (o.toString().equals("VAZIA")) {
                                    ddlStatus.setSelection(2);
                                    status = "VAZIA";
                                    _status = "VAZIA";
                                    btnData.setEnabled(true);
                                }
                                o = cursor.getString(cursor
                                        .getColumnIndex("ocorrencia"));

                                if (o == null) {
                                    ddlOcorrencia.setSelection(0);
                                }
                                else
                                {

                                    if (o.toString().equals("NENHUMA") || o.toString().equals("") || o.toString().equals(null)) {
                                        ddlOcorrencia.setSelection(0);
                                    }
                                    if (o.toString().equals("MORTE")) {
                                        ddlOcorrencia.setSelection(1);
                                    }
                                    if (o.toString().equals("DESCARTE")) {
                                        ddlOcorrencia.setSelection(2);
                                    }
                                    if (o.toString().equals("VENDA")) {
                                        ddlOcorrencia.setSelection(3);
                                    }

                                    // Toast.makeText(getActivity(),cursor.getString(cursor.getColumnIndex("enviado")).toString(),Toast.LENGTH_LONG).show();
                                }

                            } while (cursor.moveToNext());
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });


      /* String[] from = {"_id", "brinco", "nome_usual",
                "status", "peso_atual", "data_secagem",
                "categoria", "previsaoParto", "ocorrencia", "numeroDias"};

        int[] to = {R.id.lbl_id, R.id.lbl_Brinco, R.id.nomeusual,
                R.id.lbl_Status, R.id.PesoAtual, R.id.lblDataSecagem,
                R.id.catAtual, R.id.lblPrevisao, R.id.manejo, R.id.nDias};

        @SuppressWarnings({})
        SimpleCursorAdapter ad1 = new SimpleCursorAdapter(getActivity(), R.layout.model_listar, cursor1, from, to);
        lv.setAdapter(ad1);*/
    }

    public void RefreshListVerificaParto() {

        totalAnimais = 0;

        ArrayList<Vaca> vacas = new ArrayList<Vaca>();

        mDados.setCount_animais(0);
        mDados.setAnimais_verificado(0);
        mDados.setVerfica_parto(0);

        produtor = mDados.getProdutor();

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT DISTINCT numeroDias,p.verifica, p._id as _id,p.dataParto, p.previsaoParto, p.categoria, p.data_secagem,p.modificado,");
        sql.append(" p.manejo, p.peso_atual, p.id as id,p.diasPrenhez,  proj.nome as _projeto, grup.nome as _grupo, produ.nome as _propriedade,");
        sql.append(" p.data_coleta,p.area_atual,p.brinco,p.nome_usual,p.status,p.data_secagem,p.obs,p.ocorrencia,p.referencia, p.ultimo_parto");
        sql.append(" FROM paperPort p, projeto proj, grupo grup, produtor produ");
        sql.append(" WHERE ( p._projeto = proj.id AND p._grupo = grup.id AND p._propriedade = produ.id AND p._propriedade = "+ produtor +" ");
        sql.append(" AND verifica is null AND p.modificado IS NULL AND p.manejo = 'VERIFICAR PARTO. ' )");
        sql.append(" OR ");
        sql.append(" ( p._projeto = proj.id AND p._grupo = grup.id AND p._propriedade = produ.id AND p._propriedade = " + produtor + "");
        sql.append(" AND verifica is null AND p.modificado = 'SIM' AND p.manejo = 'VERIFICAR PARTO. ' )");
        sql.append(" ORDER BY p.nome_usual");

        Cursor cursor1 = db.rawQuery(sql.toString(), null);

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
            vaca.setUltimo_parto(cursor1.getString(cursor1.getColumnIndex("ultimo_parto")));
            vaca.setManejo(cursor1.getString(cursor1.getColumnIndex("manejo")));
            vaca.setDaisPrenhez(cursor1.getString(cursor1.getColumnIndex("diasPrenhez")));
            vaca.setVerifica(cursor1.getString(cursor1.getColumnIndex("verifica")));

            totalAnimais = totalAnimais + 1;

            vacas.add(vaca);

            mDados.setCount_animais(mDados.getCount_animais() + 1);

            String verifica = vaca.getVerifica();
            String alterado = vaca.getModificado();
            String ver_parto = vaca.getManejo();

            if(verifica != null || alterado != null)
            {
                mDados.setAnimais_verificado(mDados.getAnimais_verificado() + 1);
            }

            if(ver_parto != null) {

                if (ver_parto.equals("VERIFICAR PARTO. ")) {

                    mDados.setVerfica_parto(1);
                }
            }
        }

        lblTotalAnimais.setText("Animais: "+ String.valueOf(totalAnimais));

        final VacaAdapter v ;
        v = new VacaAdapter(getActivity(),vacas);

        lv.setAdapter(v);

        lv.setVerticalScrollbarPosition(positionList);
        lv.smoothScrollToPosition(positionList);
        lv.setSelection(positionList);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

                //  Spinner ddlOcorrencia = (Spinner) view.findViewById(R.id.ddlOcorrencia);
                ddlStatus.setEnabled(true);

                TextView lbl_id = (TextView) view.findViewById(R.id.lbl_id);
                String idSelecionado =   lbl_id.getText().toString();

                _id = idSelecionado;

                positionList = i;

                SQLiteDatabase db1 = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

                Cursor cursor = db1
                        .rawQuery(
                                "SELECT p._id as _id,p.dataParto,p.enviado, p.dataDiagnostico, p.previsaoParto,p.ultimo_peso,p.manejo,p.categoria, p.data_secagem,p.peso_atual, p.id as id, proj.nome as _projeto, grup.nome as _grupo, produ.nome as _propriedade,p.data_coleta,p.area_atual,p.brinco,p.nome_usual,p.status,p.data_secagem,p.obs,p.ultimo_parto,p.ocorrencia,p.referencia "
                                        + "FROM paperPort p, projeto proj, grupo grup, produtor produ "
                                        + "WHERE p._projeto = proj.id AND p._grupo = grup.id AND p._propriedade = produ.id AND p._id = "
                                        + idSelecionado + "", null);
                try {
                    if (cursor != null && cursor.getCount() > 0) {
                        if (cursor.moveToFirst()) {
                            do {

                                brinco = cursor.getString(cursor.getColumnIndex("brinco"));
                                nome = cursor.getString(cursor.getColumnIndex("nome_usual"));
                                _status = cursor.getString(cursor.getColumnIndex("status"));
                                ocorrencia = cursor.getString(cursor.getColumnIndex("ocorrencia"));

                                if(ocorrencia == null){

                                    ocorrencia = "NENHUMA";
                                }

                                Global mDados = Global.getInstance();

                                prenha = cursor.getString(cursor.getColumnIndex("categoria"));

                                //verfica status para passagem no listner
                                stPosition = 0;

                                Brinco.setText(cursor.getString(cursor
                                        .getColumnIndex("brinco")));
                                //AreaAtual.setText(cursor.getString(cursor.getColumnIndex("area_atual")));
                                NomeUsual.setText(cursor.getString(cursor
                                        .getColumnIndex("nome_usual")));
                                DataSecagem.setText(cursor.getString(cursor
                                        .getColumnIndex("data_secagem")));
                                //UltimoParto.setText(cursor.getString(cursor.getColumnIndex("ultimo_parto")));

                                PesoAtual.setText(cursor.getString(cursor
                                        .getColumnIndex("peso_atual")));
                                //txtUltimPeso.setText(cursor.getString(cursor.getColumnIndex("ultimo_peso")));
                                mDados.setStatusProdutivo(cursor.getString(cursor
                                        .getColumnIndex("categoria")));
                                mDados.setStatus(cursor.getString(cursor
                                        .getColumnIndex("status")));

                                lblparto.setText("Parto feito em: " + cursor.getString(cursor.getColumnIndex("dataParto")));
                                lblDG.setText("Diagnóstico: " +  cursor.getString(cursor.getColumnIndex("dataDiagnostico")) );

                                mDados.setIdPaper(Integer.parseInt(idSelecionado));

                                String o = "";
                                o = cursor.getString(cursor
                                        .getColumnIndex("status"));

                                if (o.toString().equals("LA")) {
                                    ddlStatus.setSelection(0);
                                    status = "LA";
                                    _status = "LA";
                                    btnData.setEnabled(true);
                                }
                                if (o.toString().equals("SECA")) {
                                    ddlStatus.setSelection(1);
                                    status = "SECA";
                                    _status = "SECA";
                                    btnData.setEnabled(true);
                                }
                                if (o.toString().equals("VAZIA")) {
                                    ddlStatus.setSelection(2);
                                    status = "VAZIA";
                                    _status = "VAZIA";
                                    btnData.setEnabled(true);
                                }
                                o = cursor.getString(cursor
                                        .getColumnIndex("ocorrencia"));

                                if (o == null) {
                                    ddlOcorrencia.setSelection(0);
                                }
                                else
                                {

                                    if (o.toString().equals("NENHUMA") || o.toString().equals("") || o.toString().equals(null)) {
                                        ddlOcorrencia.setSelection(0);
                                    }
                                    if (o.toString().equals("MORTE")) {
                                        ddlOcorrencia.setSelection(1);
                                    }
                                    if (o.toString().equals("DESCARTE")) {
                                        ddlOcorrencia.setSelection(2);
                                    }
                                    if (o.toString().equals("VENDA")) {
                                        ddlOcorrencia.setSelection(3);
                                    }

                                    // Toast.makeText(getActivity(),cursor.getString(cursor.getColumnIndex("enviado")).toString(),Toast.LENGTH_LONG).show();
                                }

                            } while (cursor.moveToNext());
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });


      /* String[] from = {"_id", "brinco", "nome_usual",
                "status", "peso_atual", "data_secagem",
                "categoria", "previsaoParto", "ocorrencia", "numeroDias"};

        int[] to = {R.id.lbl_id, R.id.lbl_Brinco, R.id.nomeusual,
                R.id.lbl_Status, R.id.PesoAtual, R.id.lblDataSecagem,
                R.id.catAtual, R.id.lblPrevisao, R.id.manejo, R.id.nDias};

        @SuppressWarnings({})
        SimpleCursorAdapter ad1 = new SimpleCursorAdapter(getActivity(), R.layout.model_listar, cursor1, from, to);
        lv.setAdapter(ad1);*/
    }

    public void trocaProdutor(View view) {

        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        TrocaProdutor cdf = new TrocaProdutor(1, 1);
        cdf.show(ft, "DG");
    }

    private void ReturnHome() {

        Intent obtnptodos = new Intent(getActivity(), main.class);
        startActivity(obtnptodos);

    }

    public void copyDataBase() {
        Log.i("info", "in copy data base at finally");
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "/data/com.pec.biosistemico.pec/databases/IbsPEC.db";
                String backupDBPath = "IbsPEC.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB)
                            .getChannel();
                    FileChannel dst = new FileOutputStream(backupDB)
                            .getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            Log.i("info", "in copy of bata base 10 ");
        }
    }

    private void change_color(ListView listView, int position) {
        listView.getChildAt(position).setBackgroundColor(Color.BLACK);
    }

    public void MessageBox(String msg) {
        Builder informa = new Builder(getActivity());
        informa.setTitle("Alerta!").setMessage(msg);
        informa.setNeutralButton("Fechar", null).show();
    }

    public int IdGrupo() {

        Global mDados = Global.getInstance();
        String Grupo = mDados.getUsuario();

        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",
                Context.MODE_PRIVATE, null);
        Cursor cursor1 = db.rawQuery("SELECT id FROM GRUPO WHERE nome = '"
                + Grupo + "'", null);
        cursor1.moveToFirst();
        int id = cursor1.getInt(cursor1.getColumnIndex("id"));

        return id;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        MessageBox("passei pelo result");

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt("lv", lv.getSelectedItemPosition());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if (savedInstanceState != null) {

            lv.setSelection(savedInstanceState.getInt("lv", 0));

        }
        super.onViewStateRestored(savedInstanceState);
    }

    private void hideKeyboard(EditText txt){

        InputMethodManager im = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(txt.getWindowToken(), 0);
    }

    public void CleanFields() {

        Brinco.setText("");
        NomeUsual.setText("");
        PesoAtual.setText("");
        DataSecagem.setText("");
        media.setText("");
    }

    public long CountCow(int id) {
        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        long x = 0;

        Cursor cursor = db.rawQuery("SELECT count(*)as TOTAL FROM paperPort WHERE ocorrencia is null AND status = 'LA' AND  _propriedade = " + id + "", null);
        cursor.moveToFirst();

        x = cursor.getLong(cursor.getColumnIndex("TOTAL"));


        return x;
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

    public void openDialogFinanceiro(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        FrmFinanceiro cdf = new FrmFinanceiro();
        cdf.show(ft, "Parto");
    }

    public void openDialogFragment(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Parto cdf = new Parto(1, 1);
        cdf.show(ft, "Parto");
    }

    public void openDialogFragmentDG(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Dg cdf = new Dg(1, 1);
        cdf.show(ft, "DG");
    }

    public void openDialogFragmentAnimais(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Animais cdf = new Animais();
        cdf.show(ft, "DG");
    }

    public String RetornaProdutor(int _id) {

        String result = "";
        SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT NOME FROM PRODUTOR WHERE ID = " + _id + "", null);
        cursor.moveToFirst();
        result = cursor.getString(cursor.getColumnIndex("nome"));

        return result;
    }

    public void turnOffDialogFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Parto cdf = (Parto) getFragmentManager()
                .findFragmentByTag("dialog");
        if (cdf != null) {
            cdf.dismiss();
            ft.remove(cdf);

            SQLiteDatabase db = getActivity().openOrCreateDatabase("IbsPEC.db",
                    Context.MODE_PRIVATE, null);

            SQLiteCursor dados = (SQLiteCursor) ddlprodutor.getAdapter()
                    .getItem(ddlprodutor.getSelectedItemPosition());
            Cursor cursor1 = db
                    .rawQuery(
                            "SELECT DISTINCT numeroDias, p._id as _id,p.previsaoParto,p.ultimo_peso, p.categoria, p.data_secagem,p.ultimo_parto, p.manejo, p.peso_atual, p.id as id,  proj.nome as _projeto, grup.nome as _grupo, produ.nome as _propriedade,p.data_coleta,p.area_atual,p.brinco,p.nome_usual,p.status,p.data_secagem,p.obs,p.ocorrencia,p.referencia "
                                    + "FROM paperPort p, projeto proj, grupo grup, produtor produ "
                                    + "WHERE p._projeto = proj.id AND p._grupo = grup.id AND p._propriedade = produ.id AND p._propriedade = "
                                    + dados.getInt(1) + "", null);


            String[] from = {"_id", "brinco", "nome_usual",
                    "status", "peso_atual", "data_secagem",
                    "categoria", "previsaoParto", "ocorrencia"};

            int[] to = {R.id.lbl_id, R.id.lbl_Brinco,
                    R.id.nomeusual, R.id.lbl_Status, R.id.PesoAtual,
                    R.id.lblDataSecagem, R.id.catAtual,
                    R.id.manejo};

            SimpleCursorAdapter ad1 = new SimpleCursorAdapter(getActivity(),
                    R.layout.model_listar, cursor1, from, to);

            lv.setAdapter(ad1);
        }

    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
    }

    @Override
    public void onClick(View v) {

    }

}
