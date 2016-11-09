package GirmanNewmanClustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
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
    	// TODO: shall we remove the edge from the Node object's property as well?    	
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
    /*
     * return the number of connected components
     * 
     */
    public int getNumOfConnectedComponents(){
    	return this.getConnectedComponents().size();
    }
    
    /*
     * return the connected components of the graph
     * this is used after the community detection algorithm has finished
     * and we want to output the result 
     * 
     */
    public ArrayList<Set<Integer>> getConnectedComponents(){
    	Set<Integer> visited = new HashSet<Integer>();
    	ArrayList<Set<Integer>> result = new ArrayList<Set<Integer>>();
    	
    	for (Map.Entry<Integer, Node> entry : nodeList.entrySet()) {
    		Integer nodeIdx = entry.getKey();
    		if (visited.contains(nodeIdx)){
    			//we have visited this node before, no point visiting it again    			
    			continue;
    		}    		
    		Set<Integer> newComponent = new HashSet<Integer>();
    		visited.add(nodeIdx);
    		newComponent.add(nodeIdx);
    		
    		Stack<Integer> st = new Stack<Integer>();
    		st.push(nodeIdx);
    		while(!st.empty()){
    			Integer nodeToVisit = st.pop();
    			
    			HashSet<Integer> neighbours = nodeList.get(nodeToVisit).getNeighbours();
    			for(Integer neighbour: neighbours){
    				if (visited.contains(neighbour)){
    	    			//this node has been visited or at least pushed to the stack, we shall skip it     			
    	    			continue;
    	    		}    	
    				
    				st.push(neighbour);
    				visited.add(neighbour);
    				newComponent.add(neighbour);
    			}
    		}  
    		
    		result.add(newComponent);
    	}
    	
    	return result;
    }
    
    private static void testGetNumOfConnectedComponents(){
		Graph testGraph = new Graph();
		for(int i=0; i<5; i++){
			Node node = new Node(i);
			for (int j=0; j<5; j++){
				if(i == j){
					continue;
				}
				node.addNeighbours(j);
				
				Edge edge = new Edge(i, j);
				testGraph.addEdge(edge);
			}
			
			testGraph.addNode(node);
		}
		for(int i=5; i<10; i++){
			Node node = new Node(i);
			for (int j=5; j<10; j++){
				if(i == j){
					continue;
				}
				node.addNeighbours(j);
				
				Edge edge = new Edge(i, j);
				testGraph.addEdge(edge);
			}
			
			testGraph.addNode(node);
		}
		
		ArrayList<Set<Integer>> components = testGraph.getConnectedComponents(); 
		System.out.println("num of connected components of test grpah is "+components.size());
		for(int i=0; i<components.size();i++){
			Set<Integer> component = components.get(i);
			String nodeIndexesStr = "";
			for(Integer nodeIdx:component){
				nodeIndexesStr += (nodeIdx+" "); 
			}
			System.out.println("component "+i+" :"+nodeIndexesStr);
		}
    }
    
    public static void main(String[] args){	
    	testGetNumOfConnectedComponents();
	}
}
