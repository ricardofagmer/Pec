package com.pec.biosistemico.pec.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CheckList implements Serializable {

    int _id;

    @SerializedName("_grupo")
    int _grupo;
    @SerializedName("_projeto")
    int _projeto;
    @SerializedName("_propriedade")
    int _propriedade;
    @SerializedName("data_coleta")
    String data_coleta;
    @SerializedName("controle1")
    String controle1;
    @SerializedName("controle2")
    String controle2;
    @SerializedName("controle3")
    String controle3;
    @SerializedName("manejon1")
    String manejon1;
    @SerializedName("manejon2")
    String manejon2;
    @SerializedName("manejon3")
    String manejon3;
    @SerializedName("manejon4")
    String manejon4;
    @SerializedName("manejon5")
    String manejon5;
    @SerializedName("manejon6")
    String manejon6;
    @SerializedName("sanidade1")
    String sanidade1;
    @SerializedName("sanidade2")
    String sanidade2;
    @SerializedName("sanidade3")
    String sanidade3;
    @SerializedName("sanidade4")
    String sanidade4;
    @SerializedName("sanidade5")
    String sanidade5;
    @SerializedName("manejor1")
    String manejor1;
    @SerializedName("manejor2")
    String manejor2;
    @SerializedName("manejor3")
    String manejor3;
    @SerializedName("manejor4")
    String manejor4;
    @SerializedName("qualidade1")
    String qualidade1;
    @SerializedName("qualidade2")
    String qualidade2;
    @SerializedName("qualidade3")
    String qualidade3;
    @SerializedName("qualidade4")
    String qualidade4;
    @SerializedName("qualidade5")
    String qualidade5;
    @SerializedName("data_importacao")
    String data_importacao;
    @SerializedName("consultor")
    String consultor;
    @SerializedName("tipo_reprodutivo")
    String tipo_reprodutivo;
    @SerializedName("situacao_encontrada")
    String situacao_encontrada;
    @SerializedName("recomendacao")
    String recomendacao;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public int get_grupo() {
        return _grupo;
    }

    public void set_grupo(int _grupo) {
        this._grupo = _grupo;
    }

    public int get_projeto() {
        return _projeto;
    }

    public void set_projeto(int _projeto) {
        this._projeto = _projeto;
    }

    public int get_propriedade() {
        return _propriedade;
    }

    public void set_propriedade(int _propriedade) {
        this._propriedade = _propriedade;
    }

    public String getData_coleta() {
        return data_coleta;
    }

    public void setData_coleta(String data_coleta) {
        this.data_coleta = data_coleta;
    }

    public String getControle1() {
        return controle1;
    }

    public void setControle1(String controle1) {
        this.controle1 = controle1;
    }

    public String getControle2() {
        return controle2;
    }

    public void setControle2(String controle2) {
        this.controle2 = controle2;
    }

    public String getControle3() {
        return controle3;
    }

    public void setControle3(String controle3) {
        this.controle3 = controle3;
    }

    public String getManejon1() {
        return manejon1;
    }

    public void setManejon1(String manejon1) {
        this.manejon1 = manejon1;
    }

    public String getManejon2() {
        return manejon2;
    }

    public void setManejon2(String manejon2) {
        this.manejon2 = manejon2;
    }

    public String getManejon3() {
        return manejon3;
    }

    public void setManejon3(String manejon3) {
        this.manejon3 = manejon3;
    }

    public String getManejon4() {
        return manejon4;
    }

    public void setManejon4(String manejon4) {
        this.manejon4 = manejon4;
    }

    public String getManejon5() {
        return manejon5;
    }

    public void setManejon5(String manejon5) {
        this.manejon5 = manejon5;
    }

    public String getManejon6() {
        return manejon6;
    }

    public void setManejon6(String manejon6) {
        this.manejon6 = manejon6;
    }

    public String getSanidade1() {
        return sanidade1;
    }

    public void setSanidade1(String sanidade1) {
        this.sanidade1 = sanidade1;
    }

    public String getSanidade2() {
        return sanidade2;
    }

    public void setSanidade2(String sanidade2) {
        this.sanidade2 = sanidade2;
    }

    public String getSanidade3() {
        return sanidade3;
    }

    public void setSanidade3(String sanidade3) {
        this.sanidade3 = sanidade3;
    }

    public String getSanidade4() {
        return sanidade4;
    }

    public void setSanidade4(String sanidade4) {
        this.sanidade4 = sanidade4;
    }

    public String getSanidade5() {
        return sanidade5;
    }

    public void setSanidade5(String sanidade5) {
        this.sanidade5 = sanidade5;
    }

    public String getManejor1() {
        return manejor1;
    }

    public void setManejor1(String manejor1) {
        this.manejor1 = manejor1;
    }

    public String getManejor2() {
        return manejor2;
    }

    public void setManejor2(String manejor2) {
        this.manejor2 = manejor2;
    }

    public String getManejor3() {
        return manejor3;
    }

    public void setManejor3(String manejor3) {
        this.manejor3 = manejor3;
    }

    public String getManejor4() {
        return manejor4;
    }

    public void setManejor4(String manejor4) {
        this.manejor4 = manejor4;
    }

    public String getQualidade1() {
        return qualidade1;
    }

    public void setQualidade1(String qualidade1) {
        this.qualidade1 = qualidade1;
    }

    public String getQualidade2() {
        return qualidade2;
    }

    public void setQualidade2(String qualidade2) {
        this.qualidade2 = qualidade2;
    }

    public String getQualidade3() {
        return qualidade3;
    }

    public void setQualidade3(String qualidade3) {
        this.qualidade3 = qualidade3;
    }

    public String getQualidade4() {
        return qualidade4;
    }

    public void setQualidade4(String qualidade4) {
        this.qualidade4 = qualidade4;
    }

    public String getQualidade5() {
        return qualidade5;
    }

    public void setQualidade5(String qualidade5) {
        this.qualidade5 = qualidade5;
    }

    public String getData_importacao() {
        return data_importacao;
    }

    public void setData_importacao(String data_importacao) {
        this.data_importacao = data_importacao;
    }

    public String getConsultor() {
        return consultor;
    }

    public void setConsultor(String consultor) {
        this.consultor = consultor;
    }

    public String getTipo_reprodutivo() {
        return tipo_reprodutivo;
    }

    public void setTipo_reprodutivo(String tipo_reprodutivo) {
        this.tipo_reprodutivo = tipo_reprodutivo;
    }

    public String getSituacao_encontrada() {
        return situacao_encontrada;
    }

    public void setSituacao_encontrada(String situacao_encontrada) {
        this.situacao_encontrada = situacao_encontrada;
    }

    public String getRecomendacao() {
        return recomendacao;
    }

    public void setRecomendacao(String recomendacao) {
        this.recomendacao = recomendacao;
    }


}
