package RandomForest_spambase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class SpectralClusterAttacker_greedy_white {
	
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
	
	
	public static void main(String[] args) throws Exception {
		
    //	String matrix_path="./data/spambase/temp/w_spectral_matrix_15.txt";
    	String matrix_path="./data/kyoto/temp/spectral_matrix.txt";
     	
    	int numCluster=5;
		int numTree=100;		
		int numSelection=1;
        
		double[][]count_matrix=new double[numTree][numTree];
      	double[][]ratio_matrix=new double[numTree][numTree]; 	
      	
    	
   	 BufferedReader brm = new BufferedReader(new FileReader(new File(matrix_path))); 
     String data = brm.readLine();
     int mnumber=0;
     while (data != null) {
         String []t = data.split("  ");  
         for (int i=0;i<t.length;i++){
         	count_matrix[mnumber][i]=Double.parseDouble(t[i]);
         }
         mnumber++;
         data=brm.readLine();
     }
     brm.close();
     
     SpectralClustering_Own st =new SpectralClustering_Own();
     
 	 List<ArrayList<Integer>>model_list=st.clustering(count_matrix,numCluster);
        
		int numRandomTree=100;	    
        List<String>medians=new ArrayList<String>();
        
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/kyoto/testing/output_normal.txt")));
	        //    "./data/spambase/testing/output_normal.txt")));
		String[] output=br.readLine().split(",");
		for (int i=0;i<output.length;i++){
			medians.add(output[i]);
		}
		br.close();
			
	      String path = "./data/kyoto/w_models_100/";
     //   String path ="./data/spambase/models_black_box/";
	 //   String path ="./data/spambase/w_models_15/"; 	
	    List<Integer>models = new ArrayList<Integer>();	
	    

         double accu =0;
         double tpr =0;
         double fpr=0;
         double numTry=0;
         double loop =0;
       
           while(loop<2) {
               double tp =0;
               double count =0;
               double fn= 0;
               double tn=0;
               double fp=0;
               double passCount=0;
               double consume_total=0;
               ArffLoader loader = new ArffLoader();	

               //    loader.setSource(new File("./data/spambase/testing/spambase_retrain_attack_normal.arff"));
           	//	loader.setSource(new File("./data/spambase/testing/spambase_all_normal.arff")); 
           		loader.setSource(new File("./data/kyoto/testing/201512_week_removal_binary_balance_sampling.arff"));
           		Instances all_data = loader.getDataSet();
           		all_data.setClassIndex(all_data.numAttributes() - 1);
                   int numAttributes=all_data.numAttributes() - 1;	  
               
         for (int n=0;n<all_data.size();n++) {
        	 int dataCount=0;
     		int budget = 5;
    		 SpectralClusterAttacker_greedy_white scagw = new SpectralClusterAttacker_greedy_white();
      	     models=scagw.getList(model_list, numSelection);
    		 VoteCount vc = new VoteCount(); 
 
     			Classifier[]m_Classifiers= new Classifier[models.size()];
     			   
     		      for (int m=0;m< m_Classifiers.length; m++){
     		    	  m_Classifiers[m] = (Classifier) weka.core.SerializationHelper.read(path+"Classifier"+models.get(m)+".model");
     		      } 
     		      
     		      
         if(vc.prediction(all_data.get(n),m_Classifiers)==0.0) {
        	 if(all_data.get(n).classValue()==0.0) {
        		 tp++;
        		 int consume =0;
		 int position = vc.getresult(m_Classifiers, all_data.get(n), numAttributes, medians); 
 		  if(position==0) {
   			  consume=consume+1;
   		  }
   		  if(position==1) {
   			  consume=consume+1;
   		  }
   		  if(position==2) {
   			  consume=consume+5;
   		  }
   		  if(position==3) {
   			  consume=consume+3;
   		  }
   		  if(position==4) {
   			  consume=consume+3;
   		  }
   		  if(position==5) {
   			  consume=consume+3;
   		  }
   		  if(position==6) {
   			  consume=consume+3;
   		  }
   		  if(position==7) {
   			  consume=consume+3;
   		  }
   		  if(position==8) {
   			  consume=consume+3;
   		  }
   		  if(position==9) {
   			  consume=consume+3;
   		  }
   		  if(position==10) {
   			  consume=consume+3;
   		  }
   		  if(position==11) {
   			  consume=consume+1;
   		  }
   		  if(position==12) {
   			  consume=consume+5;
   		  }
   	  if (! all_data.get(n).attribute(position).isNumeric()){
   		 all_data.get(n).setValue(position, medians.get(position));
		 }else {
		 all_data.get(n).setValue(position, Double.parseDouble(medians.get(position)));  
		 }
   	      count++;
   	      dataCount++;
   	      
   	    while (consume<budget) {
   	    	
     	  models=scagw.getList(model_list, numSelection);
     	  
		  m_Classifiers= new Classifier[models.size()];
			   
		      for (int m=0;m< m_Classifiers.length; m++){
		    	  m_Classifiers[m] = (Classifier) weka.core.SerializationHelper.read(path+"Classifier"+models.get(m)+".model");
		      } 
   	    	
		 if(vc.prediction(all_data.get(n),m_Classifiers)==0.0) {
		  count++;
		  dataCount++;
		  position = vc.getresult(m_Classifiers, all_data.get(n), numAttributes, medians);
  		  if(position==0) {
   			  consume=consume+1;
   		  }
   		  if(position==1) {
   			  consume=consume+1;
   		  }
   		  if(position==2) {
   			  consume=consume+5;
   		  }
   		  if(position==3) {
   			  consume=consume+3;
   		  }
   		  if(position==4) {
   			  consume=consume+3;
   		  }
   		  if(position==5) {
   			  consume=consume+3;
   		  }
   		  if(position==6) {
   			  consume=consume+3;
   		  }
   		  if(position==7) {
   			  consume=consume+3;
   		  }
   		  if(position==8) {
   			  consume=consume+3;
   		  }
   		  if(position==9) {
   			  consume=consume+3;
   		  }
   		  if(position==10) {
   			  consume=consume+3;
   		  }
   		  if(position==11) {
   			  consume=consume+1;
   		  }
   		  if(position==12) {
   			  consume=consume+5;
   		  }
		  if (! all_data.get(n).attribute(position).isNumeric()){
		   		 all_data.get(n).setValue(position, medians.get(position));
				 }else {
				 all_data.get(n).setValue(position, Double.parseDouble(medians.get(position)));  
				 }
		 }else {
		  consume_total+=consume;
		  passCount++;
		  break;
		 }
   	    }
  // 	 System.out.println(dataCount);
	 //    System.out.println(all_data.get(n).toString()); 
	}else {
		fp++;
	}
         }else {
        	 if(all_data.get(n).classValue()==0.0) {
        	 fn++;
        	 }else {
        	 tn++;	 
        	 }
         }
         }
	     System.out.println("Total Consume :"+consume_total);          
	     System.out.println("passCount :"+passCount);   
         System.out.println(tp);
 		System.out.println("Success Rate:" + (passCount/tp));
         System.out.println(fn);
         System.out.println(fp);
         System.out.println((tp+tn)/(tp+fp+tn+fn));
         System.out.println(fp/(fp+tn));
         System.out.println(tp/(tp+fn));
         System.out.println(count/(tp));

          accu+= ((tp+tn)/(tp+fp+tn+fn));
          fpr+= (fp/(fp+tn));
          tpr+= (tp/(tp+fn));
          
          numTry+= (count/(tp));
          loop++;
           }
           System.out.println(accu/(loop+0.0));
           System.out.println(fpr/(loop+0.0));
           System.out.println(tpr/(loop+0.0));
           System.out.println(numTry/(loop+0.0));
	}
}
