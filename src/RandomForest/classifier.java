package RandomForest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomTree;
import weka.classifiers.trees.RandomTree1;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.CSVLoader;

public class classifier {
	public void BuildClassifier(String path) throws Exception {
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(path));
		Instances data = loader.getDataSet();
		data.setClassIndex(data.numAttributes() - 1);
			
		CSVLoader loader1 = new CSVLoader();
		loader1.setSource(new File("./data/5,100/OOB/inbag0.txt"));
		Instances data1 = loader1.getDataSet();
		data1.setClassIndex(data.numAttributes() - 1);
		int k=0;
        BufferedReader br = new BufferedReader(new FileReader(new File("./data/5,100/weight/weight0.txt"))); 
        String line = br.readLine();
        while (line!=null){
        	data1.instance(k).setWeight(Double.parseDouble(line));
        	k++;
        	line=br.readLine();
        }
         
		
		  RandomTree1 rm = new RandomTree1();
		  rm.setKValue(5);
		  rm.setMaxDepth(5);
		  rm.setSeed(-1155869325);
	        
		  rm.buildClassifier(data1);		
		  System.out.println( rm.toString());

		boolean numeric = data.classAttribute().isNumeric();
//		System.out.println(cls.toString());		
		double treeResult=0.0;
		double treeCount=0.0;
		for (int i = 0; i < data.numInstances(); i++) {
			if (numeric) {
		//		System.out.println("numeric");
				double pred = rm.classifyInstance(data.instance(i));
			//	System.out.println(pred);
	            if (!Utils.isMissingValue(pred)) {
	            	treeResult +=StrictMath.abs(pred-data.instance(i).classValue()) * data.instance(i).weight();
	            	treeCount +=data.instance(i).weight();
	            }
			}else{
	
				 double[] newProbs = rm.distributionForInstance(data.instance(i));
		            if(Utils.maxIndex(newProbs)!=data.instance(i).classValue()){
		            treeResult+=data.instance(i).weight();
		              }
	            	treeCount +=data.instance(i).weight();		            
			}
		}
       System.out.println(treeResult/treeCount);
	}

	public static void main(String[] args) throws Exception {
		String path = "./data/5,100/OOB/outbag0.txt";
		// String path = "./data/test.txt";
		classifier cr = new classifier();
		cr.BuildClassifier(path);
	}

}