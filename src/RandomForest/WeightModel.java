package RandomForest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.RandomTree_w;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class WeightModel {
	  public void buildClassifier(String savemodelpath, Instances data, int random, int KValue) throws Exception {
		  
		  RandomTree_w rm = new RandomTree_w();
		  rm.setKValue(KValue);
		  rm.setSeed(random);
	      rm.setMaxDepth(6);  
		  rm.buildClassifier(data);		
		  weka.core.SerializationHelper.write(savemodelpath, rm);
		  System.out.println(rm.toString());
	  }
	
	public static void main(String[] args) throws Exception {
		int KValue= 5;
		int iteration =100;

        BufferedReader br = new BufferedReader(new FileReader(new File("./data/attack/spambase/model/spambase1.data"))); 
        String[] labels = br.readLine().split(",");
        br.close();
		
		//random number
		List<Integer>random = new ArrayList<Integer>();
        BufferedReader br1 = new BufferedReader(new FileReader(new File("./data/attack/spambase/model/5,50/random.txt"))); 
        String number = br1.readLine();
        while (number!=null){
        	random.add(Integer.parseInt(number));
            number = br1.readLine();	
        }
        br1.close();
        
//        System.out.println(random.size());
        //indexCount
		FileOutputStream fos = new FileOutputStream("./data/attack/spambase/model/5,50/temp/indexCount.txt"); 
		OutputStreamWriter osw = new OutputStreamWriter(fos);  
		BufferedWriter bw = new BufferedWriter(osw);
        for (int i=0;i<labels.length-1;i++){
        	bw.write(i+"=1.0\n");
        }
		bw.close();
		
		//classCount
		FileOutputStream fos1 = new FileOutputStream("./data/attack/spambase/model/5,50/temp/classCount.txt"); 
		OutputStreamWriter osw1 = new OutputStreamWriter(fos1);  
		BufferedWriter bw1 = new BufferedWriter(osw1);
        for (int i=0;i<labels.length-1;i++){
        	bw1.write(labels[i]+"=1.0\n");
        }
		bw1.close();		
		
		//iteration
		for (int i=0;i<iteration-1;i++){
	    List<Double> weight = new ArrayList<Double>();		

		String datapath = "./data/attack/spambase/model/5,50/OOB/inbag"+(i+1)+".txt";
		String weightpath = "./data/attack/spambase/model/5,50/weight/weight"+(i+1)+".txt";
		String savemodelpath = "./data/attack/spambase/model/5,50/w_models/Classifier"+(i+1)+".model";

        BufferedReader brw = new BufferedReader(new FileReader(new File(weightpath))); 	
        String nodeweight=brw.readLine();
		while(nodeweight!=null){
			weight.add(Double.parseDouble(nodeweight));
			nodeweight=brw.readLine();
		}
		brw.close();
		
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(datapath));
		Instances data = loader.getDataSet();
		data.setClassIndex(data.numAttributes() - 1);
		for (int k=0;k<data.numInstances();k++){
			data.instance(k).setWeight(weight.get(k));
		}
		WeightModel wm=new WeightModel();
		wm.buildClassifier(savemodelpath,data,random.get(i+1), KValue);
		}
			
	}
}