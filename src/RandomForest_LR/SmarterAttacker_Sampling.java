package RandomForest_LR;

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
import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;


public class SmarterAttacker_Sampling {
	
	  public List <Double>getresult(Logistic LR, Instances m_data,int guess, List<String>medians,List<Integer>real_guess_list) throws Exception {
            List<Double>med_result=new ArrayList<Double>();


			List<String> instanceresult=new ArrayList<String>();

		    	 
            double tryCount=0;
            double accuCount=0;
            double passCount=0;
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

                 if (LR.classifyInstance(m_data.instance(i))==m_data.instance(i).classValue()){
                accuCount++;
               if (m_data.instance(i).classValue()==0.0){
                	 for (int j =0;j<guess;j++ ){ 
                		 int position=real_guess_list.get(j);  	
                		 String replace=medians.get(position);
                 		  tryCount++;
                		 if (!m_data.instance(i).attribute(position).isNumeric()){
                	
                           	  m_data.instance(i).setValue(position, replace);

                		 }else {
                	  m_data.instance(i).setValue(position, Double.parseDouble(replace));  
                		 }
                	 
                	 if (LR.classifyInstance(m_data.instance(i))!=m_data.instance(i).classValue()){  
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
        int numTest=200;
        int numGuess=4;
        
		String modelPath = "./data/spambase/LR/w_model/w_model.data";
		
	    Map<String, Integer> label = new HashMap<String, Integer>();	
		List<List<Double>>result=new ArrayList<List<Double>>();
		
		for (int l=0;l<loop;l++){
	    List<Instance>instancedatas= new ArrayList<Instance>();	
	    
        List<String>medians=new ArrayList<String>();
        
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/spambase/testing/output_normal.txt")));
		String[] output=br.readLine().split(",");
		for (int i=0;i<output.length;i++){
			medians.add(output[i]);
		}
		br.close();
		
        List<Integer>guess_list=new ArrayList<Integer>();

        ArffLoader loader = new ArffLoader();		
		loader.setSource(new File("./data/spambase/testing/spambase_attack_normal.arff"));
		Instances m_data = loader.getDataSet();
		m_data.setClassIndex(m_data.numAttributes() - 1);
		loader.reset();
		
        int numAttributes=m_data.numAttributes() - 1;	
        
		loader.setSource(new File("./data/spambase/testing/spambase_all_normal.arff"));
		Instances all_data = loader.getDataSet();
		all_data.setClassIndex(all_data.numAttributes() - 1);
		loader.reset();
		
        CSVLoader cloader = new CSVLoader();	
		cloader.setSource(new File(modelPath));
		Instances data = cloader.getDataSet();
		data.setClassIndex(data.numAttributes()-1);
		Logistic LR = new Logistic();
		LR.buildClassifier(data);
		
		
	    Integer[] arr = new Integer[m_data.numAttributes()-1];
	    
	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
    }
	    Collections.shuffle(Arrays.asList(arr));
	    
	    for(int i=0;i<numAttributes;i++){
    	        guess_list.add(arr[i]);
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
		 
		 candidate_list=vc.getresult(LR,instancedatas,numAttributes,medians);
			
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
	//    result.add(sa.getresult(LR,all_data,numAttributes,medians,real_guess_list));
        result.add(sa.getresult(LR,all_data,numGuess,medians,real_guess_list));
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
