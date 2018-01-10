package com.meupolitico.entity;

/**
 * 
 * @author kjanuaria<br>
 *
 *         Representa um Processo Judicial
 * 
 */
public class ProcessoJudicial {

	public String numero;
	public String descricao;
	public String politico;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getPolitico() {
		return politico;
	}

	public void setPolitico(String politico) {
		this.politico = politico;
	}

}
