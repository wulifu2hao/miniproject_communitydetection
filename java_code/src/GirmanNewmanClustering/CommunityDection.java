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
	  
	   public static void solve(Graph graph, int numberGroundTruth, String outputPath, int sampleNum) throws Exception{
		   long solvingStartTime = System.currentTimeMillis();
		   //keep removing edges form the graph until number of communities equals the number of ground truth.
	       int numberOfCommunity=graph.getNumOfConnectedComponents();
//	       System.out.println("initial num of communities "+numberOfCommunity);
	       int counter = 0;
	       while(numberOfCommunity < numberGroundTruth){ 
	    	   long startTime = System.currentTimeMillis();
	    	   
	    	   //update the edge betweenness of all edges and find the edge with the highest betweenness
	    	   Edge edge;
	    	   if(sampleNum == 1){
	    		   edge=EdgeBetweenness.findHighestEdge(graph);
	    	   } else {
	    		   edge=EdgeBetweenness.findHighestEdgeRandom(graph, sampleNum);
	    	   }
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
	    	   
	    	   counter ++;	    	   
	    	   if (counter % 100 == 0){
//	    		   System.out.println("counter: "+counter);
	    	   }
	    	   long timeUsed = System.currentTimeMillis() - startTime;
//	    	   System.out.println("timeUsed for this round: "+ timeUsed+"ms");
		    }
		   
	       ArrayList<Set<Integer>> communities = graph.getConnectedComponents();
	        	       
	       ExportController.exportCommunities(outputPath, communities);
	       
	       long timeUsed = System.currentTimeMillis() - solvingStartTime;
	       String tag = "[Normal]";
	       if (sampleNum != 1){
	    	   tag = "[Sampling]";
	       }
	       System.out.println(tag+" timeUsed for solving problem: "+ timeUsed+"ms");
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
			
			solve(testGraph, 2, "output.txt", 1);
	   }
	   
	   private static void testSolveArtificialGraph(int numCommunities, int communitySize, double d, double e) throws Exception{
		   ArrayList<Set<Integer>> groundTruth = GraphGenerator.generateCommunityGroundTruth(numCommunities, communitySize);
		   ExportController.exportCommunities("groundtruth.txt", groundTruth);
		   
		   Graph graph1 = GraphGenerator.generateGraph(numCommunities, communitySize, d, e);
		   System.out.println("graph generated");
//		   solve(graph1, numCommunities, "output.txt", 1);
		   
		   Graph graph2 = GraphGenerator.generateGraph(numCommunities, communitySize, d, e);
		   solve(graph2, numCommunities, "output_sample.txt", 10);
	   }
	   
	   private static void testSolveArtificialGraphSample(int numCommunities, int communitySize, double d) throws Exception{
		   ArrayList<Set<Integer>> groundTruth = GraphGenerator.generateCommunityGroundTruth(numCommunities, communitySize);
		   ExportController.exportCommunities("groundtruth.txt", groundTruth);
		   
		   int numAttempts = 5;
		   for(int i=1; i<=6; i++){
			   double e = 0.05*i;
			   for (int j=0; j<numAttempts; j++){
				   Graph graph = GraphGenerator.generateGraph(numCommunities, communitySize, d, e);
				   String outputFilename  = "output_sample_i="+i+"_j="+j+".txt";
				   solve(graph, numCommunities, outputFilename, 30);
			   }			   
		   }		  
	   }
	   
	   private static void testSolveArtificialGraphNormal(int numCommunities, int communitySize, double d) throws Exception{
		   ArrayList<Set<Integer>> groundTruth = GraphGenerator.generateCommunityGroundTruth(numCommunities, communitySize);
		   ExportController.exportCommunities("groundtruth.txt", groundTruth);
		   
		   int numAttempts = 5;
		   for(int i=5; i<=5; i++){
			   double e = 0.05*i;
			   for (int j=0; j<numAttempts; j++){
				   Graph graph = GraphGenerator.generateGraph(numCommunities, communitySize, d, e);
				   String outputFilename  = "output_i="+i+"_j="+j+".txt";
				   solve(graph, numCommunities, outputFilename, 1);
			   }			   
		   }		  
	   }
	   
	   private static void testRunningTime() throws Exception{
		   int communitySize = 20;
		   for(int numCommunities=1;numCommunities<10; numCommunities++){
			   Graph graph = GraphGenerator.generateGraph(numCommunities, communitySize, 0.5, 0.1);
			   String outputFilename  = "trash.txt";
			   solve(graph, numCommunities, outputFilename, 30);
		   }		   
	   }
	   
	   
	   
	   
	   public static void main(String[] args) throws Exception{
//		   testSolveArtificialGraphSample(10, 20, 0.5);
//		   testSolveArtificialGraphNormal(5, 20, 0.5);
		   testRunningTime();
		   
		   /*if (args.length!=1) {
			   System.err.println("Usage: CommunityDection <pathToDataFile>");
			   System.exit(2);
		   }*/
		   
		   //construct graph 
//		   Graph graph=ImportController.importGraph("/Users/lifu.wu/Documents/combinatorial/community_detection/java_code/data/com-amazon.ungraph.txt");
//		   solve(graph, 5000, "output_amazon_5000.txt", 2);
		   
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
	       
//	       solve(graph, NUMBER_GROUND_TRUTH, "output.txt");
	   }
	
			
}


