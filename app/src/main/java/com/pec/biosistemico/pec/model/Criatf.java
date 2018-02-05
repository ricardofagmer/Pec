package com.pec.biosistemico.pec.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ricardo on 23/11/2016.
 */

public class Criatf implements Serializable {

    @SerializedName("_id")
    int id;
    @SerializedName("projeto")
    int _projeto;
    @SerializedName("data_protocolo")
    String data_protocolo;
    @SerializedName("data_inseminacao")
    String data_inseminacao;
    @SerializedName("vacina")
    String vacina;
    @SerializedName("raca")
    String raca;
    @SerializedName("id_animais")
    String id_animais;
    @SerializedName("propriedade")
    int propriedade;
    @SerializedName("touro")
    String touro;
    @SerializedName("id_criatf")
    int id_criatf;
    @SerializedName("d8")
    String d8;
    @SerializedName("iatf")
    String iatf;
    @SerializedName("fez_d0")
    String fez_d0;
    @SerializedName("fez_ia")
    String fez_ia;
    @SerializedName("id_reprodutivo")
    int id_reprodutivo;
    @SerializedName("id_cobertura")
    int cobertura;
    @SerializedName("grupo")
    String grupo;
    @SerializedName("criatf_pai")
    String criatf_pai;
    @SerializedName("data_d0")
    String data_d0;
    @SerializedName("consultor")
    String consultor;
    @SerializedName("enviado")
    String enviado;
    @SerializedName("nome_vaca")
    String nome_vaca;
    @SerializedName("dg")
    String dg;
    @SerializedName("diagnostico")
    String diagnostico;
    @SerializedName("numeroDias")
    String numeroDias;
    @SerializedName("previsao_parto")
    String previsao_parto;
    @SerializedName("tipo_atendimento")
    String tipo_atendimento;
    @SerializedName("nova_ia")
    String nova_ia;
    @SerializedName("cod_mobile")
    String cod_mobile;

    public String getCod_mobile() {
        return cod_mobile;
    }

    public void setCod_mobile(String cod_mobile) {
        this.cod_mobile = cod_mobile;
    }

    public Criatf() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getNumeroDias() {
        return numeroDias;
    }

    public void setNumeroDias(String numeroDias) {
        this.numeroDias = numeroDias;
    }

    public String getPrevisao_parto() {
        return previsao_parto;
    }

    public void setPrevisao_parto(String previsao_parto) {
        this.previsao_parto = previsao_parto;
    }

    public String getTipo_atendimento() {
        return tipo_atendimento;
    }

    public void setTipo_atendimento(String tipo_atendimento) {
        this.tipo_atendimento = tipo_atendimento;
    }

    public String getNova_ia() {
        return nova_ia;
    }

    public void setNova_ia(String nova_ia) {
        this.nova_ia = nova_ia;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getCriatf_pai() {
        return criatf_pai;
    }

    public void setCriatf_pai(String criatf_pai) {
        this.criatf_pai = criatf_pai;
    }

    public String getData_d0() {
        return data_d0;
    }

    public void setData_d0(String data_d0) {
        this.data_d0 = data_d0;
    }

    public String getConsultor() {
        return consultor;
    }

    public void setConsultor(String consultor) {
        this.consultor = consultor;
    }

    public String getEnviado() {
        return enviado;
    }

    public void setEnviado(String enviado) {
        this.enviado = enviado;
    }

    public String getNome_vaca() {
        return nome_vaca;
    }

    public void setNome_vaca(String nome_vaca) {
        this.nome_vaca = nome_vaca;
    }

    public int get_projeto() {
        return _projeto;
    }

    public void set_projeto(int _projeto) {
        this._projeto = _projeto;
    }

    public String getData_protocolo() {
        return data_protocolo;
    }

    public void setData_protocolo(String data_protocolo) {
        this.data_protocolo = data_protocolo;
    }

    public String getData_inseminacao() {
        return data_inseminacao;
    }

    public void setData_inseminacao(String data_inseminacao) {
        this.data_inseminacao = data_inseminacao;
    }

    public String getVacina() {
        return vacina;
    }

    public void setVacina(String vacina) {
        this.vacina = vacina;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getId_animais() {
        return id_animais;
    }

    public void setId_animais(String id_animais) {
        this.id_animais = id_animais;
    }

    public int getPropriedade() {
        return propriedade;
    }

    public void setPropriedade(int propriedade) {
        this.propriedade = propriedade;
    }

    public String getTouro() {
        return touro;
    }

    public void setTouro(String touro) {
        this.touro = touro;
    }

    public int getId_criatf() {
        return id_criatf;
    }

    public void setId_criatf(int id_criatf) {
        this.id_criatf = id_criatf;
    }

    public String getD8() {
        return d8;
    }

    public void setD8(String d8) {
        this.d8 = d8;
    }

    public String getIatf() {
        return iatf;
    }

    public void setIatf(String iatf) {
        this.iatf = iatf;
    }

    public String getFez_d0() {
        return fez_d0;
    }

    public void setFez_d0(String fez_d0) {
        this.fez_d0 = fez_d0;
    }

    public String getFez_ia() {
        return fez_ia;
    }

    public void setFez_ia(String fez_ia) {
        this.fez_ia = fez_ia;
    }

    public int getId_reprodutivo() {
        return id_reprodutivo;
    }

    public void setId_reprodutivo(int id_reprodutivo) {
        this.id_reprodutivo = id_reprodutivo;
    }

    public int getCobertura() {
        return cobertura;
    }

    public void setCobertura(int cobertura) {
        this.cobertura = cobertura;
    }


}
