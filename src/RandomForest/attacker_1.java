
package RandomForest;

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
import java.util.List;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.CSVLoader;


public class attacker_1 {
	
	  public List getresult(String path, List<Integer>models, List<Integer>models_w,int numTree, Instances m_data,Instances m_data_w, int guess,List<Integer>guess_list) throws Exception {
            List<Integer>med_result=new ArrayList<Integer>();
            int count=0;
            int count_w=0;
            
            List<Instance>pass=new ArrayList<Instance>();
            List<Instance>pass_w=new ArrayList<Instance>();
            List<Double>median=new ArrayList<Double>();
            List<Double>median_w=new ArrayList<Double>();
            for (int i=0;i<m_data.numAttributes()-1;i++){
            	median.add(0.0);
            	median_w.add(0.0);
            }
            
			Classifier[]m_Classifiers= new Classifier[models.size()];
			Classifier[]m_Classifiers_w= new Classifier[models_w.size()];
		      for (int m=0;m< m_Classifiers.length; m++){
		    	  m_Classifiers[m] = (Classifier) weka.core.SerializationHelper.read(path+"models/Classifier"+models.get(m)+".model");
		      }    
		      for (int m=0;m< m_Classifiers_w.length; m++){
		    	  m_Classifiers_w[m] = (Classifier) weka.core.SerializationHelper.read(path+"w_models/Classifier"+models_w.get(m)+".model");
		      }
		      for (int i = 0; i < m_data.numInstances(); i++) {
		    	  if (prediction(m_data,m_data.instance(i),m_Classifiers)!=m_data.instance(i).classValue()){
		    		  pass.add(m_data.instance(i));
		    		  count++;
		    	  }
		      }
		      
		      for(int i=0;i<pass.size();i++){
		    	  String[]ins=pass.get(i).toString().split(",");
		    	  for (int j=0;j<ins.length-1;j++){
			    	  median.set(j, median.get(j)+Double.parseDouble(ins[j]));
		    	  }

		      }
		      for (int i=0;i<median.size();i++){
		    	  median.set(i, median.get(i)/count);
		      }
		      
		      for (int i = 0; i < m_data_w.numInstances(); i++) {
		    	  if (prediction(m_data_w,m_data_w.instance(i),m_Classifiers_w)!=m_data_w.instance(i).classValue()){
		    		  pass_w.add(m_data_w.instance(i));
		    		  count_w++;
		    	  }
		      }
		      for(int i=0;i<pass_w.size();i++){
		    	  String[]ins=pass_w.get(i).toString().split(",");
		    	  for (int j=0;j<ins.length-1;j++){
			    	  median_w.set(j, median_w.get(j)+Double.parseDouble(ins[j]));
		    	  }

		      }
		      for (int i=0;i<median_w.size();i++){
		    	  median_w.set(i, median_w.get(i)/count);
		      }
		 
 
              int total=0;
              int total_w=0;
		      int num_correct=0;     
		      for (int i = 0; i < m_data.numInstances(); i++) {
		    	//  System.out.println(prediction(m_data,m_data.instance(i),m_Classifiers));
                 if (prediction(m_data,m_data.instance(i),m_Classifiers)==m_data.instance(i).classValue()){
                     num_correct++;
                	 for (int j =0;j<guess;j++ ){ 
                		 total++;
                	 m_data.instance(i).setValue(guess_list.get(j), median.get(guess_list.get(j)));
                	 if (prediction(m_data,m_data.instance(i),m_Classifiers)!=m_data.instance(i).classValue()){               
                		 break;
                	 }
                	 }  
            		 
                 }
		    }
			    System.out.println("number of correction_models:"+num_correct+"/"+m_data.numInstances());
			    System.out.println(total); 
			    num_correct=0;
		      for (int i = 0; i < m_data_w.numInstances(); i++) {
		    	//  System.out.println(prediction(m_data,m_data.instance(i),m_Classifiers));
                 if (prediction(m_data_w,m_data_w.instance(i),m_Classifiers_w)==m_data_w.instance(i).classValue()){
                	 num_correct++;
                	 for (int j =0;j<guess;j++ ){
                		 total_w++;

                	 m_data_w.instance(i).setValue(guess_list.get(j), median_w.get(guess_list.get(j)));
                	 if (prediction(m_data_w,m_data_w.instance(i),m_Classifiers_w)!=m_data_w.instance(i).classValue()){
                		 break;
                	 }
                	 }
                 }
		     
		    }
			    System.out.println("number of correction_models_w:"+num_correct+"/"+m_data_w.numInstances());
			    System.out.println(total_w);
			    med_result.add(total);
			    med_result.add(total_w);
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
	         //   System.out.println(pred);

	            if (!Utils.isMissingValue(pred)) {
	              votes[0] += pred;
	              voteCount++;
	            }
	          } else {
	       // 	System.out.println("non numeric");
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
		int loop=20;
		List<List<Integer>>result=new ArrayList<List<Integer>>();
		for (int l=0;l<loop;l++){
		
        int guess=57;
        List<Integer>guess_list=new ArrayList<Integer>();
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File("./data/attack/spambase/attack/spambase2.data"));
		Instances m_data = loader.getDataSet();
		m_data.setClassIndex(m_data.numAttributes() - 1);
	    Integer[] arr = new Integer[m_data.numAttributes()-1];
	    
		loader.setSource(new File("./data/attack/spambase/attack/spambase2.data"));
		Instances m_data_w = loader.getDataSet();
		m_data_w.setClassIndex(m_data_w.numAttributes() - 1);

	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
	    }
	    Collections.shuffle(Arrays.asList(arr));
	    for(int i=0;i<guess;i++){
	    	guess_list.add(arr[i]);
	    }
	    
		String path = "./data/attack/spambase/model/5,50/";
		int numTree=100;
	    List<Integer>models = new ArrayList<Integer>();
	    List<Integer>models_w = new ArrayList<Integer>();
	    
        String candidate = "0,1,2,3,4,6,7,11,13,16,19,21,23,25,27,28,33,35,41,54,55,56,60,62,65,68,77,80,91,92";
		String []items = candidate.split(",");
	    
		for (int i=0;i<items.length;i++){
			 models_w.add(Integer.parseInt(items[i]));	
	     }
		 for (int i=0;i<100;i++){
		     models.add(i);	    
		 }

	    attacker_1 at = new attacker_1();
	    result.add(at.getresult(path, models,models_w,numTree,m_data,m_data_w,guess,guess_list));
		}
		int total=0;
		int total_w=0;
		for (int i=0;i<result.size();i++){
			total+=result.get(i).get(0);
			total_w+=result.get(i).get(1);
		}
		System.out.println(total/loop+","+total_w/loop);
	}
}
