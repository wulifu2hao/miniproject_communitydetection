package GirmanNewmanClustering;

import java.io.IOException;
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
	  
	   
	   
	   public static void main(String[] args) throws IOException{
		   if (args.length!=1) {
			   System.err.println("Usage: CommunityDection <pathToDataFile>");
			   System.exit(2);
		   }
		   
		   //construct graph 
		   Graph graph=ImportController.importGraph(args[0]);
		   
		   //randomly sample a set of node pairs from the nodeList and store it into sampleTable
		   //the maximal id of node=548458
	       HashMap<Pair,LinkedList<Edge>> sampleTable=new HashMap<Pair,LinkedList<Edge>>();
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
	       }
	       
		    //keep moving edges form the graph until number of communities equals the number of ground truth.
	       int numberOfCommunity=0;
	       while(numberOfCommunity==NUMBER_GROUND_TRUTH){ 
	    	   
	    	   //recompute the betweenness score for pairs that are affected
	    	   for(Pair pair:sampleTable.keySet()){
	    		   //compute the shortest path between the pair ;edgeInshortestPath
	    		   List<Node> ShortestPath=getShortestPath(pair);
	    		   //store the shortest path to the sampleTable
	    		   sampleTable.put(pair, ShortestPath);
	    		   //increase the betweenness by one for every edge that composes the shortest path.
	    		   for (Edge e : ShortestPath) {
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


