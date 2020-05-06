
package RandomForest_KDD_binary;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;


public class SmarterAttacker_Sampling {
	
	  public List <Double>getresult(String path,List<Integer>models, Instances m_data,int guess, List<String>medians,List<Integer>real_guess_list) throws Exception {
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
        int numTest=50;
		
	    Map<String, Integer> label = new HashMap<String, Integer>();	
		List<List<Double>>result=new ArrayList<List<Double>>();
		
		for (int l=0;l<loop;l++){
	    List<Instance>instancedatas= new ArrayList<Instance>();	
	    
        List<String>medians=new ArrayList<String>();
        
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/attack/kddcup99_B/attack/output.txt")));
		String[] output=br.readLine().split(",");
		for (int i=0;i<output.length;i++){
			medians.add(output[i]);
		}
		br.close();
		
        List<Integer>guess_list=new ArrayList<Integer>();

        ArffLoader loader = new ArffLoader();		
		loader.setSource(new File("./data/attack/kddcup99_B/attack/kddcup99_non_duplicate_10_percent_attack_B.arff"));
		Instances m_data = loader.getDataSet();
		m_data.setClassIndex(m_data.numAttributes() - 1);
		loader.reset();
		
        int guess=m_data.numAttributes() - 1;	
        
		loader.setSource(new File("./data/attack/kddcup99_B/attack/kddcup99_non_duplicate_10_percent_all_B.arff"));
		Instances all_data = loader.getDataSet();
		all_data.setClassIndex(all_data.numAttributes() - 1);
		
	    Integer[] arr = new Integer[m_data.numAttributes()-1];
	    
	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
    }
	    Collections.shuffle(Arrays.asList(arr));
	    
	    for(int i=0;i<guess;i++){
    	guess_list.add(arr[i]);
    }
	
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
		 
		
	    Integer[]arr_2= new Integer[m_data.numInstances()];
	    for (int i = 0; i < arr_2.length; i++) {
	        arr_2[i] = i;
    }
	    Collections.shuffle(Arrays.asList(arr_2));
	    
		 for (int i=0;i<numTest;i++){
		     instancedatas.add((Instance) m_data.instance(arr_2[i]).copy());	
		 }
         
		 VoteChange vc = new VoteChange();
		 List<Integer>candidate_list=new ArrayList<Integer>();
		 
		 candidate_list=vc.getresult(path, models,instancedatas,guess,medians);
			
		 for (Iterator<Integer> iter = guess_list.listIterator(); iter.hasNext(); ) {
			    int a = iter.next();
			    for (int i=0;i<candidate_list.size();i++){
			    if (a==candidate_list.get(i)) {
			        iter.remove();
			    }
			    }
			}
		         
		 List<Integer>real_guess_list= new ArrayList<Integer>();

		 for (int i=0;i<candidate_list.size();i++){
			 real_guess_list.add(candidate_list.get(i));
		 }
		
		 for (int i=0;i<guess_list.size();i++){
			 real_guess_list.add(guess_list.get(i));
		 }

		 
	     SmarterAttacker_Sampling sa = new SmarterAttacker_Sampling();
	     
         result.add(sa.getresult(path,models,all_data,guess,medians,real_guess_list));
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
