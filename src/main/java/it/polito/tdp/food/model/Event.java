package it.polito.tdp.food.model;

public class Event implements Comparable<Event>{
	
	public enum EventType{
		INIZIO_LAVORAZIONE,
		TERMINE_LAVORAZIONE
	}
	
	//informazioni sull'evento
	private EventType tipo;
	private Double tempoLavorazione;
	private Food ciboLavorato;
	private Stazione s;
	public Event(Double tempoLavorazione, EventType tipo, Food ciboLavorato, Stazione s) {
		super();
		this.tipo = tipo;
		this.tempoLavorazione = tempoLavorazione;
		this.ciboLavorato = ciboLavorato;
		this.s = s;
	}
	public EventType getTipo() {
		return tipo;
	}
	public void setTipo(EventType tipo) {
		this.tipo = tipo;
	}
	public Double getTempoLavorazione() {
		return tempoLavorazione;
	}
	public void setTempoLavorazione(Double tempoLavorazione) {
		this.tempoLavorazione = tempoLavorazione;
	}
	public Food getCiboLavorato() {
		return ciboLavorato;
	}
	public void setCiboLavorato(Food ciboLavorato) {
		this.ciboLavorato = ciboLavorato;
	}
	public Stazione getS() {
		return s;
	}
	public void setS(Stazione s) {
		this.s = s;
	}
	@Override
	public int compareTo(Event o) {
		return this.tempoLavorazione.compareTo(o.getTempoLavorazione());
	}
	
	

}
