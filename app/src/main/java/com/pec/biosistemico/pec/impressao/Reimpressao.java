package com.pec.biosistemico.pec.impressao;

    import android.app.Activity;
    import android.app.AlertDialog;
    import android.app.FragmentTransaction;
    import android.app.ProgressDialog;
    import android.bluetooth.BluetoothAdapter;
    import android.bluetooth.BluetoothDevice;
    import android.bluetooth.BluetoothSocket;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.net.Uri;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.os.PowerManager;
    import android.text.Html;
    import android.util.Log;
    import android.view.Menu;
    import android.view.MenuInflater;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.CheckBox;
    import android.widget.CompoundButton;
    import android.widget.ListView;
    import android.widget.SeekBar;
    import android.widget.SimpleCursorAdapter;
    import android.widget.AdapterView.OnItemClickListener;
    import android.widget.Spinner;
    import android.widget.Switch;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.datecs.api.emsr.EMSR;
    import com.datecs.api.printer.Printer;
    import com.datecs.api.printer.PrinterInformation;
    import com.datecs.api.printer.ProtocolAdapter;
    import com.datecs.api.rfid.RC663;
    import com.datecs.api.universalreader.UniversalReader;
    import com.google.android.gms.appindexing.AppIndex;
    import com.google.android.gms.common.api.GoogleApiClient;
    import com.pec.biosistemico.pec.R;
    import com.pec.biosistemico.pec.domain.CargaDados;
    import com.pec.biosistemico.pec.fechamento.EditarFechamento;
    import com.pec.biosistemico.pec.fechamento.print;
    import com.pec.biosistemico.pec.main;
    import com.pec.biosistemico.pec.popUp.SituacaoEncontrada;
    import com.pec.biosistemico.pec.util.Global;

    import java.io.IOException;
    import java.io.InputStream;
    import java.io.OutputStream;
    import java.net.Socket;
    import java.net.URL;
    import java.net.UnknownHostException;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.UUID;

public class Reimpressao extends Activity implements SeekBar.OnSeekBarChangeListener {

    private List<String> OP1 = new ArrayList<String>();
    private List<String> OP2 = new ArrayList<String>();
    private Spinner ddlFormulario;
    private Spinner ddlProjeto;
    private ListView lstResult;
    private ListView lstProducers;
    private static final String LOG_TAG = "PrinterSample";
    private static final int REQUEST_GET_DEVICE = 0;
    private static final int DEFAULT_NETWORK_PORT = 9100;
    private GoogleApiClient client;
    private ProtocolAdapter mProtocolAdapter;
    private ProtocolAdapter.Channel mPrinterChannel;
    private ProtocolAdapter.Channel mUniversalChannel;
    private Printer mPrinter;
    private EMSR mEMSR;
    private PrinterServer mPrinterServer;
    private BluetoothSocket mBtSocket;
    private Socket mNetSocket;
    private RC663 mRC663;
    private Cursor cursor;
    private Global mDados = Global.getInstance();
    private SQLiteDatabase db;
    private long id ;
    private CheckBox chkImpressao;
    private TextView lblNumero;
    private SeekBar bar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimpressao);

        db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        Global global = Global.getInstance();
        String grupo = global.getUsuario();

        lstProducers = (ListView) findViewById(R.id.lstProducers);
        Button btnDownload = (Button) findViewById(R.id.btnDow);
        chkImpressao = (CheckBox)findViewById(R.id.chkImpressao);

        lblNumero = (TextView) findViewById(R.id.lblNumero);
        bar = (SeekBar) findViewById(R.id.seekBar);
        bar.setProgress(2);
        lblNumero.setText("Horas de atendimento: 02");


        chkImpressao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    mDados.setTermica(true);
                    waitForConnection();
                }
                else
                {
                    mDados.setTermica(false);
                }
            }
        });

        Cursor cursor1 = db.rawQuery("SELECT C.DATA, C._ID AS _id, C._PRODUTOR, P.NOME AS nome, P.ID FROM CHECKLIST C, PRODUTOR P WHERE  C._PRODUTOR = P.ID ORDER BY C._ID DESC", null);

        String[] from = {"_id","data", "nome"};
        int[] to = {R.id.lbl_id,R.id.lblDta, R.id.lblNome};
        @SuppressWarnings("deprecation")
        SimpleCursorAdapter ad1 = new SimpleCursorAdapter(this, R.layout.reimpressao_list, cursor1, from, to);
        lstProducers.setAdapter(ad1);

        db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE,null);

        TextView lbl = (TextView)findViewById(R.id.textView);

        String print_name = "";
        String nome = mDados.getLogin();
        int contador = nome.length();

        for(int i = 0;i<contador;i++){

            if (nome.substring(i,i+1).equals(" "))
            {
                int posicao = i+1;
                print_name = nome.substring(0,posicao);
            }}

        bar.setOnSeekBarChangeListener(this);


        lstProducers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView lblID = (TextView) view.findViewById(R.id.lbl_id);
                String idSelecionado = lblID.getText().toString();
                mDados.setLastID(Integer.parseInt(idSelecionado));


                if(mDados.isTermica()){

                    printImage("pec");
                    printPEC(Integer.parseInt(idSelecionado));

                }
                else{

                    Intent obtnptodos = new Intent(Reimpressao.this, print.class);
                    startActivity(obtnptodos);
                }



                return false;
            }
        });

             lstProducers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long _id) {

                TextView lblID = (TextView) v.findViewById(R.id.lbl_id);
                String idSelecionado = lblID.getText().toString();

                mDados.setLastID(Integer.parseInt(idSelecionado));
                mDados.setEdicao(true);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                EditarFechamento cdf = new EditarFechamento();
                cdf.show(ft, "Edit");
            }

        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Reimpressao.this,main.class);
                startActivity(i);
            }
        });

    }

    public int VerficaSeTem(int id) {

        int existe = 0;

        Cursor cursor = db.rawQuery("SELECT COUNT(*) as total FROM TEMP  WHERE ID = " + id + "", null);

        if (cursor.moveToFirst()) {
            existe = cursor.getInt(cursor.getColumnIndex("total"));
        }


        return existe;
    }

    public void RefreshListTEMP() {

        SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        final ListView lstResult = (ListView) findViewById(R.id.lstResult);


        Cursor cursor2 = db.rawQuery("SELECT T._id, T.ID AS id, P.NOME AS nome FROM  PRODUTOR P, TEMP T WHERE  T.NOME = P.ID", null);
        String[] from1 = {"id", "nome"};
        int[] to1 = {R.id.lbl_id, R.id.lblNome};

        @SuppressWarnings("deprecation")
        SimpleCursorAdapter ad2 = new SimpleCursorAdapter(this, R.layout.producer_list, cursor2, from1, to1);
        lstResult.setAdapter(ad2);

    }

    public void MessageBox(String msg) {
        AlertDialog.Builder informa = new AlertDialog.Builder(this);
        informa.setTitle("Alerta!").setMessage(msg);
        informa.setNeutralButton("Fechar", null).show();
    }

    public void CallJason() {

        Intent obtnptodos = new Intent(this, CargaDados.class);
        startActivity(obtnptodos);
        finish();
    }

    // ################### MENU ####################################
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_impressao, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeActiveConnection();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GET_DEVICE) {
            if (resultCode == DeviceListActivity.RESULT_OK) {
                String address = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // address = "192.168.11.136:9100";
                if (BluetoothAdapter.checkBluetoothAddress(address)) {
                    establishBluetoothConnection(address);
                } else {
                    establishNetworkConnection(address);
                }
            } else {
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    private void toast(final String text) {
        Log.d(LOG_TAG, text);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void error(final String text) {
        Log.w(LOG_TAG, text);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void dialog(final int iconResId, final String title, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Reimpressao.this);
                builder.setIcon(iconResId);
                builder.setTitle(title);
                builder.setMessage(msg);
                builder.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog dlg = builder.create();
                dlg.show();
            }
        });
    }

    private void runTask(final PrinterRunnable r, final int msgResId) {
        final ProgressDialog dialog = new ProgressDialog(Reimpressao.this);
        dialog.setTitle("Por favor - Aguarde...");
        dialog.setMessage(getString(msgResId));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    r.run(dialog, mPrinter);
                } catch (IOException e) {
                    e.printStackTrace();
                    error("I/O error occurs: " + e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    error("Critical error occurs: " + e.getMessage());
                    finish();
                } finally {
                    dialog.dismiss();
                }
            }
        });
        t.start();
    }


    protected void initPrinter(InputStream inputStream, OutputStream outputStream)
            throws IOException {
        Log.d(LOG_TAG, "Initialize printer...");

        // Here you can enable various debug information
        //ProtocolAdapter.setDebug(true);
        Printer.setDebug(true);
        EMSR.setDebug(true);

        // Check if printer is into protocol mode. Ones the object is created it can not be released
        // without closing base streams.
        mProtocolAdapter = new ProtocolAdapter(inputStream, outputStream);
        if (mProtocolAdapter.isProtocolEnabled()) {
            Log.d(LOG_TAG, "Protocol mode is enabled");

            // Into protocol mode we can callbacks to receive printer notifications
            mProtocolAdapter.setPrinterListener(new ProtocolAdapter.PrinterListener() {
                @Override
                public void onThermalHeadStateChanged(boolean overheated) {

                }

                @Override
                public void onPaperStateChanged(boolean hasPaper) {

                }

                @Override
                public void onBatteryStateChanged(boolean lowBattery) {

                }
            });


            // Get printer instance
            mPrinterChannel = mProtocolAdapter.getChannel(ProtocolAdapter.CHANNEL_PRINTER);
            mPrinter = new Printer(mPrinterChannel.getInputStream(), mPrinterChannel.getOutputStream());

            // Check if printer has encrypted magnetic head
            ProtocolAdapter.Channel emsrChannel = mProtocolAdapter
                    .getChannel(ProtocolAdapter.CHANNEL_EMSR);
            try {
                // Close channel silently if it is already opened.
                try {
                    emsrChannel.close();
                } catch (IOException e) {
                }

                // Try to open EMSR channel. If method failed, then probably EMSR is not supported
                // on this device.
                emsrChannel.open();

                mEMSR = new EMSR(emsrChannel.getInputStream(), emsrChannel.getOutputStream());
                EMSR.EMSRKeyInformation keyInfo = mEMSR.getKeyInformation(EMSR.KEY_AES_DATA_ENCRYPTION);

                mEMSR.setEncryptionType(EMSR.ENCRYPTION_TYPE_AES256);
                mEMSR.enable();
                Log.d(LOG_TAG, "Encrypted magnetic stripe reader is available");
            } catch (IOException e) {
                if (mEMSR != null) {
                    mEMSR.close();
                    mEMSR = null;
                }
            }

            // Check if printer has encrypted magnetic head
            ProtocolAdapter.Channel rfidChannel = mProtocolAdapter
                    .getChannel(ProtocolAdapter.CHANNEL_RFID);


            // Check if printer has encrypted magnetic head
            mUniversalChannel = mProtocolAdapter.getChannel(ProtocolAdapter.CHANNEL_UNIVERSAL_READER);
            new UniversalReader(mUniversalChannel.getInputStream(), mUniversalChannel.getOutputStream());

        } else {
            Log.d(LOG_TAG, "Protocol mode is disabled");

            // Protocol mode it not enables, so we should use the row streams.
            mPrinter = new Printer(mProtocolAdapter.getRawInputStream(),
                    mProtocolAdapter.getRawOutputStream());
        }

        mPrinter.setConnectionListener(new Printer.ConnectionListener() {
            @Override
            public void onDisconnect() {
                toast("Printer is disconnected");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            waitForConnection();
                        }
                    }
                });
            }
        });

    }



    private synchronized void waitForConnection() {


        closeActiveConnection();

        // Show dialog to select a Bluetooth device.
        startActivityForResult(new Intent(this, DeviceListActivity.class), REQUEST_GET_DEVICE);

        // Start server to listen for network connection.
        try {
            mPrinterServer = new PrinterServer(new PrinterServerListener() {
                @Override
                public void onConnect(Socket socket) {
                    Log.d(LOG_TAG, "Accept connection from "
                            + socket.getRemoteSocketAddress().toString());

                    // Close Bluetooth selection dialog
                    finishActivity(REQUEST_GET_DEVICE);

                    mNetSocket = socket;
                    try {
                        InputStream in = socket.getInputStream();
                        OutputStream out = socket.getOutputStream();
                        initPrinter(in, out);
                    } catch (IOException e) {
                        e.printStackTrace();
                        error("FAILED to initialize: " + e.getMessage());
                        waitForConnection();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void establishBluetoothConnection(final String address) {
        final ProgressDialog dialog = new ProgressDialog(Reimpressao.this);
        dialog.setTitle("Por favor - Aguarde...");
        dialog.setMessage("Conectando...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        closePrinterServer();

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "Connecting to " + address + "...");

                btAdapter.cancelDiscovery();

                try {
                    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                    BluetoothDevice btDevice = btAdapter.getRemoteDevice(address);

                    InputStream in = null;
                    OutputStream out = null;

                    try {
                        BluetoothSocket btSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
                        btSocket.connect();

                        mBtSocket = btSocket;
                        in = mBtSocket.getInputStream();
                        out = mBtSocket.getOutputStream();
                    } catch (IOException e) {
                        error("FAILED to connect: " + e.getMessage());
                        waitForConnection();
                        return;
                    }

                    try {
                        initPrinter(in, out);
                    } catch (IOException e) {
                        error("FAILED to initiallize: " + e.getMessage());
                        return;
                    }
                } finally {
                    dialog.dismiss();
                }
            }
        });
        t.start();
    }

    private void establishNetworkConnection(final String address) {
        closePrinterServer();

        final ProgressDialog dialog = new ProgressDialog(Reimpressao.this);
        dialog.setTitle("Por favor - Aguarde");
        dialog.setMessage("Conectando...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        closePrinterServer();

        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "Connectiong to " + address + "...");
                try {
                    Socket s = null;
                    try {
                        String[] url = address.split(":");
                        int port = DEFAULT_NETWORK_PORT;

                        try {
                            if (url.length > 1) {
                                port = Integer.parseInt(url[1]);
                            }
                        } catch (NumberFormatException e) {
                        }

                        s = new Socket(url[0], port);
                        s.setKeepAlive(true);
                        s.setTcpNoDelay(true);
                    } catch (UnknownHostException e) {
                        error("FAILED to connect: " + e.getMessage());
                        waitForConnection();
                        return;
                    } catch (IOException e) {
                        error("FAILED to connect: " + e.getMessage());
                        waitForConnection();
                        return;
                    }

                    InputStream in = null;
                    OutputStream out = null;

                    try {
                        mNetSocket = s;
                        in = mNetSocket.getInputStream();
                        out = mNetSocket.getOutputStream();
                    } catch (IOException e) {
                        error("FAILED to connect: " + e.getMessage());
                        waitForConnection();
                        return;
                    }

                    try {
                        initPrinter(in, out);
                    } catch (IOException e) {
                        error("FAILED to initiallize: " + e.getMessage());
                        return;
                    }
                } finally {
                    dialog.dismiss();
                }
            }
        });
        t.start();
    }

    private synchronized void closeBluetoothConnection() {
        // Close Bluetooth connection
        BluetoothSocket s = mBtSocket;
        mBtSocket = null;
        if (s != null) {
            Log.d(LOG_TAG, "Close Bluetooth socket");
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void closeNetworkConnection() {
        // Close network connection
        Socket s = mNetSocket;
        mNetSocket = null;
        if (s != null) {
            Log.d(LOG_TAG, "Close Network socket");
            try {
                s.shutdownInput();
                s.shutdownOutput();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    private synchronized void closePrinterServer() {
        closeNetworkConnection();

        // Close network server
        PrinterServer ps = mPrinterServer;
        mPrinterServer = null;
        if (ps != null) {
            Log.d(LOG_TAG, "Close Network server");
            try {
                ps.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void closePrinterConnection() {
        if (mRC663 != null) {
            try {
                mRC663.disable();
            } catch (IOException e) {
            }

            mRC663.close();
        }

        if (mEMSR != null) {
            mEMSR.close();
        }

        if (mPrinter != null) {
            mPrinter.close();
        }

        if (mProtocolAdapter != null) {
            mProtocolAdapter.close();
        }
    }

    private synchronized void closeActiveConnection() {
        closePrinterConnection();
        closeBluetoothConnection();
        closeNetworkConnection();
        closePrinterServer();
    }

    private void printPEC(final int id) {
        Log.d(LOG_TAG, "Print Text");

        runTask(new Reimpressao.PrinterRunnable() {
                    @Override
                    public void run(ProgressDialog dialog, Printer printer) throws IOException {

                        printer.reset();
                        printer.printTaggedText(RetornaPEC(id),"ISO-8859-1");
                        printer.setIntensity(5);
                        printer.feedPaper(110);
                        printer.flush();
                    }
                },
                R.string.title_activity_list_db);
    }

    private void printImage(final String imagem) {

        runTask(new Reimpressao.PrinterRunnable() {
            @Override
            public void run(ProgressDialog dialog, Printer printer) throws IOException {

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = true;
                options.inSampleSize = 0;
                options.outWidth = 100;
                options.outHeight = 200;
                options.inMutable = true;
                options.inDensity = 100;

                Uri otherPath = Uri.parse("android.resource://com.pec.biosistemico.pec/drawable/"+imagem);
                InputStream stream = getContentResolver().openInputStream(otherPath);

                // Uri uri = Uri.fromFile(new File(Environment.getDataDirectory(), "laudo1"));
                //  InputStream inputStream = getContentResolver().openInputStream(uri);

                final Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
                final int width = bitmap.getWidth();
                final int height = bitmap.getHeight();
                final int[] argb = new int[width * height];
                bitmap.getPixels(argb, 0, width, 0,0, width, height);
                bitmap.recycle();

                printer.setIntensity(5);

                printer.reset();
                printer.printCompressedImage(argb, width, height,printer.ALIGN_LEFT, false);
                printer.selectStandardMode();
                printer.feedPaper(10);
                printer.flush();

            }
        }, R.string.msg_printing_image);
    }

    private String RetornaPEC(int id){

        Global x = Global.getInstance();
        StringBuilder sql = new StringBuilder();

        db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        //Date data = new Date(System.currentTimeMillis());
        Cursor cursor = db.rawQuery("SELECT * FROM CHECKLIST WHERE _id = " + id + "", null);
        // cursor = db.rawQuery("SELECT * FROM agro_copel WHERE _id = 12",null);

        if(cursor.moveToFirst()) {

            int grupo = Integer.parseInt(cursor.getString(cursor.getColumnIndex("_grupo")));

            int A =  String.valueOf(RetornaProjeto(cursor.getInt(cursor.getColumnIndex("_projeto")))).toString().length();
            int C =  24 - A ;

            String espaco = "";

            for(int i = 0; i < C; i++){

                espaco += " ";
            }

            int D =  String.valueOf(RetornaProdutor(cursor.getInt(cursor.getColumnIndex("_produtor")))).toString().length();
            int F =  24 - D;

            String espaco_02 = "";

            for(int i = 0; i < F; i++){

                espaco_02 += " ";
            }

            String situacao = convertUTF8(cursor.getString(cursor.getColumnIndex("situacaoEncontrada")));


            sql.append("{b}Nº DE CONTROLE {reset}         ");
            sql.append("{b}DATA DO ATENDIMENTO{reset} \n");
            sql.append(cursor.getString(cursor.getColumnIndex("_id")) + " \t\t\t");
            sql.append(cursor.getString(cursor.getColumnIndex("data")) +"\n");

            sql.append("{b}PROJETO {reset}" + "\t\t");
            sql.append("{b}GRUPO {reset}" +"\n");
            sql.append(RetornaProjeto(cursor.getInt(cursor.getColumnIndex("_projeto"))) + espaco +"");
            sql.append(RetornaGrupo(cursor.getInt(cursor.getColumnIndex("_grupo"))) + "\n");

            sql.append("{b}PRODUTOR \t\t");
            sql.append("{b}TIPO DO ATENDIMENTO {reset}\n");
            sql.append(RetornaProdutor(cursor.getInt(cursor.getColumnIndex("_produtor"))) + "{reset}" + espaco_02);
            sql.append(cursor.getString(cursor.getColumnIndex("tipoReprodutivo")) + "\n");
            sql.append("{b}CONSULTOR {reset}\n");
            sql.append(cursor.getString(cursor.getColumnIndex("consultor")) + "\n\n");

            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b}SITUAÇÃO ENCONTRADA {reset}\n\n");
            sql.append("{i}" + situacao + " {reset}\n\n");
            sql.append("-------------------------------------------------------------------\n");

            sql.append("{b} RECOMENDAÇÕES {reset}\n\n");

            sql.append("{i}" + cursor.getString(cursor.getColumnIndex("recomendacoes")) + "\n\n");

            sql.append("-------------------------------------------------------------------\n\n\n\n\n");

            sql.append("_________________________________________________\n");
            sql.append("{b}Assinatura Consultor{reset}\n\n\n\n\n");

            sql.append("_________________________________________________\n");
            sql.append("{b}Assinatura Produtor{reset}\n\n\n\n\n\n\n\n");

        }

        return  sql.toString();
    }

    public String RetornaGrupo(int _id) {

        String result = "";
        db = openOrCreateDatabase("IbsPEC.db",
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
            db = openOrCreateDatabase("IbsPEC.db",
                    Context.MODE_PRIVATE, null);

            Cursor cursor = db.rawQuery("SELECT NOME FROM PRODUTOR WHERE ID = " + _id + "", null);
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("nome"));
        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }


        return result;
    }

    public String RetornaProjeto(int _id) {

        String result = "";

        try {

            SQLiteDatabase db = openOrCreateDatabase("IbsPEC.db",
                    Context.MODE_PRIVATE, null);

            Cursor cursor = db.rawQuery("SELECT NOME FROM PROJETO WHERE ID = "
                    + _id + "", null);
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex("nome"));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


        return result;
    }

    public static String convertUTF8(String str) {
        String ret = null;
        try {
            ret = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return ret;
    }
    private class PrintTask extends AsyncTask<URL, Integer, Long> {

        protected Long doInBackground(URL... urls) {


            return null;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {
        }

    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        lblNumero.setText("Horas de atendimento: " + progress);
        Global mDados = Global.getInstance();
        mDados.setQtdadeAT(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        seekBar.setProgress(mDados.getQtdadeAT());

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private interface PrinterRunnable {
        public void run(ProgressDialog dialog, Printer printer) throws IOException;
    }



}




