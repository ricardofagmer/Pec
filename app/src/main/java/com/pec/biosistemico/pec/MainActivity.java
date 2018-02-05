package com.pec.biosistemico.pec;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.OnCheckedChangeListener;
import com.pec.biosistemico.pec.checklist.MainCheckList;
import com.pec.biosistemico.pec.criatf.Atendimento_01;
import com.pec.biosistemico.pec.criatf.Atendimento_02;
import com.pec.biosistemico.pec.criatf.Atendimento_03;
import com.pec.biosistemico.pec.fechamento.ReportFinaly;
import com.pec.biosistemico.pec.paperTop.FrmPaperTop;
import com.pec.biosistemico.pec.popUp.TrocaProdutor;
import com.pec.biosistemico.pec.util.Global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private static String TAG = "LOG";
    private Toolbar mToolbar;
    private Toolbar mToolbarBottom;
    private Drawer.Result navigationDrawerLeft;
    private Drawer.Result navigationDrawerRight;
    private AccountHeader.Result headerNavigationLeft;
    private int mPositionClicked;
    private SQLiteDatabase db;
    private Global mDados = Global.getInstance();
    private android.app.FragmentManager fm = getFragmentManager();


    private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean b) {
            Toast.makeText(MainActivity.this, "onCheckedChanged: "+( b ? "true" : "false" ), Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._activity_main);

        db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("PEC - Instituo Biosistêmico");
        mToolbar.setTitleTextColor(Color.WHITE);
      // mToolbar.setSubtitle(mDados.getLogin().toString());
      //  mToolbar.setLogo(R.drawable.ibs);
        setSupportActionBar(mToolbar);

             // FRAGMENT
                FrmPaperTop paper = new FrmPaperTop();
                android.app.FragmentTransaction _ft = getFragmentManager().beginTransaction();
                _ft.replace(R.id.rl_fragment_container, paper, "mainFrag");
                _ft.commit();


        headerNavigationLeft = new AccountHeader()
                    .withActivity(this)
                    .withCompactStyle(false)
                    .withSavedInstance(savedInstanceState)
                    .withThreeSmallProfileImages(true)
                    .withHeaderBackground(R.drawable.vaca_drawer)
                    .addProfiles(
                            new ProfileDrawerItem().withName(mDados.getLogin()).withEmail("pec.biosistemico@gmail.com").withIcon(getResources().getDrawable(R.drawable.ibs))

                    )
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile iProfile, boolean b) {
                            Toast.makeText(MainActivity.this, "onProfileChanged: " + iProfile.getName(), Toast.LENGTH_SHORT).show();
                            headerNavigationLeft.setBackgroundRes(R.drawable.vaca_drawer);
                            return false;
                        }
                    })
                    .build();

            navigationDrawerLeft = new Drawer()
                    .withActivity(this)
                    .withToolbar(mToolbar)
                    .withDisplayBelowToolbar(false)
                    .withActionBarDrawerToggleAnimated(true)
                    .withDrawerGravity(Gravity.LEFT)
                    .withSavedInstance(savedInstanceState)
                    .withSelectedItem(0)
                    .withActionBarDrawerToggle(true)
                    .withAccountHeader(headerNavigationLeft)

                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {

                            android.app.FragmentTransaction _ft = getFragmentManager().beginTransaction();

                            switch (i){

                                case 0:
                                    FrmPaperTop paper = new FrmPaperTop();
                                    _ft.replace(R.id.rl_fragment_container, paper, "mainFrag");
                                    _ft.commit();
                                    break;

                                case 1:
                                    MainCheckList check = new MainCheckList();
                                    _ft.replace(R.id.rl_fragment_container, check, "mainFrag");
                                    _ft.commit();
                                    break;

                                case 2:
                                    ReportFinaly reportFinaly = new ReportFinaly();
                                    _ft.replace(R.id.rl_fragment_container, reportFinaly, "mainFrag");
                                    _ft.commit();
                                    break;

                                case 4:
                                     Atendimento_01 atendimento_01 = new Atendimento_01();
                                    _ft.replace(R.id.rl_fragment_container, atendimento_01, "mainFrag");
                                    _ft.commit();
                                    break;
                                case 5:
                                    Atendimento_02 atendimento_02 = new Atendimento_02();
                                    _ft.replace(R.id.rl_fragment_container, atendimento_02, "mainFrag");
                                    _ft.commit();
                                    break;

                                case 6:
                                    Atendimento_03 atendimento_03 = new Atendimento_03();
                                    _ft.replace(R.id.rl_fragment_container, atendimento_03, "mainFrag");
                                    _ft.commit();
                                    break;

                                case 8:

                                    StringBuilder sql = new StringBuilder();

                                    sql.append("DELETE FROM PAPERPORT WHERE IDANIMAIS IN ( ");
                                    sql.append("SELECT IDANIMAIS FROM PAPERPORT ");
                                    sql.append("GROUP BY IDANIMAIS ");
                                    sql.append("HAVING COUNT(IDANIMAIS) > 1 AND IDANIMAIS <> 0) ");
                                    sql.append("AND NOT _ID IN ");
                                    sql.append("(SELECT MAX(_ID) FROM PAPERPORT ");
                                    sql.append("GROUP BY IDANIMAIS HAVING COUNT(IDANIMAIS) > 1); ");

                                    db.execSQL(sql.toString());

                                    Toast.makeText(MainActivity.this,"Duplicados eliminados com sucesso.",Toast.LENGTH_LONG).show();

                                     FrmPaperTop paper1 = new FrmPaperTop();
                                    _ft.replace(R.id.rl_fragment_container, paper1, "mainFrag");
                                    _ft.commit();

                                    break;
                            }

                            mPositionClicked = i;
                            navigationDrawerLeft.getAdapter().notifyDataSetChanged();
                        }
                    })
                    .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                            Toast.makeText(MainActivity.this, "onItemLongClick: " + i, Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    })
                    .build();

       // navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("NOVOS ANIMAIS").withIcon(getResources().getDrawable(R.drawable.cow)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("PAPER TOP").withIcon(getResources().getDrawable(R.drawable.paper)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("CHECK-LIST ").withIcon(getResources().getDrawable(R.drawable.check)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("RELATÓRIO").withIcon(getResources().getDrawable(R.drawable.relatorio)));
        navigationDrawerLeft.addItem(new SectionDrawerItem().withName("ATENDIMENTO - CRIATF"));

        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("RUFIÃO").withIcon(getResources().getDrawable(R.drawable.d0)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("INSEMINAÇÃO").withIcon(getResources().getDrawable(R.drawable.ia)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("DIAGNÓSTICO").withIcon(getResources().getDrawable(R.drawable.dg)));

        navigationDrawerLeft.addItem(new SectionDrawerItem().withName("TI"));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("ELIMINAR DUPLICADOS").withIcon(getResources().getDrawable(R.drawable.check)));


    }

    private  void ReturnHome(){

        Intent obtnptodos = new Intent(this,main.class);
        startActivity(obtnptodos);
        //finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_interno, menu);
        return true;
    }
    public void trocaProdutor(View view) {
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        TrocaProdutor cdf = new TrocaProdutor(1, 1);
        cdf.show(ft, "DG");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Global mDados = Global.getInstance();
        String consultor = mDados.getLogin();

		/*if (conn.getNetworkInfo(1).isConnected() == false) {
			AlertDialog.Builder informa = new AlertDialog.Builder(this);
			informa.setTitle("Atenção!").setMessage("LIGUE O WIFI ANTES DE CARREGAR OS DADOS!");
			informa.setNeutralButton("Voltar", null).show();
		}

		*/
        if (item.getItemId() == R.id.mnuDB)
        {
            copyDataBase();
            Date dataAtual = new Date(System.currentTimeMillis());

            Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "IbsPEC.db"));
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("image/*");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "bkp_" + consultor + "_" + dataAtual.toString()+".db");
            sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(sharingIntent, "Salvar Copia do Banco de dados"));

        }
        else{

           // Intent obtnptodos = new Intent(this,main.class);
           // startActivity(obtnptodos);
            finish();
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = navigationDrawerLeft.saveInstanceState(outState);
        outState = headerNavigationLeft.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if(navigationDrawerLeft.isDrawerOpen()){
            navigationDrawerLeft.closeDrawer();
        }
        else{
            super.onBackPressed();
        }
    }
}
