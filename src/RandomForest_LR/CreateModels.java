package RandomForest_LR;
 

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
import java.util.List;

//first step, creat models data from dataset, each sub data contains log n features
public class CreateModels {
	public void create (String inputPath) throws Exception{
		int loop=100;
		 List<String>classlabels=new ArrayList<String>();
	     List<List<String>>result=new ArrayList<List<String>>();
    
		BufferedReader br = new BufferedReader(new FileReader(new File(
				inputPath)));
		String[]labels=br.readLine().split(",");
		for(int i=0;i<labels.length;i++){
			classlabels.add(labels[i]);
		}
		
		String data = br.readLine();
		
		while(data!=null){
	 List<String>medians=new ArrayList<String>();
		String[] output=data.split(",");
		for (int i=0;i<output.length;i++){
			medians.add(output[i]);
		}
		result.add(medians);	
		data = br.readLine();
		}
		br.close();
		
	    int numAtt = classlabels.size()-1;
	    int select = (int)(Math.log(numAtt)+1);
	    
	    Integer[] arr = new Integer[numAtt];   
	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
	}
	    
      for(int i=0;i<loop;i++){
    	FileOutputStream fos = new FileOutputStream("./data/kyoto/LR/models/model"+i+".data"); 
     	OutputStreamWriter osw = new OutputStreamWriter(fos);  
    	BufferedWriter bw = new BufferedWriter(osw); 
	   
    	Collections.shuffle(Arrays.asList(arr));

	    for(int j=0;j<select;j++){
	        bw.write(classlabels.get(arr[j])+",");
	    }
            bw.write(classlabels.get(classlabels.size()-1)+"\n");
        
        for (int m=0;m<result.size();m++){
       	 List<String>medians= result.get(m);
        	for (int n=0;n<select;n++){
        		bw.write(medians.get(arr[n])+",");
        	}
        	bw.write(medians.get(medians.size()-1)+"\n");
        }
	bw.close();
      }
	}
	public static void main(String[] args) throws Exception {
		String inputPath = "./data/kyoto/training/spambase_model_normal.data";
	//	String path = "./data/test.txt";
		CreateModels cm=new CreateModels();
		cm.create(inputPath);
			
	}
}