package RandomForest_Credit;

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
	public void process(String path) throws IOException{
		ArrayList<String>lines=new ArrayList<String>();	
		ArrayList<String>final_contents=new ArrayList<String>();
	    BufferedReader br = new BufferedReader(new FileReader(new File(path))); 
	    int count=0;
	    lines.add(br.readLine()+"\n");
	    String data = br.readLine(); 
	    while (data != null) {
  	String[]contents=data.split(",");
  	if (contents[1].equals("1")){
  		contents[1]="male";
  	}else if (contents[1].equals("2")){
  		contents[1]="female";
  	}else{
  		System.out.println(count);
  	}
  	if (contents[2].equals("1")){
  		contents[2]="graduate_school";
  	}else if (contents[2].equals("2")){
  		contents[2]="university";
  	}else if (contents[2].equals("3")){
  		contents[2]="high_school";
  	}else if (contents[2].equals("4")){
  		contents[2]="others";
  	}else{
  		System.out.println(count);
  	}
  	if (contents[3].equals("1")){
  		contents[3]="married";
  	}else if (contents[3].equals("2")){
  		contents[3]="single";
  	}else if (contents[3].equals("3")){
  		contents[3]="others";
  	}else{
  		System.out.println(count);
  	}
  	for (int i=0;i<contents.length-1;i++){
  		lines.add(contents[i]+",");
  	}
  	lines.add(contents[contents.length-1]+"\n");
             	count++;
	    	data=br.readLine();	    
	}
	//    System.out.println(contents.size());
	    br.close();	 
	    
		    
		FileOutputStream fos = new FileOutputStream("./data/attack/credit/attack/credit_attack_all_1.data"); 
		OutputStreamWriter osw = new OutputStreamWriter(fos);  
		BufferedWriter bw = new BufferedWriter(osw);


	    for(int i=0;i<lines.size();i++){
			bw.write(lines.get(i));
	    }
		bw.close();   
		
		
        
	}
public static void main(String[] args) throws Exception {
		String path = "./data/attack/credit/attack/credit_attack_all.data";

		preprocessing pre=new preprocessing();
		pre.process(path);
			
	}
}