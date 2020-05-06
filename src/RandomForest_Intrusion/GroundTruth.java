
package RandomForest_Intrusion;

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
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffLoader;


public class GroundTruth {
	
	  public Map<Integer, Integer> getresult(String path, List<Integer>models, List<Instance>instancedatas,int guess, List<String>medians) throws Exception {
		 Map<Integer, Integer> appear = new HashMap<Integer, Integer>(); 

			Classifier[]m_Classifiers= new Classifier[models.size()];
			
		      for (int m=0;m< m_Classifiers.length; m++){
		    	  m_Classifiers[m] = (Classifier) weka.core.SerializationHelper.read(path+"Classifier"+models.get(m)+".model");
		      }    
	      
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


                 if (prediction(instancedatas.get(i),m_Classifiers)==instancedatas.get(i).classValue()){
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
                  	    if (prediction(instancedatas.get(i),m_Classifiers)!=instancedatas.get(i).classValue()){
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
	  	  
	  public double prediction(Instance data,Classifier[]m_Classifiers) throws Exception{
		  
	      boolean numeric = data.classAttribute().isNumeric();
	        double vote;
	        double[] votes;
	        if (numeric)
	          votes = new double[1];
	        else
	          votes = new double[2];
	        
	        // determine predictions for instance
	        int voteCount = 0;

	        for (int j = 0; j < m_Classifiers.length; j++) {

	          if (numeric) {
	            double pred = m_Classifiers[j].classifyInstance(data);
	         //   System.out.println(pred);

	            if (!Utils.isMissingValue(pred)) {
	              votes[0] += pred;
	              voteCount++;
	            }
	          } else {

	            voteCount++;
	       //     System.out.println(data.toString());
	            double[] newProbs = m_Classifiers[j].distributionForInstance(data);
	            // average the probability estimates
	            for (int k = 0; k < newProbs.length; k++) {
	    	//     System.out.println(newProbs[k]);	
	              votes[k] += newProbs[k];
	            }
	          }
	        }
	   
	    //    System.out.println("size :"+result.size());
	        
	        // "vote"

	        if (numeric) {
	          if (voteCount == 0) {
	            vote = Utils.missingValue();
	          } else {
	            vote = votes[0] / voteCount;    // average
	          }
	        } else {
	          if (Utils.eq(Utils.sum(votes), 0)) {            
	            vote = Utils.missingValue();
	          } else {
	            vote = Utils.maxIndex(votes);   // predicted class
	            Utils.normalize(votes);
	          }
	        }
	               
    //    System.out.println(vote);
        return vote;
	        
	      } 
		  
	  

	public static void main(String[] args) throws Exception {

		 Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		 Map<Integer, Integer> final_result = new HashMap<Integer, Integer>();

        List<String>medians=new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/attack/spambase/attack/output.txt")));
		String[] output=br.readLine().split(",");
		for (int i=0;i<output.length;i++){
			final_result.put(i, 0);
			medians.add(output[i]);
		}
		br.close();
		
		int numTree=100;
		int loop =20;
 for(int l=0;l<loop;l++){
        List<Instance>instancedatas= new ArrayList<Instance>();
       
        ArffLoader loader = new ArffLoader();		
		loader.setSource(new File("./data/attack/spambase/attack/spambase_attack.arff"));
		Instances m_data = loader.getDataSet();
		m_data.setClassIndex(m_data.numAttributes() - 1);
        int guess=m_data.numAttributes() - 1;

		String path = "./data/attack/spambase/model/5,50/models/";

	    List<Integer>models = new ArrayList<Integer>();

	    
	    Integer[]arr= new Integer[m_data.numInstances()];
	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
    }
	    Collections.shuffle(Arrays.asList(arr));
	    
		 for (int i=0;i<m_data.numInstances();i++){
		     instancedatas.add((Instance) m_data.instance(arr[i]).copy());	
		 }	

		 for (int i=0;i<numTree;i++){
		     models.add(i);			     
		 }


	    GroundTruth gt = new GroundTruth();
	    result=gt.getresult(path, models,instancedatas,guess,medians);
	    
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
