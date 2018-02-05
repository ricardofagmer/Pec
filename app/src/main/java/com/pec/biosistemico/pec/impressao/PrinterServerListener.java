package com.pec.biosistemico.pec.impressao;

import java.net.Socket;

public interface PrinterServerListener {
    public void onConnect(Socket socket);
}
