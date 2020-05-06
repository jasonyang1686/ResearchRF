package RandomForest_LR;

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


public class test2 {
	public void process(String yes_path) throws IOException{


	    BufferedReader br = new BufferedReader(new FileReader(new File(yes_path))); 
	    int count=0;
//	    br.readLine();
//	    contents.add(br.readLine());
	    String data = br.readLine(); 
	    int yes_number=0;
	    int no=0;
	    while (data != null) {
  	         String[]line=data.split(",");
  	         if(line[line.length-1].equals("Yes")){
  	        	 yes_number++;
  	         }else{
  	        	 no++;
  	         }
  	         

	    	data=br.readLine();	    
	}
	//    System.out.println(contents.size());
	    br.close();	 
	    
		
	    System.out.println(yes_number);

	    System.out.println(no);

  
		
	
        
	}
public static void main(String[] args) throws Exception {
		String yes_path = "./data/attack/kddcup99_B/model/kddcup99_non_duplicate_10_percent.data";

		test2 pre=new test2();
		pre.process(yes_path);
			
	}
}