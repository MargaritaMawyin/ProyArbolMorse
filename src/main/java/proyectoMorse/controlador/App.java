package proyectoMorse.controlador;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import proyectoMorse.constantes.Constantes;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    public static HashMap<String, List<String>> mapaMorse = 
            cargarMapaMorse(Constantes.pathArchivoMorse);
    
    public static Media mRaya = new Media(App.class.getResource("/recursos/Raya.mpeg").toString());
    public static Media mPunto = new Media(App.class.getResource("/recursos/Punto.mpeg").toString());
    public static MediaPlayer mediaPlayerDer = new MediaPlayer(mPunto);
    public static MediaPlayer mediaPlayerIzq = new MediaPlayer(mRaya);
    public static double tiempoDer;
    public static double tiempoIzq;

    @Override
    public void start(Stage primaryStage){ 
        try{
            scene = new Scene(loadFXML("/vista/AplicacionVista"));
            primaryStage.setScene(scene);
            primaryStage.show();           
        }catch(IOException e){
            System.out.println("Hubo un problema "+e.getMessage());
            System.exit(0);
        }mediaPlayerIzq.setOnReady(new Runnable(){
            @Override
            public void run() {
                tiempoIzq = mRaya.getDuration().toMillis();
            }
        });
        mediaPlayerDer.setOnReady(new Runnable(){
            @Override
            public void run() {
                tiempoDer = mPunto.getDuration().toMillis();
            }
        });
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(App.class.getResource(fxml+".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    
    /**
     * Método que carga un Mapa con un conjunto de claves alfanuméricas y un
     * conjunto de valores que son códigos morse de la clave.
     * @param path Recibe como parámetro la ubicación del archivo.
     * @return Devuelve un mapa que representa el codigo morse de valores
     * alfanuméricos.
     */
    private static HashMap<String, List<String>> cargarMapaMorse(String path){
        HashMap<String, List<String>> respuesta = new HashMap<>();        
        try(Scanner sc = new Scanner(new File(path))){
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String[] valores = line.split("\\|");  
                List<String> listaValores = new LinkedList<>();
                for(int i=1; i<valores.length; i++){
                    listaValores.add(valores[i]);
                }
                respuesta.put(valores[0], listaValores);
            }
        }catch(Exception e){
            System.out.println("El archivo no se puede abrir error: "+e.getMessage());
        }
        return respuesta;
    }
    
    private static Media cargarMedia(String path){
        return new Media(App.class.getResource(path).toString());
        //Media mPunto = new Media(App.getClass().getResource("/recursos/Punto.mpeg").toString());
    }
}