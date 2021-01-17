/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package proyectoMorse.modelo;

import java.util.Objects;
import javafx.application.Platform;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import proyectoMorse.controlador.App;
import static proyectoMorse.controlador.App.mPunto;
import static proyectoMorse.controlador.App.mRaya;
import static proyectoMorse.controlador.App.mediaPlayerDer;
import static proyectoMorse.controlador.App.mediaPlayerIzq;
import static proyectoMorse.controlador.App.tiempoDer;
import static proyectoMorse.controlador.App.tiempoIzq;

/**
 * 
 * @author KevinChevez
 */
public class Circulo extends Circle implements Runnable {
    private Line lineaIzq;
    private Line lineaDer;
    private String mensaje;
    private Text contenido;
    private double x, y, radio, ancho;
    private boolean izqCircle;
    
    
    public Circulo(String contenido, double x, double y, double radio, double ancho) {
        super(x+12, y+18, radio);
        this.mensaje = contenido;
        this.contenido = new Text(x+11-(contenido.length()*2), y+20, contenido);
        this.x=x; this.y=y; this.radio=radio; this.ancho=ancho;
    }

    public Text getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = new Text(x+11-(contenido.length()*2), y+20, contenido);
    }

    public Line getLineaIzq() {
        return lineaIzq;
    }

    public void setLineaIzq(double extra, double extraAlt) {        
        lineaIzq = new Line(x+radio, y+radio, x-ancho-extra+radio, y+radio+extraAlt);                        
    }

    public Line getLineaDer() {
        return lineaDer;
    }

    public void setLineaDer(double extra, double extraAlt) {
        this.lineaDer = new Line(x+radio, y+radio, x+ancho+extra+radio, y+radio+extraAlt);  
    }    

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isIzqCircle() {
        return izqCircle;
    }

    public void setIzqCircle(boolean izqCicle) {
        this.izqCircle = izqCicle;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.mensaje);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Circulo other = (Circulo) obj;
        if (!Objects.equals(this.mensaje, other.mensaje)) {
            return false;
        }
        return true;
    }            
    
    @Override
    public void run() {
        Platform.runLater(() -> { 
            //this.setFill(Color.RED);
            if(isIzqCircle()){
                if(lineaIzq!=null) lineaIzq.setFill(Color.RED);
                System.out.println("Suena izq");
                //reproducirNotaIzq();     
                mediaPlayerIzq = new MediaPlayer(mRaya);
                mediaPlayerIzq.setOnReady(new Runnable(){
                    @Override
                    public void run() {
                        mediaPlayerIzq.play();
                    }
                });
                setFill(Color.GREEN);
                try {
                    Thread.sleep((long)tiempoIzq);
                }catch(InterruptedException ex) {
                    System.out.println("Error en carga de tiempo song");
                }                                   
            }else{
                if(lineaDer!=null) lineaDer.setFill(Color.RED);
                System.out.println("Suena Der");
                //reproducirNotaDer();
                mediaPlayerDer = new MediaPlayer(mPunto);
                mediaPlayerDer.setOnReady(new Runnable(){
                    @Override
                    public void run() {
                        mediaPlayerDer.play();
                    }
                });
                setFill(Color.BLUE);
                try {
                    Thread.sleep((long)App.tiempoDer);
                }catch(InterruptedException ex) {
                    System.out.println("Error en carga de tiempo song");
                }                 
            }
        });
    }
    
    
}
