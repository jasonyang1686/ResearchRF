package RandomForest_Credit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;


public class preprocessing2 {
	
public static void main(String[] args) throws Exception {

	List<String>no_lines=new ArrayList<String>();
	List<String>lines=new ArrayList<String>();
	Map<Double, Integer> count_ = new HashMap<Double, Integer>();	
	
	 BufferedReader br2 = new BufferedReader(new FileReader(new File("./data/attack/credit/attack/credit_attack_all_1.data"))); 
	 br2.readLine();
     String data = br2.readLine();

     while (data!=null){
    	 String[] contents=data.split(",");
    //	 lines.add(data);
         if (contents[2].equals("graduate_school") || contents[2].equals("high_school") ||contents[2].equals("university")||contents[2].equals("others")){
        	lines.add(data);
         }
   //      lines.add(data);
    	 data = br2.readLine();

     }
     br2.close();

     FileOutputStream fos1 = new FileOutputStream("./data/attack/credit/attack/credit_attack_all_2.data"); 
		OutputStreamWriter osw1 = new OutputStreamWriter(fos1);  
		BufferedWriter bw1 = new BufferedWriter(osw1);
        for (int i=0;i<lines.size();i++){
     	bw1.write(lines.get(i)+"\n");
        }
		bw1.close();	
		


	}
}