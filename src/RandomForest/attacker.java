
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


public class attacker {
	
	  public List getresult(String path, List<Integer>models, List<Integer>models_w,int numTree, Instances m_data,Instances m_data_w, int guess, List<String>medians,List<Integer>guess_list) throws Exception {
            List<Integer>med_result=new ArrayList<Integer>();
			Classifier[]m_Classifiers= new Classifier[models.size()];
			Classifier[]m_Classifiers_w= new Classifier[models_w.size()];
		      for (int m=0;m< m_Classifiers.length; m++){
		    	  m_Classifiers[m] = (Classifier) weka.core.SerializationHelper.read(path+"models/Classifier"+models.get(m)+".model");
		      }    
		      for (int m=0;m< m_Classifiers_w.length; m++){
		    	  m_Classifiers_w[m] = (Classifier) weka.core.SerializationHelper.read(path+"w_models/Classifier"+models_w.get(m)+".model");
		      }  
 
              int total=0;
              int total_w=0;
		      int num_correct=0;     
		      
		      for (int i = 0; i < m_data.numInstances(); i++) {
		    	//  System.out.println(prediction(m_data,m_data.instance(i),m_Classifiers));
                 if (Math.abs(prediction(m_data,m_data.instance(i),m_Classifiers)-m_data.instance(i).classValue())>0.05){
                     num_correct++;
             //        System.out.println(num_correct+":"+m_data.get(i).toString());
                	 for (int j =0;j<guess;j++ ){ 
                		 int position=guess_list.get(j);  	
                		 String replace=medians.get(position);
                		
                		 total++;
                		 if (m_data.instance(i).attribute(position).isNominal()){
                	 m_data.instance(i).setValue(position, replace);
                		 }else{
                	  m_data.instance(i).setValue(position, Double.parseDouble(replace));  
                		 }
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
		    	  
		   // 	  System.out.println(m_data.instance(i).classValue());
                 if (prediction(m_data_w,m_data_w.instance(i),m_Classifiers_w)==m_data_w.instance(i).classValue()){
                	 num_correct++;
                	 for (int j =0;j<guess;j++ ){
                		 int position=guess_list.get(j);
                		
                		 total_w++;
                		 String replace=medians.get(position);
                	//	 System.out.println("replace:"+replace);

                		 if (m_data_w.instance(i).attribute(position).isNominal()){
                    	//	 System.out.println("position:"+position);  
                    	//	 System.out.println("content"+m_data_w.instance(i).stringValue(position));                  		  
                	 m_data_w.instance(i).setValue(position, replace);  
                	 }else{
                    	//	 System.out.println("num_position:"+position); 
                    	//	 System.out.println("num_content"+m_data_w.instance(i).value(position));
                    	//	 System.out.println("num_full_content"+m_data_w.instance(i).toString());

                			 m_data_w.instance(i).setValue(position, Double.parseDouble(replace));       			
                		 }
                	 if (prediction(m_data_w,m_data_w.instance(i),m_Classifiers_w)!=m_data_w.instance(i).classValue()){
                		 break;
                	 }
                	 }
                 }
		     
		    }
			    System.out.println("number of correction_models_w:"+num_correct+"/"+m_data_w.numInstances());
			    System.out.println(total_w);
			    if ((total_w-total)>80){
			    	System.out.println(guess_list);
			    }else if((total-total_w)>50){
			    	System.out.println("reverse"+guess_list);
			    }
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
	            //System.out.println(pred);

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
		List<List<Integer>>result=new ArrayList<List<Integer>>();
		for (int l=0;l<loop;l++){
        List<String>medians=new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/attack/spambase/attack/output.txt")));
		String[] output=br.readLine().split(",");
		for (int i=0;i<output.length;i++){
			medians.add(output[i]);
		}
		br.close();
        int guess=57;
        List<Integer>guess_list=new ArrayList<Integer>();
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File("./data/attack/spambase/attack/spambase1.data"));
		Instances m_data = loader.getDataSet();
		m_data.setClassIndex(m_data.numAttributes() - 1);
	    Integer[] arr = new Integer[m_data.numAttributes()-1];
	    
		loader.setSource(new File("./data/attack/spambase/attack/spambase1.data"));
		Instances m_data_w = loader.getDataSet();
		m_data_w.setClassIndex(m_data_w.numAttributes() - 1);

	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
	    }
	    Collections.shuffle(Arrays.asList(arr));
	    
	    for(int i=0;i<guess;i++){
    	guess_list.add(arr[i]);
    }
	
	/*    
		String shuffle="23, 8, 33, 35, 22, 39, 31, 3, 30, 12, 24, 5, 40, 18, 16, 6, 32, 15, 9, 13, 37, 27, 36, 39, 7, 2, 14, 1, 28, 17, 0, 4, 26, 20, 25, 19, 10, 21, 34, 11, 29"; 
		String []shuffles=shuffle.split(",");
		for (int i=0;i<guess;i++){
			guess_list.add(Integer.parseInt(shuffles[i].trim()));
		}
*/

		String path = "./data/attack/spambase/model/5,50/";
		int numTree=100;
	    List<Integer>models = new ArrayList<Integer>();
	    List<Integer>models_w = new ArrayList<Integer>();
	/*    
        String candidate = "0,1,3,4,5,6,7";
		String []items = candidate.split(",");
	    
		for (int i=0;i<items.length;i++){
			 models_w.add(Integer.parseInt(items[i]));	
	     }
	  */   
		 for (int i=0;i<numTree;i++){
		     models.add(i);	
		     models_w.add(i);
		 }

	    attacker at = new attacker();
	    result.add(at.getresult(path, models,models_w,numTree,m_data,m_data_w,guess,medians,guess_list));
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
