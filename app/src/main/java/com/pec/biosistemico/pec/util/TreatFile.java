package com.pec.biosistemico.pec.util;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class TreatFile {

    public static final String PREFIX = "stream2file";
    public static final String SUFFIX = ".tmp";

    public static final String getContent(InputStream in) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, "ISO-8859-1"));
            String contentLine = null;
            StringBuilder content = new StringBuilder();
            while ((contentLine = reader.readLine()) != null) {
                content.append(contentLine);
            }
            return content.toString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static final String criarDiretorio(String outputFolder) {
        File folder = new File(outputFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }


    public static final void apagarArquivos(String path) {
        File folder = new File(path);
        if (folder.isDirectory()) {
            File[] sun = folder.listFiles();
            for (File toDelete : sun) {
                toDelete.delete();
            }
        }
    }

    public static void salvarImagemPastaArmazenamento(String path, Bitmap bitmap, String imageName) {

        File myDir = new File(path);
        myDir.mkdirs();
        File file = new File(myDir, imageName);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean writeOutputStreamFromFile(OutputStream os, File file) {
        boolean bOK = false;
        InputStream is = null;
        if (file != null && os != null) try {
            byte[] buf = new byte[4096];
            is = new FileInputStream(file);
            int c;
            while ((c = is.read(buf, 0, buf.length)) > 0)
                os.write(buf, 0, c);
            bOK = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.flush();
                os.close();
                if (is != null) is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bOK;
    }

    public static File createFileFromInputStream(InputStream inputStream, String pathWithFileName) {

        try {
            File f = new File(pathWithFileName);
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        } catch (IOException e) {
            Log.e("erro create file", "Erro ao criar arquivo: " + e.getCause());
            e.printStackTrace();
            //Logging exception
        }

        return null;
    }

}
