package RandomForest_KDD;

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
	 BufferedReader br2 = new BufferedReader(new FileReader(new File("./data/attack/kddcup99_B/attack/kddcup99_non_duplicate_10_percent_model_B.data"))); 
     String data = br2.readLine();

     while (data!=null){
    	 lines.add(data);
         if (data.contains("Yes")){
        	no_lines.add(data);
         }
   //      lines.add(data);
    	 data = br2.readLine();

     }
     br2.close();
     System.out.println(no_lines.size());     
     
	    Integer[] arr = new Integer[no_lines.size()];
	    
	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
 }
	    Collections.shuffle(Arrays.asList(arr));
	    
 



     FileOutputStream fos1 = new FileOutputStream("./data/attack/kddcup99_B/attack/kddcup99_non_duplicate_10_percent_attack_B.data"); 
		OutputStreamWriter osw1 = new OutputStreamWriter(fos1);  
		BufferedWriter bw1 = new BufferedWriter(osw1);
        for (int i=0;i<arr.length*3/10;i++){
        lines.remove(no_lines.get(arr[i]));	
     	bw1.write(no_lines.get(arr[i])+"\n");
        }
		bw1.close();	
		
	     FileOutputStream fos2 = new FileOutputStream("./data/attack/kddcup99_B/attack/kddcup99_non_duplicate_10_percent_all_B.data"); 
			OutputStreamWriter osw2 = new OutputStreamWriter(fos2);  
			BufferedWriter bw2 = new BufferedWriter(osw2);
	        for (int i=0;i<lines.size();i++){

	     	bw2.write(lines.get(i)+"\n");
	        }
			bw2.close();	    
		System.out.println(lines.size()); 

	}
}