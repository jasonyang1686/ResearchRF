package RandomForest_spambase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class ModelVoteTest {
	
	public static void main(String[] args) throws Exception {
        ArffLoader loader = new ArffLoader();	
		//loader.setSource(new File("./data/spambase/testing/spambase_all_normal.arff"));
		loader.setSource(new File("./data/kyoto/testing/output_normal.arff"));
		Instances m_data = loader.getDataSet();
		m_data.setClassIndex(m_data.numAttributes() - 1);
        
	    System.out.println(m_data.instance(0).classValue());	
		
	    String testPath = "./data/kyoto/IG/w_models_100_new/";
	    String modelPath = "./data/kyoto/w_models_100/";
     //   String testPath ="./data/spambase/models_test/";
	 //   String modelPath ="./data/spambase/IG/w_models_15/"; 
	   
	    List<Integer>models = new ArrayList<Integer>();		
	    for (int i=0;i<100;i++){
		     models.add(i);	
		 }
		 		 
			Classifier[]test_Classifiers= new Classifier[models.size()];
			
		      for (int test=0;test< test_Classifiers.length; test++){
		    	  test_Classifiers[test] = (Classifier) weka.core.SerializationHelper.read(testPath+"Classifier"+models.get(test)+".model");
		      } 
		
		
		
	    Map<String, Integer> label = new HashMap<String, Integer>();	
	    label.put("0.0", 0);
	    label.put("1.0",0);
        for (int j = 0; j < test_Classifiers.length; j++) {
        double pred = test_Classifiers[j].classifyInstance(m_data.instance(0));

        	label.put(pred+"", label.get(pred+"")+1);      
     //   System.out.println(pred);
        }
        
	      ArrayList<Map.Entry<String,Integer>> list_Data = new ArrayList<Map.Entry<String, Integer>>(label.entrySet());  
	      
	  	    for (int i=0;i<list_Data.size();i++){
	  	    
		 	    System.out.println(list_Data.get(i).getKey()+":"+list_Data.get(i).getValue());
	  	    }
	  	    
	}

}
