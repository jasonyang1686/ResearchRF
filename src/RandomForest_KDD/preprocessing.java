package RandomForest_KDD;

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
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


public class preprocessing {
	public void process(String yes_path) throws IOException{
		ArrayList<String>final_contents=new ArrayList<String>();

	    BufferedReader br = new BufferedReader(new FileReader(new File(yes_path))); 
	    int count=0;
	    br.readLine();
//	    contents.add(br.readLine());
	    String data = br.readLine(); 
	    while (data != null) {
  	
               final_contents.add(data);    	
	    	data=br.readLine();	    
	}
	//    System.out.println(contents.size());
	    br.close();	 
	    
	    Integer[] arr_1 = new Integer[final_contents.size()];
	    for (int i = 0; i < arr_1.length; i++) {
	        arr_1[i] = i;
    }
	    Collections.shuffle(Arrays.asList(arr_1));
		
	    
		FileOutputStream fos = new FileOutputStream("./data/attack/kddcup99_B/model/kddcup99_non_duplicate_10_percent_model_B.data"); 
		OutputStreamWriter osw = new OutputStreamWriter(fos);  
		BufferedWriter bw = new BufferedWriter(osw);


	    for(int i=0;i<arr_1.length*7/10;i++){
			bw.write(final_contents.get(arr_1[i])+"\n");
	    }
		bw.close();   
		
		FileOutputStream fos1 = new FileOutputStream("./data/attack/kddcup99_B/attack/kddcup99_non_duplicate_10_percent_model_B.data"); 
		OutputStreamWriter osw1 = new OutputStreamWriter(fos1);  
		BufferedWriter bw1 = new BufferedWriter(osw1);


	    for(int i=arr_1.length*7/10;i<arr_1.length;i++){
			bw1.write(final_contents.get(arr_1[i])+"\n");
	    }
		bw1.close(); 		
        
	}
public static void main(String[] args) throws Exception {
		String yes_path = "./data/attack/kddcup99_B/model/kddcup99_non_duplicate_10_percent_B.data";

		preprocessing pre=new preprocessing();
		pre.process(yes_path);
			
	}
}