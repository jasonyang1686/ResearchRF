package RandomForest_KDD;

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

public class average {
	public void TrainData (String outpath, String inpath, int iteration) throws Exception{
		List<String>originaldata=new ArrayList<String>();
		Map<Double, Integer> count = new HashMap<Double, Integer>();
        BufferedReader br = new BufferedReader(new FileReader(new File("./data/attack/credit/attack/credit_attack_all.data"))); 
        br.readLine();
        String header = br.readLine();
        while (header!=null){
        	if (header.contains("Yes")){
        	String[]contents=header.split(",");
        	originaldata.add(contents[23]);
        	/*
        	if (count.containsKey(Double.parseDouble(contents[10]))){
        		count.put(Double.parseDouble(contents[10]), count.get(Double.parseDouble(contents[10]))+1);
        	}else{
        		count.put(Double.parseDouble(contents[10]), 1);
        	}
        	*/
        	}
        	header =br.readLine();
        }
        br.close();
        
  double total=0.0;      
for (int i=0;i<originaldata.size();i++){
	total+=Double.parseDouble(originaldata.get(i));
}

ArrayList<Map.Entry<Double,Integer>> list_Data = new ArrayList<Map.Entry<Double, Integer>>(count.entrySet());  
  
  Collections.sort(list_Data, new Comparator<Map.Entry<Double,Integer>>(){    
    public int compare(Map.Entry<Double, Integer> o1, Map.Entry<Double, Integer> o2)  
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
List<Integer>result = new ArrayList<Integer>();
  for (int i=0;i<list_Data.size();i++){
  	
	//    System.out.println(list_Data.get(i).getKey()+":"+list_Data.get(i).getValue());
  }   
        

        System.out.println(total/originaldata.size());

	}
	
	public static void main(String[] args) throws Exception {
		String outpath = "./data/outbag.txt";
		String inpath = "./data/inbag.txt";
		int iteration=100;
		average oob=new average();

		oob.TrainData(outpath, inpath, iteration);
			
	}
}