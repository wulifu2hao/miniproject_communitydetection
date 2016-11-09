package GirmanNewmanClustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DijkstraAlgorithm {

    private Node source;
    private Graph graph;
    private Set<Node> settledNodes;
    private Set<Node> unSettledNodes;
    private Map<Node, Node> predecessors;
    private Map<Node, Integer> distance;

    public DijkstraAlgorithm(Graph graph,Node source){
            // create a copy of the array so that we can operate on this array
            this.graph=graph;
            this.source=source;
    }

    public void execute(Node source) {
            Set<Node> settledNodes = new HashSet<Node>();
            Set<Node> unSettledNodes = new HashSet<Node>();
            HashMap<Node,double> distance = new HashMap<Node, double>();
            //predecessors = new HashMap<Node, Node>();
            distance.put(source, 0.0);
            for(Node n:graph.getNodeList().values()){
            	distance.put(n,Double.POSITIVE_INFINITY);
            }
            unSettledNodes.add(source);
            while (unSettledNodes.size() > 0) {
                    Node currentNode = getMinimum(unSettledNodes);
                    settledNodes.add(currentNode);
                    unSettledNodes.remove(currentNode);
                    findMinimalDistances(currentNode);
            }
    }

    private void findMinimalDistances(Node node) {
            List<Node> adjacentNodes = getNeighbors(node);
            for (Node target : adjacentNodes) {
                    if (getShortestDistance(target) > getShortestDistance(node)
                                    + getDistance(node, target)) {
                            distance.put(target, getShortestDistance(node)
                                            + getDistance(node, target));
                            predecessors.put(target, node);
                            unSettledNodes.add(target);
                    }
            }

    }

    
    private Node getMinimum(Set<Node> Nodes) {
        double minimum =Double.POSITIVE_INFINITY;
        Node minimalNode=null;
        for (Node node : Nodes) {
                if(distance.get(node)<minimum) {
                	minimum=distance.get(node);
                	minimalNode=node;
                }
        }
        return minimalNode;
}
    
    
public LinkedList<Node> getPath(Node target) {
    LinkedList<Node> path = new LinkedList<Node>();
    Node step = target;
    // check if a path exists
    if (predecessors.get(step) == null) {
            return null;
    }
    path.add(step);
    while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
    }
    // Put it into the correct order
    Collections.reverse(path);
    return path;
}

}

