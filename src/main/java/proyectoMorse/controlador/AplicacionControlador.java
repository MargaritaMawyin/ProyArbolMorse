/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoMorse.controlador;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import proyectoMorse.modelo.BinaryTreeKevin;
import proyectoMorse.modelo.ZonaDibujoArbol;

/**
 * FXML Controller class
 *
 * @author KevinChevez
 */
public class AplicacionControlador implements Initializable {

    @FXML
    private Button btnEscuchar;
    @FXML
    private TextField campoTexToModify;
    @FXML
    private Pane zonaDeDibujo;
    
    private ZonaDibujoArbol zonaDeArbol;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        double widhtZona = zonaDeDibujo.getPrefWidth();
        zonaDeArbol = new ZonaDibujoArbol(cargarArbol(App.mapaMorse), widhtZona);
        zonaDeArbol.dibujarArbol();
        zonaDeDibujo.getChildren().add(zonaDeArbol);
    }    
    
    @FXML
    private void escuchar(ActionEvent event) {
        System.out.println("Está escuchando...");
        String textRecolected = campoTexToModify.getText().toUpperCase();
        campoTexToModify.clear();
        if(textRecolected.isBlank()) {
            System.out.println("No escribió algo");
        }else{
            System.out.println(textRecolected);
            zonaDeArbol.escuchar(textRecolected, zonaDeDibujo);
        }
    }

    private BinaryTreeKevin<String> cargarArbol(HashMap<String, List<String>> mapa){
        BinaryTreeKevin<String> arbolBinario = new BinaryTreeKevin<>();
        for(String clave : mapa.keySet()){
            arbolBinario.addMorse(clave, mapa.get(clave));
        }
        return arbolBinario;
    }
    
}
