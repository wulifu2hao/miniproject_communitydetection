package GirmanNewmanClustering;

public class EdgeBetweenness {

	// this method takes in a graph and return the edge with the highest betweenness
	public static Edge findHighestEdge(Graph graph){		
		//   1. for every node, do a BFS to find its number and distance of shortest path to all other nodes
		//		this should take O(nm)		
		//	 2. for every edge, compute its betweenness from every pair of nodes' shortest path
		//		this should take O(mn^2)
		
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
}
