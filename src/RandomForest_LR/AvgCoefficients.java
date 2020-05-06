package RandomForest_LR;
 
//step3 Calculate avg coefficients of whole data 
import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;

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

public class AvgCoefficients {
	public void process (String classPath, String inputPath) throws Exception{
		int loop=100;
		 List<String>classlabels=new ArrayList<String>();
	     List<List<String>>result=new ArrayList<List<String>>();
    
		BufferedReader br = new BufferedReader(new FileReader(new File(
				classPath)));
		String[]labels=br.readLine().split(",");
		for(int i=0;i<labels.length;i++){
			classlabels.add(labels[i]);
		}
		br.close();
		
		Map<String, Double> finalappear = new HashMap<String, Double>();
		for (int i = 0; i < classlabels.size(); i++) {
			finalappear.put(classlabels.get(i), 0.0);
		}
		
	    
      for(int i=0;i<loop;i++){
  		BufferedReader br1 = new BufferedReader(new FileReader(new File(inputPath+i+".txt"))); 
        String data = br1.readLine();
        while(data!=null){
    		String[] output=data.split(" ");
    		String name = output[0];
    		Double value = Double.parseDouble(output[output.length-1]);
    		finalappear.put(name, finalappear.get(name)+Math.abs(value));
    		data = br1.readLine();
        }
        br1.close();
      }
      
		for (Map.Entry<String, Double> entry : finalappear.entrySet()) {
			System.out.println(entry.getKey() +"   "+ entry.getValue()/100.0);
		}
	}
	public static void main(String[] args) throws Exception {
		String classPath = "./data/spambase/training/spambase_model_normal.data";
        String inputPath="./data/spambase/LR/coefficients/";
		AvgCoefficients ac=new AvgCoefficients();
		ac.process(classPath,inputPath);
			
	}
}