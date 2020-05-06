
package RandomForest_J48;

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
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.CSVLoader;


public class J48VoteChange {
	
	  public List getresult(String path, List<Instance>instancedatas,int guess,List<String>medians) throws Exception {
		 Map<Integer, Integer> appear = new HashMap<Integer, Integer>(); 

  
	      Classifier j48 = (Classifier) weka.core.SerializationHelper.read(path);
	      
		      for (int i = 0; i < instancedatas.size()-1; i++) {
		    	  
		          List<Integer>guess_list=new ArrayList<Integer>();
		  	    Integer[] arr = new Integer[guess];  
		    	  for (int m = 0; m < arr.length; m++) {
		  	        arr[m] = m;
		  	    }
		  	    Collections.shuffle(Arrays.asList(arr));
		  	    
		  	    for(int g=0;g<guess;g++){
		      	guess_list.add(arr[g]);
		      }
		    	  
		    	//  System.out.println(prediction(m_data,m_data.instance(i),m_Classifiers));
                 if (j48.classifyInstance(instancedatas.get(i))==instancedatas.get(i).classValue()){

                	 for (int j =0;j<guess;j++ ){ 

                		 int position=guess_list.get(j);  	
                		 String replace=medians.get(position);
              		 
                		 if (instancedatas.get(i).attribute(position).isNominal()){
                	 instancedatas.get(i).setValue(position, replace);
                		 }else{
                	 instancedatas.get(i).setValue(position, Double.parseDouble(replace));  
                		 }
                		 

                	    if (j48.classifyInstance(instancedatas.get(i))!=instancedatas.get(i).classValue()){
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
		      ArrayList<Map.Entry<Integer,Integer>> list_Data = new ArrayList<Map.Entry<Integer, Integer>>(appear.entrySet());  
		  	    
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
		  	  List<Integer>result = new ArrayList<Integer>();
		  	    for (int i=0;i<list_Data.size();i++){
		  	    	
		  	    	result.add(list_Data.get(i).getKey());
			  	    System.out.println(list_Data.get(i).getKey()+":"+list_Data.get(i).getValue());
		  	    }   
		  	    return result;

	  }
	  
		 	  

	public static void main(String[] args) throws Exception {
		List<Integer>finalresult=new ArrayList<Integer>(); 
      	BufferedReader br2 = new BufferedReader(new FileReader(new File("./data/attack/spambase/model/spambase1.data"))); 
        String[] labels = br2.readLine().split(",");
        br2.close();
		List<List<Integer>>result=new ArrayList<List<Integer>>();

        List<String>medians=new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/attack/spambase/attack/output_C45.txt")));
		String[] output=br.readLine().split(",");
		for (int i=0;i<output.length;i++){
			medians.add(output[i]);
		}
		
        int guess=57;
		int numTest=100;

		
        List<Integer>guess_list=new ArrayList<Integer>();
        List<Instance>instancedatas= new ArrayList<Instance>();
        
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File("./data/attack/spambase/model/spambase1.data"));
		Instances attack_data = loader.getDataSet();
		attack_data.setClassIndex(attack_data.numAttributes() - 1);
	    Integer[] arr = new Integer[attack_data.numAttributes()-1];
	    
		CSVLoader loader1 = new CSVLoader();
		loader1.setSource(new File("./data/attack/spambase/model/spambase2.data"));
		Instances model_data = loader1.getDataSet();
		model_data.setClassIndex(model_data.numAttributes() - 1);

	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
	    }
	    Collections.shuffle(Arrays.asList(arr));
	    
	    for(int i=0;i<guess;i++){
    	guess_list.add(arr[i]);
    }

	    J48 j48 = new J48();

		/*
		for (int i=0;i<options.length;i++){
		System.out.println(options[i]);
		}
		*/
		j48.setMinNumObj(100);
//		j48.setNumFolds(20);
		j48.setUnpruned(false);

		j48.buildClassifier(model_data);


	/*    
        String candidate = "0,1,3,4,5,6,7";
		String []items = candidate.split(",");
	    
		for (int i=0;i<items.length;i++){
			 models_w.add(Integer.parseInt(items[i]));	
	     }
	  */   
		 for (int i=0;i<numTest;i++){
		     instancedatas.add(attack_data.get(i));	

		 }
	    instancedatas.add(attack_data.get(attack_data.numInstances()-1));


	    J48VoteChange jvc = new J48VoteChange();
	    finalresult= jvc.getresult("./data/attack/spambase/model/spambase2.data", instancedatas,guess,medians);
	    for (int i=0;i<finalresult.size();i++){
	   System.out.println( labels[finalresult.get(i)]);
	    }

	}
}
