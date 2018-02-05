package com.pec.biosistemico.pec.util;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.util.Date;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteException;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.os.Environment;
        import android.text.format.DateFormat;
        import android.util.Log;

public class CopyPaste extends SQLiteOpenHelper {

    static String DATABASE_PATH = "/data/data/com.pec.biosistemico.pec/databases/";
    static String DATABASE_NAME = "IbsPEC.db";
    int DATABASE_VERSION = 1;
    SQLiteDatabase myDataBase;
    private final Context mContext;


    public CopyPaste(Context context) {

        super(context, DATABASE_NAME, null, 1);

        // TODO Auto-generated constructor stub
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    /**
     * This method will create database in application package /databases
     * directory when first time application launched
     **/
    public void createDataBase() throws IOException {
        boolean mDataBaseExist = checkDataBase();

        if (!mDataBaseExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException mIOException) {
                mIOException.printStackTrace();
                throw new Error("Error copying database");
            } finally {
                this.close();
            }
        }
    }

    /**
     * This method checks whether database is exists or not
     **/
    private boolean checkDataBase() {
        try {
            final String mPath = DATABASE_PATH + DATABASE_NAME;
            final File file = new File(mPath);
            if (file.exists())
                return true;
            else
                return false;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method will copy database from /assets directory to application
     * package /databases directory
     **/
    public void copyDataBase() throws IOException {

        try {

            InputStream mInputStream = mContext.getAssets().open(DATABASE_NAME);

            String outFileName = DATABASE_PATH + DATABASE_NAME;
            OutputStream mOutputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = mInputStream.read(buffer)) > 0) {
                mOutputStream.write(buffer, 0, length);
            }
            mOutputStream.flush();
            mOutputStream.close();
            mInputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method open database for operations     **/

    public boolean openDataBase() throws SQLException {

        String mPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(mPath, null,  SQLiteDatabase.OPEN_READWRITE);

        return myDataBase.isOpen();
    }

    /**
     * This method close database connection and released occupied memory  **/

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        SQLiteDatabase.releaseMemory();

        super.close();
    }
}