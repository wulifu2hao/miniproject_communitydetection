package GirmanNewmanClustering;
import java.util.HashSet;
import java.util.Set;

//try to finalize the variable because it is a static graph.
public class Node{
	private int id;
    private HashSet<Integer> neighbours;	// An array of its neighbours ; An array of edges connected with this node. 
    
    /*
     * create a node object with a single neighbour
     */
    public Node(int id,int firstNeighbour){
    	this.id=id;
    	this.neighbours=new HashSet<Integer>();
    	neighbours.add(firstNeighbour);
    }
  
    public HashSet<Integer> getNeighbours() {
        return neighbours;
    }
    
    public int getId() {
        return id;
    } 
    
    /* 
     * add a single neighbour once.
     */
    public void addNeighbours(int newNeighbour){
        neighbours.add(newNeighbour);
    }
}
