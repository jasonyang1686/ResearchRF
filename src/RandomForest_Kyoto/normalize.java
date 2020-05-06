package RandomForest_Kyoto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class normalize {
	 static void createData(String path,String normalizedPath) throws Exception {
			List<List<Double>>entities=new ArrayList<List<Double>>();
			List<String>classLabels=new ArrayList<String>();
	        BufferedReader br = new BufferedReader(new FileReader(new File(path))); 
	        String header=br.readLine();
	        String line = br.readLine();
	        while (line!=null){
	         String[]contents=line.split(",");	
             List<Double>oneLine=new ArrayList<Double>();
             for(int i=0;i<contents.length-1;i++){
            	oneLine.add(Double.parseDouble(contents[i])); 
             }
             entities.add(oneLine);
             classLabels.add(contents[contents.length-1]);
             line = br.readLine();
	        }
	        br.close();
	        
	        for(int i=0;i<entities.get(0).size();i++){
        		List<Double>sorted = new ArrayList<Double>();
	        	for (int j=0;j<entities.size();j++){
                    sorted.add(entities.get(j).get(i));
	        	}
	        	Collections.sort(sorted);
	        	Double min=sorted.get(0);
	        	Double max= sorted.get(sorted.size()-1);
	        	for (int j=0;j<entities.size();j++){
	        		entities.get(j).set(i,(entities.get(j).get(i)-min)/(max-min));
	        	}
	        }

        		FileOutputStream fos = new FileOutputStream(normalizedPath); 
        		OutputStreamWriter osw = new OutputStreamWriter(fos);  
        		BufferedWriter bw = new BufferedWriter(osw);
        		bw.write(header+"\n");
    	        for(int i=0;i<entities.size();i++){
        		for (int j=0;j<entities.get(0).size();j++){
        	    bw.write(entities.get(i).get(j)+",");
        		}
        		bw.write(classLabels.get(i)+"\n");
    	        }
        		bw.close();
    		


	  }
		public static void main(String[] args) throws Exception {
			String path="./data/attack/spambase/attack/spambase2.data";
			String normalizedPath="./data/attack/spambase/attack/spambase_attack_normal.data"; 
			createData(path,normalizedPath);
		}
}