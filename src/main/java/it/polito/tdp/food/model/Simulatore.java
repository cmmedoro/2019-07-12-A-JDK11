package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Event.EventType;
import it.polito.tdp.food.model.Food.StatoPreparazione;

public class Simulatore {
	
	//parametri in ingresso
	private int K; //numero di stazioni presenti
	
	//valori calcolati in uscita
	private int numCibiLavorati;
	private Double tLavorazioneTot;
	
	//stato del mondo
	private List<Stazione> stazioni;
	private List<Food> cibi;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private Model model;
	
	//coda degli eventi
	private PriorityQueue<Event> queue;

	//costruttore
	public Simulatore(Graph<Food, DefaultWeightedEdge> g, Model m) {
		this.grafo = g;
		this.model = m;
	}
	
	//inizializzo il Simulatore
	public void init(Food partenza) {
		//prendo i cibi dal grafo e li metto come da preparare
		this.cibi = new ArrayList<>(this.grafo.vertexSet());
		for(Food cibo : this.cibi) {
			cibo.setPrep(StatoPreparazione.DA_PREPARARE);
		}
		//prendo le stazioni e le metto come libere e senza cibo
		this.stazioni = new ArrayList<>();
		for(int i = 0;  i < this.K; i++) {
			this.stazioni.add(new Stazione(true, null));
		}
		this.tLavorazioneTot = 0.0;
		this.numCibiLavorati = 0;
		this.queue = new PriorityQueue<>();
		//prendi tutti i cibi connessi a quello di partenza
		List<Adiacenza> vicini = this.model.getMaxCalorieCongiunte(partenza);
		//i cibi "vicini" vanno messi nelle stazioni
		for(int i = 0 ; i<this.K && i<vicini.size(); i++) {
			this.stazioni.get(i).setLibera(false);
			this.stazioni.get(i).setCiboLavorato(vicini.get(i).getF2());
			vicini.get(i).getF2().setPrep(StatoPreparazione.IN_CORSO);
			Event e = new Event(vicini.get(i).getPeso(), EventType.TERMINE_LAVORAZIONE, vicini.get(i).getF2(), this.stazioni.get(i));
			queue.add(e);
		}
	}
	//eseguo la simulazione
	public void run() {
		while(!queue.isEmpty()) {
			Event e = this.queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch(e.getTipo()) {
		case INIZIO_LAVORAZIONE:
			List<Adiacenza> vicini = this.model.getMaxCalorieCongiunte(e.getCiboLavorato());
			Adiacenza prox = null;
			for(Adiacenza a : vicini) {
				//se il cibo non è ancora stato lavorato
				if(a.getF2().getPrep() == StatoPreparazione.DA_PREPARARE) {
					prox = a;
					break;
				}
			}
			if( prox != null) {
				prox.getF2().setPrep(StatoPreparazione.IN_CORSO);
				e.getS().setLibera(false);
				e.getS().setCiboLavorato(prox.getF2());
				Event e2 = new Event(e.getTempoLavorazione()+prox.getPeso(), EventType.TERMINE_LAVORAZIONE, prox.getF2(), e.getS());
				this.queue.add(e2);
			}
			break;
		case TERMINE_LAVORAZIONE:
			this.numCibiLavorati++;
			this.tLavorazioneTot = e.getTempoLavorazione();
			//libero la stazione
			e.getS().setLibera(true);
			e.getCiboLavorato().setPrep(StatoPreparazione.PREPARATO);
			Event e2 = new Event(e.getTempoLavorazione(), EventType.INIZIO_LAVORAZIONE, e.getCiboLavorato(), e.getS());
			this.queue.add(e2);
			break;
		}
		
	}

	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}

	public int getNumCibiLavorati() {
		return numCibiLavorati;
	}

	public Double gettLavorazioneTot() {
		return tLavorazioneTot;
	}
	
}
