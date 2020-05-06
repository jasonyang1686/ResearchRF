package RandomForest_spambase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class WeightConvert {
	 static void buildClassifier() throws Exception {
			List<String>weight=new ArrayList<String>();
			int i=0;
	        BufferedReader br = new BufferedReader(new FileReader(new File("./data/weight.txt"))); 
	        String line = br.readLine();
	        while (line!=null){
	        	if (line.contains("break")){
	        		FileOutputStream fos = new FileOutputStream("./data/weight/weight"+i+".txt"); 
	        		OutputStreamWriter osw = new OutputStreamWriter(fos);  
	        		BufferedWriter bw = new BufferedWriter(osw);
	        		for (int j=0;j<weight.size();j++){
	        	    bw.write(weight.get(j)+"\n");
	        		}
	        		i++;
	        		bw.close();
	    			weight=new ArrayList<String>();
	        	}else{
	        		weight.add(line);
	        	}
	        	line = br.readLine();
	        }
 br.close();
	  }
		public static void main(String[] args) throws Exception {
			final long start = System.currentTimeMillis();
			buildClassifier();
			final long end = System.currentTimeMillis(); 
			
			System.out.println("time: "+(end-start));
		}
}