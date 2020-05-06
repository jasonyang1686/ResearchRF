
package RandomForest_KDD_binary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffLoader;


public class SmarterAttacker_GroundTruth {
	
	  public List<Double> getresult(String path,List<Integer>models, Instances m_data,int guess, List<String>medians,List<Integer>real_guess_list) throws Exception {
            List<Double>med_result=new ArrayList<Double>();
			Classifier[]m_Classifiers= new Classifier[models.size()];

			List<String> instanceresult=new ArrayList<String>();

		    	  
			      for (int m=0;m< m_Classifiers.length; m++){
			    	  
			    	  m_Classifiers[m] = (Classifier) weka.core.SerializationHelper.read(path+"Classifier"+m+".model");
			      }  

              double total=0;
		      int num_fn=0;
		      int num_p=0;
		      int num_fp=0;
		      int num_n=0;
		      for (int i = 0; i < m_data.numInstances(); i++) {
		    	 
		    	  if (m_data.instance(i).classValue()==0.0){
		    		  num_p++;
		    	  }else{
		    		  num_n++;
		    	  }

                 if (prediction(m_data,m_data.instance(i),m_Classifiers)==m_data.instance(i).classValue()){

               if (m_data.instance(i).classValue()==0.0){
                	 for (int j =0;j<guess;j++ ){ 
                		 int position=real_guess_list.get(j);  	
                		 String replace=medians.get(position);
                		
                		 total++;
                		 if (!m_data.instance(i).attribute(position).isNumeric()){
                	
                           	  m_data.instance(i).setValue(position, replace);

                		 }else {
                	  m_data.instance(i).setValue(position, Double.parseDouble(replace));  
                		 }
                	 
                	 if (prediction(m_data,m_data.instance(i),m_Classifiers)!=m_data.instance(i).classValue()){  
                    	 instanceresult.add(i+":"+j);
                		 break;
                	 }
                	 }  
               } 
                 }else{
                	 if (m_data.instance(i).classValue()==0.0){
                	 num_fn++;
                	 }else{
                	 num_fp++;	 
                	 }
                 }
		    }

		//	    System.out.println(num_fn);
		//	    System.out.println(num_fp); 
			    med_result.add((num_p-num_fn+0.0)/(num_p+0.0));
			    med_result.add((num_fp)/(num_n+0.0));
			    med_result.add(total/(num_p-num_fn+0.0));
			    return med_result;
	  }
	  public double prediction(Instances m_data,Instance data,Classifier[]m_Classifiers ) throws Exception{
		  
	      boolean numeric = m_data.classAttribute().isNumeric();
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
		int numTree=100;
		int numRandomTree=100;
		int loop=100;		

			
		List<List<Double>>result=new ArrayList<List<Double>>();
		
		for (int l=0;l<loop;l++){
	    
        List<String>medians=new ArrayList<String>();
        
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/attack/kddcup99_B/attack/output.txt")));
		String[] output=br.readLine().split(",");
		for (int i=0;i<output.length;i++){
			medians.add(output[i]);
		}
		br.close();
		

        ArffLoader loader = new ArffLoader();		
		loader.setSource(new File("./data/attack/kddcup99_B/attack/kddcup99_non_duplicate_10_percent_attack_B.arff"));
		Instances m_data = loader.getDataSet();
		m_data.setClassIndex(m_data.numAttributes() - 1);
		loader.reset();
		
        int guess=m_data.numAttributes() - 1;	
        
		loader.setSource(new File("./data/attack/kddcup99_B/attack/kddcup99_non_duplicate_10_percent_all_B.arff"));
		Instances all_data = loader.getDataSet();
		all_data.setClassIndex(all_data.numAttributes() - 1);
	
		String path = "./data/attack/kddcup99_B/model/5,50/w_models/";
		
	    List<Integer>models = new ArrayList<Integer>();

	    Integer[] arr_1 = new Integer[numRandomTree];
	    for (int i = 0; i < arr_1.length; i++) {
	        arr_1[i] = i;
    }
	    Collections.shuffle(Arrays.asList(arr_1));
		
	    for (int i=0;i<100;i++){
		     models.add(arr_1[i]);	
		 }
		 

			         
	 List<Integer>real_guess_list= new ArrayList<Integer>();
	 
		 BufferedReader br1 = new BufferedReader(new FileReader(new File(
					"./data/attack/kddcup99_B/model/5,50/temp/ground_truth.txt")));
		 String static_weight=br1.readLine();
		 while(static_weight!=null){
			String[] weights=static_weight.split(":");
				real_guess_list.add(Integer.parseInt(weights[0]));
				static_weight=br1.readLine();
			}
			br1.close();
					 
	     SmarterAttacker_GroundTruth sag = new SmarterAttacker_GroundTruth();
	     
         result.add(sag.getresult(path,models,all_data,guess,medians,real_guess_list));
		}
		
		double total=0;
		double total_w=0;
		double total_try=0.0;
		for (int i=0;i<result.size();i++){
			total+=result.get(i).get(0);
			total_w+=result.get(i).get(1);
			total_try+=result.get(i).get(2);
		}
		System.out.println(total/(loop+0.0)+","+total_w/(loop+0.0)+","+total_try/(loop+0.0));
	}
}
