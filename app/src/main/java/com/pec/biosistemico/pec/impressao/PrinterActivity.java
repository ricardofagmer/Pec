package com.pec.biosistemico.pec.impressao;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.datecs.api.emsr.EMSR;
import com.datecs.api.emsr.EMSR.EMSRInformation;
import com.datecs.api.emsr.EMSR.EMSRKeyInformation;
import com.datecs.api.printer.Printer;
import com.datecs.api.printer.Printer.ConnectionListener;
import com.datecs.api.printer.PrinterInformation;
import com.datecs.api.printer.ProtocolAdapter;
import com.datecs.api.rfid.RC663;
import com.datecs.api.universalreader.UniversalReader;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pec.biosistemico.pec.R;
import com.pec.biosistemico.pec.main;
import com.pec.biosistemico.pec.util.Global;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PrinterActivity extends Activity {

    private static final String LOG_TAG = "PrinterSample";

    // Request to get the bluetooth device
    private static final int REQUEST_GET_DEVICE = 0;

    // Request to get the bluetooth device
    private static final int DEFAULT_NETWORK_PORT = 9100;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Printer Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://ibs.com.br.biodiesel.fomularios.fechamento/http/host/path")
        );
        //  AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Printer Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://ibs.com.br.biodiesel.fomularios.fechamento/http/host/path")
        );
//        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    // Interface, used to invoke asynchronous printer operation.
    private interface PrinterRunnable {
        public void run(ProgressDialog dialog, Printer printer) throws IOException;
    }

    // Member variables
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
    private TextView lblNumero;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);

       // mDados.setFormulario(1);

        waitForConnection();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

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

        lbl.setText("*******ATENÇÃO "+print_name.toUpperCase()+" IMPRIMIR 2 VIAS*******");

       /* android.app.ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ae4d")));
*/
        findViewById(R.id.imageButton3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {

                    printImage("pec");
                    printPEC();

            }
        });
    }

    private String getDateTime() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        return dateFormat.format(date);

    }

    public String RetornaCidade(int cidade) {

        String result = "";
        Cursor cursor = db.rawQuery("SELECT NOME FROM CIDADE WHERE _ID = " + cidade + "", null);
        cursor.moveToFirst();
        result = cursor.getString(cursor.getColumnIndex("nome"));

        return result;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       // if(item.getItemId() == R.id.mnuExit)
    //   {
            Intent obtnptodos = new Intent(this,main.class);
            startActivity(obtnptodos);
            finish();
       // }

   /*     Random gerador = new Random();
        int nome = gerador.nextInt();


        View v1 = getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);

        Bitmap bm = BitmapFactory.decodeResource(this.getResources(), R.drawable.laudo1);


        try
        {
            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory().toString(), nome+".jpg"));
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            printImage(nome+".jpeg");
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*
        if(mDados.getFormulario() == 1)
        {
            printImage("laudo1");
            printForm01();
        }
        if(mDados.getFormulario() == 2)
        {
            printImage("laudo2");
            printForm02();
        }
        if(mDados.getFormulario() == 3)
        {
            printImage("laudo3");
            RetornaForm03();
        }
        if(mDados.getFormulario() == 2)
        {
            printImage("laudo4");
            RetornaForm04();
        }*/

       // printPage();
        return false;
    }

    public int RetornaID() {

        int x = 0;

        try{

            Cursor cursor = db.rawQuery("SELECT _id FROM agro_copel  order by _id desc limit 1", null);
            cursor.moveToFirst();

            x = cursor.getInt(cursor.getColumnIndex("_id"));

        }

        catch(Exception ex){

            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG);
        }

        return x;

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
                AlertDialog.Builder builder = new AlertDialog.Builder(PrinterActivity.this);
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
        final ProgressDialog dialog = new ProgressDialog(PrinterActivity.this);
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
                EMSRKeyInformation keyInfo = mEMSR.getKeyInformation(EMSR.KEY_AES_DATA_ENCRYPTION);

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

        mPrinter.setConnectionListener(new ConnectionListener() {
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
        final ProgressDialog dialog = new ProgressDialog(PrinterActivity.this);
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

        final ProgressDialog dialog = new ProgressDialog(PrinterActivity.this);
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

    private void readInformation() {
        Log.d(LOG_TAG, "Read information");

        runTask(new PrinterRunnable() {
            @Override
            public void run(ProgressDialog dialog, Printer printer) throws IOException {
                StringBuffer textBuffer = new StringBuffer();
                PrinterInformation pi = printer.getInformation();

                textBuffer.append("PRINTER:");
                textBuffer.append("\n");
                textBuffer.append("Name: " + pi.getName());
                textBuffer.append("\n");
                textBuffer.append("Version: " + pi.getFirmwareVersionString());
                textBuffer.append("\n");
                textBuffer.append("\n");

                if (mEMSR != null) {
                    EMSRInformation devInfo = mEMSR.getInformation();
                    EMSRKeyInformation kekInfo = mEMSR.getKeyInformation(EMSR.KEY_AES_KEK);
                    EMSRKeyInformation aesInfo = mEMSR.getKeyInformation(EMSR.KEY_AES_DATA_ENCRYPTION);
                    EMSRKeyInformation desInfo = mEMSR.getKeyInformation(EMSR.KEY_DUKPT_MASTER);

                    textBuffer.append("ENCRYPTED MAGNETIC HEAD:");
                    textBuffer.append("\n");
                    textBuffer.append("Name: " + devInfo.name);
                    textBuffer.append("\n");
                    textBuffer.append("Serial: " + devInfo.serial);
                    textBuffer.append("\n");
                    textBuffer.append("Version: " + devInfo.version);
                    textBuffer.append("\n");
                    textBuffer.append("KEK Version: "
                            + (kekInfo.tampered ? "Tampered" : kekInfo.version));
                    textBuffer.append("\n");
                    textBuffer.append("AES Version: "
                            + (aesInfo.tampered ? "Tampered" : aesInfo.version));
                    textBuffer.append("\n");
                    textBuffer.append("DUKPT Version: "
                            + (desInfo.tampered ? "Tampered" : desInfo.version));
                }

                dialog(R.drawable.ic_info, getString(R.string.title_activity_export_db), textBuffer.toString());
            }
        }, R.string.abc_action_bar_home_description);
    }

    private void printOlerico() {
        Log.d(LOG_TAG, "Print Text");

        runTask(new PrinterRunnable() {
                    @Override
                    public void run(ProgressDialog dialog, Printer printer) throws IOException {

                        printer.reset();
                        printer.printTaggedText(RetornaOlerico(),"ISO-8859-1");
                        printer.setIntensity(5);
                        printer.feedPaper(110);
                        printer.flush();
                    }
                },
                R.string.title_activity_list_db);
    }

    private void printForm01() {
        Log.d(LOG_TAG, "Print Text");

        runTask(new PrinterRunnable() {
                    @Override
                    public void run(ProgressDialog dialog, Printer printer) throws IOException {

                        printer.reset();
                        printer.printTaggedText(RetornaForm01(),"ISO-8859-1");
                        printer.setIntensity(5);
                        printer.feedPaper(110);
                        printer.flush();
                    }
                },
                R.string.title_activity_list_db);
    }

    private void printForm02() {
        Log.d(LOG_TAG, "Print Text");

        runTask(new PrinterRunnable() {
                    @Override
                    public void run(ProgressDialog dialog, Printer printer) throws IOException {

                        printer.reset();
                        printer.printTaggedText(RetornaForm02(),"ISO-8859-1");
                        printer.setIntensity(5);
                        printer.feedPaper(110);
                        printer.flush();
                    }
                },
                R.string.title_activity_list_db);
    }

    private void printForm03() {
        Log.d(LOG_TAG, "Print Text");

        runTask(new PrinterRunnable() {
                    @Override
                    public void run(ProgressDialog dialog, Printer printer) throws IOException {

                        printer.reset();
                        printer.printTaggedText(RetornaForm03(),"ISO-8859-1");
                        printer.setIntensity(5);
                        printer.feedPaper(110);
                        printer.flush();
                    }
                },
                R.string.title_activity_list_db);
    }

    private void printForm04() {
        Log.d(LOG_TAG, "Print Text");

        runTask(new PrinterRunnable() {
                    @Override
                    public void run(ProgressDialog dialog, Printer printer) throws IOException {

                        printer.reset();
                        printer.printTaggedText(RetornaForm04(),"ISO-8859-1");
                        printer.setIntensity(5);
                        printer.feedPaper(110);
                        printer.flush();
                    }
                },
                R.string.title_activity_list_db);
    }

    private void printPEC() {
        Log.d(LOG_TAG, "Print Text");

        runTask(new PrinterRunnable() {
                    @Override
                    public void run(ProgressDialog dialog, Printer printer) throws IOException {

                        printer.reset();
                        printer.printTaggedText(RetornaPEC(),"ISO-8859-1");
                        printer.setIntensity(5);
                        printer.feedPaper(110);
                        printer.flush();
                    }
                },
                R.string.title_activity_list_db);
    }

    private void printImage(final String imagem) {

        runTask(new PrinterRunnable() {
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

    private String RetornaPEC(){

        Global x = Global.getInstance();
        StringBuilder sql = new StringBuilder();

         db = openOrCreateDatabase("IbsPEC.db", Context.MODE_PRIVATE, null);

        //Date data = new Date(System.currentTimeMillis());
        Cursor cursor = db.rawQuery("SELECT * FROM CHECKLIST WHERE _id = " + x.getLastID() + "", null);
        // cursor = db.rawQuery("SELECT * FROM agro_copel WHERE _id = 12",null);

        if(cursor.moveToFirst()) {

            int grupo = Integer.parseInt(mDados.getUsuario());

            int A =  String.valueOf(RetornaProjeto(mDados.getProjeto())).toString().length();
            int C =  24 - A ;

            String espaco = "";

            for(int i = 0; i < C; i++){

                espaco += " ";
            }

            int D =  String.valueOf(RetornaProdutor(mDados.getProdutor())).toString().length();
            int F =  24 - D;

            String espaco_02 = "";

            for(int i = 0; i < F; i++){

                espaco_02 += " ";
            }

            sql.append("{b}Nº DE CONTROLE {reset}         ");
            sql.append("{b}DATA DO ATENDIMENTO{reset} \n");
            sql.append(cursor.getString(cursor.getColumnIndex("_id")) + " \t\t\t");
            String data = mDados.getDataRelatorio();

            if(data.equals("") || data.equals(null)) {
                data = cursor.getString(cursor.getColumnIndex("data"));
            }

            sql.append(data +"\n");
            sql.append("{b}PROJETO {reset}" + "\t\t");
            sql.append("{b}GRUPO {reset}" +"\n");
            sql.append(RetornaProjeto(mDados.getProjeto()) + espaco +"");
            sql.append(RetornaGrupo(grupo) + "\n");

            sql.append("{b}PRODUTOR \t\t");
            sql.append("{b}TIPO DO ATENDIMENTO {reset}\n");
            sql.append(RetornaProdutor(mDados.getProdutor()) + "{reset}" + espaco_02);
            sql.append(cursor.getString(cursor.getColumnIndex("tipoReprodutivo")) + "\n");
            sql.append("{b}CONSULTOR {reset}\n");
            sql.append(mDados.getLogin() + "\n\n");

            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b}SITUAÇÃO ENCONTRADA {reset}\n\n");

            Cursor cursor1 = db.rawQuery("select count(*) as total from paperPort where modificado = 'SIM' and _propriedade = "+mDados.getProdutor()+" and data_coleta = '"+data+"' and dataDiagnostico <> ''",null);
            cursor1.moveToFirst();
            int totalAT = cursor.getInt(cursor1.getColumnIndex("total"));
            cursor1 = db.rawQuery("select  nome_usual from paperPort where modificado = 'SIM' and _propriedade = " + mDados.getProdutor() +" and data_coleta = '"+data+"' and dataDiagnostico <> '' limit 100",null);
            String animaisDG = "";

            cursor1.moveToFirst();
            do {
                animaisDG += cursor1.getString(cursor1.getColumnIndex("nome_usual")) + ", ";
            }while (cursor1.moveToNext());

            sql.append("{b}Total de animais diagnosticados:{reset} "+totalAT+"\n");
            sql.append("{b}Lista de animais:{reset} "+ animaisDG+"...\n\n");


            sql.append("{i}" + cursor.getString(cursor.getColumnIndex("situacaoEncontrada")) + " {reset}\n\n");
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

    private String RetornaOlerico(){

        Global x = Global.getInstance();
        StringBuilder sql = new StringBuilder();

        //Date data = new Date(System.currentTimeMillis());
        Cursor cursor = db.rawQuery("SELECT * FROM agro_copel WHERE _id = "+x.getLastID()+"",null);

       // cursor = db.rawQuery("SELECT * FROM agro_copel WHERE _id = 12",null);

        if(cursor.moveToFirst()) {

            sql.append("{b}Nº DE CONTROLE: {reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("_id")) + "\n");
            sql.append("{b}DATA DO ATENDIMENTO{reset}: ");
            sql.append(cursor.getString(cursor.getColumnIndex("data_atendimento")) + " : " + cursor.getString(cursor.getColumnIndex("hora_atendimento")) +"\n");

            sql.append("{b}GRUPO: {reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("grupo")) + "\t");
            sql.append("{b}{u}PRODUTOR: ");
            sql.append(cursor.getString(cursor.getColumnIndex("produtor")) + "{reset}\n");
            sql.append("{b}CIDADE: {reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("cidade")) + "\t");
            sql.append("{b}CONSULTOR: {reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("consultor")) + "\n\n");


            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b}SITUAÇÃO ENCONTRADA {reset}\n\n");
            sql.append("{i}" + cursor.getString(cursor.getColumnIndex("situacao_encontrada")) + " {reset}\n\n");
            sql.append("-------------------------------------------------------------------\n");

            sql.append("{b} DESCRIÇÃO DAS SOLUÇÕES PROPOSTAS {reset}\n\n");

            sql.append("{i}" + cursor.getString(cursor.getColumnIndex("descricao")) + "\n\n");
            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b}Consultor: {reset}" + cursor.getString(cursor.getColumnIndex("consultor")) + "\n");

            sql.append("-------------------------------------------------------------------\n\n\n\n\n");

            sql.append("_________________________________________________\n");
            sql.append("{b}Assinatura Consultor{reset}\n\n\n\n\n");

            sql.append("_________________________________________________\n");
            sql.append("{b}Assinatura Produtor{reset}\n\n\n\n\n\n\n\n");

        }

        return  sql.toString();
    }

    public String RetornaForm01(){

        Global x = Global.getInstance();
        StringBuilder sql = new StringBuilder();

        //Date data = new Date(System.currentTimeMillis());
        Cursor cursor = db.rawQuery("SELECT * FROM Soja1 WHERE _id = "+x.getLastID()+"",null);

      // cursor = db.rawQuery("SELECT * FROM Soja1 WHERE _id = 2",null);

        if(cursor.moveToFirst()) {

            String aduba_q = cursor.getString(cursor.getColumnIndex("adubacao_qtdade"));

            sql.append("{b}Data do Atendimento{reset}        {b}Nº de Controle {reset} \n");
            sql.append(cursor.getString(cursor.getColumnIndex("Data")) + "                  ");
            sql.append(cursor.getString(cursor.getColumnIndex("_id")) + "\n");
            sql.append("-------------------------------------------------------------------\n");

            sql.append("{b}IDENTFICAÇÃO DO PRODUTOR {reset}\n\n");

            sql.append("{b}{u}NOME:"+ (cursor.getString(cursor.getColumnIndex("Nome")).toString().toUpperCase() + "{reset}   "));
            sql.append("{b}Fone: {reset}" + cursor.getString(cursor.getColumnIndex("fone")).toString().toUpperCase() + "   \n");
            sql.append("{b}DAP: "+ cursor.getString(cursor.getColumnIndex("DAP"))+"{reset}\n");
            sql.append("{b}End.: {reset}" + cursor.getString(cursor.getColumnIndex("rua"))+ "  {b}Bairro:{reset}" + cursor.getString(cursor.getColumnIndex("bairro")) +"\n");
            sql.append("{b}CEP.: {reset}" + cursor.getString(cursor.getColumnIndex("cep"))+ "    {b}Cidade: {reset}" + cursor.getString(cursor.getColumnIndex("cidade")) +"  ");
            sql.append("{b}UF: {reset}"+  cursor.getString(cursor.getColumnIndex("uf")) + "\n");
            sql.append("{b}Área DAP: {reset}"+cursor.getString(cursor.getColumnIndex("AreaDAP"))+" ");
            sql.append("{b}Área plantada: {reset}"+cursor.getString(cursor.getColumnIndex("AreaPlantada"))+" há \n");


            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b}IDENTFICAÇÃO DA LAVOURA {reset}\n\n");
            sql.append("{b}Propriedade: {reset}"+ cursor.getString(cursor.getColumnIndex("NomeProprietario"))+"\n");
            sql.append("{b}Estrada: {reset}" + cursor.getString(cursor.getColumnIndex("lavoura_estrada")) + "  ");
            sql.append("{b}Km: {reset}" + cursor.getString(cursor.getColumnIndex("km")) + "\n");
            sql.append("{b}Bairro: {reset} " + cursor.getString(cursor.getColumnIndex("lavoura_bairro"))+"\n");
            sql.append("{b}Cidade: {reset}" +  cursor.getString(cursor.getColumnIndex("lavoura_municipio"))+"\n");
            sql.append("{b}Latitude: {reset}" +cursor.getString(cursor.getColumnIndex("lavoura_latidude"))+ "  ");
            sql.append("{b}Longitude: {reset}" +cursor.getString(cursor.getColumnIndex("lavoura_longitude"))+ "\n");
            sql.append("{b}Área contratada: {reset}"+cursor.getString(cursor.getColumnIndex("AreaSobContrato"))+"ha  ");
            sql.append("{b}Área contínua: {reset}"+cursor.getString(cursor.getColumnIndex("AreaTotalContinua"))+" \n");
            sql.append("{b}Produção principal: {reset}" +cursor.getString(cursor.getColumnIndex("lavoura_ProducaoPrincipal"))+ "\n");
            sql.append("{b}Produção secundária:{reset}"+cursor.getString(cursor.getColumnIndex("lavoura_ProducaoSecundaria"))+ "\n");

            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b}SEMEADURA DA SOJA {reset}\n\n");

            sql.append("{b}Cultivar: {reset}" + cursor.getString(cursor.getColumnIndex("cultivares_rec"))+ "\n");
            sql.append("{b}Ciclo cultura (dias): {reset}" + cursor.getString(cursor.getColumnIndex("ciclo_cultura"))+ "\n");
            sql.append("{b}Densidade (plantas/ha): {reset}" + cursor.getString(cursor.getColumnIndex("densidade"))+ "\n");
            sql.append("{b}Estimativa produção (Kg/há): {reset}" + cursor.getString(cursor.getColumnIndex("estimativa"))+ ""+ "\n");
            sql.append("{b}Data prevista semeadura: {reset}" + cursor.getString(cursor.getColumnIndex("dt_prevista_semea"))+ "\n");
            sql.append("{b}Tipo de manejo: {reset}" + cursor.getString(cursor.getColumnIndex("tipo_manejo"))+ "\n");
            sql.append("{b}Adubação: {reset}" + cursor.getString(cursor.getColumnIndex("adubacao_form")) + Html.fromHtml("{b}  Qtdade.:{reset} </b>"+aduba_q+" Kg/há \n "));
            sql.append("{b}\nMicro: {reset}" + cursor.getString(cursor.getColumnIndex("micro"))+ "\n");
            sql.append("{b}Variedade do ano anterior: {reset}" + cursor.getString(cursor.getColumnIndex("variedade_ano_ant"))+ "\n");
            sql.append("{b}Produção média ano anterior: {reset}" + cursor.getString(cursor.getColumnIndex("prod_med_ano_ant"))+" Kg/há\n");


            if(cursor.getString(cursor.getColumnIndex("cultivar_recomendada_b")).toString().equals("vazio") ||
               cursor.getString(cursor.getColumnIndex("cultivar_recomendada_b")).toString().equals(""))
            {
                String xx = null;
            }
            else {
                sql.append("-------------------------------------------------------------------\n");
                sql.append("{b}CULTIVAR B {reset}\n\n");

                sql.append("{b}Cultivar: {reset}" + cursor.getString(cursor.getColumnIndex("cultivar_recomendada_b")) + "\n");
                sql.append("{b}Ciclo cultura (dias): {reset}" + cursor.getString(cursor.getColumnIndex("ciclo_cultura_b")) + "\n");
                sql.append("{b}Densidade (plantas/ha): {reset}" + cursor.getString(cursor.getColumnIndex("densidade_b")) + "\n");
                sql.append("{b}Estimativa produção (Kg/há): {reset}" + cursor.getString(cursor.getColumnIndex("estimativa_b")) + " Kg/há " + "\n");
                sql.append("{b}Data prevista semeadura: {reset}" + cursor.getString(cursor.getColumnIndex("dt_prevista_semea_b")) + "\n");
            }
                sql.append("-------------------------------------------------------------------\n");
                sql.append("{b}PLANEJAMENTO {reset}\n\n");

                sql.append("{b}Tratamento de sementes: {reset}" + cursor.getString(cursor.getColumnIndex("plan_tratamentoSemente_1")));
                sql.append("{b}         Inseticida: {reset}" + cursor.getString(cursor.getColumnIndex("plan_inseticida_6")) + "\n");
                sql.append("{b}Inoculação de sementes: {reset}" + cursor.getString(cursor.getColumnIndex("plan_inoculacaoSementes_2")));
                sql.append("{b}         Fungicida: {reset}" + cursor.getString(cursor.getColumnIndex("plan_fungicida_8")) + "\n");
                sql.append("{b}Herbicida antes do plantio: {reset}" + cursor.getString(cursor.getColumnIndex("plan_herbicidaAntesPlantio_3")));
                sql.append("{b}     Adjuvante: {reset}" + cursor.getString(cursor.getColumnIndex("plan_oleo_10")) + "\n");
                sql.append("{b}Herbicida após o plantio: {reset}" + cursor.getString(cursor.getColumnIndex("plan_herbicidaAposPlantio")));
                sql.append("{b}       Adubo foliar: {reset}" + cursor.getString(cursor.getColumnIndex("plan_adubo_12")) + "\n");
                sql.append("{b}Foi coletado amostra de solo: {reset}" + cursor.getString(cursor.getColumnIndex("amostra_solo")) + "\n");


            sql.append("-------------------------------------------------------------------\n");

            sql.append("{b} OBSERVAÇÕES / RECOMENDAÇÕES {reset}\n\n");

            sql.append("{i}" + cursor.getString(cursor.getColumnIndex("Recom_obs")) + "\n\n");
            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b}Consultor: {reset}" + cursor.getString(cursor.getColumnIndex("consultor")) + "   ");
            sql.append("{b}CREA: {reset}" + cursor.getString(cursor.getColumnIndex("Crea")) + "\n\n");

            sql.append("-------------------------------------------------------------------\n\n\n\n\n");

            sql.append("_________________________________________________\n");
            sql.append("{b}Assinatura Consultor{reset}\n\n\n\n\n");

            sql.append("_________________________________________________\n");
            sql.append("{b}Assinatura Produtor{reset}\n\n\n\n\n\n\n\n");

        }

        return  sql.toString();
    }

    public String RetornaForm02(){

        Global x = Global.getInstance();
        StringBuilder sql = new StringBuilder();
        Cursor cursor = db.rawQuery("SELECT * FROM FrmSojaII WHERE _id = "+x.getLastID()+"",null);
        //cursor = db.rawQuery("SELECT * FROM FrmSojaII WHERE _id = 4",null);


        if(cursor.moveToFirst()) {

            sql.append("{b}Data do atendimento{reset}        {b}Nº de controle {reset} \n");
            sql.append(cursor.getString(cursor.getColumnIndex("Data")) + "                  ");
            sql.append(cursor.getString(cursor.getColumnIndex("_id")) + "\n");

            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b}IDENTFICAÇÃO DO PRODUTOR {reset}\n\n");

            sql.append("{b}{u}Nome:"+ cursor.getString(cursor.getColumnIndex("Nome")).toString().toUpperCase()+"\n");
            sql.append("{reset}{b}DAP: "+cursor.getString(cursor.getColumnIndex("DAP")) + "\t\t");
            sql.append("{b}Área contratada:{reset}"+ cursor.getString(cursor.getColumnIndex("area_contratada"))+" há\n");

            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b}CONTROLE DA LAVOURA {reset}\n\n");

            sql.append("{b}Adubação aplicada conforme recomendação?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P1")) + "\n");
            sql.append("{b}Qual / Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q1")) + " Kg/há\n");
            sql.append("{b}Adubação de cobertura conforme recomendação?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P2")) + "\n");
            sql.append("{b}Qual / Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q2")) + " Kg/há\n");
            sql.append("{b}Adubação foliar - conforme recomendação?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P3")) + "\n");
            sql.append("{b}Qual / Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q3")) + " Kg/há\n");
            sql.append("{b}Herbicida aplicado conforme orientação?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P5")) + "\n");
            sql.append("{b}Qual:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q5")) + "\t");
            sql.append("{b}Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("herbicida_qtdade")) + " Kg/há\n");
            sql.append("{b}Fungicida aplicado conforme orientação?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P6")) + "\n");
            sql.append("{b}Qual:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q6")) + "\t");
            sql.append("{b}Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("fungicida_qtdade")) + " Kg/há\n");
            sql.append("{b}Lavoura apresenta plantas daninhas ou pragas?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P4")) + "\n");
            sql.append("{b}Qual / Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q4")) + "\n");
            sql.append("{b}Inseticida aplicado conforme orientação?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P7")) + "\n");
            sql.append("{b}Qual:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q7")) + "\t");
            sql.append("{b}Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("inserticida_qtdade")) + " Kg/há\n");
            sql.append("{b}Índices pluviometricos bem distribuídos?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P8")) + "\n");
            //sql.append("{b}Qual / Quantidade:{reset}");
            //sql.append(cursor.getString(cursor.getColumnIndex("Q8")) + " Kg/há\n");
            sql.append("{b}Fertilidade do solo equilibrada?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P9")) + "\n");
            //sql.append("{b}Qual / Quantidade:{reset}");
            //sql.append(cursor.getString(cursor.getColumnIndex("Q9")) + " Kg/há\n");
            sql.append("{b}Produção esperada até a fase da cultura?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P10")) + "\n");
            sql.append("{b}Se não,Por quê?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q10")) + "\n");
            sql.append("{b}Previsão de rendimento:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("previsao_rendimento")) + " Sc/ha\n");
            sql.append("{b}Recomendação seguidas até a presente data?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P11")) + "\n");
            sql.append("{b}Se não,Por quê?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q11")) + "\n");
            sql.append("{b}Alguma ocorrência fora do normal:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P12")) + "\n");
            sql.append("{b}Qual:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q12"))+"\n\n");

            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b}ESTADO DA LAVOURA{reset}\n\n");

            sql.append("{b}Estadio fenológico da cultura:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P13"))+"\n");
            sql.append("{b}Situação da lavoura:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P14"))+"\n");
            sql.append("{b}Umidade do solo na data:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P15"))+"\n");
            sql.append("{b}Espaçamento entre linhas (CM):{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("EspacamentoLinhas"))+"\n");
            sql.append("{b}Plantas por metro linear:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("PlantasMetro"))+"\n");

            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b} OBSERVAÇÕES / RECOMENDAÇÕES {reset}\n\n");

            sql.append("{i}" + cursor.getString(cursor.getColumnIndex("Recomedacao")) + "{reset}\n\n");
            sql.append("-------------------------------------------------------------------\n\n");
            sql.append("{b}Consultor: {reset}" + cursor.getString(cursor.getColumnIndex("Consultor")) + "   ");
            sql.append("{b}CREA: {reset}" + cursor.getString(cursor.getColumnIndex("CREA")) + "\n\n");

            sql.append("-------------------------------------------------------------------\n\n\n\n\n");

            sql.append("_________________________________________________\n");
            sql.append("{b}Assinatura Consultor{reset}\n\n\n\n\n\n");

            sql.append("_________________________________________________\n");
            sql.append("{b}Assinatura Produtor{reset}\n\n\n\n\n\n\n\n");

        }

        return  sql.toString();
    }

    public String RetornaForm03(){

        Global x = Global.getInstance();
        StringBuilder sql = new StringBuilder();

        //Date data = new Date(System.currentTimeMillis());
        Cursor cursor = db.rawQuery("SELECT * FROM FrmSojaIII WHERE _id = "+x.getLastID()+"",null);
       // cursor = db.rawQuery("SELECT * FROM FrmSojaIII WHERE _id = 6",null);
        if(cursor.moveToFirst()) {

            sql.append("{b}Data do atendimento{reset}        {b}Nº de controle {reset} \n");
            sql.append(cursor.getString(cursor.getColumnIndex("Data")) + "                  ");
            sql.append(cursor.getString(cursor.getColumnIndex("_id")) + "\n");

            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b}IDENTFICAÇÃO DO PRODUTOR {reset}\n\n");

            sql.append("{b}{u}Nome:"+ cursor.getString(cursor.getColumnIndex("Nome")).toString().toUpperCase()+"\n");
            sql.append("{reset}{b}DAP: "+cursor.getString(cursor.getColumnIndex("DAP")) + "\t\t");
            sql.append("{b}Área contratada:{reset}"+ cursor.getString(cursor.getColumnIndex("area_contratada"))+" há\n");

            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b}CONTROLE DA LAVOURA {reset}\n\n");

            sql.append("{b}Adubação foliar - conforme recomendação?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P2")) + "\n");
            sql.append("{b}Qual / Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q2")) + " Kg/há\n");
            sql.append("{b}Herbicida aplicado conforme orientação?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P5")) + "\n");
            sql.append("{b}Qual:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q5")) + "\t");
            sql.append("{b}Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("herbicida_qtdade")) + " Kg/há\n");

            sql.append("{b}Fungicida aplicado conforme orientação na primeira aplicação?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("FUNGICIDA_01")) + "\n");
            sql.append("{b}Qual:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("QF_01")) + "\t");
            sql.append("{b}Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("fungicida_01_qtdade")) + " Kg/há\n");

            sql.append("{b}Fungicida aplicado conforme orientação na segunda aplicação?{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("FUNGICIDA_02")) + "\n");
            sql.append("{b}Qual:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("QF_02")) + "\t");
            sql.append("{b}Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("fungicida_02_qtdade")) + " Kg/há\n");

            sql.append("{b}Fungicida aplicado conforme orientação na terceira aplicação?{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("FUNGICIDA_03")) + "\n");
            sql.append("{b}Qual:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("QF_03")) + "\t");
            sql.append("{b}Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("fungicida_03_qtdade")) + " Kg/há\n");

            sql.append("{b}Inseticida aplicado conforme orientação na primeira aplicação?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("INSETICIDA_01")) + "\n");
            sql.append("{b}Qual:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("QI_01")) + "\t");
            sql.append("{b}Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("inseticida_01_qtdade")) + " Kg/há\n");

            sql.append("{b}Inseticida aplicado conforme orientação na segunda aplicação?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("INSETICIDA_02")) + "\n");
            sql.append("{b}Qual:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("QI_02")) + "\t");
            sql.append("{b}Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("inseticida_02_qtdade")) + " Kg/há\n");

            sql.append("{b}Inseticida aplicado conforme orientação na terceira aplicação?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("INSETICIDA_03")) + "\n");
            sql.append("{b}Qual:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("QI_03")) + "\t");
            sql.append("{b}Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("inseticida_03_qtdade")) + " Kg/há\n");

            sql.append("{b}Lavoura apresenta plantas daninhas ou pragas?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P4")) + "\n");
            sql.append("{b}Qual / Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q4")) + "\n");
            sql.append("{b}Índices pluviometricos bem distribuídos?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P8")) + "\n");
            sql.append("{b}Fertilidade do solo equilibrada?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P9")) + "\n");
            sql.append("{b}Produção esperada até a fase da cultura?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P10")) + "\n");
            sql.append("{b}Se não,Por quê?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q10")) + "\n");
            sql.append("{b}Previsão de rendimento:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("previsao_rendimento")) + " Sc/ha\n");
            sql.append("{b}Recomendação seguidas até a presente data?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P11")) + "\n");
            sql.append("{b}Se não,Por quê?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q11")) + "\n");
            sql.append("{b}Alguma ocorrência fora do normal:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P12")) + "\n");
            sql.append("{b}Qual:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q12"))+"\n\n");

            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b}ESTADO DA LAVOURA{reset}\n\n");

            sql.append("{b}Estadio fenológico da cultura:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P13"))+"\n");
            sql.append("{b}Situação da lavoura:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("P14"))+"\n");

            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b} OBSERVAÇÕES / RECOMENDAÇÕES {reset}\n\n");

            sql.append("{i}" + cursor.getString(cursor.getColumnIndex("Recomedacao")) + "{reset}\n\n");
            sql.append("-------------------------------------------------------------------\n\n");
            sql.append("{b}Consultor: {reset}" + cursor.getString(cursor.getColumnIndex("Consultor")) + "   ");
            sql.append("{b}CREA: {reset}" + cursor.getString(cursor.getColumnIndex("CREA")) + "\n\n");

            sql.append("-------------------------------------------------------------------\n\n\n\n\n");

            sql.append("_________________________________________________\n");
            sql.append("{b}Assinatura Consultor{reset}\n\n\n\n\n\n");

            sql.append("_________________________________________________\n");
            sql.append("{b}Assinatura Produtor{reset}\n\n\n\n\n\n\n\n");

        }


        return  sql.toString();
    }

    public String RetornaForm04(){

        Global x = Global.getInstance();
        StringBuilder sql = new StringBuilder();

        //Date data = new Date(System.currentTimeMillis());
        Cursor cursor = db.rawQuery("SELECT * FROM FrmSojaIV WHERE _id = "+x.getLastID()+"",null);
       // cursor = db.rawQuery("SELECT * FROM FrmSojaIV WHERE _id = 2",null);

        if(cursor.moveToFirst()) {

            sql.append("{b}Data do Atendimento{reset}        {b}Nº de Controle {reset} \n");
            sql.append(cursor.getString(cursor.getColumnIndex("data")) + "                  ");
            sql.append(cursor.getString(cursor.getColumnIndex("_id")) + "\n");

            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b}IDENTFICAÇÃO DO PRODUTOR {reset}\n\n");

            sql.append("{b}{u}Nome:"+ cursor.getString(cursor.getColumnIndex("nome")).toString().toUpperCase()+"\n");
            sql.append("{reset}{b}DAP: "+cursor.getString(cursor.getColumnIndex("dap")) + "\t\t");
            sql.append("{b}Área contratada:{reset}"+ cursor.getString(cursor.getColumnIndex("areacontratada"))+" há\n");

            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b}CONTROLE DA LAVOURA {reset}\n\n");

            sql.append("{b}Lavoura apresenta plantas daninhas para a colheita?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("lavoura_apresenta")) + "\n");
            sql.append("{b}Qual / Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q1")) + " Kg/há\n");
            sql.append("{b}Nível de tecnologia de colheita?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("nivel_colheita")) + "\n");
            sql.append("{b}Nível de tecnologia de aplicação?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("nivel_aplicacao")) + "\n");
            sql.append("{b}Fungicida aplicado conforme orientação?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("fungicida_conforme")) + "\n");
            sql.append("{b}Qual:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q4")) + "\t");
            sql.append("{b}Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("fungicida_qtdade")) + " Kg/há\n");

            sql.append("{b}Inseticida aplicado conforme orientação?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("inseticida_conforme")) + "\n");
            sql.append("{b}Qual:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q5")) + "\t");
            sql.append("{b}Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("inserticida_qtdade")) + " Kg/há\n");

            sql.append("{b}Índices pluviométricos bem distribuídos?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("indices")) + "\n");
           // sql.append("{b}Qual / Quantidade:{reset}");
           // sql.append(cursor.getString(cursor.getColumnIndex("Q6")) + "\n");
            sql.append("{b}Utilizou irrigação?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("utilizou_irrigacao")) + "\n");
            sql.append("{b}Qual / Quantidade:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q7")) + "\n");
            sql.append("{b}Produção esperada até a fase da cultura?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("prod_esperada")) + "\n");
            sql.append("{b}Se não,Por quê?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q8")) + "\n");
            sql.append("{b}Recomendações seguidas até a presente data?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("rec_seguidas")) + "\n");
            sql.append("{b}Se não,Por quê?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q9")) + "\n");
            sql.append("{b}Alguma ocorrência fora do normal?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("alguma_ocorrencia")) + "\n");
            sql.append("{b}Qual:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("Q10")) + "\n");
            sql.append("{b}Estadio fenológico da cultura?:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("estagio_fenologico")) + "\n");
           // sql.append("{b}Qual / Quantidade:{reset}");
           // sql.append(cursor.getString(cursor.getColumnIndex("Q11")) + "\n");

            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b}COLHEITA CULTIVAR A {reset}\n\n");

            sql.append("{b}Cultivar:{reset}"+"");
            sql.append(cursor.getString(cursor.getColumnIndex("cultivar_A"))+"\n");
            sql.append("{b}Espaçamento entre linhas(cm):{reset}"+"");
            sql.append(cursor.getString(cursor.getColumnIndex("espacamento_A"))+"\n");
            sql.append("{b}Plantas por metro:{reset}"+"");
            sql.append(cursor.getString(cursor.getColumnIndex("plantas_metro_A"))+"\n");
            sql.append("{b}Grãos por planta:{reset}"+"");
            sql.append(cursor.getString(cursor.getColumnIndex("grao_metro_A"))+"\n");
            sql.append("{b}Peso de 100 grãos(gramas):{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("peso_100_A"))+"\n");
            sql.append("{b}Umidade de 100 grãos(%):{reset}"+"");
            sql.append(cursor.getString(cursor.getColumnIndex("umidade_100_A"))+"\n");
            sql.append("{b}Produtividade por hectare(sc/ha):{reset}"+"");
            sql.append(cursor.getString(cursor.getColumnIndex("produtividade_HA_A"))+"\n");
            sql.append("{b}Produção total colhida na área contratada(sacas):{reset}"+"");
            sql.append(cursor.getString(cursor.getColumnIndex("producao_total_area_A"))+"\n");

            sql.append("-------------------------------------------------------------------\n");

            if(cursor.getString(cursor.getColumnIndex("cultivar_B")).toString().equals("vazio") ||
                    cursor.getString(cursor.getColumnIndex("cultivar_B")).toString().equals(""))
            {
                String xx = null;
            }
            else {
                sql.append("{b}COLHEITA CULTIVAR B {reset}\n\n");

                sql.append("{b}Cultivar:{reset}" + "");
                sql.append(cursor.getString(cursor.getColumnIndex("cultivar_B")) + "\n");
                sql.append("{b}Espaçamento entre linhas(cm):{reset}" + "");
                sql.append(cursor.getString(cursor.getColumnIndex("espacamento_B")) + "\n");
                sql.append("{b}Plantas por metro:{reset}" + "");
                sql.append(cursor.getString(cursor.getColumnIndex("plantas_metro_B")) + "\n");
                sql.append("{b}Grãos por planta:{reset}" + "");
                sql.append(cursor.getString(cursor.getColumnIndex("grao_metro_B")) + "\n");
                sql.append("{b}Peso de 100 grãos(gramas):{reset}" + "");
                sql.append(cursor.getString(cursor.getColumnIndex("peso_100_B")) + "\n");
                sql.append("{b}\nUmidade de 100 grãos(%):{reset}" + "");
                sql.append(cursor.getString(cursor.getColumnIndex("umidade_100_B")) + "\n");
                sql.append("{b}Produtividade por hectare(sc/há):{reset}" + "");
                sql.append(cursor.getString(cursor.getColumnIndex("produtividade_HA_B")) + "\n");
                sql.append("{b}Produção total colhida na área contratada(sacas):{reset}" + "");
                sql.append(cursor.getString(cursor.getColumnIndex("producao_total_area_B")) + "\n");

                sql.append("-------------------------------------------------------------------\n");
            }
            sql.append("{b}PLANILHA DE CUSTOS DE PRODUÇÃO R$/HÁ{reset}\n\n");

            sql.append("{b}Sementes R$/há:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("sementes"))+"\n");
            sql.append("{b}Fertilizantes R$/há:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("fertilizantes"))+"\n");
            sql.append("{b}Agrotóxicos R$/há:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("agrotoxicos"))+"\n");
            sql.append("{b}Outros R$/há:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("outros"))+"\n");
            sql.append("{b}Total R$/há:{reset}");
            sql.append(cursor.getString(cursor.getColumnIndex("total"))+"\n");

            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b} OBSERVAÇÕES / RECOMENDAÇÕES {reset}\n\n");

            sql.append("{i}" + cursor.getString(cursor.getColumnIndex("recomendacao")) + "\n\n");
            sql.append("-------------------------------------------------------------------\n");
            sql.append("{b}Consultor: {reset}" + cursor.getString(cursor.getColumnIndex("consultor")) + "   ");
            sql.append("{b}CREA: {reset}" + cursor.getString(cursor.getColumnIndex("crea")) + "\n\n");

            sql.append("-------------------------------------------------------------------\n\n\n\n\n");

            sql.append("_________________________________________________\n");
            sql.append("{b}Assinatura Consultor{reset}\n\n\n\n\n");

            sql.append("_________________________________________________\n");
            sql.append("{b}Assinatura Produtor{reset}\n\n\n\n\n\n\n\n");

        }

        return  sql.toString();
    }

    private void printPage() {
        Log.d(LOG_TAG, "Print Page");

        runTask(new PrinterRunnable() {
            @Override
            public void run(ProgressDialog dialog, Printer printer) throws IOException {
                PrinterInformation pi = printer.getInformation();

                if (!pi.isPageSupported()) {
                    dialog(R.drawable.ic_page, "erro", "");
                    return;
                }

                printer.reset();
                printer.selectPageMode();

                printer.setPageRegion(2, 0, 790, 479, Printer.PAGE_LEFT);
                printer.setPageXY(1, 1);
                printer.printTaggedText("{reset}CABEÇALHO"
                        + ", feed to bottom. Starting point in left top corner " +
                        "dasdasasdsadasdsadasdasdsadsadsadsadasdasd" +
                        "asdasdasdasasdasdasdasdasasdasdasdasdasdasdasdasdasdasdsadas" +
                        "dasdasdasdasdasdasdasdasdasdasdasdsaasdasasdasdasdasdasdasdasd" +
                        "asdasdsadsadasdasdasdasdasdasdsadsof the page.{br}");
                printer.drawPageFrame(0, 0, 790, 479, Printer.FILL_BLACK, 1);

                printer.setPageRegion(2, 539, 790, 214, Printer.PAGE_LEFT);
                printer.setPageXY(1, 1);
                printer.printTaggedText("{reset}BLOCO 1 BLOCO1 1BLOCO 1BLOCO 1BLOCO 1BLOCO 1"
                        + ", feed to bottom. Starting point in left top corner " +
                        "dasdasasdsadasdsadasdasdsadsadsadsadasdasd" +
                        "asdasdasdasasdasdasdasdasasdasdasdasdasdasdasdasdasdasdsadas" +
                        "dasdasdasdasdasdasdasdasdasdasdasdsaasdasasdasdasdasdasdasdasd" +
                        "asdasdsadsadasdasdasdasdasdasdsadsof the page.{br}");
                printer.drawPageFrame(0, 0, 790, 214, Printer.FILL_BLACK, 1);

                printer.setPageRegion(2, 813, 790, 265, Printer.PAGE_LEFT);
                printer.setPageXY(1, 1);
                printer.printTaggedText("{reset} BLOCO 2  BLOCO 2  BLOCO 2  BLOCO 2  BLOCO 2  BLOCO 2 "
                        + ", feed to bottom. Starting point in left top corner " +
                        "dasdasasdsadasdsadasdasdsadsadsadsadasdasd" +
                        "asdasdasdasasdasdasdasdasasdasdasdasdasdasdasdasdasdasdsadas" +
                        "dasdasdasdasdasdasdasdasdasdasdasdsaasdasasdasdasdasdasdasdasd" +
                        "asdasdsadsadasdasdasdasdasdasdsadsof the page.{br}");
                printer.drawPageFrame(0, 0, 790, 265, Printer.FILL_BLACK, 1);

                printer.setPageRegion(2, 1138, 790, 325, Printer.PAGE_LEFT);
                printer.setPageXY(1, 1);
                printer.printTaggedText("{reset} BLOCO 3  BLOCO 3  BLOCO 3  BLOCO 3  BLOCO 3 "
                        + ", feed to bottom. Starting point in left top corner " +
                        "dasdasasdsadasdsadasdasdsadsadsadsadasdasd" +
                        "asdasdasdasasdasdasdasdasasdasdasdasdasdasdasdasdasdasdsadas" +
                        "dasdasdasdasdasdasdasdasdasdasdasdsaasdasasdasdasdasdasdasdasd" +
                        "asdasdsadsadasdasdasdasdasdasdsadsof the page.{br}");
                printer.drawPageFrame(0, 0, 790, 325, Printer.FILL_BLACK, 1);

                printer.setPageRegion(2, 1523, 790, 188, Printer.PAGE_LEFT);
                printer.setPageXY(1, 1);
                printer.printTaggedText("{reset} BLOCO 4  BLOCO 4  BLOCO 4  BLOCO 4  BLOCO 4"
                        + ", feed to bottom. Starting point in left top corner " +
                        "dasdasasdsadasdsadasdasdsadsadsadsadasdasd" +
                        "asdasdasdasasdasdasdasdasasdasdasdasdasdasdasdasdasdasdsadas" +
                        "dasdasdasdasdasdasdasdasdasdasdasdsaasdasasdasdasdasdasdasdasd" +
                        "asdasdsadsadasdasdasdasdasdasdsadsof the page.{br}");
                printer.drawPageFrame(0, 0, 790, 188, Printer.FILL_BLACK, 1);

                printer.setPageRegion(2, 1771, 790, 865, Printer.PAGE_LEFT);
                printer.setPageXY(1, 1);
                printer.printTaggedText("{reset} BLOCO 5 BLOCO 5 BLOCO 5 BLOCO 5 BLOCO 5 BLOCO 5 BLOCO 5"
                        + ", feed to bottom. Starting point in left top corner " +
                        "dasdasasdsadasdsadasdasdsadsadsadsadasdasd" +
                        "asdasdasdasasdasdasdasdasasdasdasdasdasdasdasdasdasdasdsadas" +
                        "dasdasdasdasdasdasdasdasdasdasdasdsaasdasasdasdasdasdasdasdasd" +
                        "asdasdsadsadasdasdasdasdasdasdsadsof the page.{br}");
                printer.drawPageFrame(0, 0, 790, 865, Printer.FILL_BLACK, 1);

                printer.setPageRegion(2, 2653, 790, 51, Printer.PAGE_LEFT);
                printer.setPageXY(1, 1);
                printer.printTaggedText("{reset} BLOCO 6 BLOCO 6 {br}");
                printer.drawPageFrame(0, 0, 790, 51, Printer.FILL_BLACK, 1);

                printer.setPageRegion(2, 2756, 790, 86, Printer.PAGE_LEFT);
                printer.setPageXY(1, 1);
                printer.drawPageFrame(0, 0, 790, 86, Printer.FILL_BLACK, 1);

                printer.setPageRegion(2, 2859, 790, 86, Printer.PAGE_LEFT);
                printer.setPageXY(1, 1);
                printer.drawPageFrame(0, 0, 790, 86, Printer.FILL_BLACK, 1);

                printer.printPage();
                printer.selectStandardMode();
                printer.feedPaper(255);
                printer.flush();
            }
        }, R.string.title_activity_export_db);
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
}
