package GirmanNewmanClustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Logger;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.EdgeIterable;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;


public class CommunityDection{
	
	public class Pair{

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
	   private static final int NUMBER_Of_GROUND_TRUTH;		//termination condition
	   private static int sampleSize;	//<half of the number of node.
	   
	   //construct graph 
	   Graph graph=new Graph();
	   importGraph(graph);
	   
	   /*//construct a edge-betweenness table and initialize the betweenness of every edge to zero.
	   HashMap<Edge, Double> betweenness = new HashMap<Edge, Double>();
       for (Edge e : graph.getEdgeList()){
           betweenness.put(e, 0.0);
       }*/
	   
	   //randomly sample a set of node pairs from the nodeList and store it into hashMap
	   //548458
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

    		   //store the shortest path to the sampleTable

    		   //increase the betweenness by one for every edge that composes the shortest path.
    		   for (Edge e : edgeInShortestPath) {
    			   e.betweenness++;
    		   }
    	   }
    	   //iterate the edge_betweenness list and search the edge with highest betweenness
    	   double maxBetw=0.0;
    	   Edge edge;
    	   for (Edge e : graph.getEdgeList()) {
    			if (e.betweenness > maxBetw) {
    				maxBetw = e.betweenness;
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


/*public class CommunityDection {

	public HashMap<Edge, Double> getWeight(final Graph g) {

        if (g == null) {
            throw new IllegalArgumentException("Graph passed to EdgeBetweenness"
                        + " Centrality is null.");
        }*/

        /* Calculating the unweighted Edge-Betweenness centrality.
         * I modified Brandes algorithm, which i used to calculate the
         * Node Betweenness, so that it Calcultes the Edge-Betweenness
         * For further information, please see the NodeBetweenness-
         * Implementation within SONAR or read
         * Brandes, Ulrik: "A Faster Algorithm for Betweenes Centrality",
         * Published in Journal of Mathematical Sociology 25(2):163-177, 2001.
         */

        // init the betweenness-values for each node with 0.0.
        /*HashMap<Edge, Double> betweenness = new HashMap<Edge, Double>();
        for (Edge e : g.getEdgeList()){
            betweenness.put(e, 0.0);
        }

        for (Node startNode : g.getNodeList().values()) {

             //Use dijkstra's algorithm to calculate all shortest paths from the startode: 

            //A Stack used to fetch the used nodes in reversed direction:
            Stack<Node> destinationStack = new Stack<Node>();

             //For each node, store a list of incident edges over which shortest paths to that node lead: 
            HashMap<Node, LinkedList<Edge>> shortestPathIncEdges = new HashMap<Node, LinkedList<Edge>>();

             //For each node, store how many shortest paths exist between it and the Startnode 
            HashMap<Node, Integer> numberOfShortPaths = new HashMap<Node, Integer>();

             //For each node, store the length of the shortest path to it from the startnode 
            HashMap<Node, Integer> shortestPathLength = new HashMap<Node, Integer>();

            // initialize the HashMaps
            for (Node n : g.getNodeList().values()) {
                shortestPathIncEdges.put(n, new LinkedList<Edge>());
                numberOfShortPaths.put(n, 0);
                shortestPathLength.put(n, -1);
            }
            numberOfShortPaths.put(startNode, 1);
            shortestPathLength.put(startNode, 0);

            Queue<Node> bfsQueue = new LinkedList<Node>();
            bfsQueue.add(startNode);

            //the Dijkstra-main-loop:
            while (!bfsQueue.isEmpty()) {
                Node predecessor = bfsQueue.remove();
                destinationStack.push(predecessor);


                //repeat for all outgoing edges:
                for (Edge edge : predecessor.getEdges()) {
                    if (edge.isOutgoingEdge(predecessor)) {

                        // get the node the edge is pointing to:
                        Node neighbor = edge.getDestinationNode();

                        //is the neighbor found for the first time?
                        if (shortestPathLength.get(neighbor) < 0) {
                            bfsQueue.add(neighbor);
                            shortestPathLength.put(neighbor,
                                    shortestPathLength.get(predecessor) + 1);
                        }
                        // Is the path found to the neighbor a shortest path?
                        if (shortestPathLength.get(neighbor)
                                == shortestPathLength.get(predecessor) + 1) {
                             Add together the numbers of shortest paths and
                             * append the predecessor to the list: 
                            numberOfShortPaths.put(neighbor,
                                      numberOfShortPaths.get(neighbor)
                                    + numberOfShortPaths.get(predecessor));
                            shortestPathIncEdges.get(neighbor)
                                                    .add(edge);
                        }
                    }
                }
            }*/

            /* For each edge, we calculate the dependency of the startnode
             * on it. For an explanation what it means,
             * please see Brandes' paper referenced above. */

            // init the dependencys:
           /* HashMap<Edge, Double> dependency =
                        new HashMap<Edge, Double>();
            for (Edge e : g.getEdgeList()) {
               dependency.put(e, 0.0);
            }
*/
            /* calculate the number of shortest paths through the edges
             * from the startnode.
             * therefore sum up the number of paths beginning with the
             * node reached at last during dijkstra-bfs-search.
             */
  
            /*while (!destinationStack.isEmpty()) {
                Node v = destinationStack.pop();
                double depSum = 0.0;
                for (Edge e : v.getEdges()) {
                    if (e.isOutgoingEdge(v)) {
                        depSum += dependency.get(e);
                    }
                }
                for (Edge e : shortestPathIncEdges.get(v)) {

                    dependency.put(e,
                            ((double) numberOfShortPaths.get(e.getSourceNode())
                            / (double) numberOfShortPaths.get(v))
                            * (1.0 + depSum));

                    // add the dependency to the overall betweeenness:
                    betweenness.put(e,
                            betweenness.get(e) + dependency.get(e));

                }
            }
        }
        return betweenness;

    }*/


