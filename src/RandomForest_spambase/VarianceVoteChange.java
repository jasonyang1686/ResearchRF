
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.CSVLoader;


public class VarianceVoteChange {
	
	  public List getresult( ArrayList<List<Double>>matrix,String path,int numTree,int numCandidate, List<String>classlabels,List<Instance>instancedatas,int guess, List<String>medians,List<Integer>guess_list) throws Exception {
		 Map<Integer, Integer> appear = new HashMap<Integer, Integer>(); 

		      for (int i = 0; i < instancedatas.size(); i++) {	
		 		 List<Integer>models= new ArrayList<Integer>();

				Classifier[]m_Classifiers= new Classifier[numCandidate];

		 		 variance va= new variance();
			     models=va.getResult(matrix,numTree, numCandidate);
			//     System.out.println(models);
			     
			      for (int m=0;m<numCandidate; m++){
			    	  m_Classifiers[m] = (Classifier) weka.core.SerializationHelper.read(path+"Classifier"+models.get(m)+".model");
			      } 
		     	  
		    	//  System.out.println(prediction(m_data,m_data.instance(i),m_Classifiers));
                 if (prediction(instancedatas.get(i),m_Classifiers)==instancedatas.get(i).classValue()){


                	 for (int j =0;j<guess;j++ ){ 
                		 int position=guess_list.get(j);  	
                		 String replace=medians.get(position);
              		 
                		 if (instancedatas.get(i).attribute(position).isNominal()){
                	 instancedatas.get(i).setValue(position, replace);
                		 }else{
                	 instancedatas.get(i).setValue(position, Double.parseDouble(replace));  
                		 }
                		                 		 
                 	    if (prediction(instancedatas.get(i),m_Classifiers)!=instancedatas.get(i).classValue()){
                	    	if (appear.containsKey(position)){
                	    		appear.put(position, appear.get(position)+1);
                	    	}else{
                	    		appear.put(position, 1);
                	    	}
                	    	break;                	    	
                	    }
                	    
                	 }  
                 }

		    }
		      ArrayList<Map.Entry<Integer,Integer>> list_Data = new ArrayList<Map.Entry<Integer, Integer>>(appear.entrySet());  
		  	    
		  	    Collections.sort(list_Data, new Comparator<Map.Entry<Integer,Integer>>(){    
		  	      public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2)  
		  	      {  
		  	        if ((o2.getValue() - o1.getValue())>0)  
		  	          return 1;  
		  	        else if((o2.getValue() - o1.getValue())==0)  
		  	          return 0;  
		  	        else   
		  	          return -1;  
		  	      }  
		  	        } 
		  	    );
		  	  List<Integer>result = new ArrayList<Integer>();
		  	    for (int i=0;i<list_Data.size();i++){
		  	    	
		  	    	result.add(list_Data.get(i).getKey());
			 // 	   System.out.println(list_Data.get(i).getKey()+":"+list_Data.get(i).getValue());
		  	    }   
		  	    return result;

	  }
	  
	  public double getVote(Instance data,Classifier[]m_Classifiers, double classValue ) throws Exception{
	      boolean numeric = data.classAttribute().isNumeric();
	        double result=0.0;
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
	              if (pred==classValue){
	            	 result++; 
	              }
	              voteCount++;
	            }
	          } else {
	        //	System.out.println("non numeric");
	            voteCount++;
	            double[] newProbs = m_Classifiers[j].distributionForInstance(data);
	            // average the probability estimates
	            for (int k = 0; k < newProbs.length; k++) {
	              votes[k] += newProbs[k];
	            }
	            if (Utils.maxIndex(votes)==classValue){
	            	result++;
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
      return result;

	  }
	  
	  public double prediction(Instance data,Classifier[]m_Classifiers) throws Exception{
		  
	      boolean numeric = data.classAttribute().isNumeric();
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

		List<List<Integer>>result=new ArrayList<List<Integer>>();

        List<String>medians=new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/attack/spambase/attack/output.txt")));
		String[] output=br.readLine().split(",");
		for (int i=0;i<output.length;i++){
			medians.add(output[i]);
		}
		
		 List<String>classlabels = new ArrayList<String>(); 
       	 BufferedReader br2 = new BufferedReader(new FileReader(new File("./data/attack/spambase/model/spambase1.data"))); 
         String[] labels = br2.readLine().split(",");
         br2.close();
         
         for (int i=0;i<labels.length-1;i++){
          	classlabels.add(labels[i]); 
          }
		
        int guess=57;
		int numTest=50;
		int numTree=100;
		int numCandidate=10;
		
        List<Integer>guess_list=new ArrayList<Integer>();
        List<Instance>instancedatas= new ArrayList<Instance>();
        
	    
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

		 for (int i=0;i<numTest;i++){
		     instancedatas.add(m_data.get(i));	

		 }
		 
	    VarianceVoteChange vvc = new VarianceVoteChange();
	    vvc.getresult(matrix,path,numTree,numCandidate,classlabels,instancedatas,guess,medians,guess_list);
		

	}
}
