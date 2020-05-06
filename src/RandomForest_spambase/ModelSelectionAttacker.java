
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
import java.util.Iterator;
import java.util.List;

import RandomForest.attacker;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.CSVLoader;


public class ModelSelectionAttacker {
	
	  public List getresult(String path, List<Integer>models,int numCandidate,int numTest, Instances m_data, int guess, List<String>medians,List<Integer>real_guess_list) throws Exception {
            List<Integer>med_result=new ArrayList<Integer>();
			List<String> instanceresult=new ArrayList<String>();
		
              int total=0;
              int total_w=0;
		      int num_correct=0;  
		      
		      for (int i = 0; i < m_data.numInstances()-1; i++) {
		    	  		    	  
		    	  Integer[]arr=new Integer[models.size()];
			  	    for (int k = 0; k < arr.length; k++) {
			  	        arr[k] = k;
			  	    }
				    Collections.shuffle(Arrays.asList(arr));
				    
				    if (numCandidate>models.size()){
				    	numCandidate=models.size();
				    }
					Classifier[]m_Classifiers= new Classifier[numCandidate];
					//  System.out.println(m_Classifiers.length);
				      for (int m=0;m<numCandidate; m++){
				    //	  System.out.println(arr[m]);
				    	  m_Classifiers[m] = (Classifier) weka.core.SerializationHelper.read(path+"Classifier"+arr[m]+".model");
				      } 
		    	  
		    	//  System.out.println(prediction(m_data,m_data.instance(i),m_Classifiers));
                 if (prediction(m_data,m_data.instance(i),m_Classifiers)==m_data.instance(i).classValue()){
                     num_correct++;
                //     System.out.println(num_correct+":"+m_data.get(i).toString());
                	 for (int j =0;j<guess;j++ ){
                		 
                		 int position=real_guess_list.get(j);  	
                		 String replace=medians.get(position);
                		
                		 total++;
                		 if (m_data.instance(i).attribute(position).isNominal()){
                	  m_data.instance(i).setValue(position, replace);
                		 }else {
                	  m_data.instance(i).setValue(position, Double.parseDouble(replace));  
                		 }
                	 if (prediction(m_data,m_data.instance(i),m_Classifiers)!=m_data.instance(i).classValue()){  
                    	 instanceresult.add(i+":"+j);
                		 break;
                	 }
                	 }  
            		 
                 }else{
                	 instanceresult.add(i+":0");
                 }


		    }
		      for (int i=0;i<instanceresult.size();i++){
		    //	  System.out.println(instanceresult.get(i));
		      }
			//    System.out.println("number of correction_models:"+num_correct+"/"+m_data.numInstances());
			    med_result.add(total);
			    med_result.add(num_correct);

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
		int loop=100;
		int numTree=100;
		int numRandomTree=100;
	
		while(numTree>0){
		List<List<Integer>>result=new ArrayList<List<Integer>>();
		for (int l=0;l<loop;l++){
			
	    List<Instance>instancedatas= new ArrayList<Instance>();	
	    
        List<String>medians=new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/attack/spambase/attack/output.txt")));
		String[] output=br.readLine().split(",");
		for (int i=0;i<output.length;i++){
			medians.add(output[i]);
		}
		br.close();
        int guess=57;
        int numTest=100;

		int numCandidate=14;
		

        List<Integer>guess_list=new ArrayList<Integer>();
	    
        CSVLoader loader = new CSVLoader();		
		loader.setSource(new File("./data/attack/spambase/attack/spambase2.data"));
		Instances m_data = loader.getDataSet();
		m_data.setClassIndex(m_data.numAttributes() - 1);

	    Integer[] arr = new Integer[m_data.numAttributes()-1];


	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
    }
	    Collections.shuffle(Arrays.asList(arr));
	    
	    for(int i=0;i<guess;i++){
    	guess_list.add(arr[i]);
    }
	

		String path = "./data/attack/spambase/model/5,50/models/";

		
	    List<Integer>models = new ArrayList<Integer>();

	/*    
        String candidate = "0,1,3,4,5,6,7";
		String []items = candidate.split(",");
	    
		for (int i=0;i<items.length;i++){
			 models_w.add(Integer.parseInt(items[i]));	
	     }
	  */   
	    Integer[] arr_1 = new Integer[14];
//	    for (int i = 0; i < arr_1.length; i++) {
	        arr_1[0] = 78;
	        arr_1[0] = 8;
	        arr_1[0] = 40;
	        arr_1[0] = 71;
	        arr_1[0] = 93;
	        arr_1[0] = 5;
	        arr_1[0] = 49;
	        arr_1[0] = 60;
	        arr_1[0] = 26;
	        arr_1[0] = 13;
	        arr_1[0] = 2;
	        arr_1[0] = 39;
	        arr_1[0] = 17;
	        arr_1[0] = 97;
//    }
	    Collections.shuffle(Arrays.asList(arr_1));
		 
	    for (int i=0;i<14;i++){
		     models.add(arr_1[i]);	

		 }
		 for (int i=0;i<numTest;i++){
		     instancedatas.add((Instance) m_data.instance(i).copy());	
		 }
         

		 ModelSelectionVoteChange msvc = new ModelSelectionVoteChange();
		 
		 List<Integer>candidate_list=msvc.getresult(path, models,numCandidate,numTest,instancedatas,guess,medians,guess_list);
		 
	//	 System.out.println(candidate_list.size());
		 for (Iterator<Integer> iter = guess_list.listIterator(); iter.hasNext(); ) {
			    int a = iter.next();
			    for (int i=0;i<candidate_list.size();i++){
			    if (a==candidate_list.get(i)) {
			        iter.remove();
			    }
			    }
			}
//		 System.out.println(guess_list.size());

	 
//		 System.out.println(candidate_list_w.size());

         
		 List<Integer>real_guess_list= new ArrayList<Integer>();

		 for (int i=0;i<candidate_list.size();i++){
			 real_guess_list.add(candidate_list.get(i));
		 }
		 for (int i=0;i<guess_list.size();i++){
			 real_guess_list.add(guess_list.get(i));
		 }

	//	 System.out.println(real_guess_list.size());
	//	 System.out.println(real_guess_list_w.size());
		 for (int i=0;i<numTest;i++){
		     m_data.remove(i);	
		 }

		 for(int i=0;i<real_guess_list.size();i++){
	//		 System.out.print(real_guess_list.get(i)+",");
		 }
	//	 System.out.println();

		 
	//	 System.out.println();
	     ModelSelectionAttacker msa = new ModelSelectionAttacker();
	     
         result.add(msa.getresult(path,models,numCandidate,numTest,m_data,guess,medians,real_guess_list));
		}
		
		int total=0;
		int total_w=0;
		for (int i=0;i<result.size();i++){
			total+=result.get(i).get(0);
			total_w+=result.get(i).get(1);
		}
		System.out.println(total/loop+","+total_w/loop);
	//	numTree--;
		}
	}
}
