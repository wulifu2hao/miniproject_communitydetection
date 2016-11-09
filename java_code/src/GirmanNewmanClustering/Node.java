package GirmanNewmanClustering;
import java.util.HashSet;
import java.util.Set;

//try to finalize the variable because it is a static graph.
public class Node{
	private int id;
    private HashSet<Integer> neighbours;	// An array of its neighbours ; An array of edges connected with this node. 
    
    /*
     * create a node object
     */
    public Node(int id){
    	this.id=id;
    	this.neighbours=new HashSet<Integer>();
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
    
    public void removeNeighbour(int neighbour){
    	if (neighbours.contains(neighbour)){
    		neighbours.remove(neighbour);
    	}
    }
}
