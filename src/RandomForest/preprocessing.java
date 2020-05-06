package RandomForest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


public class preprocessing {
	public static void process(String path) throws IOException{
		ArrayList<ArrayList<String>>treeStr=new ArrayList<ArrayList<String>>();
		ArrayList<String>singletreeStr=new ArrayList<String>();
	    BufferedReader br = new BufferedReader(new FileReader(new File(path))); 
        String str="";
	    String data = br.readLine();  
	    while (data != null) {
	    	if (data.contains("RandomTree")){
	    		data=br.readLine();
	    		data=br.readLine();
	    	}else if (data.contains("Size of the tree")){
	    		treeStr.add(singletreeStr);
	    		data=br.readLine();
	    		data=br.readLine();
	    		singletreeStr = new ArrayList<String>(); ;
	    	}else {
	    		singletreeStr.add(data);
	    	}
	    	/*
            if(classCount.containsKey(data)){
            	classCount.put(data, classCount.get(data)+1);
            }else{
            	classCount.put(data,1);
            }
            */
	    	data=br.readLine();
	    
	}
    //    System.out.println(treeStr.get(4).size());
        for (int m=0;m<treeStr.size();m++){
        	System.out.println("tree"+m+":");
    		Map<String, Integer> classCount = new HashMap<String, Integer>();
    		Map<String, String> classDistribution = new HashMap<String, String>();
            for (int k=0;k<treeStr.get(m).size();k++){
            	if (treeStr.get(m).get(k).contains("<")){
            		String[] attr=treeStr.get(m).get(k).split("<");
            	    int count = StringUtils.countMatches(attr[0], "|");
            	    String result = count+"";
            		str=attr[0].trim().replaceAll("\\|", "");
            		str=str.trim();
                    if(classCount.containsKey(str)){
                    	classCount.put(str, classCount.get(str)+1);
                    }else{
                    	classCount.put(str,1);
                    }
                    if(classDistribution.containsKey(str)){
                    	classDistribution.put(str, classDistribution.get(str)+result+" ");
                    }else{
                    	classDistribution.put(str,result+" ");
                    }
            	}else if (treeStr.get(m).get(k).contains(">")){
            		String[] attr=treeStr.get(m).get(k).split(">");
            	    int count = StringUtils.countMatches(attr[0], "|");
            	    String result = count+"";
            		str=attr[0].replaceAll("\\|", "");
            		str=str.trim();
                    if(classCount.containsKey(str)){
                    	classCount.put(str, classCount.get(str)+1);
                    }else{
                    	classCount.put(str,1);
                    }
                    if(classDistribution.containsKey(str)){
                    	classDistribution.put(str, classDistribution.get(str)+result+" ");
                    }else{
                    	classDistribution.put(str,result+" ");
                    }
            	}
            }
            System.out.println(classCount.size());
            ArrayList<Map.Entry<String,Integer>> list_Data = new ArrayList<Map.Entry<String, Integer>>(classCount.entrySet());  
    	    
    	    Collections.sort(list_Data, new Comparator<Map.Entry<String,Integer>>(){    
    	      public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)  
    	      {  
    	        if ((o2.getValue() - o1.getValue())>0)  
    	          return 1;  
    	        else if((o2.getValue() - o1.getValue())==0)  
    	          return 0;  
    	        else   
    	          return -1;  
    	      }  
    	        } 
    	    );
            for (int e=0;e<list_Data.size();e++){
            	System.out.println(list_Data.get(e).getKey()+"  :"+list_Data.get(e).getValue());
            }
            System.out.println(classDistribution.get("capital_run_length_average"));
            
        }
        
        
	}
public static void main(String[] args) throws Exception {
		String path = "./data/result.txt";
	//	String path = "./data/test.txt";
		preprocessing pp=new preprocessing();
		pp.process(path);
			
	}
}