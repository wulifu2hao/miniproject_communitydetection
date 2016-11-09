package GirmanNewmanClustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
		
		Set<Edge> edgeList = graph.getEdgeList();
		Edge bestEdge = null;
		float highestBetweeness = -1;
		for(Edge edge:edgeList){
			int sourceIdx = edge.getSourceNodeID();
			int destIdx = edge.getDestinationNodeID();
			
			float betweenessOfThisEdge = 0;
			
			for (Map.Entry<Integer, Node> nodeEntry1 : nodeList.entrySet()) {
				Integer node1Idx = nodeEntry1.getKey();
				HashMap<Integer, ShortestPaths> shortestPathMapForNode1 = shortestPathMap.get(node1Idx);
				
				if (!shortestPathMapForNode1.containsKey(sourceIdx)){
					//node1 is not even connected to sourceIdx, this edge won't be on the shortest path between node1 and node2
					continue;
				}
				if (!shortestPathMapForNode1.containsKey(destIdx)){
					//node1 is not even connected to destIdx, this edge won't be on the shortest path between node1 and node2
					continue;
				}
				
				for (Map.Entry<Integer, Node> nodeEntry2 : nodeList.entrySet()) {
					Integer node2Idx = nodeEntry2.getKey();
					if (node1Idx == node2Idx){
						continue;
					}
					
					if (!shortestPathMapForNode1.containsKey(node2Idx)){
						// these two nodes are not even connected, skip
						// this happens when the graph is already split into disconnected components						
						continue;
					}
					
					HashMap<Integer, ShortestPaths> shortestPathMapForNode2 = shortestPathMap.get(node2Idx);
					if (!shortestPathMapForNode2.containsKey(sourceIdx)){
						//node2 is not even connected to sourceIdx, this edge won't be on the shortest path between node1 and node2
						continue;
					}
					if (!shortestPathMapForNode2.containsKey(destIdx)){
						//node2 is not even connected to destIdx, this edge won't be on the shortest path between node1 and node2
						continue;
					}
										
					ShortestPaths shortestPathsBetweenNode1AndNode2 = shortestPathMapForNode1.get(node2Idx);
					
					//1st attempt: node1 - source - edge - dest - node2					
					ShortestPaths shortestPathsBetweenNode1AndSource = shortestPathMapForNode1.get(sourceIdx);
					ShortestPaths shortestPathsBetweenNode2AndDest = shortestPathMapForNode2.get(destIdx);
					if(shortestPathsBetweenNode1AndSource.distanceOfShortestPath+1+shortestPathsBetweenNode2AndDest.distanceOfShortestPath == shortestPathsBetweenNode1AndNode2.distanceOfShortestPath){
						// edge lies on the shortest path from node1 to node 2
						float edgeBetweennessIncrease =  (float)(shortestPathsBetweenNode1AndSource.numOfShortestPaths * shortestPathsBetweenNode2AndDest.numOfShortestPaths) / shortestPathsBetweenNode1AndNode2.numOfShortestPaths;
						betweenessOfThisEdge += edgeBetweennessIncrease;

						//1st attempt succeeded, no point doing 2nd attempt 						
						continue;
					}

					//2nd attempt: node1 - source - edge - dest - node2					
					ShortestPaths shortestPathsBetweenNode2AndSource = shortestPathMapForNode2.get(sourceIdx);
					ShortestPaths shortestPathsBetweenNode1AndDest = shortestPathMapForNode1.get(destIdx);
					if(shortestPathsBetweenNode2AndSource.distanceOfShortestPath+1+shortestPathsBetweenNode1AndDest.distanceOfShortestPath == shortestPathsBetweenNode1AndNode2.distanceOfShortestPath){
						// edge lies on the shortest path from node2 to node 1
						float edgeBetweennessIncrease =  (float)(shortestPathsBetweenNode2AndSource.numOfShortestPaths * shortestPathsBetweenNode1AndDest.numOfShortestPaths) / shortestPathsBetweenNode1AndNode2.numOfShortestPaths;
						betweenessOfThisEdge += edgeBetweennessIncrease;
					}					
				}
			}
		
			if (betweenessOfThisEdge > highestBetweeness){
				highestBetweeness = betweenessOfThisEdge;
				bestEdge = edge;
			}			
		}
		
		if (bestEdge == null){
			System.out.println("no bestEdge can be found");
		} else {
			System.out.println("bestEdge with betweenness "+highestBetweeness+" found");
		}
		
		return bestEdge;
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
			if (i==4){
				node.addNeighbours(5);
				
				Edge edge = new Edge(i, 5);
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
			if (i==5){
				node.addNeighbours(4);
				
				Edge edge = new Edge(i, 4);
				testGraph.addEdge(edge);
			}
			
			testGraph.addNode(node);
		}
		
		Edge edge = EdgeBetweenness.findHighestEdge(testGraph);
		System.out.println("edge with highest betweenness: source: "+edge.getSourceNodeID()+" dest: "+edge.getDestinationNodeID());
		
	}
	
	public static void main(String[] args){	
		testFindHighestEdge();
	}
}
