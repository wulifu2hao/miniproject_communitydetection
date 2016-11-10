package Evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;
/*
 * ground truth communities do not overlap with each other;
 */

public class Evaluation {
	
	public  static void main(String[] args) throws IOException{
		
		/*if (args.length!=2) {
			System.err.println("Usage: Evaluation <path to output file> <path to ground truth>");
			System.exit(2);
		}*/
		/*
		String pathIn=args[0];
		String pathOut=args[1];*/
		
		
		String pathIn="/Users/yangtingting/Documents/miniproject/miniproject_communitydetection/java_code/output.txt";
		String pathOut1="/Users/yangtingting/Documents/miniproject/miniproject_communitydetection/java_code/groundtruth.txt";
		String pathOut2="/Users/yangtingting/Documents/miniproject/miniproject_communitydetection/datafile/groundtruth1.txt";
		String pathOut3="/Users/yangtingting/Documents/miniproject/miniproject_communitydetection/datafile/log.txt";
		

		File file2=new File(pathOut2);
		if(!file2.exists()){
			file2.createNewFile();
		}
		else System.err.println("groundtruth filealready existed!");
		
		File file3=new File(pathOut3);
		if(!file3.exists()){
			file3.createNewFile();
		}
		else System.err.println("log file already existed!");
		
		int evaluation=0;
		
		//add index to every line in the ground truth file;
		try{
			FileReader fr=new FileReader(pathOut1);
			BufferedReader br=new BufferedReader(fr);
			FileWriter fw=new FileWriter(pathOut2);
			PrintWriter pw=new PrintWriter(fw);
			FileWriter Logfw=new FileWriter(pathOut3);
			PrintWriter Logpw=new PrintWriter(Logfw);
			String str="";
			int count=1;	//index starts from 1
			while((str=br.readLine())!=null){
				pw.write((""+count+" "+str+"\n").toCharArray());
				count++;
			}
			Logpw.write(("the total number of communities in groundtruth file is:"+(count-1)+"\n").toCharArray());
			br.close();
			pw.close();
		
			//compare every community in output file with ground truth file
			Scanner sc1 =new Scanner(new File(pathIn));
		    String[] community=null;
		    
		    while(sc1.hasNextLine()){
		    	//store one community into the community variable
		    	community=sc1.nextLine().split(" ");
		    	HashMap<String,Integer> container=new HashMap<String,Integer>();
		    	for(String s:community){
		    		container.put(s, 0);
		    	}
		    	
		    	//search the cluster indexes for each node in the community
		    	Scanner sc2 =new Scanner(new File(pathOut2));
		    	String[] temp;
			    while(sc2.hasNextLine()){
			    	//store one community into the temp
			    	temp=sc2.nextLine().split(" ");
			    	String[] communityGroundTruth=new String[temp.length-1];
			    	int index=Integer.parseInt(temp[0]);
			    	for(int i=1;i<temp.length;i++){
			    		communityGroundTruth[i-1]=temp[i];
			    	}
			    	Set<String>  communnityGroundTruthNodeSet = new HashSet<String>(Arrays.asList(communityGroundTruth));	
			    	
			    	//search the cluster indexes for each node in the community
			    	for(String node:community){
			    		if(communnityGroundTruthNodeSet.contains(node)){
			    			/*Set<Integer> set= container.get(node);
			    			if(container.get(node)==null) {
			    				Set<Integer> set1= new HashSet<Integer>();
			    				set1.add(index);
			    				container.put(node,set1);
			    			}
			    			else{
			    				Set<Integer> set2= container.get(node);
			    				set2.add(index);
			    				container.put(node, set2);
			    			}*/
			    			container.put(node,index);
			    		}
			    	}
			    	
			    }
			    
			    //remove the mapping<node,set<index>> whose node doesnot belong to any community in the grundtruth file
			    Iterator iter1 = container.entrySet().iterator();
			    while (iter1.hasNext())
			    {
			       Map.Entry entry = (Map.Entry)iter1.next();
			       if(entry.getValue()==(Integer)0){
			    	   Logpw.write(("no grundtruth community includes node"+entry.getKey()+"\n").toCharArray());
			    	   //container.remove(entry.getKey());
			    	   iter1.remove();
			       }
			       else{
			    	   Logpw.write(("********************"+"\n").toCharArray());
			    	   Logpw.write((""+entry.getKey()+"\n").toCharArray());
			    	   Logpw.write((""+entry.getValue()+"\n").toCharArray());
			       }
			       
			       
			    }

			    //compute the sharing index of the community;
			    Iterator iter2 = container.entrySet().iterator();
			    HashMap<Integer,Integer> frequencyList=new HashMap<Integer,Integer>();
			    while (iter2.hasNext())
			    {
			       Map.Entry entry = (Map.Entry)iter2.next();
			       int intkey=(Integer)entry.getValue();
			       if(!frequencyList.containsKey(intkey)) {
			    	   frequencyList.put(intkey,1);
			    	   Logpw.write(("new one"+"\n").toCharArray());
			       }
			       else {
			    	   int freqencyValue=frequencyList.get(intkey);
			    	   frequencyList.put(intkey,freqencyValue+1);
			    	   Logpw.write(("increase by 1:"+intkey+"    "+freqencyValue+"\n").toCharArray());
				   }
			    }
			    
			    
			    
			    /*HashMap<Integer,Integer> frequencyList=new HashMap<Integer,Integer>();
			    Collection<Integer> set=container.values();
			    for(Integer i:set){
		    		if(!frequencyList.containsKey(i)) frequencyList.put(i,1);
		    		if(frequencyList.containsKey(i)) {
		    			int freqencyValue=frequencyList.get(i);
		    			frequencyList.put(i,freqencyValue+1);
		    		}
		    	}*/
			    /*for(Set<Integer> subSet:set){
			    	for(Integer i:subSet){
			    		if(!frequencyList.containsKey((int)i)) frequencyList.put((int)i,1);
			    		if(frequencyList.containsKey((int)i)) {
			    			int freqencyValue=frequencyList.get((int)i);
			    			frequencyList.put((int)i,freqencyValue+1);
			    		}
			    	}
			    }*/
			    
			    int maximumfrequency=0;
			    Integer indexOfTheCommunity=null;
			    for (Integer key : frequencyList.keySet()) { 
			    	  if(frequencyList.get(key) > maximumfrequency){
			    		  maximumfrequency=frequencyList.get(key);
			    		  indexOfTheCommunity=key;
			    		  
			    	  }
			    }  
			    Logpw.write(("index Of The Community is "+indexOfTheCommunity+"\n").toCharArray());
			    Logpw.write(("maximal frequency is "+maximumfrequency+"\n").toCharArray());
			    //append error of the community;
			    for (Integer key : frequencyList.keySet()) {  
			    	  if(key!=indexOfTheCommunity){
			    		  evaluation+=frequencyList.get(key);
			    	  }
			    }  
			    
		    }
		    Logpw.write(("************error evaluation*****************"+"\n").toCharArray());
			Logpw.write(("evaluation= "+evaluation+"\n").toCharArray());
			Logpw.close();
		}catch(FileNotFoundException ee){
			ee.printStackTrace();
		}catch(ArrayIndexOutOfBoundsException ee){
			ee.printStackTrace();}
		System.out.println("************error evaluation*****************");
		System.out.println("evaluation= "+evaluation);
}
}
