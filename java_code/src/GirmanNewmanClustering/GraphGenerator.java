package GirmanNewmanClustering;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GraphGenerator {

	public static Graph generateGraph(int numCommunities, int communitySize, double d, double e){
		Random rand = new Random(System.currentTimeMillis());
		int randIntThresholdForWithinCommunity = (int) (d*100);
		int randIntThresholdForBetweenCommunity = (int)(e*100);
		
		Graph graph = new Graph();
		for(int i=0; i<numCommunities; i++){
			for(int j=0; j<communitySize; j++){
				int nodeIdx = i*communitySize + j;
				Node node = new Node(nodeIdx);
				graph.addNode(node);
			}
		}
		
		for(int node1Idx=0; node1Idx<numCommunities*communitySize; node1Idx++){
			int node1CommunityIdx = node1Idx/communitySize;
			for(int node2Idx=node1Idx+1; node2Idx<numCommunities*communitySize; node2Idx++){
				int node2CommunityIdx = node2Idx/communitySize;				
				int randIntThreshold = randIntThresholdForBetweenCommunity;
				if(node1CommunityIdx == node2CommunityIdx){
					randIntThreshold = randIntThresholdForWithinCommunity;
				}
				
				int randomInteger = rand.nextInt(100);
				if(randomInteger < randIntThreshold){
					Edge edge = new Edge(node1Idx, node2Idx);
					graph.getNodeList().get(node1Idx).addNeighbours(node2Idx);
					graph.getNodeList().get(node2Idx).addNeighbours(node1Idx);
					graph.addEdge(edge);
				}				
			}
		}
		
		
		return graph;		
	}
	
	public static ArrayList<Set<Integer>> generateCommunityGroundTruth(int numCommunities, int communitySize){
		ArrayList<Set<Integer>> result = new ArrayList<Set<Integer>>();
		for(int i=0; i<numCommunities; i++){
			Set<Integer> community = new HashSet<Integer>();
			for(int j=0; j<communitySize; j++){				
				community.add(i*communitySize+j);
			}
			result.add(community);
		}
		
		return result;
	}
	
	private static void testGenerateGraph(){
		Graph testGraph = generateGraph(3, 5, 0.9, 0.1);
		testGraph.printSelfMatrix();
	}
	
	public static void main(String[] args){
		testGenerateGraph();
	}
	
}
