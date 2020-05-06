package Preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.core.Instance;

public class frequency {
	public List frequency(List<List<String>>pass){
		List<String>result=new ArrayList<String>();


		for (int i=0;i<pass.get(0).size();i++){
			Map<String,Integer>check=new HashMap<String,Integer>();
			for (int j=0;j<pass.size();j++){
			if(check.containsKey(pass.get(j).get(i))){
				check.put(pass.get(j).get(i),check.get(pass.get(j).get(i))+1);
			}else{
				check.put(pass.get(j).get(i),1);
			}
			}
			ArrayList<Map.Entry<String,Integer>> list_Data = new ArrayList<Map.Entry<String, Integer>>(check.entrySet());  
		    
		    Collections.sort(list_Data, new Comparator<Map.Entry<String,Integer>>(){    
		      public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)  
		      {  
		        if ((o2.getValue() - o1.getValue())>0)  
		          return 1;  
		        else if((o2.getValue() - o1.getValue())==0)  
		          return 0;  
		        else   
		          return -1;  
		      }  
		        } 
		    );

			result.add(list_Data.get(0).getKey());
		    System.out.println(result.size());
		}
		    
			return result;
	}
	public void getAverage (String inpath, String outpath) throws Exception{
		List<List<String>>data=new ArrayList<List<String>>();
        BufferedReader br = new BufferedReader(new FileReader(new File(inpath))); 
        br.readLine();
        String header = br.readLine();
        double count=0.0;
        while (header!=null){
            List<String>median=new ArrayList<String>();
        	count+=1.0;
        	String[]datas=header.split(",");
        	for (int i=0;i<datas.length-1;i++){
        		median.add(datas[i]);
        	}
        	data.add(median);
        	header =br.readLine();
        }
        br.close();
        
        List<String>result=frequency(data);
		FileOutputStream fos = new FileOutputStream(outpath); 
		OutputStreamWriter osw = new OutputStreamWriter(fos);  
		BufferedWriter bw = new BufferedWriter(osw);
        for (int i=0;i<result.size();i++){
	    bw.write(result.get(i)+",");
		}
	    bw.close();
	
	}
	
	public static void main(String[] args) throws Exception {
		String outpath = "./data/attack/kddcup99/attack/output.txt";
		String inpath = "./data/attack/kddcup99/attack/kddcup993.data";

		frequency fre=new frequency();

		fre.getAverage(inpath, outpath);
			
	}
}