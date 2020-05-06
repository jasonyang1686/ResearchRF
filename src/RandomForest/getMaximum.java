package RandomForest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.CSVLoader;

public class getMaximum {
	public void BuildClassifier(int iteration,String path,Map<String,Integer> appear,List<String>classes) throws Exception {
		List<Double>accuracy=new ArrayList<Double>();
        BufferedReader br = new BufferedReader(new FileReader(new File(path+"/w_result.txt"))); 	
        String nodeweight=br.readLine();
		while(nodeweight!=null){
			accuracy.add(1.0-Double.parseDouble(nodeweight));
			nodeweight=br.readLine();
		}
		br.close();		
		Double total=0.0;
		int totalvalue=0;
		List<Integer>record=new ArrayList<Integer>();
		for (int i=0;i<iteration;i++){
//			System.out.println(totalvalue);
			int value =0;
			List<String> result =process(path+"w_models/Classifier"+i+".model", classes);
			for (int k=0;k<result.size();k++){
				if (appear.containsKey(result.get(k))){
					value+=1;				
			}		
		}
			if (total <selection(i,accuracy,(totalvalue+value))){
				total+=selection(i,accuracy,(totalvalue+value));
				totalvalue+=value;
				record.add(i);
			}
//			System.out.println(value);
		}
		for (int i=0;i<record.size();i++){
		System.out.println(record.get(i));
		}
	}
	
	public double selection(int i, List<Double>accuracy, int totalvalue){
		double sum=0.0;
            for (int k=0;k<i;k++){
            	sum+=accuracy.get(k);
            }
            return sum*((double)totalvalue);
	}
	public List<String> process(String path, List<String> classlabels) throws Exception{
		Map<String, Integer> appear = new HashMap<String, Integer>();
		Classifier cls = (Classifier) weka.core.SerializationHelper.read(path);
		String[]contents = cls.toString().split("\n");
		for (int i=0;i<contents.length;i++){
			for (int k=0;k<classlabels.size();k++){
				//careful, there is a space!
			  	if (contents[i].contains(classlabels.get(k)+" ")){
			  		appear.put(classlabels.get(k), 1);
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
  	//    System.out.println(result.size());
  	    return result;
        
	}
	public static void main(String[] args) throws Exception {
		int iteration=20;
		String path = "./data/5,20/";
		Map<String, Integer> appear = new HashMap<String, Integer>();
		List<String>classes = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(new File("./data/spambase/spambase.data"))); 
        String[] labels = br.readLine().split(",");
        br.close();
        for(int i=0;i<labels.length;i++){
        	appear.put(labels[i], 0);
        	classes.add(labels[i]);
        }
        
		getMaximum gm = new getMaximum();
		gm.BuildClassifier(iteration,path,appear,classes);
	}

}