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


public class randomize {
	public static void process(String yes_path,String no_path) throws IOException{
		ArrayList<String>final_contents=new ArrayList<String>();
		ArrayList<String>contents=new ArrayList<String>();
	    BufferedReader br = new BufferedReader(new FileReader(new File(yes_path))); 
	    br.readLine();
	    String data = br.readLine(); 
	    while (data != null) {
		    contents.add(data);
	    	data=br.readLine();	    
	}
	    br.close();
	    /*
	    BufferedReader br1 = new BufferedReader(new FileReader(new File(no_path))); 
	    br1.readLine(); 
	    int number=0;
	    String data1 = br1.readLine(); 
	    while (data1 != null) {
	    	String[]labels=data1.split(",");
	    	if (labels[labels.length-1].equals("No") && number<=300)
		    contents.add(data1);
	    	data1=br1.readLine();	
	    	number++;
	}
	    br1.close();
	    System.out.println(contents.size());
   Integer[] arr = new Integer[contents.size()-1];
	    
	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
    }
	    System.out.println(arr.length);
	    Collections.shuffle(Arrays.asList(arr));
	    
	    final_contents.add(contents.get(0));
	    for(int i=1;i<arr.length;i++){
    	final_contents.add(contents.get(arr[i]));
    }
		FileOutputStream fos = new FileOutputStream("./data/attack/kddcup99/attack/kddcup99_all.data"); 
		OutputStreamWriter osw = new OutputStreamWriter(fos);  
		BufferedWriter bw = new BufferedWriter(osw);


	    for(int i=0;i<final_contents.size();i++){
			bw.write(final_contents.get(i)+"\n");
	    }
		bw.close();      
        */
	}
public static void main(String[] args) throws Exception {
		String yes_path = "./data/attack/kddcup99/attack/kddcup99_attack.data";
		String no_path = "./data/attack/kddcup99/model/kddcup99_model.data";
		randomize ra=new randomize();
		ra.process(yes_path,no_path);
			
	}
}