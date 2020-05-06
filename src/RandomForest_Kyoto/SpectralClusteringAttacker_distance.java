
package RandomForest_Kyoto;

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
import weka.core.converters.ArffLoader;


public class SpectralClusteringAttacker_distance {
	
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
	
	  public List<Double> getresult( List<ArrayList<Integer>> model_list,String path,int numCandidate,int numTree, List<String>classlabels,Instances m_data, int guess, List<String>medians,List<Integer>guess_list) throws Exception { 
		    List<Integer>models = new ArrayList<Integer>();
            List<Double>med_result=new ArrayList<Double>();
			List<String> instanceresult=new ArrayList<String>();
		
              double total=0;
		      int num_tp=0;
		      int num_p=0;
		      int num_fp=0;
		      int num_n=0;
	

					      for (int i = 0; i < m_data.numInstances(); i++) {
					    	  
								      models=getList(model_list, numCandidate);
								 //     System.out.println(models);
										Classifier[]m_Classifiers= new Classifier[models.size()];	
							//	      total_entropy+=va.gettopk(matrix, models,k);
								      for (int m=0;m<models.size(); m++){
								    	  m_Classifiers[m] = (Classifier) weka.core.SerializationHelper.read(path+"Classifier"+models.get(m)+".model");
								      } 
					    	  
							    	  if (m_data.instance(i).classValue()==0.0){
							    		  num_p++;
							    	  }else{
							    		  num_n++;
							    	  }
					                 double predict_result=prediction(m_data,m_data.instance(i),m_Classifiers);
					                 if (predict_result==0.0){

					               if (m_data.instance(i).classValue()==predict_result){
					            	    num_tp++;
					               }else{
					            	   num_fp++;
					               }
					                	 for (int j =0;j<guess;j++ ){ 
					                		 int position=guess_list.get(j);  	
					                		 String replace=medians.get(position);
					                		
					                		 total++;
					                		 if (!m_data.instance(i).attribute(position).isNumeric()){

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
					                 
							    }

								    med_result.add((num_tp+0.0)/(num_p+0.0));
								    med_result.add((num_fp+0.0)/(num_n+0.0));
								    med_result.add(total/(num_p+0.0));
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
    	String matrix_path="./data/attack/spambase/model/5,50/temp/spectral_matrix.txt";
     	
    	int numCluster=5;
		int loop=4;
		int numTree=100;		
		int numSelection=1;
		
		while(numCluster<=100){ 	    
        List<String>medians=new ArrayList<String>();
 		BufferedReader br = new BufferedReader(new FileReader(new File(
 				"./data/attack/spambase/attack/output_normal.txt")));
 		String[] output=br.readLine().split(",");
 		for (int i=0;i<output.length;i++){
 			medians.add(output[i]);
 		}
 		br.close();
 		
 		List<String>classlabels = new ArrayList<String>(); 
 		Map<String, Integer> label = new HashMap<String, Integer>();
      	 BufferedReader br2 = new BufferedReader(new FileReader(new File("./data/attack/spambase/model/spambase_model_normal.data"))); 
        String[] labels = br2.readLine().split(",");
        br2.close();
        
        for (int i=0;i<labels.length-1;i++){
         	classlabels.add(labels[i]);
         	label.put(labels[i], i);
         }
 		
		double[][]count_matrix=new double[numTree][numTree];
    	double[][]ratio_matrix=new double[numTree][numTree]; 	
    	
   	 BufferedReader br4 = new BufferedReader(new FileReader(new File(matrix_path))); 
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
     
	SpectralClustering_Own st =new SpectralClustering_Own();
    
	List<ArrayList<Integer>>model_list=st.clustering(count_matrix,numCluster);
         	
		List<List<Double>>result=new ArrayList<List<Double>>();
		
		for (int l=0;l<loop;l++){
	        ArffLoader loader = new ArffLoader();		
			loader.setSource(new File("./data/attack/spambase/attack/spambase_attack_normal.arff"));
			Instances m_data = loader.getDataSet();
			m_data.setClassIndex(m_data.numAttributes() - 1);
			
			ArffLoader loader1 = new ArffLoader();		
			loader1.setSource(new File("./data/attack/spambase/attack/spambase_all_normal.arff"));
		    Instances all_data = loader1.getDataSet();
		    all_data.setClassIndex(all_data.numAttributes() - 1);
		    
			
	    List<Instance>instancedatas= new ArrayList<Instance>();	

        int guess=m_data.numAttributes() - 1;
        int numTest=100;

		String path = "./data/attack/spambase/model/5,50/models/";

		 for (int i=0;i<numTest;i++){
		     instancedatas.add((Instance) m_data.instance(i).copy());	
		 }

         List<Integer>guess_list=new ArrayList<Integer>();
		 
	  	    Integer[] arr = new Integer[guess];  
	    	  for (int m = 0; m < arr.length; m++) {
	  	        arr[m] = m;
	  	    }
	  	    Collections.shuffle(Arrays.asList(arr));
	  	    
	  	    for(int g=0;g<guess;g++){
	      	guess_list.add(arr[g]);
	      }
	  	    

		

	     SpectralClusteringAttacker_distance sca = new SpectralClusteringAttacker_distance();
	     
         result.add(sca.getresult(model_list,path,numSelection,numTree,classlabels,all_data,guess,medians,guess_list));
		}
		
		double total=0.0;
		double total_w=0.0;
		double total_try=0.0;
		double total_entropy=0.0;
		for (int i=0;i<result.size();i++){
			total+=result.get(i).get(0);
			total_w+=result.get(i).get(1);
			total_try+=result.get(i).get(2);

		}
		System.out.println(total/(loop+0.0)+","+total_w/(loop+0.0)+","+total_try/(loop+0.0)+","+total_entropy/(loop+0.0));
		 numCluster=numCluster+5;
		}
	}
}
