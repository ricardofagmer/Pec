package com.pec.biosistemico.pec.domain;

/**
 * Created by Ricardo on 01/12/2016.
 */

public class Retorno {

    private boolean sucesso;
    private String mensagem;

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }


}