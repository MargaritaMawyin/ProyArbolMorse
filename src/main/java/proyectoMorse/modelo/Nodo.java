package proyectoMorse.modelo;

/**
 * 
 * @author KevinChevez
 */
public class Nodo<E>{
    private E data;
    private Nodo<E> right;
    private Nodo<E> left;

    public Nodo(E data){
        this.data = data;
    }

    public boolean isHoja(){
        return (right == null && left== null);
    }

    public boolean isComplete(){
        return (left!=null && right!=null);
    }

    public E getData() {
        return data;
    }

    public Nodo<E> getRight() {
        return right;
    }

    public Nodo<E> getLeft() {
        return left;
    }

    public void setData(E data) {
        this.data = data;
    }

    public void setRight(Nodo<E> right) {
        this.right = right;
    }

    public void setLeft(Nodo<E> left) {
        this.left = left;
    }
/**
 * Cuenta el numero de nodos padres
 * @param parent es la raiz del arbol
 * @return ??????????
 */
    public int numNodesCompletes(Nodo<E> parent){
        if(parent==null)
            return 0;
        else{
            if(parent.isComplete())
                return 1 + numNodesCompletes(parent.getLeft()) + 
                        numNodesCompletes(parent.getRight());
            return numNodesCompletes(parent.getLeft()) + 
                    numNodesCompletes(parent.getRight());
        }
    }
    
    public int height(Nodo<E> n){
        if(n == null){
            return 0;
        }
        return 1 + Math.max(height(n.getLeft()), height(n.getRight()));
    }
}
