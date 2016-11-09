package GirmanNewmanClustering;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExportController {
	public static void exportCommunities(String path, ArrayList<Set<Integer>> communities) throws IOException{
		BufferedWriter out = null;
		try  
		{
		    FileWriter fstream = new FileWriter(path);
		    out = new BufferedWriter(fstream);
		    
		    for(int i=0;i<communities.size();i++){
		    	Set<Integer> community = communities.get(i);
		    	List<Integer> sortedList = new ArrayList<Integer>(community);
		    	Collections.sort(sortedList);
		    	
		    	for(Integer nodeIdx:sortedList){
		    		out.write(nodeIdx+" ");
		    	}
		    	out.write("\n");
		    }
		    
		}
		catch (IOException e)
		{
		    System.err.println("Error: " + e.getMessage());
		}
		finally
		{
		    if(out != null) {
		        out.close();
		    }
		}	
	}
	
	private static void testExportCommunities() throws IOException{
		String path = "output.txt";
		ArrayList<Set<Integer>> communities = new ArrayList<Set<Integer>>();
		Set<Integer> community1 = new HashSet<Integer>();
		for(int i=0; i<5;i++){
			community1.add(i);
		}
		communities.add(community1);
		Set<Integer> community2 = new HashSet<Integer>();
		for(int i=5; i<15;i++){
			community2.add(i);
		}
		communities.add(community2);
		exportCommunities(path, communities);
	}
	
	public static void main(String[] args) throws IOException{	
		testExportCommunities();
	}
	
}
