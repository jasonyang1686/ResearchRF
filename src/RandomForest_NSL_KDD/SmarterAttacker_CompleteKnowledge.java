
package RandomForest_NSL_KDD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffLoader;


public class SmarterAttacker_CompleteKnowledge {
	
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
		    	 
		    	  if (m_data.instance(i).classValue()==1.0){
		    		  num_p++;
		    	  }else{
		    		  num_n++;
		    	  }
                 if (prediction(m_data,m_data.instance(i),m_Classifiers)!=m_data.instance(i).classValue()){
                	 m_data.remove(i);
                	 if (m_data.instance(i).classValue()==1.0){
                	 num_fn++;
                	 }else{
                	 num_fp++;	 
                	 }
                 }
		    }  
		    int loop_number=0;

		    process(total,real_guess_list,m_data,m_Classifiers,loop_number,medians);
		   

		//	    System.out.println(num_fn);
		//	    System.out.println(num_fp); 
			    med_result.add((num_p-num_fn+0.0)/(num_p+0.0));
			    med_result.add((num_fp)/(num_n+0.0));
			    med_result.add(total/(num_p-num_fn+0.0));
			    return med_result;
	  }
	    void process(double total,List<Integer>real_guess_list,Instances m_data,Classifier[]m_Classifiers,int loop_number, List<String>medians) throws Exception{
		    int count_number=0;
	    	if(m_data.numInstances()>0){
	    		loop_number++;
	    	List<Integer> pos = new ArrayList<Integer>();
	 	    Map<Integer, Double> label = new HashMap<Integer, Double>();	

              for(int i=0;i<real_guess_list.size();i++){
                  double vote_result=0.0;
            	  List<Instance>ins=new ArrayList<Instance>();
            	  for (int m=0;m<m_data.numInstances();m++){
            		  ins.add((Instance) m_data.instance(m).copy());
            	  }
            	  pos =replace(m_data,ins,real_guess_list.get(i),m_Classifiers,medians);
                  for(int m=0;m<ins.size();m++){
                  vote_result+=(vote(m_data,ins.get(m),m_Classifiers));
                  }
                	  label.put(i,vote_result);
                  
            	 }

              ArrayList<Map.Entry<Integer, Double>> list_Data = new ArrayList<Map.Entry<Integer, Double>>(
      				label.entrySet());

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
             int position=list_Data.get(list_Data.size()-1).getKey();
             System.out.println("vote_result :"+list_Data.get(0).getValue());
             System.out.println("vote_result :"+list_Data.get(list_Data.size()-1).getValue());
             System.out.println("position"+position+" :"+real_guess_list.get(position));
             System.out.println(m_data.numInstances());
             for(int i=0;i<m_data.numInstances();i++){
            	 m_data.instance(i).setValue(position, Double.parseDouble(medians.get(position)));
             }
             real_guess_list.remove(position);
             
             for(int i=0;i<m_data.numInstances();i++){
                 if (prediction(m_data,m_data.instance(i),m_Classifiers)!=m_data.instance(i).classValue()){
                	 m_data.remove(i);
                	 count_number++;
                 }
             }
             total+=count_number*loop_number;
             System.out.println(m_data.numInstances());
             System.out.println(total);
 		    process(total,real_guess_list,m_data,m_Classifiers,loop_number,medians);
	    	}
		    }
	    
	  public List<Integer> replace(Instances m_data,List<Instance>ins, int position, Classifier[]m_Classifiers,List<String>medians) throws Exception{
	 	    List<Integer> pos = new ArrayList<Integer>();
	 	    for(int i=0;i<ins.size();i++){
	 	    	ins.get(i).setValue(position, Double.parseDouble(medians.get(position)));
	 	       if (prediction(m_data,ins.get(i),m_Classifiers)!=m_data.instance(i).classValue()){
	 	    	   pos.add(i);
	 	       }
	 	    }
	 	    return pos;
	  }
	  public double vote(Instances m_data,Instance data,Classifier[]m_Classifiers ) throws Exception{
		  List<Double>result = new ArrayList<Double>();
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
	    //    	System.out.println(j);
	            voteCount++;
	            double[] newProbs = m_Classifiers[j].distributionForInstance(data);
	            // average the probability estimates
	            for (int k = 0; k < newProbs.length; k++) {	
	              votes[k] += newProbs[k];
	            }
	          }
	        }
             return votes[0];
	        // "vote"        
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
		int loop=1;		

			
		List<List<Double>>result=new ArrayList<List<Double>>();
		
		for (int l=0;l<loop;l++){
	    
        List<String>medians=new ArrayList<String>();
        
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/attack/NSL_KDD/attack/output.txt")));
		String[] output=br.readLine().split(",");
		for (int i=0;i<output.length;i++){
			medians.add(output[i]);
		}
		br.close();
		

        ArffLoader loader = new ArffLoader();		
		loader.setSource(new File("./data/attack/NSL_KDD/attack/KDDTest-21_B.arff"));
		Instances m_data = loader.getDataSet();
		m_data.setClassIndex(m_data.numAttributes() - 1);
		loader.reset();
		
        int guess=m_data.numAttributes() - 1;	
        
		String path = "./data/attack/NSL_KDD/model/5,50/models/";
		
	    List<Integer>models = new ArrayList<Integer>();
		
	    for (int i=0;i<numTree;i++){
		     models.add(i);	
		 }
			         
	 List<Integer>real_guess_list= new ArrayList<Integer>();
	 
	    for (int i=0;i<guess;i++){
		  real_guess_list.add(i);
			}
					 
	     SmarterAttacker_CompleteKnowledge sac = new SmarterAttacker_CompleteKnowledge();
	     
         result.add(sac.getresult(path,models,m_data,guess,medians,real_guess_list));
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
