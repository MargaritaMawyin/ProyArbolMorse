package proyectoMorse.colecciones;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author Margarita Mawyin
 */
public class BinaryTree <E>{

    private Node<E> root;
    private Comparator<E> f;
    
    private static class Node<E> {

        private E data;
        private Node<E> left;
        private Node<E> right;

        public Node(E data) {
            this.data = data;
        }

    }

    public BinaryTree(Comparator<E> f) {
        this.f = f;
    }
public boolean add(E child,E parent){
        Node<E> nChild = new Node<>(child);
        if(isEmpty() && parent ==null){
            root =nChild;
            return true;
        }
    Node<E> np = searchNode(parent);
    Node<E> nChildExist = searchNode(child);
    if(nChildExist==null && np!= null){
        if(np.left == null){
            np.left=nChild;
            return true;
        }
        else if (np.right==null){
            np.right=nChild;
            return true;
        }
    }
    return false;    
    }
public String codificarArbolMorse(List<String> lista, BinaryTree<String> arbol){
    StringBuilder sb = new StringBuilder();
    Node<String> p = arbol.root;
    for(String cod: lista){
        if(p.data.endsWith(" ")){
            if(cod.equals("-"))
                p= p.left;
            else
                p=p.right;
        }
        if(!p.data.equals(" ")){
            sb.append(p.data);
            p = arbol.root;
        }
    }
    return sb.toString();
}
public  BinaryTree<String> crearArbolMorse(HashMap<String,List<String>> mapa){
        BinaryTree<String> arbol= new BinaryTree<>(String::compareTo);
        arbol.root =new Node<>(" ");     
        
        for(Entry<String,List<String>> e: mapa.entrySet()){
            Node<String> p= arbol.root;
            for(String cod: e.getValue())
                if(cod.equals("-")){
                    if(p.left==null)
                        p.left =new Node<>(" ");
                    p= p.left;
                }
                else{
                    if(p.right==null)
                        p.right = new Node<>(" ");
                    p=p.right;
                 }          
            p.data=e.getKey();
        }
      return arbol;  
        
}


 private Node<E> searchNode(E data){
        return searchNode(data, root);
    }
    
    private Node<E> searchNode(E data, Node<E> p){
        if(p==null) return p; // caso base cuando me dieron nulo
        else if(p.data.equals(data)) return p; //caso base cuando encontre el nodo de la data
        
        else{
            Node<E> rl= searchNode(data,p.left);
            if(rl!=null) return rl;
            return searchNode(data, p.right);
            
//        return (rl!=null)?rl:rr; // ternaria , retorna o rl o rr
    }}
private boolean isFull(Node<E> n){
        if(n==null)
            return true;
        else if((n.left!= null && n.right==null) || (n.left==null && n.right!=null))
            return false;
        return height(n.left)== height(n.right) && isFull(n.left)&& isFull(n.right);
        
    }
    public int height(){
        return height(root);
    }
    
    private int height(Node<E> n){
        if(n==null)
            return 0;
        return 1 +  Math.max(height(n.left), height(n.right));
        
    }
    
    public boolean isEmpty(){
        return root== null;
    }
    public void anchura(){
        if(!isEmpty()){
            Queue<Node<E>> cola= new LinkedList<>();
            cola.offer(root);
            while(!cola.isEmpty()){
                Node<E> n = cola.poll();
                System.out.println(n.data);
                if(n.left!=null)cola.offer(n.left);
                if(n.right!=null)cola.offer(n.right);
                    
                
                
            }
        }
        System.out.println("");
    }    
    
    @Override
    public boolean equals(Object otroArbol) {
        if (otroArbol == null) {
            return false;
        }
        if (otroArbol == this) {
            return true;
        }
        if (otroArbol.getClass() != this.getClass()) {
            return false;
        }

        BinaryTree<E> otro = (BinaryTree<E>) otroArbol;
        return equals(root, otro.root);

    }

    private boolean equals(Node<E> parent1, Node<E> parent2) {
        if (parent1 == null && parent2 == null) {
            return true;
        }

        if (parent1 == null || parent2 == null || !parent1.data.equals(parent2.data)) {
            return false;
        }

        return equals(parent1.left, parent2.left) && equals(parent1.right, parent2.right);

    }
    
}
