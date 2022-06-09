package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	private Map<Integer, Food> idMapFood;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private Simulatore sim;
	
	public Model() {
		this.dao = new FoodDao();
		this.idMapFood = new HashMap<>();
		this.dao.listAllFoods(idMapFood);
	}
	
	public void creaGrafo(int porzioni) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//aggiungo i vertici al grafo
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(porzioni, idMapFood));
		//aggiungo gli archi
		List<Adiacenza> lista = new ArrayList<>(this.dao.getArchi(idMapFood));
		for(Adiacenza a : lista) {
			Graphs.addEdgeWithVertices(this.grafo, a.getF1(), a.getF2(), a.getPeso());
		}
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	public Set<Food> getVertici(){
		return this.grafo.vertexSet();
	}
	public boolean grafoCreato() {
		if(this.grafo == null)
			return false;
		return true;
	}
	
	public List<Adiacenza> getMaxCalorieCongiunte(Food f){
		//5 cibi con peso degli archi massimo, fra quelli che sono adiacenti a f
		List<Food> vicini = Graphs.neighborListOf(this.grafo, f);
		List<Adiacenza> temp = new ArrayList<>();
		List<Adiacenza> a = new ArrayList<>();
		for(Food cibo : vicini) {
			DefaultWeightedEdge e = this.grafo.getEdge(f, cibo);
			double peso = this.grafo.getEdgeWeight(e);
			temp.add(new Adiacenza(f, cibo, peso));
		}
		Collections.sort(temp);
		return temp;
	}
	
	public String simula(int K, Food cibo) {
		this.sim = new Simulatore(this.grafo, this);
		sim.setK(K);;
		sim.init(cibo);
		sim.run();
		String msg = "Preparati "+sim.getNumCibiLavorati()+" cibi in "+sim.gettLavorazioneTot()+" minuti\n";
		return msg;
	}

}
