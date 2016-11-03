package GirmanNewmanClustering;

import java.io.*;
import java.lang.ArrayIndexOutOfBoundsException;
import java.util.Scanner;

/*
 * data set features:
 * format:<node1,node2>
 * no duplicate edge in the edge list.
 * undirected,unweighted,static graph
 */

public class ImportController {
	
		//public void importGraph(Graph graph) throws IOException{
		public static void main(String[] args) throws IOException{
			Graph graph=new Graph();
			int countEdge=0;
			int countNode=0;
			try{
				Scanner sc =new Scanner(new File("/Users/yangtingting/Documents/miniproject/miniproject_communitydetection/datafile/network.txt"));
		        String[] temp=null;
		        //store a graph into a graph data structure
		        while(sc.hasNextLine()){
		        	//sparse into two nodes and store them in A and B
		        	temp=sc.nextLine().split("\t");
		        	System.out.println("temp[0] is "+temp[0]);
		        	System.out.println("temp[1] is "+temp[1]);
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
		        		graph.addNode(new Node(A,B));
		        		countNode++;
		        	}
		        	else {
		        		graph.getNodeList().get(A).addNeighbours(B);
		        	}
		        	if (!graph.getNodeList().containsKey(B)){
		        		graph.addNode(new Node(B,A));
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
	}
		

}
