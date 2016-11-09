package GirmanNewmanClustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

class ShortestPaths {
	public int numOfShortestPaths;
	public int distanceOfShortestPath;
	public boolean finalized;
	
	public ShortestPaths(){
		numOfShortestPaths = 1;
		distanceOfShortestPath = 0;
		finalized = false;
	}
}

public class EdgeBetweenness {

	// this method takes in a graph and return the edge with the highest betweenness
	public static Edge findHighestEdge(Graph graph){		
		//   1. for every node, do a BFS to find its number and distance of shortest path to all other nodes
		//		this should take O(nm)		
		//	 2. for every edge, compute its betweenness from every pair of nodes' shortest path	
		//		this should take O(mn^2)
		
		HashMap<Integer, Node> nodeList = graph.getNodeList();
		HashMap<Integer, HashMap<Integer, ShortestPaths>> shortestPathMap = new HashMap<Integer, HashMap<Integer, ShortestPaths>>(); 
		for (Map.Entry<Integer, Node> entry : nodeList.entrySet()) {
			
			Integer nodeIdx = entry.getKey();
			
			HashMap<Integer, ShortestPaths> shortestPathMapForThisNode = new HashMap<Integer, ShortestPaths>();			
			shortestPathMap.put(nodeIdx, shortestPathMapForThisNode);
			
			// set ShortestPaths to self
			ShortestPaths shortestPathsToItself = new ShortestPaths();
			shortestPathsToItself.finalized = true;
			shortestPathMapForThisNode.put(nodeIdx, shortestPathsToItself);
			
			ArrayList<Integer> frontier = new ArrayList<Integer>();
			frontier.add(nodeIdx);
			while (frontier.size() > 0){
				ArrayList<Integer> newFrontier = new ArrayList<Integer>();
				
				//expand all nodes in the frontier				
				for(int i=0; i<frontier.size(); i++){
					Integer curNodeIdx = frontier.get(i);
					ShortestPaths shortestPathsToCurNode = shortestPathMapForThisNode.get(curNodeIdx);
					
					HashSet<Integer> neighbours = nodeList.get(curNodeIdx).getNeighbours();
					for (Integer neighbourIdx : neighbours) {
						if (!shortestPathMapForThisNode.containsKey(neighbourIdx)){
							//if a neighbour's shortest paths has not exist, we will create a new entry and add it to next frontier
							ShortestPaths shortestPathsToNeighbour = new ShortestPaths();
							shortestPathsToNeighbour.numOfShortestPaths = shortestPathsToCurNode.numOfShortestPaths;
							shortestPathsToNeighbour.distanceOfShortestPath = shortestPathsToCurNode.distanceOfShortestPath + 1;
							shortestPathMapForThisNode.put(neighbourIdx, shortestPathsToNeighbour);
							
							//add this newly discovered node to new frontier							
							newFrontier.add(neighbourIdx);
							
						} else {
							ShortestPaths shortestPathsToNeighbour = shortestPathMapForThisNode.get(neighbourIdx);
							if (shortestPathsToNeighbour.finalized){
								//we have visited this node in the previous frontier, there is nothing interesting about this node
								continue;
							}else{
								//this node has been visited by the current frontier, but now we found other shortest paths with the same distance
								shortestPathsToNeighbour.numOfShortestPaths += shortestPathsToCurNode.numOfShortestPaths;;
							}							
						}
					}
					
					//finalize all shortest paths for the nodes in the new frontier					
					for(int j=0; j<newFrontier.size();j++){
						Integer newNodeIdx = newFrontier.get(j);
						shortestPathMapForThisNode.get(newNodeIdx).finalized = true;
					}
					
					frontier = newFrontier;
				}
			}
						
		}
		
		
		return null;
	}
	
	// this method takes in a graph and return the edge with the highest betweenness by random sampling
	public static Edge findHighestEdgeRandom(Graph graph, float sampleRate){		
		//   1. randomly sample a set of nodes according to the sample rate
		//		for each node in the sample, do a BFS to find its number and distance of shortest path to all other nodes
		//		this should take O(sm)		
		//	 2. for every edge, compute its betweenness from every pair of nodes' in the sample
		//		this should take O(ms^2)
		
		return null;
	}
	
	private static void testFindHighestEdge(){
		System.out.println("succeed");
		Graph testGraph = new Graph();
		for(int i=0; i<5; i++){
			Node node = new Node(i);
			for (int j=0; j<5; j++){
				if(i == j){
					continue;
				}
				node.addNeighbours(j);
			}
			if (i==4){
				node.addNeighbours(5);
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
			}
			if (i==5){
				node.addNeighbours(4);
			}
			
			testGraph.addNode(node);
		}
		
		EdgeBetweenness.findHighestEdge(testGraph);		
		
	}
	
	public static void main(String[] args){	
		testFindHighestEdge();
	}
}
