package com.pec.biosistemico.pec;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.Task;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.pec.biosistemico.pec.cloud.BaixarProjetos;
import com.pec.biosistemico.pec.cloud.BroadReceiver;
import com.pec.biosistemico.pec.cloud.ReplicarDados;
import com.pec.biosistemico.pec.cloud.Service;
import com.pec.biosistemico.pec.criatf.AtualizarConsultor;
import com.pec.biosistemico.pec.helpers.DbHelper;
import com.pec.biosistemico.pec.impressao.Reimpressao;
import com.pec.biosistemico.pec.offLine.ListFoldersActivity;
import com.pec.biosistemico.pec.offLine.UploadDataBaseActivity;
import com.pec.biosistemico.pec.paperTop.SelectProducers;
import com.pec.biosistemico.pec.ti.Password;
import com.pec.biosistemico.pec.util.Global;
import com.pec.biosistemico.pec.util.Ping;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class main extends Activity implements OnClickListener, Animation.AnimationListener {

    private int Produtor;
    private Button btnBuscar;
    private Spinner ddlCliente;
    private Spinner ddlEscritorio;
    private Spinner ddlProjeto;
    private Spinner ddlGrupo;
    private Spinner ddlProdutor;
    private String Grupo;
    private String Projeto;
    private String Cliente;
    private TextView lblLogin;
    private Global mDados = Global.getInstance();
    private SharedPreferences spinnersaving;
    private String versionName;
    private String pct;
    private JSONObject json = null;
    private android.support.v7.widget.Toolbar mToolbar;
    private Animation animFadein;
    private Animation aniCliente;
    private Animation animFadeout;
    private DbHelper dbHelper;
    private ProgressDialog mProgressDialog;
    private int mPositionClicked;
    //private static MaterialDialog materialDialog;
    private String line = "",versao = "", numero = "";
    private SQLiteDatabase db;
    private CheckBox chkLAN;
    private String endereco_rede;
    private Drawer.Result navigationDrawerLeft;
    private Drawer.Result navigationDrawerRight;
    private AccountHeader.Result headerNavigationLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

       // db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
       // db.execSQL("UPDATE CRIATF SET ENVIADO = 'SIM'");
       //db.execSQL("UPDATE PAPERPORT SET ENVIADO = 'SIM'");

       /* CopyPaste c = new CopyPaste(this);
        c.getDatabaseName();
        c.getReadableDatabase();
        c.getWritableDatabase();

        try
        {
            c.copyDataBase();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }*/

        try {
            MakeTables();
            copyDataBase();
        }
        catch (Exception ex){

            Toast.makeText(main.this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("PEC - Instituo Biosistêmico / retorno da conexão: " + mDados.getStatus_conexao());
        mToolbar.canShowOverflowMenu();
        mToolbar.animate();
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        mToolbar.setDrawingCacheBackgroundColor(Color.WHITE);
      //  setSupportActionBar(mToolbar);

        dbHelper = DbHelper.getHelper(getApplicationContext());
        dbHelper.open();

        copyDataBase();

        headerNavigationLeft = new AccountHeader()
                .withActivity(this)
                .withCompactStyle(true)
                .withSavedInstance(savedInstanceState)
                .withThreeSmallProfileImages(false)
                .withHeaderBackground(R.drawable.ceu)
                .addProfiles(
                        new ProfileDrawerItem().withName(mDados.getLogin()).withEmail("pec.biosistemico@gmail.com").withIcon(getResources().getDrawable(R.drawable.home))

                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile iProfile, boolean b) {
                        Toast.makeText(main.this, "onProfileChanged: " + iProfile.getName(), Toast.LENGTH_SHORT).show();
                        headerNavigationLeft.setBackgroundRes(R.drawable.ceu);
                        return false;
                    }
                })
                .build();

        navigationDrawerLeft = new Drawer()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(0)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerNavigationLeft)

                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {

                        final Global mDados = Global.getInstance();
                        mDados.setUsuario(Grupo);
                        mDados.setProjeto(Integer.parseInt(Projeto));
                        mDados.setCliente(Integer.parseInt(Cliente));

                        android.app.FragmentTransaction _ft = getFragmentManager().beginTransaction();

                        switch (i) {

                            case 0:
                                Intent intent = new Intent(main.this, Reimpressao.class);
                                startActivity(intent);
                                finish();
                                break;
                            case 1:
                                if (Grupo.equals("9999")) {
                                    MessageBox("Antes de carregar os dados é necessário selecionar um Grupo");
                                } else {

                                   if(VerificaConexao() == 1)
                                   {
                                       MessageBox("LIGUE O WIFI ANTES DE CARREGAR OS DADOS!");
                                   }
                                    else {
                                       Intent obtnptodos = new Intent(main.this, SelectProducers.class);
                                       startActivity(obtnptodos);
                                       finish();
                                   }
                                }
                                break;
                            case 2:

                                if(VerificaConexao() == 1)
                                {
                                    MessageBox("LIGUE O WIFI ANTES DE CARREGAR OS DADOS!");
                                }
                                else {
                                    Intent obtnptodos = new Intent(main.this, ReplicarDados.class);
                                    startActivity(obtnptodos);
                                    finish();
                                }
                                break;

                            case 3:
                                new TaskVerificarVersao(main.this).execute();
                                break;
                            case 4:
                                if(VerificaConexao() == 1)
                                {
                                    MessageBox("LIGUE O WIFI ANTES DE CARREGAR OS DADOS!");
                                }
                                else {
                                    Intent obtnptodoss = new Intent(main.this, AtualizarConsultor.class);
                                    startActivity(obtnptodoss);
                                    finish();
                                }
                                break;
                            case 5:
                                copyDataBase();
                                Date dataAtual = new Date(System.currentTimeMillis());
                                Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "IbsPEC.db"));
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("image/*");
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "bkp_" + mDados.getConsultor().toString() + "_" + dataAtual.toString() + ".db");
                                sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
                                startActivity(Intent.createChooser(sharingIntent, "Salvar Cópia do Banco de dados"));
                                break;
                            case 6:
                                if (mDados.getLogin() == "") {

                                    Toast.makeText(main.this, "FAVOR CADASTRAR O O SEU ID ANTES DE LOGAR", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    if(VerificaConexao() == 1)
                                    {
                                        MessageBox("LIGUE O WIFI ANTES DE CARREGAR OS DADOS!");
                                    }
                                    else {
                                        Intent obtnptodaos = new Intent(main.this, BaixarProjetos.class);
                                        startActivity(obtnptodaos);
                                        finish();
                                    }
                                }
                                break;
                            case 7:
                                frmSql(view);
                                break;


                        }



                        mPositionClicked = i;
                        navigationDrawerLeft.getAdapter().notifyDataSetChanged();
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        Toast.makeText(main.this, "nothing to do: " + i, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })
                .build();

        // navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("NOVOS ANIMAIS").withIcon(getResources().getDrawable(R.drawable.cow)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("REIMPRESSÃO RAT").withIcon(getResources().getDrawable(R.drawable.paper)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("BAIXAR PAPERTOP").withIcon(getResources().getDrawable(R.drawable.ia)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("ENVIAR DADOS AO IBS").withIcon(getResources().getDrawable(R.drawable.d0)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("ATUALIZAR VERSÃO APP").withIcon(getResources().getDrawable(R.drawable.ic_action_refresh)));

        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("ALTERAR ID DO CONSULTOR").withIcon(getResources().getDrawable(R.drawable.check)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("ENVIAR BASE DE DADOS AO TI").withIcon(getResources().getDrawable(R.drawable.relatorio)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("BAIXAR PROJETOS").withIcon(getResources().getDrawable(R.drawable.dg)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("TI").withIcon(getResources().getDrawable(R.drawable.ia)));

        if(mDados.getAtualizacao() == false) {

            ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                try {
                        start();

                        Intent it = new Intent(this, Service.class);
                        startService(it);
                   }
                catch (Exception ex) {
                }        }

        //dbHelper.cargaInicial();

        //mToolbar.setSubtitle("");
        //mToolbar.setLogo(R.drawable.ibs);
        //db.execSQL("INSERT INTO versao (versao) VALUES ('2')");

        SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);
        DbHelper dh = new DbHelper(this);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animFadein.setAnimationListener(this);

        aniCliente = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        aniCliente.setAnimationListener(this);

        animFadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        animFadeout.setAnimationListener(this);



        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            pct = getPackageManager().getPackageInfo(getPackageName(), 0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Cursor cursor = db.rawQuery("SELECT versao FROM VERSAO", null);

  /*      if (cursor.moveToFirst()) {

            int versao = cursor.getInt(cursor.getColumnIndex("versao"));
            dh.onUpgrade(db, versao, 2);
        }
*/
        ddlCliente = (Spinner) findViewById(R.id.ddlCliente);
        ddlProjeto = (Spinner) findViewById(R.id.ddlProjeto);
        ddlGrupo = (Spinner) findViewById(R.id.ddlGrupo);
        btnBuscar = (Button) findViewById(R.id.btnLoad);

        if (!mDados.getResumo_paperTop().toString().equals("")) {

            MessageBox(mDados.getResumo_paperTop().toString());
            mDados.setResumo_paperTop("");
        }

        if (spinnersaving != null) {

            ddlCliente.setSelection(spinnersaving.getInt("spinnerSelection", 0));
        }

        if (savedInstanceState != null) {

            ddlCliente.setSelection(savedInstanceState.getInt("ddlCliente", 0));
            ddlProjeto.setSelection(savedInstanceState.getInt("ddlProjeto", 0));
            ddlGrupo.setSelection(savedInstanceState.getInt("ddlGrupo", 0));
        }

        // spinnersaving = getSharedPreferences("spinnerstate",0);
        // ddlCliente.setSelection(spinnersaving.getInt("spinnerPos", 0));

        //db = openOrCreateDatabase("IbsPEC.db",Context.MODE_PRIVATE, null);
        cursor = db.rawQuery("SELECT usuario, nome, usuario FROM login", null);

        String x;

        //########### VERIFICA LOGIN ##########
        if (cursor.moveToFirst() == true) {

            x = cursor.getString(cursor.getColumnIndex("usuario")).toString();

            mDados.setProject(x);
            mToolbar.setSubtitle(cursor.getString(cursor.getColumnIndex("nome")) + "  /  Região: " + mDados.getProject().toString() + "  /   Versão " + versionName);
            mDados.setLogin(cursor.getString(cursor.getColumnIndex("nome")));

        }
        if (mDados.getLogin() == "") {
            mToolbar.setSubtitle("Cadastre o ID do Consultor");
        }

        //#############INICIANDO SERVIÇO DE REPLICAÇÃO##################
      //  startService();
        //#############################################################

        ddlCliente.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                // Recupera o objeto selecionado no spinner
                ddlCliente = (Spinner) findViewById(R.id.ddlCliente);
                SQLiteCursor dados = (SQLiteCursor) ddlCliente.getAdapter()
                        .getItem(ddlCliente.getSelectedItemPosition());
                populaProjeto(dados.getInt(1));
                Cliente = dados.getString(1);

                if (ddlCliente.getSelectedItemPosition() != 0) {

                    if (ddlCliente.getSelectedItemPosition() != mDados.getCliente_position()) {

                        mDados.setGrupo_position(0);
                    }

                    try {
                        mDados.setCliente_position(ddlCliente.getSelectedItemPosition());

                        // mDados.setGrupo_position(0);

                    } catch (Exception ex) {
                        Toast.makeText(main.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                if (ddlCliente.getSelectedItemPosition() != 0) {

                    ddlCliente.setBackgroundResource(R.drawable.spinner_selected);
                    ddlCliente.startAnimation(animFadein);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        ddlProjeto = (Spinner) findViewById(R.id.ddlProjeto);
        ddlProjeto.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                ddlProjeto = (Spinner) findViewById(R.id.ddlProjeto);
                SQLiteCursor dados2 = (SQLiteCursor) ddlProjeto.getAdapter()
                        .getItem(ddlProjeto.getSelectedItemPosition());

                populaGrupo(dados2.getInt(1));
                Projeto = dados2.getString(1);

                if (ddlCliente.getSelectedItemPosition() != 0) {

                    try {
                        mDados.setProjeto_position(ddlProjeto.getSelectedItemPosition());
                    } catch (Exception ex) {
                        Toast.makeText(main.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                if (ddlProjeto.getSelectedItemPosition() != 0) {

                    ddlProjeto.setBackgroundResource(R.drawable.spinner_selected);
                    ddlProjeto.startAnimation(animFadein);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        ddlGrupo = (Spinner) findViewById(R.id.ddlGrupo);
        ddlGrupo.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                findViewById(R.id.ddlGrupo);
                SQLiteCursor dados3 = (SQLiteCursor) ddlGrupo.getAdapter().getItem(ddlGrupo.getSelectedItemPosition());
                populaProdutor(dados3.getInt(1));
                Grupo = dados3.getString(1);

                if (ddlProjeto.getSelectedItemPosition() != 0) {

                    try {
                        mDados.setGrupo_position(ddlGrupo.getSelectedItemPosition());
                    } catch (Exception ex) {
                        Toast.makeText(main.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                if (ddlGrupo.getSelectedItemPosition() != 0) {

                    ddlGrupo.setBackgroundResource(R.drawable.spinner_selected);
                    ddlGrupo.startAnimation(animFadein);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        ddlProdutor = (Spinner) findViewById(R.id.ddlProdutor);
        ddlProdutor.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                findViewById(R.id.ddlProdutor);
                SQLiteCursor dados3 = (SQLiteCursor) ddlProdutor.getAdapter()
                        .getItem(ddlProdutor.getSelectedItemPosition());
                Produtor = dados3.getInt(1);

                if (ddlProdutor.getSelectedItemPosition() != 0 && ddlProdutor.getSelectedItemPosition() != 1) {

                    ddlProdutor.setBackgroundResource(R.drawable.spinner_selected);
                    ddlProdutor.startAnimation(animFadein);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mToolbar.getSubtitle().toString() == ("Cadastre o ID do Consultor")) {

                    MessageBox("FAVOR CADASTRAR O SEU ID ANTES DE LOGAR");
                } else {

                    try {
                        copyDataBase();

                        Global mDados = Global.getInstance();
                        if (String.valueOf(Produtor) != null) {

                            if (Produtor == 99999 || Grupo.equals(""))
                            {
                                MessageBox("Selecione um Produtor antes de Entrar!");
                            }
                            else
                            {
                                mDados.setUsuario(Grupo);
                                mDados.setProdutor(Produtor);
                                mDados.setProjeto(Integer.parseInt(Projeto));
                                mDados.setCliente(Integer.parseInt(Cliente));

                                mDados.setCliente_position(ddlCliente.getSelectedItemPosition());
                                mDados.setProjeto_position(ddlProjeto.getSelectedItemPosition());
                                mDados.setGrupo_position(ddlGrupo.getSelectedItemPosition());

                                startActivity(new Intent(getBaseContext(), MainActivity.class));

                            }

                        } else {
                            MessageBox("Carregue os dados antes de entrar!");
                        }

                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

        });

        try {
            populaCliente();

        } catch (Exception ex) {
            Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void start() {

        try {

            ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            if (conn.getNetworkInfo(1).isConnected() == true) {

                Ping ping = new Ping();
                List<String> commands = new ArrayList<String>();
                commands.add("ping");
                commands.add("-c");
                commands.add("1");
                commands.add("www.google.com.br");

                if (ping.doCommand(commands) != 0) {

                    Intent alarmIntent = new Intent(main.this, BroadReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(main.this, 0, alarmIntent, 0);

                    AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    int interval = 1000 * 60 * 20;

                   /* Set the alarm to start at 07:00 AM */
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, 07);
                    calendar.set(Calendar.MINUTE, 00);

                    manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 600, pendingIntent);

                    startService();
                }
            }
        }
        catch (Exception ex){

            ex.getMessage();
        }
    }

    public String getVersionName() {

        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            pct = getPackageManager().getPackageInfo(getPackageName(), 0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return  versionName;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt("ddlCliente", ddlCliente.getSelectedItemPosition());
        outState.putInt("ddlProjeto", ddlProjeto.getSelectedItemPosition());
        outState.putInt("ddlGrupo", ddlGrupo.getSelectedItemPosition());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {

        if (savedInstanceState != null) {

            ddlCliente.setSelection(savedInstanceState.getInt("ddlCliente", 0));
            ddlProjeto.setSelection(savedInstanceState.getInt("ddlProjeto", 0));
            ddlGrupo.setSelection(savedInstanceState.getInt("ddlGrupo", 0));
        }
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {

        ddlCliente.setSelection(mDados.getCliente_position());
        ddlProjeto.setSelection(mDados.getProjeto_position());

        super.onResume();
    }

    protected void onStop() {
        super.onStop();
       /* SharedPreferences spinnersaving = getSharedPreferences("spinnerstate",0);

        SharedPreferences.Editor editor = spinnersaving.edit();
        editor.putInt("spinnerPos", mDados.getCliente_position());
        editor.commit();*/
    }

    @Override
    public void onClick(DialogInterface arg0, int arg1) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Global mDados = Global.getInstance();
        mDados.setUsuario(Grupo);
        mDados.setProjeto(Integer.parseInt(Projeto));
        mDados.setCliente(Integer.parseInt(Cliente));

        try {

            if (item.getItemId() == R.id.mnuDB)
            {
                copyDataBase();
                Date dataAtual = new Date(System.currentTimeMillis());

                Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "IbsPEC.db"));
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/*");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "bkp_"+mDados.getConsultor().toString()+ "_" + dataAtual.toString()+".db");
                sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(sharingIntent, "Salvar Cópia do Banco de dados"));

            }

            if(item.getItemId() ==  R.id.mnuReimpressao){

                Intent intent = new Intent(main.this, Reimpressao.class);

                startActivity(intent);
                finish();

            }
            else
            if (item.getItemId() == R.id.menuDriveDownload) {
                Intent intent = new Intent(main.this, ListFoldersActivity.class);

                startActivity(intent);
                finish();
            }
            if (item.getItemId() == R.id.menuDriveUpload) {
                if (mDados.getLogin() != null && !mDados.getLogin().equals("")) {
                    Intent intent = new Intent(main.this, UploadDataBaseActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("NAME_CONSULTOR", mDados.getConsultor());
                    intent.putExtras(bundle);

                    startActivity(intent);
                    finish();
                } else {
                    MessageBox("Erro ao sincronizar dados com o Google Drive. Por gentileza cadastre o nome do consultor e reinicie o Ibs PEC.");
                }
            }

            if (conn.getNetworkInfo(1).isConnected() == false)
            {
                AlertDialog.Builder informa = new AlertDialog.Builder(this);
                informa.setTitle("Atenção!").setMessage("LIGUE O WIFI ANTES DE CARREGAR OS DADOS!");
                informa.setNeutralButton("Voltar", null).show();
            }

            if (item.getItemId() == R.id.mnuID) {
                Intent obtnptodos = new Intent(this, AtualizarConsultor.class);
                startActivity(obtnptodos);
                finish();
                return true;
            }

            if (mDados.getLogin() == "") {

                Toast.makeText(this, "FAVOR CADASTRAR O O SEU ID ANTES DE LOGAR", Toast.LENGTH_LONG).show();
            }
            else {
                   /* Ping ping = new Ping();
                    List<String> commands = new ArrayList<String>();
                    commands.add("ping");
                    commands.add("-c");
                    commands.add("1");
                    commands.add("www.google.com.br");

                if(ping.doCommand(commands) == 0)
                {
                    MessageBox("A CONEXÃO COM A INTERNET ESTA COM PROLBEMAS.");
                }
                if (ping.doCommand(commands) != 0) {
                */

                    if (item.getItemId() == R.id.mnuProjetos)
                    {
                        Intent obtnptodos = new Intent(this, BaixarProjetos.class);
                        startActivity(obtnptodos);
                        finish();

                    } else if (item.getItemId() == R.id.mnuPaper)
                    {
                        if (Grupo.equals("9999"))
                        {
                            MessageBox("Antes de carregar os dados é necessário selecionar um Grupo");
                        } else
                        {
                            Intent obtnptodos = new Intent(this,SelectProducers.class);
                            //FormCargaPaperTop
                            startActivity(obtnptodos);
                            finish();
                        }
                    } else if (item.getItemId() == R.id.mnuEnviar) {
                        Intent obtnptodos = new Intent(this,
                                ReplicarDados.class);
                        startActivity(obtnptodos);
                        finish();
                        return true;
                    }
                }


        } catch (Exception e) {

            Toast.makeText(this, "Erro contate o TI do Ibs" + e.getMessage(), Toast.LENGTH_LONG).show();

        }


        return false;

    }

    public int VerificaConexao() {

        int result = 0;

        ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conn.getNetworkInfo(1).isConnected() == false) {
            result = 1;
        }

        return result;
   }

    public void startService() {

        Intent it = new Intent(this,Service.class);
        startService(it);
    }

    public void stopService() {
        Intent it = new Intent("REPLICACAO_IBS");
        stopService(it);
    }

    private void addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(), main.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Ibs - PEC");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher));

        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        addIntent.putExtra("duplicate", false);  //may it's already there so don't duplicate
        getApplicationContext().sendBroadcast(addIntent);
    }

    public void installAPP() {

        String filePath = Environment.getExternalStorageDirectory().toString() + "/DCIM/pec.apk";
        Process installProcess = null;
        int installResult = -1337;

        try {
            //installProcess = Runtime.getRuntime().exec("su -c pm install -r " + filePath);
            File file = new File(filePath);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            // Handle IOException the way you like.
        }

        if (installProcess != null) {
            try {
                installResult = installProcess.waitFor();
            } catch (InterruptedException e) {
                // Handle InterruptedException the way you like.
            }

            if (installResult == 0) {
                // Success!
            } else {
                // Failure. :-/
            }
        } else {
            // Failure 2. :-(
        }

        Intent i = new Intent(main.this, main.class);
        startActivity(i);
    }

    public void populaProjeto(int _cliente) {
        SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db",
                Context.MODE_PRIVATE, null);
        Spinner ddlProjeto = (Spinner) findViewById(R.id.ddlProjeto);
        Cursor cursor = db
                .rawQuery(
                        "SELECT rowid _id, id, nome, _escritorio, _cliente FROM PROJETO WHERE _cliente = "
                                + _cliente
                                + " OR NOME = '-SELECIONE O PROJETO-' ORDER BY NOME",
                        null);
        String[] from2 = {"nome", "id", "_escritorio"};
        int[] to2 = {R.id.lblNome, R.id.lblId, R.id.lbl_id_FK};
        @SuppressWarnings("deprecation")
        SimpleCursorAdapter ad = new SimpleCursorAdapter(getBaseContext(),
                R.layout.spinner, cursor, from2, to2);
        ddlProjeto.setAdapter(ad);

        try {
            if (ddlCliente.getSelectedItemPosition() != 0) {

                ddlProjeto.setSelection(mDados.getProjeto_position());
            }
        } catch (Exception ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void populaCliente() {

        SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db",
                Context.MODE_PRIVATE, null);
        Spinner ddlCliente = (Spinner) findViewById(R.id.ddlCliente);
        Cursor cursor = db
                .rawQuery(
                        "SELECT DISTINCT rowid _id, ID,NOME FROM CLIENTE  ORDER BY NOME",
                        null);
        String[] from = {"nome", "id"};
        int[] to = {R.id.lblNome, R.id.lblId};
        @SuppressWarnings("deprecation")
        SimpleCursorAdapter ad = new SimpleCursorAdapter(getBaseContext(),
                R.layout.spinner, cursor, from, to);
        ddlCliente.setAdapter(ad);
    }

    public void populaGrupo(int _projeto) {
        SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        Spinner ddlGrupo = (Spinner) findViewById(R.id.ddlGrupo);
        Cursor cursor = db.rawQuery(
                "SELECT rowid _id, id, nome, _projeto FROM GRUPO WHERE _projeto = "
                        + _projeto
                        + " OR NOME = '-SELECIONE O GRUPO-' ORDER BY NOME",
                null);
        String[] from3 = {"nome", "id", "_projeto"};
        int[] to3 = {R.id.lblNome, R.id.lblId, R.id.lbl_id_FK};
        @SuppressWarnings("deprecation")
        SimpleCursorAdapter ad = new SimpleCursorAdapter(getBaseContext(),
                R.layout.spinner, cursor, from3, to3);
        ddlGrupo.setAdapter(ad);

        try {

            if (ddlProjeto.getSelectedItemPosition() != 0) {

                ddlGrupo.setSelection(mDados.getGrupo_position());
            }
        } catch (Exception ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public void populaProdutor(int _grupo) {
        SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        Spinner ddlPrdutor = (Spinner) findViewById(R.id.ddlProdutor);
        Cursor cursor = db.rawQuery("SELECT rowid _id, id, nome, _grupo FROM PRODUTOR WHERE _grupo = "
                + _grupo +
                " OR NOME = '-SELECIONE O PRODUTOR-' ORDER BY NOME", null);
        String[] from = {"nome", "_id", "_grupo"};
        int[] to = {R.id.lblNome, R.id.lblId, R.id.lbl_id_FK};

        @SuppressWarnings("deprecation")
        SimpleCursorAdapter ad1 = new SimpleCursorAdapter(getBaseContext(),
                R.layout.spinner, cursor, from, to);
        ddlPrdutor.setAdapter(ad1);
        db.close();
    }

    public void MessageBox(String msg) {
        AlertDialog.Builder informa = new AlertDialog.Builder(this);
        informa.setTitle("Alerta!").setMessage(msg);
        informa.setNeutralButton("Fechar", null).show();
    }

    //################### MENU ####################################
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
       // inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    // #############################################################

    public void copyDataBase() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String data_corrente = dateFormat.format(date);

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
                    @SuppressWarnings("resource")
                    FileChannel src = new FileInputStream(currentDB)
                            .getChannel();
                    @SuppressWarnings("resource")
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

    public void MakeTables() {

        db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        //db.execSQL("DELETE FROM CRIATF");
        //db.execSQL("UPDATE CRIATF SET processo_iatf = 'N�O'");
        // db.execSQL("UPDATE FrmSojaII SET FormEnviado = 'N�O'");

        StringBuilder sqlVersion = new StringBuilder();
        sqlVersion.append("CREATE TABLE IF NOT EXISTS [versao](");
        sqlVersion.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlVersion.append("versao varchar(10)); ");
        db.execSQL(sqlVersion.toString());

        sqlVersion = new StringBuilder();
        sqlVersion.append("CREATE TABLE IF NOT EXISTS [financeiro](");
        sqlVersion.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlVersion.append("idProduto INTEGER,");
        sqlVersion.append("enviado varchar(5), ");
        sqlVersion.append("_projeto INTEGER,");
        sqlVersion.append("_grupo INTEGER,");
        sqlVersion.append("_propriedade INTEGER,");
        sqlVersion.append("quantidade INTEGER,");
        sqlVersion.append("descricao varchar(100), ");
        sqlVersion.append("unidadeMedida varchar(10), ");
        sqlVersion.append("valorUnitario double, ");
        sqlVersion.append("valorMaximo double, ");
        sqlVersion.append("tipo varchar(20),");
        sqlVersion.append("valorMinimo double); ");
        db.execSQL(sqlVersion.toString());

        StringBuilder sqlTouro = new StringBuilder();
        sqlTouro.append("CREATE TABLE IF NOT EXISTS [touro](");
        sqlTouro.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlTouro.append("id INTEGER, ");
        sqlTouro.append("nome varchar(30), ");
        sqlTouro.append("sememDisponivel INTEGER, ");
        sqlTouro.append("brinco INTEGER, ");
        sqlTouro.append("rg varchar(30), ");
        sqlTouro.append("id_raca INTEGER, ");
        sqlTouro.append("raca varchar(30)); ");
        db.execSQL(sqlTouro.toString());

        StringBuilder sqlAtend01 = new StringBuilder();
        sqlAtend01.append("CREATE TABLE IF NOT EXISTS [criatf](");
        sqlAtend01.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlAtend01.append("id INTEGER, ");
        sqlAtend01.append("_projeto INTEGER, ");
        sqlAtend01.append("_grupo INTEGER, ");
        sqlAtend01.append("_propriedade INTEGER, ");
        sqlAtend01.append("id_criatf INTEGER,");
        sqlAtend01.append("criatf_pai INTEGER,");
        sqlAtend01.append("data_protocolo varchar(20), ");
        sqlAtend01.append("data_inseminacao varchar(20), ");
        sqlAtend01.append("data_d0 varchar(20), ");
        sqlAtend01.append("vacina varchar(10), ");
        sqlAtend01.append("_raca INTEGER, ");
        sqlAtend01.append("touro varchar(30), ");
        sqlAtend01.append("consultor varchar(20), ");
        sqlAtend01.append("cod_mobile varchar(20),");
        sqlAtend01.append("enviado varchar(5), ");
        sqlAtend01.append("fez_d0 varchar(5), ");
        sqlAtend01.append("nome_vaca varchar(30), ");
        sqlAtend01.append("_id_animais INTEGER, ");
        sqlAtend01.append("id_reprodutivo INTEGER, ");
        sqlAtend01.append("id_cobertura INTEGER, ");
        sqlAtend01.append("processo_d8 varchar(10), ");
        sqlAtend01.append("processo_iatf varchar(10), ");
        sqlAtend01.append("fez_ia varchar(10), ");
        sqlAtend01.append("dg varchar(10), ");
        sqlAtend01.append("diagnostico varchar(10), ");
        sqlAtend01.append("numeroDias varchar(20), ");
        sqlAtend01.append("previsao_parto varchar(20), ");
        sqlAtend01.append("tipo_atendimento varchar(20), ");
        sqlAtend01.append("nova_ia varchar(5)); ");
        db.execSQL(sqlAtend01.toString());

		/*StringBuilder sqlAtend02 = new StringBuilder();
        sqlAtend02.append("CREATE TABLE IF NOT EXISTS [atendimento_02](");
		sqlAtend02.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
		sqlAtend02.append("id INTEGER, ");
		sqlAtend02.append("_projeto INTEGER, ");
		sqlAtend02.append("_grupo INTEGER, ");
		sqlAtend02.append("_propriedade INTEGER, ");
		sqlAtend02.append("data_atendimento varchar(20), ");
		sqlAtend02.append("id_vaca INTEGER, ");
		sqlAtend02.append("id_cobertura INTEGER, ");
		sqlAtend02.append("id_reprodutivo INTEGER, ");
		sqlAtend02.append("nome_vaca varchar(30), ");		
		sqlAtend02.append("processo_d8 varchar(5), ");
		sqlAtend02.append("processo_iatf varchar(5), ");		
		sqlAtend02.append("raca varchar(20), ");
		sqlAtend02.append("enviado varchar(5), ");
		sqlAtend02.append("dg varchar(20), ");
		sqlAtend02.append("consultor varchar(10), ");
		sqlAtend02.append("nome_touro varchar(30)); ");
	    db.execSQL(sqlAtend02.toString());
	    
	    StringBuilder sqlAtend03 = new StringBuilder();
	    sqlAtend03.append("CREATE TABLE IF NOT EXISTS [atendimento_03](");
	    sqlAtend03.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
		sqlAtend03.append("id INTEGER, ");
		sqlAtend03.append("_projeto INTEGER, ");
		sqlAtend03.append("_grupo INTEGER, ");
		sqlAtend03.append("_propriedade INTEGER, ");
		sqlAtend03.append("data_atendimento varchar(20), ");
		sqlAtend03.append("nome_vaca varchar(30), ");	
		sqlAtend03.append("enviado varchar(5), ");	
		sqlAtend03.append("previsao_parto varchar(20), ");
		sqlAtend03.append("consultor varchar(20), ");
		sqlAtend03.append("diagnostico varchar(30)); ");
	    db.execSQL(sqlAtend03.toString());		*/

        StringBuilder sqlTemp = new StringBuilder();
        sqlTemp.append("CREATE TABLE IF NOT EXISTS [temp](");
        sqlTemp.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlTemp.append("id INTEGER, ");
        sqlTemp.append("nome varchar(10)); ");
        db.execSQL(sqlTemp.toString());

        StringBuilder sqlLogin = new StringBuilder();
        sqlLogin.append("CREATE TABLE IF NOT EXISTS [login](");
        sqlLogin.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlLogin.append("nome varchar(20), ");
        sqlLogin.append("crmv varchar(10), ");
        sqlLogin.append("usuario varchar(10));");
        db.execSQL(sqlLogin.toString());

        StringBuilder sqlCliente = new StringBuilder();
        sqlCliente.append("CREATE TABLE IF NOT EXISTS [cliente](");
        sqlCliente.append("[id] INTEGER PRIMARY KEY, ");
        sqlCliente.append("nome varchar(100));");
        db.execSQL(sqlCliente.toString());

        StringBuilder sqlEscritorio = new StringBuilder();
        sqlEscritorio.append("CREATE TABLE IF NOT EXISTS [escritorio](");
        sqlEscritorio.append("[id] INTEGER PRIMARY KEY , ");
        sqlEscritorio.append("nome varchar(100), ");
        sqlEscritorio.append("_cliente integer);");
        db.execSQL(sqlEscritorio.toString());

        StringBuilder sqlProjeto = new StringBuilder();
        sqlProjeto.append("CREATE TABLE IF NOT EXISTS [projeto](");
        sqlProjeto.append("[id] INTEGER PRIMARY KEY , ");
        sqlProjeto.append("nome varchar(100), ");
        sqlProjeto.append("_cliente integer,");
        sqlProjeto.append("_escritorio integer);");
        db.execSQL(sqlProjeto.toString());

        StringBuilder sqlGrupo = new StringBuilder();
        sqlGrupo.append("CREATE TABLE IF NOT EXISTS [grupo](");
        sqlGrupo.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlGrupo.append("[id] INTEGER , ");
        sqlGrupo.append("nome varchar(100), ");
        sqlGrupo.append("_projeto integer);");
        db.execSQL(sqlGrupo.toString());

        StringBuilder sqlProdutor = new StringBuilder();
        sqlProdutor.append("CREATE TABLE IF NOT EXISTS [produtor](");
        sqlProdutor.append("[_id] INTEGER PRIMARY KEY , ");
        sqlProdutor.append("[id] INTEGER  , ");
        sqlProdutor.append("nome varchar(100), ");
        sqlProdutor.append("longitude varchar(100), ");
        sqlProdutor.append("latidude varchar(100), ");
        sqlProdutor.append("_grupo integer);");
        db.execSQL(sqlProdutor.toString());

        StringBuilder sqlPaperPort = new StringBuilder();
        sqlPaperPort.append("CREATE TABLE IF NOT EXISTS [paperPort](");
        sqlPaperPort.append("[_id] INTEGER PRIMARY KEY , ");
        sqlPaperPort.append("[id] INTEGER  , ");
        sqlPaperPort.append("_projeto integer, ");
        sqlPaperPort.append("_grupo integer, ");
        sqlPaperPort.append("_propriedade integer, ");
        sqlPaperPort.append("id_criatf integer, ");
        sqlPaperPort.append("modificado varchar(3), ");
        sqlPaperPort.append("data_coleta varchar(20), ");
        sqlPaperPort.append("area_atual varchar(50), ");
        sqlPaperPort.append("brinco varchar(50), ");
        sqlPaperPort.append("enviado varchar(3), ");
        sqlPaperPort.append("nome_usual varchar(50), ");
        sqlPaperPort.append("dataCobertura varchar(20), ");
        sqlPaperPort.append("dataDiagnostico varchar(20), ");
        sqlPaperPort.append("dataParto varchar(20), ");
        sqlPaperPort.append("dataPesagem varchar(20), ");
        sqlPaperPort.append("diasPrenhez varchar(20), ");
        sqlPaperPort.append("verifica varchar(3), ");
        sqlPaperPort.append("fez_d0 varchar(5), ");
        sqlPaperPort.append("idCobertura integer, ");
        sqlPaperPort.append("idReprodutivo integer, ");
        sqlPaperPort.append("idAnimais integer, ");
        sqlPaperPort.append("numeroFetos integer, ");
        sqlPaperPort.append("numeroDias integer, ");
        sqlPaperPort.append("previsaoParto varchar(20), ");
        sqlPaperPort.append("categoria varchar(20), ");
        sqlPaperPort.append("previsaoSecagem varchar(20), ");
        sqlPaperPort.append("statusProdutivo varchar(20), ");
        sqlPaperPort.append("statusReprodutivo varchar(20), ");
        sqlPaperPort.append("manejo varchar(50), ");
        sqlPaperPort.append("peso_atual varchar(20), ");
        sqlPaperPort.append("tipoParto varchar(20), ");
        sqlPaperPort.append("status varchar(50), ");
        sqlPaperPort.append("data_secagem varchar(20), ");
        sqlPaperPort.append("obs varchar(100), ");
        sqlPaperPort.append("nome_projeto varchar(100), ");
        sqlPaperPort.append("nome_grupo varchar(100), ");
        sqlPaperPort.append("nome_propriedade varchar(100), ");
        sqlPaperPort.append("ultimo_parto varchar(20), ");
        sqlPaperPort.append("ultimo_peso varchar(20), ");
        sqlPaperPort.append("ocorrencia varchar(20), ");
        sqlPaperPort.append("cod_mobile varchar(20), ");
        sqlPaperPort.append("raca varchar(20), ");
        sqlPaperPort.append("data_nascimento varchar(20), ");
        sqlPaperPort.append("referencia varchar(20));");
        db.execSQL(sqlPaperPort.toString());

        // db.execSQL("DROP TABLE CHECKLIST");

        StringBuilder sqlCheckList = new StringBuilder();
        sqlCheckList.append("CREATE TABLE IF NOT EXISTS [checklist](");
        sqlCheckList.append("[_id] INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlCheckList.append("[id] INTEGER, ");
        sqlCheckList.append("_cliente integer, ");
        sqlCheckList.append("_projeto integer, ");
        sqlCheckList.append("_grupo integer, ");
        sqlCheckList.append("_produtor varchar(100), ");
        sqlCheckList.append("data varchar(20), ");
        sqlCheckList.append("enviado varchar(3), ");
        sqlCheckList.append("controle1 varchar(10), ");
        sqlCheckList.append("controle2 varchar(10), ");
        sqlCheckList.append("controle3 varchar(10), ");
        sqlCheckList.append("manejoN1 varchar(10), ");
        sqlCheckList.append("manejoN2 varchar(10), ");
        sqlCheckList.append("manejoN3 varchar(10), ");
        sqlCheckList.append("manejoN4 varchar(10), ");
        sqlCheckList.append("manejoN5 varchar(10), ");
        sqlCheckList.append("manejoN6 varchar(10), ");
        sqlCheckList.append("sanidade1 varchar(10), ");
        sqlCheckList.append("sanidade2 varchar(10), ");
        sqlCheckList.append("sanidade3 varchar(10), ");
        sqlCheckList.append("sanidade4 varchar(10), ");
        sqlCheckList.append("sanidade5 varchar(10), ");
        sqlCheckList.append("manejoR1 varchar(10), ");
        sqlCheckList.append("manejoR2 varchar(10), ");
        sqlCheckList.append("manejoR3 varchar(10), ");
        sqlCheckList.append("manejoR4 varchar(10), ");
        sqlCheckList.append("qualidade1 varchar(10), ");
        sqlCheckList.append("qualidade2 varchar(10), ");
        sqlCheckList.append("qualidade3 varchar(10), ");
        sqlCheckList.append("consultor varchar(20), ");
        sqlCheckList.append("tipoReprodutivo varchar(20), ");
        sqlCheckList.append("situacaoEncontrada varchar(999), ");
        sqlCheckList.append("recomendacoes varchar(999), ");
        sqlCheckList.append("qualidade4 varchar(10));");
        db.execSQL(sqlCheckList.toString());

        StringBuilder sqlReport = new StringBuilder();
        sqlReport.append("CREATE TABLE IF NOT EXISTS [report](");
        sqlReport.append("[_id] INTEGER PRIMARY KEY , ");
        sqlReport.append("[id] INTEGER  , ");
        sqlReport.append("_cliente integer, ");
        sqlReport.append("_projeto integer, ");
        sqlReport.append("_grupo integer, ");
        sqlReport.append("_produtor varchar(100), ");
        sqlReport.append("data varchar(20), ");
        sqlReport.append("consultor varchar(20), ");
        sqlReport.append("enviado varchar(3), ");
        sqlReport.append("tipoReprodutivo varchar(100), ");
        sqlReport.append("situacaoEncontrada varchar(2000), ");
        sqlReport.append("caminho_imagem varchar(100), ");
        sqlReport.append("recomedacoes varchar(2000));");
        db.execSQL(sqlReport.toString());

        StringBuilder sqlAnimal = new StringBuilder();
        sqlAnimal.append("CREATE TABLE IF NOT EXISTS [animal](");
        sqlAnimal.append("[_id] INTEGER PRIMARY KEY , ");
        sqlAnimal.append("[id] INTEGER  , ");
        sqlAnimal.append("_cliente integer, ");
        sqlAnimal.append("_projeto integer, ");
        sqlAnimal.append("_grupo integer, ");
        sqlAnimal.append("_produtor varchar(100), ");
        sqlAnimal.append("data varchar(20), ");
        sqlAnimal.append("consultor varchar(20), ");
        sqlAnimal.append("enviado varchar(3), ");
        sqlAnimal.append("status varchar(6), ");
        sqlAnimal.append("brinco varchar(100), ");
        sqlAnimal.append("nome varchar(50), ");
        sqlAnimal.append("numero_partos varchar(10), ");
        sqlAnimal.append("sexo varchar(10), ");
        sqlAnimal.append("origem_entrada varchar(50), ");
        sqlAnimal.append("origem_entrada_p INTEGER, ");
        sqlAnimal.append("raca varchar(50), ");
        sqlAnimal.append("categoria varchar(50), ");
        sqlAnimal.append("pelagem varchar(50), ");
        sqlAnimal.append("entrada_plantel varchar(50), ");
        sqlAnimal.append("ultimo_parto varchar(20), ");
        sqlAnimal.append("data_nascimento varchar(20));");
        db.execSQL(sqlAnimal.toString());

        StringBuilder sqlCategoria = new StringBuilder();
        sqlCategoria.append("CREATE TABLE IF NOT EXISTS [categoria](");
        sqlCategoria.append("[_id] INTEGER PRIMARY KEY , ");
        sqlCategoria.append("[id] INTEGER  , ");
        sqlCategoria.append("nome varchar(50));");
        db.execSQL(sqlCategoria.toString());

        StringBuilder sqlRaca = new StringBuilder();
        sqlRaca.append("CREATE TABLE IF NOT EXISTS [raca](");
        sqlRaca.append("[_id] INTEGER PRIMARY KEY , ");
        sqlRaca.append("[id] INTEGER  , ");
        sqlRaca.append("nome varchar(50));");
        db.execSQL(sqlRaca.toString());

        Cursor cursor1 = db.rawQuery("SELECT COUNT(*)as TOTAL FROM cliente WHERE id = 9999", null);
        cursor1.moveToFirst();
        int count1 = cursor1.getInt(cursor1.getColumnIndex("TOTAL"));

        if (count1 == 0) {

            db.execSQL("INSERT INTO cliente   (id,nome) VALUES (9999,'-SELECIONE O CLIENTE-')");
            db.execSQL("INSERT INTO projeto   (id,nome) VALUES (9999,'-SELECIONE O PROJETO-')");
            db.execSQL("INSERT INTO grupo     (id,nome) VALUES (9999,'-SELECIONE O GRUPO-')");
            db.execSQL("INSERT INTO produtor  (id,nome) VALUES (99999,'-SELECIONE O PRODUTOR-')");

            db.execSQL("INSERT INTO versao (versao) VALUES ('2')");
        }

        cursor1 = db.rawQuery("SELECT COUNT(*)as TOTAL FROM CRIATF WHERE _ID = 1", null);
        cursor1.moveToFirst();
        int count = cursor1.getInt(cursor1.getColumnIndex("TOTAL"));

        if (count == 0) {

            db.execSQL("INSERT INTO CRIATF (_id,nome_vaca) VALUES (1,'-SELECIONE O ANIMAL-')");
        }


        cursor1 = db.rawQuery("SELECT COUNT(*)as TOTAL FROM categoria", null);
        cursor1.moveToFirst();
        count = cursor1.getInt(cursor1.getColumnIndex("TOTAL"));

        if (count == 0) {

            db.execSQL("INSERT INTO categoria  (id,nome) VALUES (1,'VACA')");
            db.execSQL("INSERT INTO categoria  (id,nome) VALUES (2,'TOURO')");
            //db.execSQL("INSERT INTO categoria  (id,nome) VALUES (3,'BEZERRA')");
            //db.execSQL("INSERT INTO categoria  (id,nome) VALUES (4,'BEZERRO')");
            /*db.execSQL("INSERT INTO categoria  (id,nome) VALUES (5,'BZA DESMAMA')");
			db.execSQL("INSERT INTO categoria  (id,nome) VALUES (6,'BZP DESMAMA')");
			db.execSQL("INSERT INTO categoria  (id,nome) VALUES (7,'NOVILHA')");
			db.execSQL("INSERT INTO categoria  (id,nome) VALUES (8,'GARROTE')");
			db.execSQL("INSERT INTO categoria  (id,nome) VALUES (9,'FEMEA RECRIA')");*/
            db.execSQL("INSERT INTO categoria  (id,nome) VALUES (11,'BOI')");

            db.execSQL("INSERT INTO raca  (id,nome) VALUES (1,'SEM RAÇA DEFINIDA')");
            db.execSQL("INSERT INTO raca  (id,nome) VALUES (2,'CRUZAMENTO INDUSTRIAL')");
            db.execSQL("INSERT INTO raca  (id,nome) VALUES (3,'GIR')");
            db.execSQL("INSERT INTO raca  (id,nome) VALUES (4,'GIROLANDO')");
            db.execSQL("INSERT INTO raca  (id,nome) VALUES (5,'JERSEY')");
            db.execSQL("INSERT INTO raca  (id,nome) VALUES (6,'NELORE')");
            db.execSQL("INSERT INTO raca  (id,nome) VALUES (7,'HOLANDESA')");

        }

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private class TaskVerificarVersao extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;
        private int atualiza = 0;

        public TaskVerificarVersao(Context context) {
            this.context = context;
        }


        @Override
        protected String doInBackground(String... sUrl) {

            try {
                if (getVersionName().equals(VerficaVersao())) {
                    atualiza = 0;
                } else {
                    NewVersion();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(main.this);
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(mProgressDialog.THEME_DEVICE_DEFAULT_DARK);
            mProgressDialog.setMessage("Verficando Versão Ibs Pec...");
            mProgressDialog.setTitle("Instituto biosistêmico");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false

            mProgressDialog.setProgress(progress[0]);


        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();

            if(atualiza == 0){
                showMessage("OK", "Você já esta com a última versão do App.");

            }
            else {

                showMessage("Ok","Download concluído, instale o app.");
            }

        }

    }

    public String VerficaVersao() throws JSONException {

        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://179.184.44.116:8086/VersaoApp.aspx?app=Pec");

        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {

                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                if ((line = reader.readLine()) != null)
                {
                    JSONArray jArray = new JSONArray(line);

                    line = line.replace(":{",":[{");
                    line = line.replace("}}","}]}");

                    for (int j = 0; j < jArray.length(); j++)
                    {
                        JSONObject jReal = jArray.getJSONObject(j);

                        versao = jReal.getString("nome");
                        numero = jReal.getString("numero");

                    }
                }
            }

        } catch (ClientProtocolException e)
        {
            e.printStackTrace();

        } catch (IOException e)
        {
            e.printStackTrace();

        }
        catch(RuntimeException e){
            e.getMessage();
        }

        catch (Exception ex) {

        }

        
        return versao;
    }

    public String NewVersion() throws JSONException {

        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        String versao_corrente = VerficaVersao();

        if (!versao_corrente.equals("")) {

            if (versao_corrente.equals(getVersionName())) {
                // Toast.makeText(main.this, "?ltima vers?o j? instalada!", Toast.LENGTH_LONG).show();
            } else {
                try {

                    // URL url = new URL("http://192.168.25.100:8086/download/pec.apk");
                    URL url = new URL("http://179.184.44.116:8086/download/pec.apk");

                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    // expect HTTP 200 OK, so we don't mistakenly save error report
                    // instead of the file
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        return "Server returned HTTP " + connection.getResponseCode()
                                + " " + connection.getResponseMessage();
                    }

                    // this will be useful to display download percentage
                    // might be -1: server did not report the length
                    int fileLength = connection.getContentLength();

                    // download the file
                    input = connection.getInputStream();
                    output = new FileOutputStream("/sdcard/DCIM/pec.apk");

                    byte data[] = new byte[4096];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        // allow canceling with back button
                        if (false) {
                            input.close();
                            return null;
                        }
                        total += count;
                        // publishing the progress....
                        if (fileLength > 0) // only if total length is known
                            //   publishProgress((int) (total * 100 / fileLength));
                            output.write(data, 0, count);
                    }
                } catch (Exception e) {
                    return e.toString();
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
                    } catch (IOException ignored) {
                    }

                    if (connection != null)
                        connection.disconnect();
                }

                installAPP();
                addShortcut();
            }
        }


        return null;
    }

    private void showMessage(String msg, String msgbody) {

    MessageBox(msgbody);

    }

    public void frmSql(View view) {
        FragmentTransaction ft;

        ft = getFragmentManager().beginTransaction();
        Password f = new Password();
        f.show(ft, "form1");

    }



}