/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package proyectoMorse.modelo;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import proyectoMorse.constantes.Constantes;
import proyectoMorse.controlador.App;
import proyectoMorse.modelo.BinaryTreeKevin;

/**
 * 
 * @author KevinChevez
 */
public class ZonaDibujoArbol extends Pane {
    private BinaryTreeKevin arbolBinario;
    public static double CENTRO;
    public static final double RADIO = 32/2;
    public static final double ANCHO = 10;

    public ZonaDibujoArbol(BinaryTreeKevin arbolBinario, double x) {
        this.arbolBinario = arbolBinario;      
        this.CENTRO = x/2;
    }

    public ZonaDibujoArbol(BinaryTreeKevin arbolBinario, double x, Node... arg0) {
        super(arg0);
        this.arbolBinario = arbolBinario;
        this.CENTRO = x;
    }

    public void setArbolBinario(BinaryTreeKevin arbolBinario) {
        this.arbolBinario = arbolBinario;        
    }
    
    
    public ZonaDibujoArbol dibujarArbol(){
        return dibujar(this, CENTRO-RADIO, 20, arbolBinario.getRoot(), 0);
    }
    private ZonaDibujoArbol dibujar(Pane p, double x, double y, Nodo<String> parent, int nivel){
        if(parent!=null){            
            double EXTRA = parent.numNodesCompletes(parent)*(ANCHO);
            double EXTRA_ALT = ANCHO*(nivel+2);
            Circulo circulo = new Circulo(parent.getData(), x, y, RADIO, ANCHO);
            circulo.setFill(Color.CYAN);
            circulo.setStroke(Color.BLACK);
            if(parent.getLeft()!=null){
                circulo.setLineaIzq(EXTRA, EXTRA_ALT);      
                if(circulo.getLineaIzq()!=null) p.getChildren().add(circulo.getLineaIzq());
            }
            if(parent.getRight()!=null){
                circulo.setLineaDer(EXTRA, EXTRA_ALT);   
                if(circulo.getLineaDer()!=null) p.getChildren().add(circulo.getLineaDer());
            }
            p.getChildren().addAll(circulo, circulo.getContenido());
            
            dibujar(p, x-ANCHO-EXTRA, y+EXTRA_ALT, parent.getLeft(), ++nivel); 
            nivel--;
            dibujar(p, x+ANCHO+EXTRA, y+EXTRA_ALT, parent.getRight(), ++nivel);
        }
        return (ZonaDibujoArbol)p;
    }
    
    public void escuchar(String texto, Pane zonaDibujo){
        String[] textArray = texto.trim().split(" ");
        this.getChildren().clear();
        ZonaDibujoArbol zonaLimpia = dibujarArbol();
        for(int j=0; j<textArray.length; j++){
            String palabra = textArray[j];
            for(int i=0; i<palabra.length(); i++){
                String letra = palabra.charAt(i)+"";
                List<String> listaMorse = App.mapaMorse.get(letra);
                ListIterator<String> it = (listaMorse!=null)?listaMorse.listIterator():null;
                if(listaMorse!=null){
                    try {
                        Thread.sleep((long)contarTiempo(listaMorse));
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                escucharLetra(it, arbolBinario.getRoot(), zonaDibujo);
            }
        }       
    }
    
    public Circulo obtenerCirculo(String data, ZonaDibujoArbol zonaDibujoArbol){
        Circulo circuloBuscar = new Circulo(data, CENTRO-RADIO, 20, RADIO, ANCHO);
        for(Node n : zonaDibujoArbol.getChildren()){
            if(n instanceof Circulo && n.equals(circuloBuscar)){
                return (Circulo)n;
            }
        }
        return null;
    }
    
    private void escucharLetra(ListIterator<String> it, Nodo<String> parent, Pane pane){        
//        String pathPunto = this.getClass().getResource("/recursos/Punto.mpeg").toString();
//        String pathRaya = this.getClass().getResource("/recursos/Raya.mpeg").toString();
        if(it!=null && it.hasNext()){
            try{
                String codigo = it.next();
                if(codigo.equals("-")){
                    String data = parent.getLeft().getData();
                    Circulo circuloModificar = obtenerCirculo(data, this);
                    circuloModificar.setIzqCircle(true);
                    Thread t1 = new Thread(circuloModificar);
                    //t1.start();
                    t1.run();
                    if(it.hasNext()){
                        escucharLetra(it, parent.getLeft(), pane);
                    }                    
                }
                else{
                    String data = parent.getRight().getData();
                    Circulo circuloModificar = obtenerCirculo(data, this);
                    circuloModificar.setIzqCircle(false);
                    Thread t1 = new Thread(circuloModificar);
                    //t1.start();
                    t1.run();
                    if(it.hasNext()){
                        escucharLetra(it, parent.getRight(), pane);
                    }
                }

            }catch(Exception e){
                System.out.println("Error en los audios: "+e.getMessage());
            }
        }else{
            System.out.println("NO SE ENCONTRO LETRA, AGREGAR UNA ALERTA EN ZonaDibujoArbol, Linea 145");
        }
    }
    
    public double contarTiempo(List<String> listaMorse){
        double tiempo = 0;
        for(String code : listaMorse){
            if(code.equals("-")){
                tiempo+=App.tiempoIzq;
            }
            else{
                tiempo+=App.tiempoDer;
            }
        }
        return tiempo;
    }
}
