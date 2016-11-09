package Evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Evaluation {
	
	public  static void main(String[] args) throws IOException{
		
		if (args.length!=2) {
			System.err.println("Usage: Evaluation <path to output file> <path to ground truth>");
			System.exit(2);
		}
		
		String pathIn=args[0];
		String pathOut=args[1];
		
		int evaluation=0;
		
		//add index to every line in the ground truth file;
		try{
			FileReader fr=new FileReader(args[1]);
			BufferedReader br=new BufferedReader(fr);
			FileWriter fw=new FileWriter(args[0]);
			PrintWriter pw=new PrintWriter(fw);
			String str="";
			int count=1;
			while((str=br.readLine())!=null){
				pw.write((""+count+"\t"+str+"\n").toCharArray());
				count++;
			}
			System.out.print("the total number of lines is:"+count);
			br.close();
			pw.close();
		
		
			//compare every community in output file with ground truth file
			Scanner sc1 =new Scanner(new File(pathIn));
		    String[] community=null;
		    
		    while(sc1.hasNextLine()){
		    	//store one community into the community variable
		    	community=sc1.nextLine().split("\t");
		    	HashMap<String,Set<Integer>> container=new HashMap<String,Set<Integer>>();
		    	for(String s:community){
		    		container.put(s, null);
		    	}
		    	
		    	//search the cluster indexes for each node in the community
		    	Scanner sc2 =new Scanner(new File(pathOut));
			    String[] communityGroundTruth=null;
			    String[] temp;
			    while(sc2.hasNextLine()){
			    	//store one community into the temp
			    	temp=sc2.nextLine().split("\t");
			    	int index=Integer.parseInt(temp[0]);
			    	for(int i=1;i<temp.length;i++){
			    		communityGroundTruth[i-1]=temp[i];
			    	}
			    	Set<String>  communnityGroundTruthNodeSet = new HashSet<String>(Arrays.asList(communityGroundTruth));	
			    	//search the cluster indexes for each node in the community
			    	Set<Integer> setOfIndex= new HashSet<Integer>();
			    	for(String node:community){
			    		if(communnityGroundTruthNodeSet.contains(node)){
			    			Set<Integer> set= container.get(node);
			    			set.add(index);
			    			container.put(node, set);
			    		}
			    	}
			    }
			    //compute the sharing index of the community;
			    HashMap<Integer,Integer> frequencyList=new HashMap<Integer,Integer>();
			    Collection<Set<Integer>> setPackage=container.values();
			    for(Set set:setPackage){
			    	for(int i: set){
			    		if(!frequencyList.containsKey(i)) frequencyList.put(i,1);//new one
			    		if(frequencyList.containsKey(i)) {
			    			int freqencyValue=frequencyList.get(i);
			    			frequencyList.put(i,freqencyValue+1);//add 1
			    		}
			    	}
			    }
			    int maximumfrequency=0;
			    int keyOfMaximumFrequency=0;
			    for (Integer key : frequencyList.keySet()) {  
			    	  if(frequencyList.get(key) > maximumfrequency){
			    		  maximumfrequency=frequencyList.get(key);
			    		  keyOfMaximumFrequency=key;
			    	  }
			    }  
			    
			    //compute the error of the community;
			    
			    for (Integer key : frequencyList.keySet()) {  
			    	  if(key!=keyOfMaximumFrequency){
			    		  evaluation+=frequencyList.get(key);
			    	  }
			    }  
			    
		    }
		}catch(FileNotFoundException ee){
			ee.printStackTrace();
		}catch(ArrayIndexOutOfBoundsException ee){
			ee.printStackTrace();
		}
	    
	}
}
