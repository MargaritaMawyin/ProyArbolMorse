package proyectoMorse.controlador;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * JavaFX App
 */

   
    
    
    
    public class App extends Application {
    public static final String PATH_ARCHIVO_MORSE = "src/main/resources/recursos/codMorse.txt";
    public static final String PATH_AUDIO_PUNTO = "src/main/resources/recursos/Punto.mpeg";
    public static final String PATH_AUDIO_RAYA = "src/main/resources/recursos/Raya.mpeg";
    private static Scene scene;
    private final static HashMap<String, List<String>> mapaMorse = 
            cargarMapaMorse(PATH_ARCHIVO_MORSE);
    
    private static Media mRaya = new Media(App.class.getResource("/recursos/Raya.mpeg").toString());
    private static Media mPunto = new Media(App.class.getResource("/recursos/Punto.mpeg").toString());
    private static MediaPlayer mediaPlayerDer = new MediaPlayer(mPunto);
    private static MediaPlayer mediaPlayerIzq = new MediaPlayer(mRaya);
    private static double tiempoDer;
    private static double tiempoIzq;

    public static HashMap<String, List<String>> getMapaMorse() {
        return mapaMorse;
    }
    
    public static Media getmRaya() {
        return mRaya;
    }

    public static Media getmPunto() {
        return mPunto;
    }

    public static double getTiempoDer() {
        return tiempoDer;
    }

    public static double getTiempoIzq() {
        return tiempoIzq;
    }

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
    
   
}