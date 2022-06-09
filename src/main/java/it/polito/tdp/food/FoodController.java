/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	//prendo l'input
    	int porzioni = 0;
    	try {
    		porzioni = Integer.parseInt(this.txtPorzioni.getText());
    	}catch(NumberFormatException e){
    		this.txtResult.setText("Devi inserire un valore numerico intero!");
    		return;
    	}
    	//se sono qui posso costruire il grafo
    	this.model.creaGrafo(porzioni);
    	this.boxFood.getItems().clear();
    	for(Food f : this.model.getVertici()) {
    		this.boxFood.getItems().add(f);
    	}
    	this.txtResult.appendText("Grafo creato con "+this.model.nVertici()+" vertici e "+this.model.nArchi()+" archi.\n");
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	//prendo l'input
    	//controllo che il grafo sia stato creato
    	if(!this.model.grafoCreato()) {
    		this.txtResult.setText("Devi prima creare il grafo!");
    		return;
    	}
    	//controllo che sia stato selezionato il cibo
    	Food f = this.boxFood.getValue();
    	if( f == null) {
    		this.txtResult.setText("Devi selezionare un cibo dal menu a tendina");
    		return;
    	}
    	//se sono qui va tutto bene e posso procedere
    	List<Adiacenza> ad = this.model.getMaxCalorieCongiunte(f);
    	for(int i = 0; i < 5 && i <ad.size(); i++) {
    		Adiacenza a = ad.get(i);
    		this.txtResult.appendText(a.getF2()+" - "+a.getPeso()+" calorie congiunte\n");
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	int k = 0;
    	try {
    		k = Integer.parseInt(this.txtK.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Devi inserire un numero intero");
    		return;
    	}
    	if( k > 10 || k < 1) {
    		this.txtResult.setText("Devi inserire un numero intero fra 1 e 10 (compresi)!");
    		return;
    	}
    	Food f = this.boxFood.getValue();
    	if( f == null) {
    		this.txtResult.setText("Devi selezionare un cibo dal menu a tendina");
    		return;
    	}
    	//qui posso iniziare la simulazione
    	String msg = this.model.simula(k, f);
    	this.txtResult.setText(msg);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
