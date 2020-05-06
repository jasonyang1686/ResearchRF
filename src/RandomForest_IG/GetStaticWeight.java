package RandomForest_IG;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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

//step 2, get static weights
public class GetStaticWeight {
	public void process(String path,List<String>classlabels) throws IOException{
        int numCount=100;

		Map<Integer,Double>result=new HashMap<Integer,Double>();  
		for (int i=0;i<classlabels.size();i++){
			result.put(i, 0.0);
		}
		Map<Integer,Double>medians=new HashMap<Integer,Double>();
    
		for(int i=1;i<=numCount;i++){
	    BufferedReader br = new BufferedReader(new FileReader(new File(path+"Val"+i+".txt"))); 

	    String data = br.readLine(); 
	    while (data != null) {
             String[]content=data.split(":");
  	        	   if(medians.containsKey(Integer.parseInt(content[0]))){
  	        		   if(medians.get(Integer.parseInt(content[0]))<=Double.parseDouble(content[1])){
  	        			   medians.put(Integer.parseInt(content[0]), Double.parseDouble(content[1]));
  	        		   }
  	        	   }else{
  	        			 medians.put(Integer.parseInt(content[0]), Double.parseDouble(content[1]));
  	        		   }   
  	            	
	    	data=br.readLine();	    
	}
	    br.close();	 
	    
		for (Map.Entry<Integer, Double> entry : medians.entrySet()) {
			result.put(entry.getKey(), result.get(entry.getKey()) + entry.getValue());
		}
         }
         ArrayList<Map.Entry<Integer, Double>> list_Data = new ArrayList<Map.Entry<Integer, Double>>(
 				result.entrySet());

 		Collections.sort(list_Data, new Comparator<Map.Entry<Integer, Double>>() {
 			public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
 				if ((o2.getValue() - o1.getValue()) > 0)
 					return 1;
 				else if ((o2.getValue() - o1.getValue()) == 0)
 					return 0;
 				else
 					return -1;
 			}
 		});

 		for (int i = 0; i < list_Data.size(); i++) {
 			 System.out.println(classlabels.get(list_Data.get(i).getKey())+","+list_Data.get(i).getValue());
 
 		}
	}
public static void main(String[] args) throws Exception {
		String path = "./data/spambase/IG/Vals/";
		List<String>classlabels = new ArrayList<String>();		
		 BufferedReader br = new BufferedReader(new FileReader(new File("./data/spambase/training/spambase_model_normal.data"))); 
	     String[] labels = br.readLine().split(",");
	     br.close();
	     for (int i=0;i<labels.length-1;i++){
	     	classlabels.add(labels[i]); 
	     }

		GetStaticWeight gw=new GetStaticWeight();
		gw.process(path,classlabels);
			
	}
}