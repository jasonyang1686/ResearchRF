package RandomForest_spambase;

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


public class classDistribution {
	public List<String> process(String path, List<Integer>pathlist,List<String> classlabels) throws Exception{
		Map<String, Integer> count = new HashMap<String, Integer>();	
		for (int p=0;p<pathlist.size();p++){
			
		
		Map<String, Integer> appear = new HashMap<String, Integer>();
		Classifier cls = (Classifier) weka.core.SerializationHelper.read(path+"Classifier"+pathlist.get(p)+".model");
		String[]contents = cls.toString().split("\n");
		
		for (int i=0;i<contents.length;i++){
			for (int k=0;k<classlabels.size();k++){
				//careful, there is a space!
			  	if (contents[i].contains(classlabels.get(k)+" ")){
			  		
			  		appear.put(classlabels.get(k), 1);
			  	}
			}

		}
		for (Map.Entry<String, Integer> entry : appear.entrySet()) {

		if(count.containsKey(entry.getKey())){
			count.put(entry.getKey(),count.get(entry.getKey())+1);
		}else{
			count.put(entry.getKey(), 1);
		}
				
		}
		}
	      ArrayList<Map.Entry<String,Integer>> list_Data = new ArrayList<Map.Entry<String, Integer>>(count.entrySet());  
  	    
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
  	    	
  	    	result.add(list_Data.get(i).getKey()+","+list_Data.get(i).getValue());
  	    }   
  	    System.out.println(result);
  	    return result;
        
	}
public static void main(String[] args) throws Exception {
	List<Integer> pathlist = new ArrayList<Integer>();
	List<String>classlabels = new ArrayList<String>();
	Map<String, Integer> label = new HashMap<String, Integer>();
	Map<Integer, Integer> count = new HashMap<Integer, Integer>();	
	BufferedReader br2 = new BufferedReader(
			new FileReader(new File("./data/spambase/training/spambase_model_normal.data")));
	//		new FileReader(new File("./data/kyoto/training/201512_tendays_removal_binary_balance_sampling.csv")));
	String[] labels = br2.readLine().split(",");
	br2.close();
	for (int i = 0; i < labels.length - 1; i++) {
		classlabels.add(labels[i]);
	}
	String path = "./data/spambase/models/";
		for (int i=0;i<100;i++){
			pathlist.add(i);
		}
	    List<String>result = new ArrayList<String>();	
		classDistribution cd=new classDistribution();
		result=cd.process(path,pathlist, classlabels);

		for(int j=0;j<result.size();j++){
			if(label.containsKey(result.get(j))){
				count.put(label.get(result.get(j)), count.get(label.get(result.get(j)))+1);
			}
		}

	}
	
}