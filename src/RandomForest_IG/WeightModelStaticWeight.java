package RandomForest_IG;

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
import weka.classifiers.trees.RandomTree_KDD;
import RandomForest_NSL_KDD.probability;
import weka.classifiers.trees.RandomTree_spambase_random_fly;
import weka.classifiers.trees.RandomTree_spambase_static_weight;
import weka.core.Instances;
import weka.core.converters.ArffLoader;


public class WeightModelStaticWeight {
	  public void buildClassifier(String savemodelpath, Instances data, int random, int KValue) throws Exception {
		  
		  RandomTree_spambase_static_weight sw = new RandomTree_spambase_static_weight();
		  sw.setSeed(random);
		  sw.setKValue(KValue);
	      sw.setMaxDepth(6);  
		  sw.buildClassifier(data);	
		  weka.core.SerializationHelper.write(savemodelpath, sw);
		  System.out.println(sw.toString());
	  }
	
	public static void main(String[] args) throws Exception {
		int KValue=5;
		int iteration =100;
		List<String> classlabels = new ArrayList<String>();

		
		String path = "./data/spambase/models/";

		List<Integer> pathlist = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++) {
			pathlist.add(i);
		}
		
		double r=0.5;
		
		List<String>staticCount = new ArrayList<String>();
		List<Double>temp_staticValue= new ArrayList<Double>();
		List<Double>final_staticValue= new ArrayList<Double>();

        BufferedReader br = new BufferedReader(new FileReader(new File("./data/spambase/training/spambase_model_normal.data"))); 
        String[] labels = br.readLine().split(",");
        br.close();
        
		for (int i = 0; i < labels.length - 1; i++) {
			classlabels.add(labels[i]);
		}
		
		//random number
		List<Integer>random = new ArrayList<Integer>();
        BufferedReader br1 = new BufferedReader(new FileReader(new File("./data/spambase/temp/random.txt"))); 
        String number = br1.readLine();
        while (number!=null){
        	random.add(Integer.parseInt(number));
            number = br1.readLine();	
        }
        br1.close();
        
        BufferedReader br3 = new BufferedReader(new FileReader(new File("./data/spambase/IG/temp/static_weight.txt"))); 
        String staticline = br3.readLine();
        while(staticline!=null){
        	
        	String[]staticcontents = staticline.split(",");
        	staticCount.add(staticcontents[0]);
        	temp_staticValue.add(Double.parseDouble(staticcontents[1]));
        	
        	staticline =br3.readLine();

        }
        br3.close();
        
        for (int i=0;i<temp_staticValue.size();i++){
        	final_staticValue.add((temp_staticValue.get(i)-temp_staticValue.get(temp_staticValue.size()-1))/(temp_staticValue.get(0)-temp_staticValue.get(temp_staticValue.size()-1)));
        	    }
//        System.out.println(random.size());
        
		List<String> med_result = new ArrayList<String>();
		probability prob = new probability();
		med_result = prob.process(path, pathlist, classlabels,r);
		
		
        //indexCount
		FileOutputStream fos = new FileOutputStream("./data/spambase/temp/indexCount.txt"); 
		OutputStreamWriter osw = new OutputStreamWriter(fos);  
		BufferedWriter bw = new BufferedWriter(osw);
        for (int i=0;i<labels.length-1;i++){
        	String[]indexcontents=med_result.get(i).split(",");
        		bw.write(i+","+0+"\n");      		      	
        }
		bw.close();
		
		//classCount
		FileOutputStream fos1 = new FileOutputStream("./data/spambase/temp/classCount.txt"); 
		OutputStreamWriter osw1 = new OutputStreamWriter(fos1);  
		BufferedWriter bw1 = new BufferedWriter(osw1);
        for (int i=0;i<labels.length-1;i++){
        	String[]classcontents=med_result.get(i).split(",");
        	    	
        	bw1.write(classcontents[0]+","+classcontents[1]+"\n");
        }
		bw1.close();	
		
		//sliding windows
		FileOutputStream fos2 = new FileOutputStream("./data/spambase/temp/slidingCount.txt"); 
		OutputStreamWriter osw2 = new OutputStreamWriter(fos2);  
		BufferedWriter bw2 = new BufferedWriter(osw2);
        for (int i=0;i<labels.length-1;i++){
        	bw2.write(labels[i]+",0\n");
        }
		bw2.close();
		//iteration
		for (int i=0;i<iteration-1;i++){
	    List<Double> weight = new ArrayList<Double>();		

		String datapath = "./data/spambase/temp/OOB/inbag"+(i+1)+".arff";
		String weightpath = "./data/spambase/temp/weight/weight"+(i+1)+".txt";
		String savemodelpath = "./data/spambase/w_models/Classifier"+(i+1)+".model";

        BufferedReader brw = new BufferedReader(new FileReader(new File(weightpath))); 	
        String nodeweight=brw.readLine();
		while(nodeweight!=null){
			weight.add(Double.parseDouble(nodeweight));
			nodeweight=brw.readLine();
		}
		brw.close();
		
		ArffLoader loader = new ArffLoader();
		loader.setSource(new File(datapath));
		Instances data = loader.getDataSet();
		data.setClassIndex(data.numAttributes() - 1);
		for (int k=0;k<data.numInstances();k++){
			data.instance(k).setWeight(weight.get(k));
		}
		WeightModelStaticWeight wmsw=new WeightModelStaticWeight();
		wmsw.buildClassifier(savemodelpath,data,random.get(i+1), KValue);
		}
			
	}
}