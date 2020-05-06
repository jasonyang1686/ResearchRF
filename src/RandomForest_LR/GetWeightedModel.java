package RandomForest_LR;
 
//step 4, get the weighted model from coefficients file
import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class GetWeightedModel {
	public void buildModel (String modelPath,String coefficientPath, String outputPath) throws Exception{
    double r=0.0;


	
//	bw.write(Double.parseDouble(line[i])/(Math.log(Math.E+r*Math.abs(cone.get(i))))+",");	
	List<String>classlabels=new ArrayList<String>();
    List<List<String>>result=new ArrayList<List<String>>();

	BufferedReader br1 = new BufferedReader(new FileReader(new File(modelPath)));
	String[]labels=br1.readLine().split(",");
	for(int i=0;i<labels.length;i++){
		classlabels.add(labels[i]);
	}
	
	String data = br1.readLine();
	while(data!=null){
List<String>medians=new ArrayList<String>();
	String[] output1=data.split(",");
	for (int i=0;i<output1.length;i++){
		medians.add(output1[i]);
	}
	result.add(medians);	
	data = br1.readLine();
	}
	br1.close();
	
	Map<String, Double> cone = new HashMap<String, Double>();
	for (int i = 0; i < classlabels.size(); i++) {
		cone.put(classlabels.get(i), 0.0);
	}
	System.out.println(cone.size());
    
	BufferedReader br = new BufferedReader(new FileReader(new File(coefficientPath)));
	String output=br.readLine();
	while(output!=null){
		String[]content=output.split(" ");
		cone.put(content[0],Math.abs(Double.parseDouble(content[content.length-1])));
	output=br.readLine();
	}
	br.close();
	
	
	
	FileOutputStream fos = new FileOutputStream(outputPath+"w_model.data"); 
 	OutputStreamWriter osw = new OutputStreamWriter(fos);  
	BufferedWriter bw = new BufferedWriter(osw); 
   

    for(int j=0;j<classlabels.size()-1;j++){
        bw.write(classlabels.get(j)+",");
    }
        bw.write(classlabels.get(classlabels.size()-1)+"\n");
    
    for (int m=0;m<result.size();m++){
   	 List<String>medians= result.get(m);
    	for (int n=0;n<medians.size()-1;n++){
    		bw.write(Double.parseDouble(medians.get(n))/(Math.log(Math.E+r*cone.get(classlabels.get(n))))+",");
    	}
    	bw.write(medians.get(medians.size()-1)+"\n");
    }
bw.close();
	
	}
	public static void main(String[] args) throws Exception {
		String modelPath="./data/spambase/training/spambase_model_normal.data";
		String coefficientPath = "./data/spambase/LR/coefficients.txt";
		String outputPath = "./data/spambase/LR/w_model/";
		GetWeightedModel model=new GetWeightedModel();
		model.buildModel(modelPath,coefficientPath,outputPath);
			
	}
}