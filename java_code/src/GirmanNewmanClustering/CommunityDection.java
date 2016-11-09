package GirmanNewmanClustering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;


public class CommunityDection{
	
	public static class Pair{

		private final Integer left;
		private final Integer right;

		public Pair(int left, int right) {
			this.left = left;
			this.right = right;
		}

		public int getLeft() {
			return left; 
		}
		public int getRight() {
			return right;
		}

		@Override
		public int hashCode() { 
			return left.hashCode() ^ right.hashCode(); 
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Pair)) return false;
			Pair pairo = (Pair) o;
			return this.left.equals(pairo.getLeft()) &&
					this.right.equals(pairo.getRight());
		}
	}
	 
	   //parameter setting
	   private static final int NUMBER_GROUND_TRUTH=5000;		//used to check if satisfying termination condition,the number of communities in reality.
	   private static int sampleSize;	//maximum =Cn2,n is the number of distinct nodes in the graph.
	  
	   public static void solve(Graph graph, int numberGroundTruth, String outputPath) throws Exception{
		   //keep removing edges form the graph until number of communities equals the number of ground truth.
	       int numberOfCommunity=1;
	       int counter = 0;
	       while(numberOfCommunity != numberGroundTruth){ 
	    	   long startTime = System.currentTimeMillis();
	    	   
	    	   //update the edge betweenness of all edges and find the edge with the highest betweenness
	    	   Edge edge=EdgeBetweenness.findHighestEdge(graph);
	    	   // TODO: possible improvement: update only those that are affected by the previous edge removal
	    	   // TODO: possible improvement: update base on a sample of nodes instead of all nodes
	    	   if (edge == null) {
	    		   // TODO: handle error when the algorithm cannot proceed anymore
	    		   System.out.println("numberOfCommunity: "+numberOfCommunity);
	    		   throw new Exception("cannot proceed");
	    	   }
	    	   
	    	   //remove the edge with the highest betweenness	    	   
	    	   graph.deleteEdge(edge);
	    	   
	    	   //update numberOfCommunity by finding the number of connected components    	   
	    	   numberOfCommunity = graph.getNumOfConnectedComponents();
	    	   
<<<<<<< HEAD
	    	   counter++;
	    	   System.out.println(counter);
=======
	    	   counter ++;
	    	   System.out.println("counter: "+counter);
	    	   long timeUsed = System.currentTimeMillis() - startTime;
	    	   System.out.println("timeUsed for this round: "+ timeUsed);
>>>>>>> 9233c4806d40a6a7f3ef9cf536cdead0575be094
		    }
		   
	       ArrayList<Set<Integer>> communities = graph.getConnectedComponents();
	        	       
	       ExportController.exportCommunities(outputPath, communities);
	   }
	   
	   private static void testSolve() throws Exception{
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
			
			solve(testGraph, 2, "output.txt");
	   }
	   
	   private static void testSolveArtificialGraph(int numCommunities, int communitySize, double d, double e) throws Exception{
		   ArrayList<Set<Integer>> groundTruth = GraphGenerator.generateCommunityGroundTruth(numCommunities, communitySize);
		   ExportController.exportCommunities("groundtruth.txt", groundTruth);
		   
		   Graph graph = GraphGenerator.generateGraph(numCommunities, communitySize, d, e);		   		   
		   System.out.println("graph generated");
		   solve(graph, numCommunities, "output.txt");
	   }
	   
	   
	   public static void main(String[] args) throws Exception{
<<<<<<< HEAD
		   /*if (args.length!=1) {
			   System.err.println("Usage: CommunityDection <pathToDataFile>");
			   System.exit(2);
		   }*/
		   
		   //construct graph 
		   Graph graph=ImportController.importGraph("/Users/yangtingting/Documents/miniproject/miniproject_communitydetection/datafile/network.txt");
=======
		   testSolveArtificialGraph(5, 10, 0.8, 0.2);
>>>>>>> 9233c4806d40a6a7f3ef9cf536cdead0575be094
		   
		   //randomly sample a set of node pairs from the nodeList and store it into sampleTable
		   //the maximal id of node=548458
	       /*HashMap<Pair,LinkedList<Edge>> sampleTable=new HashMap<Pair,LinkedList<Edge>>();
	       Random rand = new Random(System.currentTimeMillis());
	       Set<Integer> nodeIdList=graph.getNodeList().keySet();
	       int left;
	       int right;
	       for(int i=0;i<sampleSize;){
	    	   do{
	    		   left=rand.nextInt(548459); //sample from 1 to 548459
	    	   }while(!nodeIdList.contains(left) | left==0);
	    	   
	    	   do{
	    		   right=rand.nextInt(548459); //sample from 1 to 548459
	    	   }while(!nodeIdList.contains(right) | right==0 | right==left);
	    	   
	    	   Pair pair=new Pair(left,right);
	    	   if(sampleTable.containsKey(pair)) continue;
	    	   sampleTable.put(pair, null);
	    	   i++;
	       }*/
	       
	       solve(graph, NUMBER_GROUND_TRUTH, "output.txt");
	   }
	
			
}


