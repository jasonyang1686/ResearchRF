package RandomForest_spambase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;


public class utility {
    double[] normailize(List<Double>data)
    {
    	 double[] result = new double[2];	
    	 double[]array = new double[data.size()];
        for (int i=0;i<array.length;i++){
        	array[i]=data.get(i);
        }
        Arrays.sort(array);
        result[0]=array[0];
        result[1]=array[array.length-1];
        return result;
    }
	public static void main(String[] args) throws Exception {
		
	List<String>classlabels = new ArrayList<String>();
	Map<String, Double> getResult = new HashMap<String, Double>();	
	BufferedReader br2 = new BufferedReader(new FileReader(new File("./data/attack/spambase/model/spambase1.data"))); 
    String[] labels = br2.readLine().split(",");
    br2.close();
     
     for (int i=0;i<labels.length-1;i++){
     	classlabels.add(labels[i]); 
     }
     System.out.println(classlabels.size());
	 String path = "./data/attack/spambase/model/5,50/models/";
		List<Integer> pathlist = new ArrayList<Integer>();
		for (int i=0;i<100;i++){
			pathlist.add(i);
		}	

	        List<String>med_result=new ArrayList<String>();	
			LeafDistribution ln=new LeafDistribution();
			med_result=ln.process(path, pathlist,classlabels);
//			System.out.println(med_result);
			for (int j=0;j<med_result.size();j++){
				String[]contents=med_result.get(j).split("=");				
				getResult.put(contents[0], Double.parseDouble(contents[1]));				
			}

	    List<String>result = new ArrayList<String>();	
		classDistribution cd=new classDistribution();
		result=cd.process(path,pathlist, classlabels);
		for (int i=0;i<result.size();i++){
			String[]contents=result.get(i).split("=");
			/*
			if (getResult.containsKey(contents[0])){
				if(getResult.get(contents[0])==0.0){
				getResult.put(contents[0], (Double.parseDouble(contents[1])));
				}
				getResult.put(contents[0], (Double.parseDouble(contents[1]))/getResult.get(contents[0]));
			}
			*/
			if (getResult.containsKey(contents[0])){
				if(Double.parseDouble(contents[1])==0.0){
				getResult.put(contents[0], getResult.get(contents[0]));
				}
				getResult.put(contents[0], getResult.get(contents[0])/(Double.parseDouble(contents[1])));
			}
		}
		List<Double>data = new ArrayList<Double>();
		
		for (Map.Entry<String, Double> entry : getResult.entrySet()) {
		System.out.println(entry.getKey()+"="+entry.getValue());
			data.add(entry.getValue());
		}
		
	 utility util = new utility();

	 double[] finalresult=util.normailize(data);
	 System.out.println(finalresult[0]+","+finalresult[1]);
		for (Map.Entry<String, Double> entry : getResult.entrySet()) {
			System.out.println(entry.getKey()+"="+(entry.getValue()-finalresult[0])/(finalresult[1]-finalresult[0]));

		}
		
	 /*
	 List<String>staticCount= new ArrayList<String>();
	    BufferedReader br3 = new BufferedReader(new FileReader(new File("./data/attack/spambase/model/5,50/temp/static_weight.txt"))); 
	    String staticline = br3.readLine();
	    while(staticline!=null){
	    	
	    	String[]staticcontents = staticline.split("=");
	    	staticCount.add(staticcontents[0]);
	    	if (Double.parseDouble(staticcontents[1])>20.0){
	        	data.add(2.0/Double.parseDouble(staticcontents[1]));
	    	}else
	    	data.add(1.0/Double.parseDouble(staticcontents[1]));
	    	
	    	staticline =br3.readLine();
	    	
	    }
	    br3.close(); 
	    
	    double[] finalresult=util.normailize(data);
		 System.out.println(finalresult[0]+","+finalresult[1]);
			for (int i=0;i<staticCount.size();i++) {
				System.out.println(staticCount.get(i)+"="+data.get(i));

			}
			*/
}
}