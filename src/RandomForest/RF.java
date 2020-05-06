package RandomForest;
 
import weka.classifiers.Classifier;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
 
public class RF {
	public void BuildRF (String path) throws Exception{
	CSVLoader loader = new CSVLoader();
	loader.setSource(new File(path));
	Instances data = loader.getDataSet();
	data.setClassIndex(data.numAttributes()-1);
	System.out.println( data.numClasses());
	RandomForest RandomF = new RandomForest();
	String[]options=RandomF.getOptions();
	/*
	for (int i=0;i<options.length;i++){
	System.out.println(options[i]);
	}
	*/
	
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
	
 /*
	for (int i = 0; i < sum; i++) {  
	    double a = data.instance(i).classValue();
	    double b = RandomF.classifyInstance(data.instance(i)); 
	    double[]distribute = RandomF.distributionForInstance(data.instance(i));  
	    
	    System.out.print(b + " ");  

	    if (distribute != null) {  
	        distribute = RandomF.distributionForInstance((data.instance(i)));  
	        float dis0 = (float) (Math.round(distribute[0] * 10000)) / 10000;
	        float dis1 = (float) (Math.round(distribute[1] * 10000)) / 10000;  
	        float dis2 = (float) (Math.round(distribute[2] * 10000)) / 10000;  

	        System.out.print(dis0 + " ");  
	        System.out.print(dis1 + " ");  
	        System.out.print(dis2 + " \n");  
	        String str = a + " " + b + " " + dis0 + " " + dis1 + " " + dis2  
	                + "\n";  
	        bw.write(str);  
	    }
	}
	*/
	bw.close();
	}
	public static void main(String[] args) throws Exception {
		String path = "./data/attack/spambase/model/spambase1.data";
	//	String path = "./data/test.txt";
		RF rf=new RF();
		rf.BuildRF(path);
			
	}
}