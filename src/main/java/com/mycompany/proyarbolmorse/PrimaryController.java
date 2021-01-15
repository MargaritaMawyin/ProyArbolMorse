package com.mycompany.proyarbolmorse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

public class PrimaryController {
    public static HashMap<String, List<String>> cod = new HashMap<>();
    public static BinaryTree<String> arbolMorse = new BinaryTree<>(String::compareTo);
    @FXML
    private FlowPane flowPane;
    private Button BotonTraducir;
    private TextField codigoIngresdo;
    
    
    public void initialize() throws IOException{
        System.out.println("Inicializa el arbol morse");
        cargarArchivoConBT();
        arbolMorse.crearArbolMorse(cod);
        arbolMorse.anchura();
        
        
    }
    @FXML
    private void switchToSecondary() throws IOException { // ahora es boton traducir 
        arbolMorse.crearArbolMorse(cod);
        arbolMorse.anchura();
//        flowPane.getChildren().addAll(arbolMorse.crearArbolMorse(cod));
//        arbolMorse.codificarArbolMorse(codigoIngresdo.getText(), arbolMorse);
//        App.setRoot("secondary");
    }
    public  void cargarArchivoConBT() throws IOException {
        try{
            List<String> lineas = Files.readAllLines(Paths.get("codMorse.txt")); 
            for (String line: lineas){
                String[] division = line.split("\\|");
                List codigo = new LinkedList();
                int i = 1;
                while(i < division.length){
                    codigo.add(division[i]);
                    
                    i++;
                }
                System.out.println(codigo);
                cod.put(division[0], codigo);
            }
        }catch(IOException e){
            System.err.println("No se ha encontrado archivo "+ e.getMessage());
        }

    }
}
