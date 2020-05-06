
package RandomForest_LR;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;


public class GroundTruth {
	
	  public Map<Integer, Integer> getresult(Logistic LR, List<Instance>instancedatas,int guess, List<String>medians) throws Exception {
		 Map<Integer, Integer> appear = new HashMap<Integer, Integer>(); 

	      
		      for (int i = 0; i < instancedatas.size(); i++) {
		    	  
		    	//  System.out.println(instancedatas.get(i));
		          List<Integer>guess_list=new ArrayList<Integer>();
		  	    Integer[] arr = new Integer[guess];  
		    	  for (int m = 0; m < arr.length; m++) {
		  	        arr[m] = m;
		  	    }
		  	    Collections.shuffle(Arrays.asList(arr));
		  	    
		  	    for(int g=0;g<guess;g++){
		      	guess_list.add(arr[g]);
		      }


                 if (LR.classifyInstance(instancedatas.get(i))==instancedatas.get(i).classValue()){
       		 // 	  System.out.println(i+":"+prediction(instancedatas.get(i),m_Classifiers)); 
                	  
                     List<Double>sequence=new ArrayList<Double>();
                	 for (int j =0;j<guess;j++ ){ 
                		 int position=guess_list.get(j);  	
                		 String replace=medians.get(position);

                		 if (!instancedatas.get(i).attribute(position).isNumeric()){
                	 instancedatas.get(i).setValue(position, replace);
                		 }else{
                	 instancedatas.get(i).setValue(position, Double.parseDouble(replace));  
                		 }
                  	    if (LR.classifyInstance(instancedatas.get(i))!=instancedatas.get(i).classValue()){
                 	    	if (appear.containsKey(position)){
                 	    		appear.put(position, appear.get(position)+1);
                 	    	}else{
                 	    		appear.put(position, 1);
                 	    	}
                 	    	break;                	    	
                 	    }
                	 } 
                	
                 }

		    }

		  	    return appear;

	  }
	  	  

		 	  

	public static void main(String[] args) throws Exception {

		 Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		 Map<Integer, Integer> final_result = new HashMap<Integer, Integer>();
		String path = "./data/attack/kddcup99_LR/model/kddcup_model_w.data";

        List<String>medians=new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/attack/kddcup99_B/attack/output.txt")));
		String[] output=br.readLine().split(",");
		for (int i=0;i<output.length;i++){
			final_result.put(i, 0);
			medians.add(output[i]);
		}
		br.close();

		int loop =40;
 for(int l=0;l<loop;l++){
        List<Instance>instancedatas= new ArrayList<Instance>();
       
        ArffLoader loader = new ArffLoader();		
		loader.setSource(new File("./data/attack/kddcup99_B/attack/kddcup99_non_duplicate_10_percent_attack_B.arff"));
		Instances m_data = loader.getDataSet();
		m_data.setClassIndex(m_data.numAttributes() - 1);
        int guess=m_data.numAttributes() - 1;
        
		loader.reset();
		
        CSVLoader loader1 = new CSVLoader();	
		loader1.setSource(new File(path));
		Instances data = loader1.getDataSet();
		data.setClassIndex(data.numAttributes()-1);
		Logistic LR = new Logistic();
		LR.buildClassifier(data);
	    
	    Integer[]arr= new Integer[m_data.numInstances()];
	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
    }
	    Collections.shuffle(Arrays.asList(arr));
	    
		 for (int i=0;i<m_data.numInstances();i++){
		     instancedatas.add((Instance) m_data.instance(arr[i]).copy());	
		 }	


	    GroundTruth gt = new GroundTruth();
	    result=gt.getresult(LR,instancedatas,guess,medians);
	    
		for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
			final_result.put(entry.getKey(), final_result.get(entry.getKey())+entry.getValue());
		}
 }
		
	      ArrayList<Map.Entry<Integer,Integer>> list_Data = new ArrayList<Map.Entry<Integer, Integer>>(final_result.entrySet());  
	  	    
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
		  	    System.out.println(list_Data.get(i).getKey()+":"+list_Data.get(i).getValue());
	  	    }   
		
	}
}
