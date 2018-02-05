package com.pec.biosistemico.pec.util;

import java.util.List;

public class Global {

    private static Global instance;
    private int myInt;
    private String myString;
    private String myString2;
    private String myString3;
    private String myString4;
    private String myString5;
    private String myString6;
    private String statusProdutivo;
    private String status_conexao;
    private String status;
    private String login;
    private int myInt01;
    private int position;
    private int id;
    private int LastID;
    private int Respondido;
    private int idPaper;
    private int produtor;
    private int start;
    private int idAnimal;
    private String dataRelatorio;
    private String project;
    private int cliente_position;
    private int projeto_position;
    private int grupo_position;
    private String consultor;
    private String idProjetoDrive;
    private String resumo_paperTop;
    private boolean atualizacao;
    private boolean edicao;
    private boolean termica;
    private int qtdadeAT;
    private boolean lan;
    private boolean envio;
    private int count_animais;
    private int animais_verificado;
    private int verfica_parto;
    private int paperTop;

    public int getVerfica_parto() {
        return verfica_parto;
    }

    public void setVerfica_parto(int verfica_parto) {
        this.verfica_parto = verfica_parto;
    }

    public int getCount_animais() {
        return count_animais;
    }

    public void setCount_animais(int count_animais) {
        this.count_animais = count_animais;
    }

    public boolean isEnvio() {
        return envio;
    }

    public void setEnvio(boolean envio) {
        this.envio = envio;
    }

    public int getAnimais_verificado() {
        return animais_verificado;
    }

    public void setAnimais_verificado(int animais_verificado) {
        this.animais_verificado = animais_verificado;
    }

    private Global() {
        envio = false;

        edicao = false;
        termica = false;
        myInt = 0;
        status_conexao = "sem conexão";
        myString = "";
        myString2 = "";
        myString3 = "";
        myString4 = "";
        qtdadeAT = 0;
        myString5 = "";
        myString6 = "";
        resumo_paperTop = "";
        login = "";
        dataRelatorio = "";
        project = "";
        cliente_position = 0;
        projeto_position = 0;
        grupo_position = 0;
        status = "";
        lan = false;
        statusProdutivo = "";
        myInt01 = 0;
        position = 0;
        id = 0;
        LastID = 0;
        Respondido = 0;
        idPaper = 0;
        produtor = 0;
        start = 0;
        idAnimal = 0;
        consultor = "";
        atualizacao = false;
        count_animais = 0;
        animais_verificado = 0;
        verfica_parto = 0;
        paperTop = 0;
    }

    public int getPaperTop() {
        return paperTop;
    }

    public void setPaperTop(int paperTop) {
        this.paperTop = paperTop;
    }

    public static Global getInstance() {
        if (Global.instance == null) {
            Global.instance = new Global();
        }
        return Global.instance;
    }

    public boolean isLan() {
        return lan;
    }

    public void setLan(boolean lan) {
        this.lan = lan;
    }

    public int getQtdadeAT() {
        return qtdadeAT;
    }

    public void setQtdadeAT(int qtdadeAT) {
        this.qtdadeAT = qtdadeAT;
    }

    public boolean isTermica() {
        return termica;
    }

    public void setTermica(boolean termica) {
        this.termica = termica;
    }

    public boolean isEdicao() {
        return edicao;
    }

    public void setEdicao(boolean edicao) {
        this.edicao = edicao;
    }

    public boolean getAtualizacao() {
        return atualizacao;
    }

    public void setAtualizacao(boolean atualizacao) {
        this.atualizacao = atualizacao;
    }

    public String getResumo_paperTop() {
        return resumo_paperTop;
    }

    public void setResumo_paperTop(String resumo_paperTop) {
        this.resumo_paperTop = resumo_paperTop;
    }

    public String getIdProjetoDrive() {
        return idProjetoDrive;
    }

    public void setIdProjetoDrive(String idProjetoDrive) {
        this.idProjetoDrive = idProjetoDrive;
    }

    public String getStatus_conexao() {
        return status_conexao;
    }

    public void setStatus_conexao(String status_conexao) {
        this.status_conexao = status_conexao;
    }

    public String getConsultor() {
        return consultor;
    }

    public void setConsultor(String consultor) {
        this.consultor = consultor;
    }

    public int getCliente_position() {
        return cliente_position;
    }

    public void setCliente_position(int cliente_position) {
        this.cliente_position = cliente_position;
    }

    public int getProjeto_position() {
        return projeto_position;
    }

    public void setProjeto_position(int projeto_position) {
        this.projeto_position = projeto_position;
    }

    public int getGrupo_position() {
        return grupo_position;
    }

    public void setGrupo_position(int grupo_position) {
        this.grupo_position = grupo_position;
    }

    public String getDataRelatorio() {
        return dataRelatorio;
    }

    public void setDataRelatorio(String i) {
        this.dataRelatorio = i;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int i) {
        this.start = i;
    }

    public int getIdPaper() {
        return idPaper;
    }

    public void setIdPaper(int i) {
        this.idPaper = i;
    }

    public int getMyInt() {
        return myInt;
    }

    public void setMyInt(int i) {
        this.myInt = i;
    }

    public int getLastID() {
        return LastID;
    }

    public void setLastID(int i) {
        this.LastID = i;
    }

    public int getRespondido() {
        return Respondido;
    }

    public void setRespondido(int i) {
        this.Respondido = i;
    }

    public String getMyString() {
        return myString;
    }

    public void setMyString(String s) {
        this.myString = s;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String s) {
        this.login = s;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String s) {
        this.status = s;
    }


    public String getStatusProdutivo() {
        return statusProdutivo;
    }

    public void setStatusProdutivo(String s) {
        this.statusProdutivo = s;
    }

    public String getUsuario() {
        return myString;
    }

    public void setUsuario(String u) {
        this.myString = u;
    }

    public String getSenha() {
        return myString3;
    }

    public void setSenha(String s) {
        this.myString3 = s;
    }

    public String getEmail() {
        return myString2;
    }

    public void setEmail(String s) {
        this.myString2 = s;
    }

    public String getSQL() {
        return myString;
    }

    public void setSQL(String sql) {
        this.myString = sql;
    }

    public int getProdutor() {
        return produtor;
    }

    public void setProdutor(int produtor) {
        this.produtor = produtor;
    }

    public int getIDProdutor() {
        return id;
    }

    public void setIDProdutor(int id) {
        this.id = id;
    }

    public String getMessage() {
        return myString4;
    }

    public void setMessage(String s) {
        this.myString4 = s;
    }

    public String getGrupo() {
        return myString6;
    }

    public void setGrupo(String s) {
        this.myString6 = s;
    }

    public int getProjeto() {
        return myInt;
    }

    public void setProjeto(int s) {
        this.myInt = s;
    }

    public int getCliente() {
        return myInt01;
    }

    public void setCliente(int s) {
        this.myInt01 = s;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int s) {
        this.position = s;
    }

    public String getCrea() {
        return myString5;
    }

    public void setCrea(String crea) {
        this.myString5 = crea;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int animal) {
        this.idAnimal = animal;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

}