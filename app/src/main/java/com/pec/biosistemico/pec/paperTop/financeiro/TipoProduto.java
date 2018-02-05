package com.pec.biosistemico.pec.paperTop.financeiro;

/**
 * Created by Ricardo on 26/11/2016.
 */

public enum TipoProduto {

    DESPESA("Despesa"),RECEITA("Receita");

    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    TipoProduto(String descricao){

        this.descricao = descricao;
    }
}
