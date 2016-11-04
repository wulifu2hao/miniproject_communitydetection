package GirmanNewmanClustering;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
//try to finalize the variable because it is a static graph.
public class Graph {
    private Set<Edge> edgeList;
    private HashMap<Integer,Node> nodeList;

    public Graph() {    
        edgeList = new HashSet<Edge>();
        nodeList = new HashMap<Integer, Node>();
    }

    public void addEdge(final Edge edge) {

        edgeList.add(edge);
    }
    public void addNode(final Node node) {
        nodeList.put(node.getId(), node);
    }
    public void deleteEdge(Edge edge){
    	edgeList.remove(edge);
    }
    public HashMap<Integer, Node> getNodeList() {
        return nodeList;
    }
    
    public Set<Edge> getEdgeList() {
        return edgeList;
    }
    /*
     * return info about the number of edges
     */
    public int getNumberOfEdge(){
    	return edgeList.size();
    }
    /*
     * return info about the number of nodes
     */
    public int getNumberOfNode(){
    	return nodeList.size();
    }
}
