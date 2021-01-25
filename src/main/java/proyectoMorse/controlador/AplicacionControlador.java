/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoMorse.controlador;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import proyectoMorse.modelo.BinaryTree;
import proyectoMorse.modelo.ZonaDibujoArbol;

/**
 * FXML Controller class
 *
 * @author KevinChevez
 */
public class AplicacionControlador implements Initializable {

    
    @FXML
    private TextField campoTexToModify;
    @FXML
    private Pane zonaDeDibujo;
    
    private ZonaDibujoArbol zonaDeArbol;

    /**
     * Initializes the controller class.
     * @param url Recibe una url implícita de FXML para construir la clase.
     * @param rb Recibe un ResourseBundle de parte de FXML.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        double widhtZona = zonaDeDibujo.getPrefWidth();
        zonaDeArbol = new ZonaDibujoArbol(cargarArbol(App.getMapaMorse()), widhtZona);
        zonaDeArbol.dibujarArbol();
        zonaDeDibujo.getChildren().add(zonaDeArbol);
    }    
    
    @FXML
    private void escuchar(ActionEvent event) throws InterruptedException {        
        String textRecolected = campoTexToModify.getText().toUpperCase();
        campoTexToModify.clear();
            System.out.println(textRecolected);
            zonaDeArbol.escuchar(textRecolected, zonaDeDibujo);
//        }
    }

    private BinaryTree<String> cargarArbol(HashMap<String, List<String>> mapa){
        BinaryTree<String> arbolBinario = new BinaryTree<>();
        mapa.keySet().forEach(clave -> {
            arbolBinario.addMorse(clave, mapa.get(clave));
        });
        return arbolBinario;
    }
    
}
