package RandomForest_J48;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

public class SmarterAttacker_CompleteKnowledge {
	  public List <Double>getresult(String path, Instances m_data,int numGuess, List<String>medians,List<Integer>real_guess_list) throws Exception {
          List<Double>med_result=new ArrayList<Double>();

	      Classifier classifier = (Classifier) weka.core.SerializationHelper.read(path);
			      
            double tryCount=0;
            double accuCount=0;
            double passCount=0;
		      int num_fn=0;
		      int num_p=0;
		      int num_fp=0;
		      int num_n=0;
            //  System.out.println(m_data.instance(0).classValue());
for (int i = 0; i < m_data.numInstances(); i++) {
		    	  if (m_data.instance(i).classValue()==0.0){
		    		  num_p++;
		    	  }else{
		    		  num_n++;
		    	  }

             if (classifier.classifyInstance(m_data.instance(i))==m_data.instance(i).classValue()){
                accuCount++;
             if (m_data.instance(i).classValue()==0.0){
            	 
              	 for (int j =0;j<numGuess;j++ ){ 
              		 int position=real_guess_list.get(j);  	
              		 String replace=medians.get(position);
              		  tryCount++;
              		 if (!m_data.instance(i).attribute(position).isNumeric()){

                        m_data.instance(i).setValue(position, replace);

              		 }else {
              	        m_data.instance(i).setValue(position, Double.parseDouble(replace));  
              		 }
              	 
              	 if (classifier.classifyInstance(m_data.instance(i))!=m_data.instance(i).classValue()){  
                  	 passCount++;
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
		        System.out.println("count: "+passCount);
		        med_result.add(accuCount/(num_p+num_n+0.0));
                med_result.add((passCount+num_fn)/(num_p+0.0));
               
			    med_result.add((num_p-num_fn+0.0)/(num_p+0.0));
			    med_result.add((tryCount)/(num_p-num_fn+0.0));

			    return med_result;
	  }
		  
	  

	public static void main(String[] args) throws Exception {

		int loop=20;		
        int numGuess=5;
        
        String  modelPath="./data/kyoto/C45/j48.model";
		
	    Map<String, Integer> label = new HashMap<String, Integer>();	
		List<List<Double>>result=new ArrayList<List<Double>>();
		
		for (int l=0;l<loop;l++){
	    List<Instance>instancedatas= new ArrayList<Instance>();	
	    
        List<String>medians=new ArrayList<String>();
        
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/kyoto/testing/output_normal.txt")));
		String[] output=br.readLine().split(",");
		for (int i=0;i<output.length;i++){
			medians.add(output[i]);
		}
		br.close();
		
        List<Integer>guess_list=new ArrayList<Integer>();

        ArffLoader loader = new ArffLoader();		       
		loader.setSource(new File("./data/kyoto/testing/201512_week_removal_binary_balance_sampling.arff"));
		Instances all_data = loader.getDataSet();
        int numAttributes=all_data.numAttributes() - 1;	
		all_data.setClassIndex(numAttributes);
		
	    Integer[] arr = new Integer[numAttributes];
	    
	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
    }
	    Collections.shuffle(Arrays.asList(arr));
	    
	    for(int i=0;i<numAttributes;i++){
      	guess_list.add(arr[i]);
    }
	
		 
		
	    Integer[]arr_2= new Integer[all_data.numInstances()];
	    for (int i = 0; i < arr_2.length; i++) {
	        arr_2[i] = i;
    }
	    Collections.shuffle(Arrays.asList(arr_2));
	    
		 for (int i=0;i<all_data.size();i++){
		     instancedatas.add((Instance) all_data.instance(arr_2[i]).copy());	
		 }

		 J48VoteChange jvc = new J48VoteChange();
		 List<Integer>candidate_list=new ArrayList<Integer>();
		 
		 candidate_list=jvc.getresult(modelPath,instancedatas,numAttributes,medians);
			
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

		 
	     SmarterAttacker_CompleteKnowledge sck = new SmarterAttacker_CompleteKnowledge();
	 //      result.add(sck.getresult(modelPath,all_data,numAttributes,medians,real_guess_list));	     
             result.add(sck.getresult(modelPath,all_data,numGuess,medians,real_guess_list));
		}
		
		double total_accuracy=0;
		double total_success=0;
		double total_truep=0.0;
		double total_falsep=0.0;		
		for (int i=0;i<result.size();i++){
			total_accuracy+=result.get(i).get(0);
			total_success+=result.get(i).get(1);
			total_truep+=result.get(i).get(2);
			total_falsep+=result.get(i).get(3);
			
		}
		System.out.println(total_accuracy/(loop+0.0)+","+total_success/(loop+0.0)+","+total_truep/(loop+0.0)+","+total_falsep/(loop+0.0));
	}
}
