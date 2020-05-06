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
import weka.classifiers.trees.RandomTree_KDD_IG_dynamic;
import weka.classifiers.trees.RandomTree_KDD_static_weight;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

//not finished, need more time to investigate
public class WeightModelDynamic {
	  public void buildClassifier(String savemodelpath, Instances data, int random, int KValue) throws Exception {
		  
		  RandomTree_KDD_IG_dynamic rl = new RandomTree_KDD_IG_dynamic();
		  rl.setSeed(random);
		  rl.setKValue(KValue);
	      rl.setMaxDepth(6);  
		  rl.buildClassifier(data);	
		  weka.core.SerializationHelper.write(savemodelpath, rl);
		  System.out.println(rl.toString());
	  }
	
	public static void main(String[] args) throws Exception {
		
		double r=2.5;
		int KValue=5;
		int iteration =100;
		List<String> classlabels = new ArrayList<String>();
		Map<String,Double> indexlabels = new HashMap<String,Double>();

		
		String path = "./data/attack/NSL_KDD/model/5,50/models/";

		List<Integer> pathlist = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++) {
			pathlist.add(i);
		}
				

		List<Double>temp_staticValue= new ArrayList<Double>();
		List<Double>final_staticValue= new ArrayList<Double>();

        BufferedReader br = new BufferedReader(new FileReader(new File("./data/spambase/training/spambase_model_normal.data"))); 
        String[] labels = br.readLine().split(",");
        br.close();
        
		for (int i = 0; i < labels.length - 1; i++) {
			classlabels.add(labels[i]);
			indexlabels.put(labels[i], 0.0);
		}
		
		//random number
		
		List<Integer>random = new ArrayList<Integer>();
        BufferedReader br1 = new BufferedReader(new FileReader(new File("./data/attack/NSL_KDD/model/5,50/temp/random.txt"))); 
        String number = br1.readLine();
        while (number!=null){
        	random.add(Integer.parseInt(number));
            number = br1.readLine();	
        }
        br1.close();
        
        BufferedReader br3 = new BufferedReader(new FileReader(new File("./data/attack/NSL_KDD/model/5,50/temp/Val/Val1.txt"))); 
        String staticline = br3.readLine();
        while(staticline!=null){
        	
        	String[]staticcontents = staticline.split(":");        	    
        	indexlabels.put(classlabels.get(Integer.parseInt(staticcontents[0])), Double.parseDouble(staticcontents[1]));
        	
        	staticline =br3.readLine();

        }
        br3.close();
        
		List<String>staticCount = new ArrayList<String>();
		for(int i=0;i<classlabels.size();i++){
			staticCount.add(classlabels.get(i)+","+indexlabels.get(classlabels.get(i)));
		}
        
        List<String> med_result = new ArrayList<String>();
        probability prob = new probability();
        med_result = prob.ig_process(staticCount, classlabels,r);
		
        //indexCount
		FileOutputStream fos = new FileOutputStream("./data/attack/NSL_KDD/model/5,50/temp/indexCount.txt"); 
		OutputStreamWriter osw = new OutputStreamWriter(fos);  
		BufferedWriter bw = new BufferedWriter(osw);
        for (int i=0;i<labels.length-1;i++){
        	String[]medresult=med_result.get(i).split(",");
        		bw.write(i+","+medresult[1]+"\n");      		      	
        }
		bw.close();
		
		//classCount
		FileOutputStream fos1 = new FileOutputStream("./data/attack/NSL_KDD/model/5,50/temp/classCount.txt"); 
		OutputStreamWriter osw1 = new OutputStreamWriter(fos1);  
		BufferedWriter bw1 = new BufferedWriter(osw1);
        for (int i=0;i<labels.length-1;i++){  
        	String[]medresult=med_result.get(i).split(",");
        	bw1.write(labels[i]+","+medresult[1]+"\n");
        }
		bw1.close();		
		//sliding windows
		FileOutputStream fos2 = new FileOutputStream("./data/attack/NSL_KDD/model/5,50/temp/Val_acc.txt"); 
		OutputStreamWriter osw2 = new OutputStreamWriter(fos2);  
		BufferedWriter bw2 = new BufferedWriter(osw2);
        for (int i=0;i<labels.length-1;i++){
        	bw2.write(labels[i]+","+indexlabels.get(labels[i])+"\n");
        }
		bw2.close();
		
		//iteration
		for (int i=0;i<iteration-1;i++){
	    List<Double> weight = new ArrayList<Double>();		

		String datapath = "./data/attack/NSL_KDD/model/5,50/OOB/inbag"+(i+1)+".arff";
		String weightpath = "./data/attack/NSL_KDD/model/5,50/weight/weight"+(i+1)+".txt";
		String savemodelpath = "./data/attack/NSL_KDD/model/5,50/w_models/Classifier"+(i+1)+".model";

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
		WeightModelDynamic wmd=new WeightModelDynamic();
		wmd.buildClassifier(savemodelpath,data,random.get(i+1), KValue);
		}
			
	}
}