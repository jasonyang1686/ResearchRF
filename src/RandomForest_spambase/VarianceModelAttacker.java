
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

import RandomForest.attacker;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.CSVLoader;


public class VarianceModelAttacker {
	
	  public List<Double> getresult( ArrayList<List<Double>>matrix,String path,int numCandidate,int numTree, List<String>classlabels,Instances m_data, int guess, List<String>medians,List<Integer>real_guess_list,int k) throws Exception { 
		    List<Integer>models = new ArrayList<Integer>();
            List<Double>med_result=new ArrayList<Double>();
			List<String> instanceresult=new ArrayList<String>();
		
              double total=0;
              double total_entropy=0;
		      int num_fn=0;
		      int num_p=0;
		      int num_fp=0;
		      int num_n=0;

				Classifier[]m_Classifiers= new Classifier[numCandidate];	
		    	  

					      for (int i = 0; i < m_data.numInstances(); i++) {
					    	  
							    	  variance va= new variance();
								      models=va.getResult(matrix,numTree, numCandidate);
								      total_entropy+=va.gettopk(matrix, models,k);
								      for (int m=0;m<models.size(); m++){
								    	  m_Classifiers[m] = (Classifier) weka.core.SerializationHelper.read(path+"Classifier"+models.get(m)+".model");
								      } 
					    	  
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
			               } 
			                 }else{
			                	 if (m_data.instance(i).classValue()==0.0){
			                	 num_fn++;
			                	 }else{
			                	 num_fp++;	 
			                	 }
			                 }
					    }
		      	
		      //			    System.out.println(total_entropy); 
		     //			    System.out.println(num_p); 
		     // 			    System.out.println(num_fn);
		  //    			    System.out.println(num_fp); 
		      			    med_result.add((num_p-num_fn+0.0)/(num_p+0.0));
		      			    med_result.add((num_fp)/(num_n+0.0));
		      			    med_result.add(total/(num_p-num_fn+0.0));
		      			    med_result.add(total_entropy/(m_data.numInstances()+0.0));
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
		String vapath= "./data/attack/spambase/model/5,50/variance.txt";
        String dispath= "./data/attack/spambase/model/5,50/distribution.txt"; 
        int k=15;
		int loop=15;
		int numTree=100;		
		int numSelection=5;
		while(numSelection<=99){
		 List<String>classlabels = new ArrayList<String>(); 
		 Map<String,Integer>match= new HashMap<String,Integer>();
       	 BufferedReader br2 = new BufferedReader(new FileReader(new File("./data/attack/spambase/model/spambase1.data"))); 
         String[] labels = br2.readLine().split(",");
         br2.close();
         
         for (int i=0;i<labels.length-1;i++){
          	classlabels.add(labels[i]); 
          }
         
         ArrayList<List<Double>>matrix= new ArrayList<List<Double>>();
         BufferedReader br4 = new BufferedReader(new FileReader(new File(vapath))); 
         String data = br4.readLine();
         while (data != null) {
         	List<Double>result= new ArrayList<Double>();
             String []t = data.split(",");  
             for (int i=0;i<t.length;i++){
             	result.add(Double.parseDouble(t[i]));
             }
             matrix.add(result);
             data=br4.readLine();
         }
         br4.close();
 	    
        List<String>medians=new ArrayList<String>();
 		BufferedReader br = new BufferedReader(new FileReader(new File(
 				"./data/attack/spambase/attack/output.txt")));
 		String[] output=br.readLine().split(",");
 		for (int i=0;i<output.length;i++){
 			medians.add(output[i]);
 		}
 		br.close();
 		
		List<List<Double>>result=new ArrayList<List<Double>>();
		
		for (int l=0;l<loop;l++){
	        CSVLoader loader = new CSVLoader();		
			loader.setSource(new File("./data/attack/spambase/attack/spambase2.data"));
			Instances m_data = loader.getDataSet();
			m_data.setClassIndex(m_data.numAttributes() - 1);
			
			CSVLoader loader1 = new CSVLoader();		
			loader1.setSource(new File("./data/attack/spambase/attack/spambase_all.data"));
		    Instances all_data = loader1.getDataSet();
		    all_data.setClassIndex(all_data.numAttributes() - 1);
		    
			
	    List<Instance>instancedatas= new ArrayList<Instance>();	

        int guess=57;
        int numTest=50;

        List<Integer>guess_list=new ArrayList<Integer>();
	    
	    Integer[] arr = new Integer[m_data.numAttributes()-1];

	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
    }
	    Collections.shuffle(Arrays.asList(arr));
	    
	    for(int i=0;i<guess;i++){
    	guess_list.add(arr[i]);
    }
		String path = "./data/attack/spambase/model/5,50/w_models/";

		 for (int i=0;i<numTest;i++){
		     instancedatas.add((Instance) m_data.instance(i).copy());	
		 }
		 
        
		 VarianceVoteChange vvc = new VarianceVoteChange();
		/* 
		 List<Integer>candidate_list=vvc.getresult(matrix,path,numTree,numSelection,classlabels,instancedatas,guess,medians,guess_list);		
		 for (Iterator<Integer> iter = guess_list.listIterator(); iter.hasNext(); ) {
			    int a = iter.next();
			    for (int i=0;i<candidate_list.size();i++){
			    if (a==candidate_list.get(i)) {
			        iter.remove();
			    }
			    }
			}
	   */ 
		 List<Integer>real_guess_list= new ArrayList<Integer>();
/*
		 for (int i=0;i<candidate_list.size();i++){
			 real_guess_list.add(candidate_list.get(i));
		 }
		 for (int i=0;i<guess_list.size();i++){
			 real_guess_list.add(guess_list.get(i));
		 }
	*/	
	
		 	BufferedReader br3 = new BufferedReader(new FileReader(new File("./data/attack/spambase/model/5,50/temp/classCount.txt"))); 
	        String label = br3.readLine();
	        int index=0;
	        while (label!=null){
	        	String[]content=label.split("=");
	        	match.put(content[0],index);
	        	index++;
	            label = br3.readLine();	
	        }
	        br3.close();

	        BufferedReader br1 = new BufferedReader(new FileReader(new File(dispath))); 
	        String number = br1.readLine();
	        while (number!=null){
	        	String[]content=number.split("=");
	        	real_guess_list.add(match.get(content[0]));
	            number = br1.readLine();	
	        }
	        br1.close();


	     VarianceModelAttacker vma = new VarianceModelAttacker();
	     
         result.add(vma.getresult(matrix,path,numSelection,numTree,classlabels,all_data,guess,medians,real_guess_list,k));
		}
		
		double total=0.0;
		double total_w=0.0;
		double total_try=0.0;
		double total_entropy=0.0;
		for (int i=0;i<result.size();i++){
			total+=result.get(i).get(0);
			total_w+=result.get(i).get(1);
			total_try+=result.get(i).get(2);
			total_entropy+=result.get(i).get(3);
		}
		System.out.println(total/(loop+0.0)+","+total_w/(loop+0.0)+","+total_try/(loop+0.0)+","+total_entropy/(loop+0.0));
		if(numSelection==50){
         numSelection=numSelection+49;}
		else
		 numSelection=numSelection+5;
		}
	}
}
