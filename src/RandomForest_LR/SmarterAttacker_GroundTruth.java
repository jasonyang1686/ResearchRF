
package RandomForest_LR;

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
import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;


public class SmarterAttacker_GroundTruth {
	
	  public List<Double> getresult(Logistic LR, Instances m_data,int guess, List<String>medians,List<Integer>real_guess_list) throws Exception {
            List<Double>med_result=new ArrayList<Double>();

			List<String> instanceresult=new ArrayList<String>();

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

                 if (LR.classifyInstance(m_data.instance(i))==m_data.instance(i).classValue()){

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
                	 
                	 if (LR.classifyInstance(m_data.instance(i))!=m_data.instance(i).classValue()){  
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
		  
	  

	public static void main(String[] args) throws Exception {


		int loop=20;		

		String path = "./data/attack/kddcup99_LR/model/kddcup_model_w.data";
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
		loader.reset();
		
        CSVLoader loader1 = new CSVLoader();	
		loader1.setSource(new File(path));
		Instances data = loader1.getDataSet();
		data.setClassIndex(data.numAttributes()-1);
		Logistic LR = new Logistic();
		LR.buildClassifier(data);
			         
	 List<Integer>real_guess_list= new ArrayList<Integer>();
	 
		 BufferedReader br1 = new BufferedReader(new FileReader(new File(
					"./data/attack/kddcup99_LR/model/5,50/temp/ground_truth.txt")));
		 String static_weight=br1.readLine();
		 while(static_weight!=null){
			String[] weights=static_weight.split(":");
				real_guess_list.add(Integer.parseInt(weights[0]));
				static_weight=br1.readLine();
			}
			br1.close();
					 
	     SmarterAttacker_GroundTruth sag = new SmarterAttacker_GroundTruth();
	     
         result.add(sag.getresult(LR,all_data,guess,medians,real_guess_list));
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
