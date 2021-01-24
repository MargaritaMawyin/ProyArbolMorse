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
public class ZonaDibujoArbol extends Pane  {
    private BinaryTree arbolBinario;
    private double centro;
    public static final double RADIO = 16;
    public static final double ANCHO = 10;

    public double getCENTRO() {
        return centro;
    }

    public ZonaDibujoArbol() {
    }

    public ZonaDibujoArbol(BinaryTree arbolBinario, double x) {
        this.arbolBinario = arbolBinario;      
        this.centro = x/2;
    }

    public ZonaDibujoArbol(BinaryTree arbolBinario, double x, Node... arg0) {
        super(arg0);
        this.arbolBinario = arbolBinario;
        this.centro = x;
    }

    public void setArbolBinario(BinaryTree arbolBinario) {
        this.arbolBinario = arbolBinario;        
    }
    
    
    public ZonaDibujoArbol dibujarArbol(){
        return dibujar(this, centro-RADIO, 20, arbolBinario.getRoot(), 0);
    }
    /**
     * Dado los parametros se va creando el arbol morse grafico, se crea el cicrulo con la data
     * para los nodos circulos no se crecen a medida que se van creando circulos hijos, se usan las
     * variables extra y extra alt para que los circulos tomen distancia.
     * @param p
     * @param x
     * @param y
     * @param parent
     * @param nivel
     * @return el arbol binario con los caracteres
     */
    
    private ZonaDibujoArbol dibujar(Pane p, double x, double y, Nodo<String> parent, int nivel){
        if(parent!=null){            
            double extra_ancho = parent.numNodesCompletes(parent)*(ANCHO);
            double extra_altura = ANCHO*(nivel+2);
            Circulo circulo = new Circulo(parent.getData(), x, y, RADIO, ANCHO); //,this
            circulo.setFill(Color.CYAN);
            circulo.setStroke(Color.BLACK);
            if(parent.getLeft()!=null){
                circulo.setLineaIzq(extra_ancho, extra_altura);      
                if(circulo.getLineaIzq()!=null) p.getChildren().add(circulo.getLineaIzq());
            }
            if(parent.getRight()!=null){
                circulo.setLineaDer(extra_ancho, extra_altura);   
                if(circulo.getLineaDer()!=null) p.getChildren().add(circulo.getLineaDer());
            }
            p.getChildren().addAll(circulo, circulo.getContenido());
            
            dibujar(p, x-ANCHO-extra_ancho, y+extra_altura, parent.getLeft(), ++nivel); 
            nivel--;
            dibujar(p, x+ANCHO+extra_ancho, y+extra_altura, parent.getRight(), ++nivel);
        }
        return (ZonaDibujoArbol)p;
    }
    
    public void escuchar(String texto, Pane zonaDibujo){
        String[] textArray = texto.trim().split(" ");
        this.getChildren().clear();dibujarArbol();
        List<String> listText = Arrays.asList(textArray);
        Iterator<String> palabraIte = listText.iterator();
        iterarPalabras(palabraIte, zonaDibujo);
    }
    
    public Circulo obtenerCirculo(String data, ZonaDibujoArbol zonaDibujoArbol){
        Circulo circuloBuscar = new Circulo(data, centro-RADIO, 20, RADIO, ANCHO);//, this
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
            System.out.println("NO SE ENCONTRO LETRA");
        }
    }
    
    public double contarTiempo(List<String> listaMorse){
        double tiempo = 0;
        for(String code : listaMorse){
            if(code.equals("-")){
                tiempo+=App.getTiempoIzq();
            }
            else{
                tiempo+=App.getTiempoDer();
            }
        }
        return tiempo;
    }
    
    private void iterarLetras(Iterator<String> letraIte, Pane zonaDibujo){
        String letra = letraIte.next();
        List<String> listaMorse = App.getMapaMorse().get(letra);
        ListIterator<String> it = (listaMorse!=null)?listaMorse.listIterator():null;
        escucharLetra(it, arbolBinario.getRoot(), zonaDibujo);
        if(listaMorse!=null){
            try {
                Thread.sleep((long)contarTiempo(listaMorse));
            } catch (InterruptedException ex) {
                ex.getLocalizedMessage();
                Thread.currentThread().interrupt();
            }           
        }
        if(letraIte.hasNext()) iterarLetras(letraIte, zonaDibujo);
    }
    
    private void iterarPalabras(Iterator<String> palabraIte, Pane zonaDibujo){
        String palabra = palabraIte.next();
        List<String> letraList = new ArrayList<>();
        for(int i=0; i<palabra.length(); i++){
            letraList.add(palabra.charAt(i)+"");
        }
        Iterator<String> letraIte = letraList.iterator();
        iterarLetras(letraIte, zonaDibujo);
        if(palabraIte.hasNext()) iterarPalabras(palabraIte, zonaDibujo);
    }

   
}
