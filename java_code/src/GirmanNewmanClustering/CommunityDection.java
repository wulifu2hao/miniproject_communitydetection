package GirmanNewmanClustering;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
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
	   private static final int NUMBER_Of_GROUND_TRUTH=5000;		//termination condition
	   private static int sampleSize;	//<half of the number of node.
	   
	   public static void main(String[] args) throws IOException{
		   
		   if (args.length!=1) {
			   System.err.println("Usage: CommunityDection <pathToDataFile>");
			   System.exit(2);
		   }
		   
		   //construct graph 
		   Graph graph=ImportController.importGraph(args[0]);
		   
		   //randomly sample a set of node pairs from the nodeList and store it into hashMap
		   //the maximal id of node=548458
	       HashMap<Pair,HashSet<Edge>> sampleTable=new HashMap<Pair,HashSet<Edge>>();
	       Random rand = new Random(System.currentTimeMillis());
	       Set<Integer> nodeIdList=graph.getNodeList().keySet();
	       int left;
	       int right;
	       for(int i=0;i<sampleSize;i++){
	    	   do{
	    		   left=rand.nextInt(548459); //sample from 1 to 548459
	    	   }while(!nodeIdList.contains(left) && left==0);
	    	   nodeIdList.remove(left);
	    	   do{
	    		   right=rand.nextInt(548459); //sample from 1 to 548459
	    	   }while(!nodeIdList.contains(right) && right==0);
	    	   nodeIdList.remove(right);
	    	   sampleTable.put(new Pair(left,right), null);
	       }
	       
		    //iterate until N edges are removed from the graph.
	       int numberOfCommunity=0;
	       while(numberOfCommunity==NUMBER_Of_GROUND_TRUTH){ 
	    	   //recompute the betweenness score for pairs that are affected
	    	   for(Pair pair:sampleTable.keySet()){
	    		   //compute the shortest path between the pair ;edgeInshortestPath
	    		   HashSet<Edge> edgeInShortestPath=new HashSet<Edge>();
	    		   
	    		   
	    		   
	    		   //store the shortest path to the sampleTable
	    		   
	    		   
	
	    		   //increase the betweenness by one for every edge that composes the shortest path.
	    		   for (Edge e : edgeInShortestPath) {
	    			   e.increaseBetweennessByOne();
	    		   }
	    	   }
	    	   //iterate the edge_betweenness list and search the edge with highest betweenness
	    	   double maxBetw=0.0;
	    	   Edge edge=null;
	    	   for (Edge e : graph.getEdgeList()) {
	    			if (e.getBetweenness() > maxBetw) {
	    				maxBetw = e.getBetweenness();
	    				edge=e;
	    			}
				}
	    	   //remove the edge E from graph's edgeList.
	    	   graph.deleteEdge(edge);
	    	   //generate the list to store node pairs whose shortest path are affected because of removing edge E
	
	
	    	   
	    	   //modify the edge_betweenness list through decreasing the betweenness_socre by 1 for every edge in the shortest path.
	
	    	   
	
	    	   //cluster the graph into connected components and compute the value of N
		    }
	   }
			
			
}


