import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Node{
	// An array of edges connected with this node.   
    private HashSet<Edge> edges;
    private Integer Id;
    private int id = -1;
    public Set<Edge> getEdges() {
        return edges;
    }
    public int getId() {
        return id;
    }
    
    public void addEdge(final Edge e){
        //if (!e.isOutgoingEdge(this) && !e.isIncomingEdge(this))  exception;
        edges.add(e);
    }
}
