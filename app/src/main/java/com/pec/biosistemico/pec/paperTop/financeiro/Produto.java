package com.pec.biosistemico.pec.paperTop.financeiro;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("idProduto")
	int idProduto;
	@SerializedName("descricao")
	String descricao;
	@SerializedName("unidadeMedida")
	String unidadeMedida;
	@SerializedName("valorUnitario")
	Double valorUnitario;
	@SerializedName("valorMaximo")
	Double valorMaximo;
	@SerializedName("valorMinimo")
	Double valorMinimo;
	@SerializedName("tipo")
	String tipo;
	@SerializedName("_propriedade")
	int _propriedade;
	@SerializedName("_grupo")
	int _grupo;
	@SerializedName("_projeto")
	int _projeto;
	@SerializedName("Quantidade")
	int quantidade;
	@SerializedName("_id")
	int _id;
	@SerializedName("enviado")
	String enviado;

	public String getEnviado() {
		return enviado;
	}

	public void setEnviado(String enviado) {
		this.enviado = enviado;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int get_projeto() {
		return _projeto;
	}

	public void set_projeto(int _projeto) {
		this._projeto = _projeto;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int get_propriedade() {
		return _propriedade;
	}

	public void set_propriedade(int _propriedade) {
		this._propriedade = _propriedade;
	}

	public int get_grupo() {
		return _grupo;
	}

	public void set_grupo(int _grupo) {
		this._grupo = _grupo;
	}

	public int getIdProduto() {
		return idProduto;
	}
	public void setIdProduto(int idProduto) {
		this.idProduto = idProduto;
	}

	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Double getValorMaximo() {
		return valorMaximo;
	}
	public void setValorMaximo(Double valorMaximo) {
		this.valorMaximo = valorMaximo;
	}

	public Double getValorMinimo() {
		return valorMinimo;
	}
	public void setValorMinimo(Double valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	public String getUnidadeMedida() {
		return unidadeMedida;
	}
	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}
	
	

}