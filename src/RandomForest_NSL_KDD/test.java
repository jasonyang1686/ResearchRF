package RandomForest_NSL_KDD;
 
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
 
public class test {
	public void BuildRF (String path) throws Exception{
		
		Classifier cls = (Classifier) weka.core.SerializationHelper
				.read(path);
	System.out.print(cls.toString());   
	
	}
	public static void main(String[] args) throws Exception {
		String path = "./data/attack/NSL_KDD/model/5,50/models/Classifier1.model";
	//	String path = "./data/test.txt";
		test te=new test();
		te.BuildRF(path);
			
	}
}