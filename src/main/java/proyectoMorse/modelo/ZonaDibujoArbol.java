/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package proyectoMorse.modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import proyectoMorse.controlador.App;

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
        this.getChildren().clear(); dibujarArbol();
        List<String> listText = Arrays.asList(textArray);
        Iterator<String> palabraIte = listText.iterator();
        while(palabraIte.hasNext()){
            String palabra = palabraIte.next();
            List<String> letraList = new ArrayList<>();
            for(int i=0; i<palabra.length(); i++){
                letraList.add(palabra.charAt(i)+"");
            }
            Iterator<String> letraIte = letraList.iterator();
            iterarLetras(letraIte, zonaDibujo);
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
        if(it!=null && it.hasNext()){
            try{
                String codigo = it.next();
                if(codigo.equals("-")){
                    String data = parent.getLeft().getData();
                    Circulo circuloModificar = obtenerCirculo(data, this);
                    circuloModificar.setIzqCircle(true);
                    circuloModificar.setLastCircle(!it.hasNext());
                    Thread t1 = new Thread(circuloModificar);
                    t1.start();
                    if(it.hasNext()){
                        escucharLetra(it, parent.getLeft(), pane);
                    }                    
                }
                else{
                    String data = parent.getRight().getData();
                    Circulo circuloModificar = obtenerCirculo(data, this);
                    circuloModificar.setIzqCircle(false);
                    circuloModificar.setLastCircle(!it.hasNext());
                    Thread t1 = new Thread(circuloModificar);
                    t1.start();
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
    
    private void iterarLetras(Iterator<String> letraIte, Pane zonaDibujo){
        String letra = letraIte.next();
        List<String> listaMorse = App.mapaMorse.get(letra);
        ListIterator<String> it = (listaMorse!=null)?listaMorse.listIterator():null;
        escucharLetra(it, arbolBinario.getRoot(), zonaDibujo);
        if(listaMorse!=null){
//            try {
//                Thread.sleep((long)contarTiempo(listaMorse));
//            } catch (InterruptedException ex) {
//                System.out.println("Error en iterar letras: "+ex.getMessage());
//            }           
        }
        if(letraIte.hasNext()) iterarLetras(letraIte, zonaDibujo);
    }
}
