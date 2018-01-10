package com.meupolitico.entity;

import java.util.List;

/**
 * 
 * @author kjanuaria <br>
 * 
 *         Representa um Político
 */
public class Politico {

	private String oid;
	private String nome;
	private List<ProcessoJudicial> processosJudiciais;

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<ProcessoJudicial> getProcessosJudiciais() {
		return processosJudiciais;
	}

	public void setProcessosJudiciais(List<ProcessoJudicial> processosJudiciais) {
		this.processosJudiciais = processosJudiciais;
	}

}
