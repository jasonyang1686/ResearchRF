package RandomForest_IG;

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

import org.apache.commons.lang3.StringUtils;

//step 1, seperate each Val file. started from Val1 to Val100

public class SeperateVal {
	public void process(String path) throws IOException{

		ArrayList<String>medians=new ArrayList<String>();

	    BufferedReader br = new BufferedReader(new FileReader(new File(path))); 
	    int count=-1;
//	    contents.add(br.readLine());
	    String data = br.readLine(); 
	    while (data != null) {
  	           if(data.contains("break")){
  	        	   count++;
  	        	 FileOutputStream fos = new FileOutputStream("./data/spambase/IG/Vals/Val"+count+".txt"); 
	        		OutputStreamWriter osw = new OutputStreamWriter(fos);  
	        		BufferedWriter bw = new BufferedWriter(osw);
	        		for (int i=0;i<medians.size();i++){
	        			bw.write(medians.get(i)+"\n");
	        		}
	        		bw.close();
  	        	 medians=new ArrayList<String>();
  	           }else{
  	        	   medians.add(data);
  	           } 	
	    	data=br.readLine();	    
	}
	    System.out.println(count);
	    br.close();	 

	}
public static void main(String[] args) throws Exception {
		String path = "./data/spambase/IG/Val.txt";

		SeperateVal sv=new SeperateVal();
		sv.process(path);
			
	}
}