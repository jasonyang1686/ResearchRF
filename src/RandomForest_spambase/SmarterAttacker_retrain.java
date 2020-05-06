package RandomForest_spambase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;

public class SmarterAttacker_retrain {
	public static void main(String[] args) throws Exception {
		final long start = System.currentTimeMillis();
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
		
        CSVLoader loader = new CSVLoader();	

	//	loader.setSource(new File("./data/spambase/training/spambase_model_normal_14.arff")); 
	//	loader.setSource(new File("./data/kyoto/testing/201501_twodays_test_removal_binary_balance_sampling.arff"));
        loader.setSource(new File("./data/kyoto/training/201512_tendays_removal_binary_100_13.csv"));
		Instances all_data = loader.getDataSet();
		all_data.setClassIndex(all_data.numAttributes() - 1);
        int numAttributes=all_data.numAttributes() - 1;	
	
	  //    String path = "./data/kyoto/models_test/";
      //  String path ="./data/spambase/models_34_1/";
	    String path ="./data/models_100%_23/"; 	
	    List<Integer>models = new ArrayList<Integer>();		
	    for (int i=0;i<numRandomTree;i++){
		     models.add(i);	
		 }
		 
		 
			Classifier[]m_Classifiers= new Classifier[models.size()];
			
		      for (int m=0;m< m_Classifiers.length; m++){
		    	  m_Classifiers[m] = (Classifier) weka.core.SerializationHelper.read(path+"Classifier"+models.get(m)+".model");
		      } 
        
		 VoteCount vc = new VoteCount();
		 
         double count =0;
         double tp =0;
         double fn= 0;
         double tn=0;
         double fp=0;
         System.out.println(all_data.size());
         
     	FileOutputStream fos = new FileOutputStream("./data/201512_tendays_removal_binary_100_13_attack.csv"); 
    	OutputStreamWriter osw = new OutputStreamWriter(fos);  
    	BufferedWriter bw = new BufferedWriter(osw); 
         
         for (int n=0;n<all_data.size();n++) {
         if(vc.prediction(all_data.get(n),m_Classifiers)==0.0) {
        	 if(all_data.get(n).classValue()==0.0) {
        		 tp++;
		 int position = vc.getresult(m_Classifiers, all_data.get(n), numAttributes, medians); 
   	  if (! all_data.get(n).attribute(position).isNumeric()){
   		 all_data.get(n).setValue(position, medians.get(position));
		 }else {
		 all_data.get(n).setValue(position, Double.parseDouble(medians.get(position)));  
		 }
   	      count++;
   	    for (int i=0;i<numAttributes;i++) {
		 if(vc.prediction(all_data.get(n),m_Classifiers)==0.0) {
		  count++;	 
		  position = vc.getresult(m_Classifiers, all_data.get(n), numAttributes, medians);
		  if (! all_data.get(n).attribute(position).isNumeric()){
		   		 all_data.get(n).setValue(position, medians.get(position));
				 }else {
				 all_data.get(n).setValue(position, Double.parseDouble(medians.get(position)));  
				 }
		 }else {
		  break;
		 }
   	    }
	     bw.write(all_data.get(n).toString()+"\n"); 
	}else {
		fp++;
	}
         }else {
    	     bw.write(all_data.get(n).toString()+"\n"); 
        	 if(all_data.get(n).classValue()==0.0) {
        	 fn++;
        	 }else {
        	 tn++;	 
        	 }
         }
         }
         
      bw.close();  
		final long end = System.currentTimeMillis(); 
		
		System.out.println("time: "+(end-start));
      System.out.println((tp+tn)/(tp+fp+tn+fn));  
      System.out.println(tp/(tp+fn));
      System.out.println(count/(tp));
	}
}
