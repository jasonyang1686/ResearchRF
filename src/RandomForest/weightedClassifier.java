package RandomForest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.CSVLoader;

public class weightedClassifier {
	public List<Double> BuildClassifier(int iteration,String path) throws Exception {
		FileOutputStream fos3 = new FileOutputStream("./data/textresult2.txt"); 
		OutputStreamWriter osw3 = new OutputStreamWriter(fos3);  
		BufferedWriter bw3 = new BufferedWriter(osw3); 
		List<Double>result=new ArrayList<Double>();
	      List<Double> treeResult = new ArrayList<Double>();  
	      List<Double> treeCount = new ArrayList<Double>();
	      for (int m=0;m< iteration; m++){
	    	  treeResult.add(0.0);
	    	  treeCount.add(0.0);
	      }
	      
		for (int i=0; i<iteration;i++){
			
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(path+"OOB/outbag"+i+".txt"));
		Instances data = loader.getDataSet();
		data.setClassIndex(data.numAttributes() - 1);
		
		Classifier cls = (Classifier) weka.core.SerializationHelper.read(path+"models/Classifier"+i+".model");	
		System.out.println(cls.toString());
		  System.out.println( data.classIndex());
		boolean numeric = data.classAttribute().isNumeric();
//		System.out.println(cls.toString());		
		for (int k = 0; k < data.numInstances(); k++) {
			if (numeric) {
		//		System.out.println("numeric");
				double pred = cls.classifyInstance(data.instance(k));
	        	bw3.write(pred+"\n"); 
	            if (!Utils.isMissingValue(pred)) {
	                treeResult.set(i, treeResult.get(i)+StrictMath.abs(pred-data.instance(k).classValue()) * data.instance(k).weight());	
	                treeCount.set(i,treeCount.get(i)+data.instance(k).weight());
	            }
			}else{
	
				 double[] newProbs = cls.distributionForInstance(data.instance(k));
		            if(Utils.maxIndex(newProbs)!=data.instance(k).classValue()){
		                treeResult.set(i,treeResult.get(i)+data.instance(k).weight());
		              }
	                treeCount.set(i,treeCount.get(i)+data.instance(k).weight());	            
			}
		}

		}
	      for (int m=0;m<treeResult.size();m++){
	    	  System.out.println(treeResult.get(m));
	    	  System.out.println(treeCount.get(m));
	  	   result.add(treeResult.get(m)/treeCount.get(m));
	  	    }        
	      
	    bw3.close();
		return result;
	}

	public static void main(String[] args) throws Exception {
		int iteration=1;
		String path = "./data/5,100/";
		weightedClassifier cr = new weightedClassifier();
		List <Double> result=cr.BuildClassifier(iteration,path);
		for (int i=0;i<result.size();i++){
			System.out.println(result.get(i));
		}
	}

}