
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


public class SpectralClusteringAttacker_GroundTruth {
	
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
	
	  public List<Double> getresult( List<ArrayList<Integer>> model_list,String path,int numCandidate,int numTree, List<String>classlabels,Instances m_data, int guess, List<String>medians,List<Integer>real_guess_list) throws Exception { 
		    List<Integer>models = new ArrayList<Integer>();
            List<Double>med_result=new ArrayList<Double>();
			List<String> instanceresult=new ArrayList<String>();
		
              double total=0;
              double total_entropy=0;
		      int num_fn=0;
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

    	String count_path="./data/attack/spambase/model/5,50/temp/count_matrix.txt";
    	String ratio_path="./data/attack/spambase/model/5,50/temp/ratio_matrix.txt";  
    	
    	int numCluster=5;
		int loop=6;
		int numTree=100;		
		int numSelection=1;
		
		while(numSelection<=21){ 	    
        List<String>medians=new ArrayList<String>();
 		BufferedReader br = new BufferedReader(new FileReader(new File(
 				"./data/attack/spambase/attack/output.txt")));
 		String[] output=br.readLine().split(",");
 		for (int i=0;i<output.length;i++){
 			medians.add(output[i]);
 		}
 		br.close();
 		
 		List<String>classlabels = new ArrayList<String>(); 
 		Map<String, Integer> label = new HashMap<String, Integer>();
      	 BufferedReader br2 = new BufferedReader(new FileReader(new File("./data/attack/spambase/model/spambase_model.data"))); 
        String[] labels = br2.readLine().split(",");
        br2.close();
        
        for (int i=0;i<labels.length-1;i++){
         	classlabels.add(labels[i]);
         	label.put(labels[i], i);
         }
 		
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
         	
		List<List<Double>>result=new ArrayList<List<Double>>();
		
		for (int l=0;l<loop;l++){
	        ArffLoader loader = new ArffLoader();		
			loader.setSource(new File("./data/attack/spambase/attack/spambase_attack.arff"));
			Instances m_data = loader.getDataSet();
			m_data.setClassIndex(m_data.numAttributes() - 1);
			
			ArffLoader loader1 = new ArffLoader();		
			loader1.setSource(new File("./data/attack/spambase/attack/spambase_all.arff"));
		    Instances all_data = loader1.getDataSet();
		    all_data.setClassIndex(all_data.numAttributes() - 1);
		    
			
	    List<Instance>instancedatas= new ArrayList<Instance>();	

        int guess=57;
        int numTest=50;

		String path = "./data/attack/spambase/model/5,50/w_models/";

		 for (int i=0;i<numTest;i++){
		     instancedatas.add((Instance) m_data.instance(i).copy());	
		 }
		 
		 List<Integer>real_guess_list= new ArrayList<Integer>();


	  	    
		 BufferedReader br1 = new BufferedReader(new FileReader(new File(
					"./data/attack/spambase/model/5,50/temp/ground_truth.txt")));
		 String static_weight=br1.readLine();
		 while(static_weight!=null){
			String[] weights=static_weight.split(":");
				real_guess_list.add(Integer.parseInt(weights[0]));
				static_weight=br1.readLine();
			}
			br1.close();
		
		 

	/*	 
		 BufferedReader br1 = new BufferedReader(new FileReader(new File(dispath)));
		 String static_weight=br1.readLine();
		 while(static_weight!=null){
			String[] weights=static_weight.split("=");
				candidate_list.add(label.get(weights[0]));
				static_weight=br1.readLine();
			}
			br1.close();
			 for (int i=0;i<candidate_list.size();i++){
				 real_guess_list.add(candidate_list.get(i));
			 }
*/

	     SpectralClusteringAttacker_GroundTruth scag = new SpectralClusteringAttacker_GroundTruth();
	     
         result.add(scag.getresult(model_list,path,numSelection,numTree,classlabels,all_data,guess,medians,real_guess_list));
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
//		if(numSelection==50){
//         numSelection=numSelection+49;
//		}
//		else
		 numSelection=numSelection+5;
		}
	}
}
