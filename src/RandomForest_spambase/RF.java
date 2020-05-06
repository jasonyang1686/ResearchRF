package RandomForest_spambase;
 
import weka.classifiers.Classifier;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
 
public class RF {
	public void BuildRF (String path) throws Exception{
	

	
	//ArffLoader loader = new ArffLoader();
	CSVLoader loader = new CSVLoader();
	loader.setSource(new File(path));
	Instances data = loader.getDataSet();
	data.setClassIndex(data.numAttributes()-1);

	RandomForest RandomF = new RandomForest();

//	RandomF.setSeed(100);
	RandomF.setNumFeatures(5);
	RandomF.setMaxDepth(6);
	RandomF.setNumTrees(100);
	RandomF.buildClassifier(data);
    RandomF.setPrintTrees(true);
	RandomF.setDontCalculateOutOfBagError(false);
//	System.out.println("print? "+RandomF.getDontCalculateOutOfBagError());
	FileOutputStream fos = new FileOutputStream("./data/result.txt"); 
	OutputStreamWriter osw = new OutputStreamWriter(fos);  
	BufferedWriter bw = new BufferedWriter(osw); 

//  System.out.print(RandomF.toString());   
    bw.write(RandomF.toString());
    

	
	int sum = data.numInstances(); 
	System.out.print(sum);   
	
	bw.close();
	}
	public static void main(String[] args) throws Exception {
	//	String path = "./data/kyoto/training/201512_tendays_removal_binary_balance_sampling_12_2.arff";
	//	String path = "./data/spambase/training/spambase_model_normal_12_2.arff";
	//	String path = "./data/spambase/testing/spambase_all_normal.arff";
	//	String path = "./data/kyoto/testing/test_retrain.arff";
		String path = "./data/kyoto/training/201512_tendays_removal_binary_100_full.csv";
		final long start = System.currentTimeMillis(); 	
		RF rf=new RF();
		rf.BuildRF(path);
		final long end = System.currentTimeMillis(); 
		
		System.out.println("time: "+(end-start));
			
	}
}