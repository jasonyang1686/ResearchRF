package RandomForest_spambase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import RandomForest_Kyoto.VoteChange;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffLoader;

public class VoteCount {
	
	  public int getresult(Classifier[]m_Classifiers, Instance instanceData,int numAttributes, List<String>medians) throws Exception {
		 Map<Integer, Double> count = new HashMap<Integer, Double>(); 
  
		    
		      for (int i = 0; i < numAttributes; i++) {
		          Instance testData = (Instance) instanceData.copy();
		    	  int previous = getVotes(testData,m_Classifiers);
		    	  if (!testData.attribute(i).isNumeric()){
                      testData.setValue(i, medians.get(i));
            		 }else {
            		  testData.setValue(i, Double.parseDouble(medians.get(i)));  
            		 }
		    	  int after = getVotes(testData,m_Classifiers);
		    	  
		  		  if(i==0) {
	            	     count.put(i, (Math.abs(previous-after))/1.0);  
		   		  }
		  		  if(i==1) {
	            	     count.put(i, (Math.abs(previous-after))/1.0);  
		   		  }
		  		  if(i==2) {
	            	     count.put(i, (Math.abs(previous-after))/50000.0);  
		   		  }
		  		  if(i==3) {
	            	     count.put(i, (Math.abs(previous-after))/3.0);  
		   		  }
		  		  if(i==4) {
	            	     count.put(i, (Math.abs(previous-after))/3.0);  
		   		  }
		  		  if(i==5) {
	            	     count.put(i, (Math.abs(previous-after))/3.0);  
		   		  }
		  		  if(i==6) {
	            	     count.put(i, (Math.abs(previous-after))/3.0);  
		   		  }
		  		  if(i==7) {
	            	     count.put(i, (Math.abs(previous-after))/3.0);  
		   		  }
		  		  if(i==8) {
	            	     count.put(i, (Math.abs(previous-after))/3.0);  
		   		  }
		  		  if(i==9) {
	            	     count.put(i, (Math.abs(previous-after))/3.0);  
		   		  }
		  		  if(i==10) {
	            	     count.put(i, (Math.abs(previous-after))/3.0);  
		   		  }
		  		  if(i==11) {
	            	     count.put(i, (Math.abs(previous-after))/1.0);  
		   		  }
		  		  if(i==12) {
	            	     count.put(i, (Math.abs(previous-after))/50000.0);  
		   		  } 
		    }


		      ArrayList<Map.Entry<Integer,Double>> list_Data = new ArrayList<Map.Entry<Integer, Double>>(count.entrySet());  
		  	    
		  	    Collections.sort(list_Data, new Comparator<Map.Entry<Integer,Double>>(){    
		  	      public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2)  
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
		//   System.out.println("position: "+list_Data.get(0).getKey());
		   int first = list_Data.get(0).getKey();
//		   if (first !=2 && first !=12) {
//			   return first;
//		   }else {
//			   return list_Data.get(1).getKey();
//		   }
	       return list_Data.get(0).getKey();
	  }
	
	public int getVotes(Instance data,Classifier[]m_Classifiers) throws Exception {
	    Map<String, Integer> label = new HashMap<String, Integer>();	
	    label.put("0.0", 0);
	    label.put("1.0",0);
        for (int j = 0; j < m_Classifiers.length; j++) {
        double pred = m_Classifiers[j].classifyInstance(data);

        	label.put(pred+"", label.get(pred+"")+1);      
     //   System.out.println(pred);
        }
        /*
	      ArrayList<Map.Entry<String,Integer>> list_Data = new ArrayList<Map.Entry<String, Integer>>(label.entrySet());  
	      
	  	    for (int i=0;i<list_Data.size();i++){
	  	    
		 	    System.out.println(list_Data.get(i).getKey()+":"+list_Data.get(i).getValue());
	  	    }
	  	  */  
	  	    int result = label.get("0.0");
		//    System.out.println(result);
	  	    return result;

	      
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

	            if (!Utils.isMissingValue(pred)) {
	              votes[0] += pred;
	              voteCount++;
	            }
	          } else {
	       // 	System.out.println(j);
	            voteCount++;
	            double[] newProbs = m_Classifiers[j].distributionForInstance(data);
	            // average the probability estimates
	            for (int k = 0; k < newProbs.length; k++) {
	              votes[k] += newProbs[k];
	            }
	          }
	        }
	        
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
		int numRandomTree=100;	    
        List<String>medians=new ArrayList<String>();
        
		BufferedReader br = new BufferedReader(new FileReader(new File(
		//		"./data/kyoto/testing/output_normal.txt")));
				"./data/spambase/testing/output_normal.txt")));
		String[] output=br.readLine().split(",");
		for (int i=0;i<output.length;i++){
			medians.add(output[i]);
		}
		br.close();
		
        ArffLoader loader = new ArffLoader();		
		loader.setSource(new File("./data/spambase/testing/spambase_attack_normal.arff"));
	//	loader.setSource(new File("./data/kyoto/testing/201501_twodays_test_removal_binary_balance_sampling.arff"));
		Instances m_data = loader.getDataSet();
		m_data.setClassIndex(m_data.numAttributes() - 1);
		loader.reset();
		
        int numAttributes=m_data.numAttributes() - 1;	
		loader.setSource(new File("./data/spambase/testing/spambase_all_normal.arff")); 
	//	loader.setSource(new File("./data/kyoto/testing/201512_week_removal_binary_balance_sampling.arff"));
		Instances all_data = loader.getDataSet();
		all_data.setClassIndex(all_data.numAttributes() - 1);
			
	//	String path = "./data/kyoto/models/";
		String path = "./data/spambase/models/";
		
	    List<Integer>models = new ArrayList<Integer>();		
	    for (int i=0;i<numRandomTree;i++){
		     models.add(i);	
		 }
		 
		 
			Classifier[]m_Classifiers= new Classifier[models.size()];
			
		      for (int m=0;m< m_Classifiers.length; m++){
		    	  m_Classifiers[m] = (Classifier) weka.core.SerializationHelper.read(path+"Classifier"+models.get(m)+".model");
		      } 
        
		 VoteCount vc = new VoteCount();
		 
         int count =0;
		 int position = vc.getresult(m_Classifiers, m_data.get(0), numAttributes, medians); 
   	  if (! m_data.get(0).attribute(position).isNumeric()){
   		 m_data.get(0).setValue(position, medians.get(position));
		 }else {
		 m_data.get(0).setValue(position, Double.parseDouble(medians.get(position)));  
		 }
   	    for (int i=0;i<numAttributes;i++) {
		 if(vc.prediction(m_data.get(0),m_Classifiers)==0.0) {
		  count++;	 
		  position = vc.getresult(m_Classifiers, m_data.get(0), numAttributes, medians);
		  if (! m_data.get(0).attribute(position).isNumeric()){
		   		 m_data.get(0).setValue(position, medians.get(position));
				 }else {
				 m_data.get(0).setValue(position, Double.parseDouble(medians.get(position)));  
				 }
		 }else {
		  break;
		 }
   	    }
         System.out.println(count);
	     System.out.println(m_data.get(0).toString()); 
	     
	}
}
