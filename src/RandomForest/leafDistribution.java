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
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import weka.classifiers.Classifier;


public class leafDistribution {
	public List<String> process(String path, List<String> classlabels) throws Exception{
		Map<String, Integer> appear = new HashMap<String, Integer>();
		Classifier cls = (Classifier) weka.core.SerializationHelper.read(path);
		String[]contents = cls.toString().split("\n");
		for (int i=0;i<contents.length;i++){
			for (int k=0;k<classlabels.size();k++){
				//careful, there is a space!
			  	if (contents[i].contains(classlabels.get(k)+" ")){
			  		if (contents[i].contains(":")){
			  		appear.put(classlabels.get(k), 1);
			  	}
			  	}
			}

		}
	      ArrayList<Map.Entry<String,Integer>> list_Data = new ArrayList<Map.Entry<String, Integer>>(appear.entrySet());  
  	    
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
       List<String>result = new ArrayList<String>();
  	    for (int i=0;i<list_Data.size();i++){
  	    	
  	    	result.add(list_Data.get(i).getKey());
  	    }   
  	    System.out.println(result);
  	    return result;
        
	}
public static void main(String[] args) throws Exception {
	List<String>classlabels = new ArrayList<String>();
	Map<String, Integer> label = new HashMap<String, Integer>();
	Map<Integer, Integer> count = new HashMap<Integer, Integer>();	
	 BufferedReader br2 = new BufferedReader(new FileReader(new File("./data/attack/spambase/model/spambase1.data"))); 
     String[] labels = br2.readLine().split(",");
     br2.close();
     for (int i=0;i<labels.length-1;i++){
     	classlabels.add(labels[i]);
     	label.put(labels[i], i);
     	count.put(i, 0);
     }
		String path = "./data/attack/spambase/model/5,50/w_models/";
		for (int i=0;i<10;i++){
	    List<String>result = new ArrayList<String>();	
		leafDistribution ld=new leafDistribution();
		result=ld.process(path+"Classifier"+i+".model", classlabels);
		for(int j=0;j<result.size();j++){
			if(label.containsKey(result.get(j))){
				count.put(label.get(result.get(j)), count.get(label.get(result.get(j)))+1);
			}
		}
		}
	      ArrayList<Map.Entry<Integer,Integer>> list_Data = new ArrayList<Map.Entry<Integer, Integer>>(count.entrySet());  
	  	    
	    Collections.sort(list_Data, new Comparator<Map.Entry<Integer,Integer>>(){    
	      public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2)  
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
  	    for (int i=0;i<list_Data.size();i++){
  	    	
  	    	System.out.print(list_Data.get(i).getKey()+",");
  	    }  
	}
}