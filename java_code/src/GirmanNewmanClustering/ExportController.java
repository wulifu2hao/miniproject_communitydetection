package GirmanNewmanClustering;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
		    	for(Integer nodeIdx:community){
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
}
