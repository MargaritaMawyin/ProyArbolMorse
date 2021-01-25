package proyectoMorse.modelo;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.Objects;

/**
 * 
 * @author KevinChevez
 * @param <E> Recibe como parámetro una variable parametrizada, indicando así
 * que es apta para cualquier tipo de variable.
 */
public class BinaryTree<E> {
    private Nodo<E> root;
    
    /**
     * Nodo queda vacio
     */
    public BinaryTree(){
    }    
    
    /**
     * Metodo que verifica si el arbol está vacio
     * @return true si el arbol está vacio y false, si no lo está
     */
    public boolean isEmpty(){
        return root == null;
    }
    
    public boolean add(E child, E parent){
        Nodo<E> nchild = new Nodo<>(child);
        if(isEmpty() && parent == null){
            root = nchild;
            return true;
        }
        //add(B,A)
        Nodo<E> nParent = searchNode(parent);
        Nodo<E> nChildExist = searchNode(child);
        if(nChildExist == null && nParent != null){
            if(nParent.getLeft() == null){
                nParent.setLeft(nchild); 
                return true;
            }
            else if(nParent.getRight() == null){
                nParent.setRight(nchild);
                return true;
            }
        }
        return false;
    }     
    
    public boolean remove(E data){
        Nodo<E> parent = searchParent(data);
        if(parent == null){
            return false;
        }
        if(parent.getLeft()!=null && parent.getLeft().getData().equals(data)){
            parent.setLeft(null);
        }
        else{
            parent.setRight(null);
        }
        return true;
    }
    private Nodo<E> searchParent(E data){
        return searchParent(data, root);
    }
    private Nodo<E> searchParent(E data, Nodo<E> n){
        if(n == null){
            return n;
        }
        else if((n.getLeft()!=null && n.getLeft().getData().equals(data)) || (n.getRight()!= null && n.getRight().getData().equals(data))){
            //System.out.println("Devuelve el arbol p: "+n.data);
            return n;
        }
        else{ //hay que seguir descendiendo
            Nodo<E> restultLeft = searchParent(data, n.getLeft());
            if(restultLeft!=null) return restultLeft;
            return searchParent(data, n.getRight());
        }
    }
    
    private Nodo<E> searchNode(E data){
        return searchNode(data, root);
    }
    
    /**
     * Funcion que ayuda a la recursividad y envia Nodo que irá cambiando por etapa
     * @param data Recibe la data a buscar
     * @param p Recibe el nodo a buscar en seccion
     * @return Devuelve el nodo que encontró y null si no encontró
     */
    private Nodo<E> searchNode(E data, Nodo<E> p){
        //Caso base 1
        if(p==null){
            return null; //tambien puede retornar p
        }
        //Caso base 2
        else if(p.getData().equals(data)){
            return p; // devuelve el nodo investigado
        }
        //Caso recursivo
        else{
            Nodo<E> restultLeft = searchNode(data, p.getLeft());
            if(restultLeft!=null) return restultLeft;
            return searchNode(data, p.getRight());
        }
    }
    
    public boolean add(E parent, E data, boolean insertIzq){
        Nodo<E> hijo = new Nodo<>(data);
        if(parent == null && isEmpty()){
            root = hijo;
            return true;
        }
        Nodo<E> nParent = searchNodo(parent);
        if(nParent==null){
            return false;
        }
        else if(nParent.isComplete()){
            return false;
        }        
        else if(nParent.isHoja()){
            if(insertIzq)
                nParent.setLeft(hijo);
            else
                nParent.setRight(hijo);
        }
        else{
            if(insertIzq){
                Nodo<E> n = (nParent.getLeft()==null)? hijo:nParent.getLeft();
                nParent.setLeft(n);
            }
            else{
                Nodo<E> n = (nParent.getRight()==null)? hijo:nParent.getRight();
                nParent.setRight(n);
            }
                
        }
        return true;
    }
    
    public Nodo<E> searchNodo(E data){
        return (data!=null)? searchNodo(data, root) : null;
    }
    private Nodo<E> searchNodo(E data, Nodo<E> n){
        if(n == null){
            return null;
        }
        else if(n.getData().equals(data) && !n.isComplete()){
            return n;
        }
        else{
            Nodo<E> izqResult = searchNodo(data, n.getLeft());
            return (izqResult!=null)? izqResult : searchNodo(data, n.getRight());
        }
    }
    
    public void anchura(){
        if(!isEmpty()){
            Queue<Nodo<E>> cola = new LinkedList<>();
            cola.offer(root);
            while(!cola.isEmpty()){
                Nodo<E> n = cola.poll();
                System.out.print(n.getData()+" ");
                if(n.getLeft()!=null) cola.offer(n.getLeft());
                if(n.getRight()!=null) cola.offer(n.getRight());
            }
        }
        System.out.println("");
    }
    
  
    
    public int size(){
        return size(root);
    }
    private int size(Nodo<E> n){
        if(n == null){
            return 0;
        }
        return 1+size(n.getLeft())+size(n.getRight());
    }
    
    public int height(){
        return height(root);
    }
    private int height(Nodo<E> n){
        if(n == null){
            return 0;
        }
        return 1 + Math.max(height(n.getLeft()), height(n.getRight()));
    }
    
    
    
    public boolean isFull(){
        return isFull(root);
    }
    private boolean isFull(Nodo<E> n){
        if(n == null){
            return true;
        }
        if((n.getLeft() == null && n.getRight() ==null) ){
            return false;
        }
        return isFull(n.getLeft()) && isFull(n.getRight()) && height(n.getLeft())== height(n.getRight());
    }
    
    public boolean addMorse(E child, List<String> direcciones){
        this.root = (this.root == null)? (Nodo<E>)(new Nodo<>("INICIO")) : this.root;
        Iterator<String> it = direcciones.iterator();
        if(child!=null ){
            addMorse(child, it, root);
            return true;
        }
        return false;    
    }
    private Nodo<E> addMorse(E child, Iterator<String> it, Nodo<E> parent){
        Nodo<E> nodoHijo = new Nodo<>(child);        
        Nodo<String> nodoVacio =  new Nodo<>("*");
        String direccion = (it.hasNext())? it.next() : null;
        if((direccion!=null)&& direccion.equals("-")){
            if(parent.getLeft()==null){                
                Nodo<E> nodo = (it.hasNext())? addMorse(child, it, (Nodo<E>)nodoVacio) : nodoHijo;
                parent.setLeft(nodo);
            }
            else{
                nodoHijo.setRight(parent.getLeft().getRight());
                nodoHijo.setLeft(parent.getLeft().getLeft());
                Nodo<E> nodo =(it.hasNext())? addMorse(child, it, parent.getLeft()) : nodoHijo;
                parent.setLeft(nodo);
            }
        }
        else if((direccion!=null)&& direccion.equals(".")){
            if(parent.getRight()==null){
                Nodo<E> nodo = (it.hasNext())? addMorse(child, it, (Nodo<E>)nodoVacio) : nodoHijo;
                parent.setRight(nodo);
            }
            else{
                nodoHijo.setRight(parent.getRight().getRight());
                nodoHijo.setLeft(parent.getRight().getLeft());
                Nodo<E> nodo = (it.hasNext())? addMorse(child, it, parent.getRight()) : nodoHijo;
                parent.setRight(nodo);
            }
        }                       
        return parent;
    }
    
    public String codifyMorse(List<String> codigos){
        Iterator<String> it = codigos.iterator();
        return codifyMorse(it, root);
    }
    private String codifyMorse(Iterator<String> it, Nodo<E> parent){
        StringBuilder resp = new StringBuilder();        
        if(parent != null){
            if(parent.isHoja()){
                resp.append(parent.getData());
                resp.append(codifyMorse(it, root));
            }
            String code = (it.hasNext())? it.next() : null;
            if(code!=null && code.equals("-")){
                resp.append(codifyMorse(it, parent.getLeft()));
            }
            else if(code!=null && code.equals(".")){
                resp.append(codifyMorse(it, parent.getRight()));
            }
        }
        else if(it.hasNext()){
            resp.append(codifyMorse(it, root));
        }
        return resp.toString();
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.root);
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
        final BinaryTree<E> other = (BinaryTree<E>) obj;        
        return (this.height() == other.height())? equals(this.root, other.root) : false;
    }
    private boolean equals(Nodo<E> thisParent, Nodo<E> otherParent){        
        if(thisParent == null && otherParent==null){
            return true;
        }
        if(thisParent != null && otherParent!=null){
            return thisParent.getData().equals(otherParent.getData()) &&
                    equals(thisParent.getLeft(), otherParent.getLeft()) &&
                    equals(thisParent.getRight(), otherParent.getRight());
        }
        return false;
    }
    
    public Nodo<E> getRoot(){
        return root;
    }
    
}
