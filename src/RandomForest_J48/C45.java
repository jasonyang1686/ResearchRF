package RandomForest_J48;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import RandomForest_IG.RF;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class C45 {
	public void buildModel (String path) throws Exception{
		
	ArffLoader loader = new ArffLoader();
	loader.setSource(new File(path));
	Instances data = loader.getDataSet();
	data.setClassIndex(data.numAttributes()-1);
	System.out.println( data.numClasses());
	J48 j48 = new J48();

	j48.setUnpruned(false);
	j48.buildClassifier(data);

//	System.out.println("print? "+RandomF.getDontCalculateOutOfBagError());
	FileOutputStream fos = new FileOutputStream("./data/result.txt"); 
	OutputStreamWriter osw = new OutputStreamWriter(fos);  
	BufferedWriter bw = new BufferedWriter(osw); 

//  System.out.print(RandomF.toString());   
    bw.write(j48.toString());
	int sum = data.numInstances(); 
	System.out.print(sum);  
	bw.close();
	weka.core.SerializationHelper.write("./data/j48.model", j48);
	}
	public static void main(String[] args) throws Exception {
		String path = "./data/kyoto/training/201512_tendays_removal_binary_balance_sampling.arff";
	//	String path = "./data/test.txt";
		C45 c45=new C45();
		c45.buildModel(path);
			
	}
}
