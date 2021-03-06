package GirmanNewmanClustering;

import java.io.*;
import java.lang.ArrayIndexOutOfBoundsException;
import java.util.Scanner;

/*
 * requirement on data set:
 * format:<node1,node2>
 * no duplicate edge in the edge list.
 * undirected,unweighted,static graph
 */

public class ImportController {
	
		public static Graph importGraph(String path) throws IOException{
			Graph graph=new Graph();
			int countEdge=0;
			int countNode=0;
			try{
				Scanner sc =new Scanner(new File(path));
		        String[] temp=null;
		        //store a graph into a graph data structure
		        while(sc.hasNextLine()){
		        	//sparse into two nodes and store them in A and B
		        	temp=sc.nextLine().split("\t");
		        	int A=Integer.parseInt(temp[0]);
		        	int B=Integer.parseInt(temp[1]);
		        	
		        	//create an new edge object,and append the edge to the graph's edgeList
		        	Edge edge= new Edge(A,B);
		        	graph.addEdge(edge);
		        	countEdge++;
		        	
		        	//deal with the two node
		        	//if(node not in the nodeList by checking the nodeId) create a new node object and append it into the graph's nodeList;
		        	//else append the other node to the node's neighbour set
		        	if (!graph.getNodeList().containsKey(A)){
		        		Node newNode = new Node(A);
		        		newNode.addNeighbours(B);
		        		graph.addNode(newNode);
		        		countNode++;
		        	}
		        	else {
		        		graph.getNodeList().get(A).addNeighbours(B);
		        	}
		        	if (!graph.getNodeList().containsKey(B)){
		        		Node newNode = new Node(B);
		        		newNode.addNeighbours(A);
		        		graph.addNode(newNode);
		        		countNode++;
		        	}
		        	else {
		        		graph.getNodeList().get(B).addNeighbours(A); 
		        	}
		        }
			}catch(FileNotFoundException ee){
				ee.printStackTrace();
			}catch(ArrayIndexOutOfBoundsException ee){
				ee.printStackTrace();
			}
			//integrity checking: the total number of nodes and the number of  edges
			System.out.println("the number of edges in the dataset is "+countEdge);
			System.out.println("the number of nodes in the dataset is "+countNode);
			System.out.println("the number of edges in the graph is "+graph.getNumberOfEdge());
			System.out.println("the number of nodes in the graph is "+graph.getNumberOfNode());
			return graph;
	}
		

}
