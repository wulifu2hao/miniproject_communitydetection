import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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

    public HashMap<Integer, Node> getNodeList() {
        return nodeList;
    }
    
    public Set<Edge> getEdgeList() {
        return edgeList;
    }
}
