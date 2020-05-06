package NN_spambase;

import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
 
public class NN {
	public void BuildNN (String path) throws Exception{
		
	ArffLoader loader = new ArffLoader();
	loader.setSource(new File(path));
	Instances data = loader.getDataSet();
	data.setClassIndex(data.numAttributes()-1);

	MultilayerPerceptron mlp = new MultilayerPerceptron();

	mlp.setLearningRate(0.3);
	mlp.setMomentum(0.2);
	mlp.setTrainingTime(2000);
	mlp.setHiddenLayers("a");
	mlp.buildClassifier(data);
	weka.core.SerializationHelper.write("", mlp);
	FileOutputStream fos = new FileOutputStream("./data/result.txt"); 
	OutputStreamWriter osw = new OutputStreamWriter(fos);  
	BufferedWriter bw = new BufferedWriter(osw); 
	
    bw.write(mlp.toString());
	int sum = data.numInstances(); 
	System.out.print(sum);   
	
	bw.close();
	}
	public static void main(String[] args) throws Exception {
		String path = "./data/kyoto/training/201512_tendays_removal_binary_balance_sampling.arff";
	//	String path = "./data/spambase/training/spambase_model_normal_12_2.arff";
	//	String path = "./data/spambase/testing/spambase_all_normal.arff";
	//	String path = "./data/kyoto/testing/test_retrain.arff";
		
		
		NN nn=new NN();
		nn.BuildNN(path);
			
	}
}
