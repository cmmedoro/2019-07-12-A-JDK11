package it.polito.tdp.food.model;

public class Stazione {
	
	private boolean libera;
	private Food ciboLavorato;
	public Stazione(boolean libera, Food ciboLavorato) {
		super();
		this.libera = libera;
		this.ciboLavorato = ciboLavorato;
	}
	public boolean isLibera() {
		return libera;
	}
	public void setLibera(boolean libera) {
		this.libera = libera;
	}
	public Food getCiboLavorato() {
		return ciboLavorato;
	}
	public void setCiboLavorato(Food ciboLavorato) {
		this.ciboLavorato = ciboLavorato;
	}
}
