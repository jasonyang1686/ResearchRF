
package RandomForest_spambase;

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
import RandomForest_NSL_KDD.distance;


public class SmarterAttacker_distance {
	
	  public List <Double>getresult(String path,List<Integer>models, Instances m_data) throws Exception {
            List<Double>med_result=new ArrayList<Double>();
			Classifier[]m_Classifiers= new Classifier[models.size()];

			List<String> instanceresult=new ArrayList<String>();

		    	  
			      for (int m=0;m< m_Classifiers.length; m++){
			    	  
			    	  m_Classifiers[m] = (Classifier) weka.core.SerializationHelper.read(path+"Classifier"+m+".model");
			      }  


		      int num_fp=0;
		      int num_p=0;
		      int num_tp=0;
		      int num_n=0;
		      int num_fn=0;

			    List<Instance>pos= new ArrayList<Instance>();	
			    List<Instance>neg= new ArrayList<Instance>();	
			    
		      for (int i = 0; i < m_data.numInstances(); i++) {
		    	  if (m_data.instance(i).classValue()==0.0){
		    		  num_p++;
		    	  }else{
		    		  num_n++;
		    	  }
               double predict_result=prediction(m_data,m_data.instance(i),m_Classifiers);
               if (predict_result==0.0){
          	     
             if (m_data.instance(i).classValue()==predict_result){
          	   pos.add(m_data.instance(i));
          	    num_tp++;
             }else{
          	   num_fp++;
             }

             }else{
          	 neg.add(m_data.instance(i));
          	 if (m_data.instance(i).classValue()!=predict_result){
          		 num_fn++;
          	 }
             }
               
		    }

		  //    System.out.println(num_fp);
		       distance dis = new distance();
	
			    med_result.add((num_tp+0.0)/(num_p+0.0));
			    med_result.add((num_fp+0.0)/(num_n+0.0));
			    med_result.add(dis.getresult(pos, neg)/(num_tp+num_fn+0.0));
			    med_result.add(dis.getresult(pos, neg)/(1.0+0.0));

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

		int numRandomTree=100;	
		List<List<Double>>result=new ArrayList<List<Double>>();
		
        ArffLoader loader = new ArffLoader();		
		loader.setSource(new File("./data/attack/spambase/attack/spambase_all_normal.arff"));
		Instances all_data = loader.getDataSet();
		all_data.setClassIndex(all_data.numAttributes() - 1);
		
		String path = "./data/attack/spambase/model/5,50/w_models/";
		
	    List<Integer>models = new ArrayList<Integer>();

	    Integer[] arr_1 = new Integer[numRandomTree];
	    for (int i = 0; i < arr_1.length; i++) {
	        arr_1[i] = i;
    }
	    Collections.shuffle(Arrays.asList(arr_1));
		
	    for (int i=0;i<numRandomTree;i++){
		     models.add(arr_1[i]);	
		 }
		 
	     SmarterAttacker_distance sad = new SmarterAttacker_distance();
	     
         result.add(sad.getresult(path,models,all_data));
		
		
		double total=0;
		double total_w=0;
		double total_distance=0.0;
		double avg_distance=0.0;
		for (int i=0;i<result.size();i++){
			total+=result.get(i).get(0);
			total_w+=result.get(i).get(1);
			total_distance+=result.get(i).get(2);
			avg_distance+=result.get(i).get(3);
		}
		System.out.println(total+","+total_w+","+total_distance+","+avg_distance);
	}
}
