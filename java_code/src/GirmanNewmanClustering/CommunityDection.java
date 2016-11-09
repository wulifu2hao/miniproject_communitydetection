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
	       while(numberOfCommunity != NUMBER_GROUND_TRUTH){ 
	    	   
	    	   //update the edge betweenness of all edges and find the edge with the highest betweenness
	    	   Edge edge=EdgeBetweenness.findHighestEdge(graph);
	    	   // TODO: possible improvement: update only those that are affected by the previous edge removal
	    	   // TODO: possible improvement: update base on a sample of nodes instead of all nodes
	    	   if (edge == null) {
	    		   // TODO: handle error when the algorithm cannot proceed anymore 	    		   
	    	   }
	    	   
	    	   //remove the edge with the highest betweenness	    	   
	    	   graph.deleteEdge(edge);
	    	   
	    	   //update numberOfCommunity by finding the number of connected components    	   
	    	   numberOfCommunity = graph.getNumOfConnectedComponents();
		    }
		   
	       ArrayList<Set<Integer>> communities = graph.getConnectedComponents();
	       
	       // TODO: maybe give the graph a name so that we can name the output file properly 	       
	       ExportController.exportCommunities("output.txt", communities);
	   }
			
			
}


