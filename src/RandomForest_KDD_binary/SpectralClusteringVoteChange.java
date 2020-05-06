
package RandomForest_KDD_binary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
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
import weka.core.converters.ArffLoader;


public class SpectralClusteringVoteChange {
	
	public List<Integer> getList(List<ArrayList<Integer>> model_list,int numCandidate){
		List<Integer>models=new ArrayList<Integer>();
		for (int i=0;i<model_list.size();i++){
			
		    Integer[] arr = new Integer[model_list.get(i).size()];

		    for (int j = 0; j < arr.length; j++) {
		        arr[j] = model_list.get(i).get(j);
	    }
		    Collections.shuffle(Arrays.asList(arr));
		    
		    if (numCandidate>=arr.length){
		    for(int m=0;m<arr.length;m++){
	    	models.add(arr[m]);
		    }
	    }else{
		    for(int m=0;m<numCandidate;m++){
	    	models.add(arr[m]);
		    }
	    }
		    
		}
		return models;
	}
	
	  public List getresult( List<ArrayList<Integer>> model_list,String path,int numTree,int numCandidate, List<String>classlabels,List<Instance>instancedatas,int guess, List<String>medians) throws Exception {
		 Map<Integer, Integer> appear = new HashMap<Integer, Integer>(); 

		      for (int i = 0; i < instancedatas.size(); i++) {
		          List<Integer>guess_list=new ArrayList<Integer>();
		  	    Integer[] arr = new Integer[guess];  
		    	  for (int m = 0; m < arr.length; m++) {
		  	        arr[m] = m;
		  	    }
		  	    Collections.shuffle(Arrays.asList(arr));
		  	    
		  	    for(int g=0;g<guess;g++){
		      	guess_list.add(arr[g]);
		      }
		 		 List<Integer>models= new ArrayList<Integer>();

			     models=getList(model_list, numCandidate);
		//	     System.out.println(models);
					Classifier[]m_Classifiers= new Classifier[models.size()];
			      for (int m=0;m<models.size(); m++){
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
			//  	   System.out.println(list_Data.get(i).getKey()+":"+list_Data.get(i).getValue());
		  	    }   
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

	            voteCount++;
	       //     System.out.println(data.toString());
	            double[] newProbs = m_Classifiers[j].distributionForInstance(data);
	            // average the probability estimates
	            for (int k = 0; k < newProbs.length; k++) {
	    	//     System.out.println(newProbs[k]);	
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
        int guess=41;
		int numTest=200;
		int numTree=100;
		int numCluster=10;
		int numCandidate=1;
    	String count_path="./data/attack/spambase/model/5,50/temp/count_matrix.txt";
    	String ratio_path="./data/attack/spambase/model/5,50/temp/ratio_matrix.txt"; 
		double[][]count_matrix=new double[numTree][numTree];
    	double[][]ratio_matrix=new double[numTree][numTree]; 	
    	
   	 BufferedReader br4 = new BufferedReader(new FileReader(new File(count_path))); 
     String data = br4.readLine();
     int count=0;
     while (data != null) {
         String []t = data.split("  ");  
         for (int i=0;i<t.length;i++){
         	count_matrix[count][i]=Double.parseDouble(t[i]);
         }
         count++;
         data=br4.readLine();
     }
     br4.close();
     /*
     count=0;
	 BufferedReader br5 = new BufferedReader(new FileReader(new File(ratio_path))); 
	 String data2=br5.readLine();
	 while(data2!=null){
		 
     String[] t = data2.split("  ");
     for (int i=0;i<t.length;i++){
      	ratio_matrix[count][i]=Double.parseDouble(t[i]);
      }
      count++;
     data2=br5.readLine();
	 }
     br5.close();
     */
     
	SpectralClustering_Own st =new SpectralClustering_Own();
    
	List<ArrayList<Integer>>model_list=st.clustering(count_matrix,numCluster);
	

		List<List<Integer>>result=new ArrayList<List<Integer>>();

        List<String>medians=new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/attack/kddcup99/attack/output.txt")));
		String[] output=br.readLine().split(",");
		for (int i=0;i<output.length;i++){
			medians.add(output[i]);
		}
		
		 List<String>classlabels = new ArrayList<String>(); 
       	 BufferedReader br2 = new BufferedReader(new FileReader(new File("./data/attack/kddcup99/model/kddcup99_non_duplicate_10_percent_model.data"))); 
         String[] labels = br2.readLine().split(",");
         br2.close();
         
         for (int i=0;i<labels.length-1;i++){
          	classlabels.add(labels[i]); 
          }

        List<Instance>instancedatas= new ArrayList<Instance>();
        
	    
		ArffLoader loader = new ArffLoader();
		loader.setSource(new File("./data/attack/kddcup99/attack/kddcup99_non_duplicate_10_percent_attack.arff"));
		Instances m_data = loader.getDataSet();
		m_data.setClassIndex(m_data.numAttributes() - 1);
	    
		String path = "./data/attack/kddcup99/model/5,50/models/";

	    Integer[] arr_1 = new Integer[m_data.numInstances()];	   
	    for (int i = 0; i < arr_1.length; i++) {
	        arr_1[i] = i;
    }
	    Collections.shuffle(Arrays.asList(arr_1));
	    
		 for (int i=0;i<numTest;i++){
		     instancedatas.add((Instance) m_data.instance(arr_1[i]).copy());	
		 }	
		 
		 
	    SpectralClusteringVoteChange scvc = new SpectralClusteringVoteChange();
	    scvc.getresult(model_list,path,numTree,numCandidate,classlabels,instancedatas,guess,medians);
		

	}
}
