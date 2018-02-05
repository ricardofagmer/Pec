package com.pec.biosistemico.pec.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaperTop implements Serializable {

    int id;
    @SerializedName("dataCobertura")
    String dataCobertura;
    @SerializedName("dataDiagnostico")
    String dataDiagnostico;
    @SerializedName("dataParto")
    String dataParto;
    @SerializedName("diasPrenhez")
    String diasPrenhez;
    @SerializedName("idAnimais")
    String idAnimais;
    @SerializedName("idCobertura")
    String idCobertura;
    @SerializedName("idReprodutivo")
    String idReprodutivo;
    @SerializedName("numeroFetos")
    String numeroFetos;
    @SerializedName("previsaoParto")
    String previsaoParto;
    @SerializedName("statusProdutivo")
    String StatusProdutivo;
    @SerializedName("brinco")
    String brinco;
    @SerializedName("categoria")
    String categoria;
    @SerializedName("dataPesagem")
    String dataPesagem;
    @SerializedName("idPropriedade")
    String idPropriedade;
    @SerializedName("manejo")
    String manejo;
    @SerializedName("nome")
    String nome;
    @SerializedName("previsaoSecagem")
    String previsaoSecagem;
    @SerializedName("status")
    String status;
    @SerializedName("ocorrencia")
    String ocorrencia;
    @SerializedName("obs")
    String obs;
    @SerializedName("ultimoparto")
    String ultimoparto;
    @SerializedName("area")
    String area;
    @SerializedName("referencia")
    String referencia;
    @SerializedName("projeto")
    int projeto;
    @SerializedName("grupo")
    int grupo;
    @SerializedName("tipoParto")
    String tipoParto;
    @SerializedName("numeroDias")
    int numeroDias;
    @SerializedName("data_coleta")
    String data_coleta;
    @SerializedName("data_nascimento")
    String data_nascimento;
    @SerializedName("raca")
    int raca;
    @SerializedName("cod_mobile")
    String cod_mobile;
    @SerializedName("consultor")
    String consultor;
    @SerializedName("id_criatf")
    int id_criatf;
    @SerializedName("peso_atual")
    String peso_atual;

    public int get_id() {
        return id;
    }

    public void set_id(int _id) {
        this.id = _id;
    }

    public String getPeso_atual() {
        return peso_atual;
    }

    public void setPeso_atual(String peso_atual) {
        this.peso_atual = peso_atual;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoParto() {
        return tipoParto;
    }

    public void setTipoParto(String tipoParto) {
        this.tipoParto = tipoParto;
    }

    public int getNumeroDias() {
        return numeroDias;
    }

    public void setNumeroDias(int numeroDias) {
        this.numeroDias = numeroDias;
    }

    public String getData_coleta() {
        return data_coleta;
    }

    public void setData_coleta(String data_coleta) {
        this.data_coleta = data_coleta;
    }

    public String getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public int getRaca() {
        return raca;
    }

    public void setRaca(int raca) {
        this.raca = raca;
    }

    public String getCod_mobile() {
        return cod_mobile;
    }

    public void setCod_mobile(String cod_mobile) {
        this.cod_mobile = cod_mobile;
    }

    public String getConsultor() {
        return consultor;
    }

    public void setConsultor(String consultor) {
        this.consultor = consultor;
    }

    public int getId_criatf() {
        return id_criatf;
    }

    public void setId_criatf(int id_criatf) {
        this.id_criatf = id_criatf;
    }

    public int getProjeto() {
        return projeto;
    }

    public void setProjeto(int projeto) {
        this.projeto = projeto;
    }

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    public PaperTop(){

    }

    public String getDataCobertura() {
        return dataCobertura;
    }

    public void setDataCobertura(String dataCobertura) {
        this.dataCobertura = dataCobertura;
    }

    public String getDataDiagnostico() {
        return dataDiagnostico;
    }

    public void setDataDiagnostico(String dataDiagnostico) {
        this.dataDiagnostico = dataDiagnostico;
    }

    public String getDataParto() {
        return dataParto;
    }

    public void setDataParto(String dataParto) {
        this.dataParto = dataParto;
    }

    public String getDiasPrenhez() {
        return diasPrenhez;
    }

    public void setDiasPrenhez(String diasPrenhez) {
        this.diasPrenhez = diasPrenhez;
    }

    public String getIdAnimais() {
        return idAnimais;
    }

    public void setIdAnimais(String idAnimais) {
        this.idAnimais = idAnimais;
    }

    public String getIdCobertura() {
        return idCobertura;
    }

    public void setIdCobertura(String idCobertura) {
        this.idCobertura = idCobertura;
    }

    public String getIdReprodutivo() {
        return idReprodutivo;
    }

    public void setIdReprodutivo(String idReprodutivo) {
        this.idReprodutivo = idReprodutivo;
    }

    public String getNumeroFetos() {
        return numeroFetos;
    }

    public void setNumeroFetos(String numeroFetos) {
        this.numeroFetos = numeroFetos;
    }

    public String getPrevisaoParto() {
        return previsaoParto;
    }

    public void setPrevisaoParto(String previsaoParto) {
        this.previsaoParto = previsaoParto;
    }

    public String getStatusProdutivo() {
        return StatusProdutivo;
    }

    public void setStatusProdutivo(String statusProdutivo) {
        StatusProdutivo = statusProdutivo;
    }

    public String getBrinco() {
        return brinco;
    }

    public void setBrinco(String brinco) {
        this.brinco = brinco;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDataPesagem() {
        return dataPesagem;
    }

    public void setDataPesagem(String dataPesagem) {
        this.dataPesagem = dataPesagem;
    }

    public String getIdPropriedade() {
        return idPropriedade;
    }

    public void setIdPropriedade(String idPropriedade) {
        this.idPropriedade = idPropriedade;
    }

    public String getManejo() {
        return manejo;
    }

    public void setManejo(String manejo) {
        this.manejo = manejo;
    }

    public String getNomeVaca() {
        return nome;
    }

    public void setNomeVaca(String nomeVaca) {
        this.nome = nomeVaca;
    }

    public String getPrevisaoSecagem() {
        return previsaoSecagem;
    }

    public void setPrevisaoSecagem(String previsaoSecagem) {
        this.previsaoSecagem = previsaoSecagem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOcorrencia() {
        return ocorrencia;
    }

    public void setOcorrencia(String ocorrencia) {
        this.ocorrencia = ocorrencia;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getUltimoparto() {
        return ultimoparto;
    }

    public void setUltimoparto(String ultimoparto) {
        this.ultimoparto = ultimoparto;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }


}
