package com.pec.biosistemico.pec.paperTop.financeiro;

import java.io.Serializable;
import java.util.List;

public class LancamentoFinanceiro implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String dataColeta;
	private int idProjeto;
	private String nomeProjeto;
	private int idGrupo;
	private String nomeGrupo;
	private int idPropriedade;
	private String nomePropriedade;
	private  boolean enviado;
	private List<LancamentoFinanceiroItem> itens;

	public boolean isEnviado() {
		return enviado;
	}

	public void setEnviado(boolean enviado) {
		this.enviado = enviado;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdProjeto() {
		return idProjeto;
	}

	public void setIdProjeto(int idProjeto) {
		this.idProjeto = idProjeto;
	}

	public String getNomeProjeto() {
		return nomeProjeto;
	}

	public void setNomeProjeto(String nomeProjeto) {
		this.nomeProjeto = nomeProjeto;
	}

	public int getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(int idGrupo) {
		this.idGrupo = idGrupo;
	}

	public String getNomeGrupo() {
		return nomeGrupo;
	}

	public void setNomeGrupo(String nomeGrupo) {
		this.nomeGrupo = nomeGrupo;
	}

	public int getIdPropriedade() {
		return idPropriedade;
	}

	public void setIdPropriedade(int idPropriedade) {
		this.idPropriedade = idPropriedade;
	}

	public String getNomePropriedade() {
		return nomePropriedade;
	}

	public void setNomePropriedade(String nomePropriedade) {
		this.nomePropriedade = nomePropriedade;
	}

	public String getDataColeta() {
		return dataColeta;
	}

	public void setDataColeta(String dataColeta) {
		this.dataColeta = dataColeta;
	}

	public List<LancamentoFinanceiroItem> getListatens()
	{
		return itens;
	}

	public void setListaItens(List<LancamentoFinanceiroItem> itens) {
		this.itens = itens;
	}

}
