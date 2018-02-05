package com.pec.biosistemico.pec.paperTop.financeiro;

import java.io.Serializable;

public class LancamentoFinanceiroItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer idLancamento;
	private Integer idProduto;
	private String tipo;
	private Double quantidade;
	private Double valorUnitario;
	private Double valorTotal;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdLancamento() {
		return idLancamento;
	}

	public void setIdLancamento(Integer idLancamento) {
		this.idLancamento = idLancamento;
	}

	public Integer getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public Double getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}
}