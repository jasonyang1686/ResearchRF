package RandomForest_spambase;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class test1 {
	

	public static void main(String[] args) throws Exception {
		int numRandomTree=100;	    
        List<String>medians=new ArrayList<String>();
        
		BufferedReader br = new BufferedReader(new FileReader(new File(
			//	"./data/kyoto/testing/output_normal.txt")));
	            "./data/spambase/testing/output_normal.txt")));
		String[] output=br.readLine().split(",");
		for (int i=0;i<output.length;i++){
			medians.add(output[i]);
		}
		br.close();
		
        ArffLoader loader = new ArffLoader();	
	//	loader.setSource(new File("./data/spambase/testing/spambase_attack_normal.arff"));
	//	loader.setSource(new File("./data/kyoto/testing/201501_twodays_test_removal_binary_balance_sampling.arff"));
	//	Instances m_data = loader.getDataSet();
	//	m_data.setClassIndex(m_data.numAttributes() - 1);
	//	loader.reset();
		

    //    loader.setSource(new File("./data/spambase/testing/spambase_retrain_attack_normal.arff"));
	//	loader.setSource(new File("./data/spambase/testing/spambase_all_normal.arff")); 
		loader.setSource(new File("./data/kyoto/testing/test.arff"));
		Instances all_data = loader.getDataSet();
		all_data.setClassIndex(all_data.numAttributes() - 1);
        int numAttributes=all_data.numAttributes() - 1;	
			
	 //  String path = "./data/kyoto/w_models_40/";
      //    String path = "./data/kyoto/models_test/";
      // String path ="./data/kyoto/models_retrain_test/";
	   String path ="./data/spambase/models_34_2/"; 	
	    List<Integer>models = new ArrayList<Integer>();		
	    for (int i=0;i<numRandomTree;i++){
		     models.add(i);	
		 }
		 
		 
			Classifier[]m_Classifiers= new Classifier[models.size()];
			
		      for (int m=0;m< m_Classifiers.length; m++){
		    	  m_Classifiers[m] = (Classifier) weka.core.SerializationHelper.read(path+"Classifier"+models.get(m)+".model");
		      } 
        
		 VoteCount vc = new VoteCount();
		 
		 System.out.println(vc.getVotes(all_data.get(0),m_Classifiers));
		 
	}


// main()
}
// class Fw