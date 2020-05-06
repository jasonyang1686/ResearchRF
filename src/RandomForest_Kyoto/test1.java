package RandomForest_Kyoto;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;

public class test1 {
	
public static void calculator(String path) throws Exception{

  List<Double>accu= new ArrayList<Double>();
  List<Integer>tries= new ArrayList<Integer>();
  BufferedReader br = new BufferedReader(new FileReader(new File(path))); 
   
int count=0;

    String data = br.readLine();
    
    while (data != null) { 
        if (data.endsWith("break")){
 //   if (data.endsWith(":1.0")||data.endsWith(":0.0")){
    	count++;
    }
        data=br.readLine(); 
   }

    br.close();
System.out.println(count);
}

public static void main(String[] args) throws Exception { 

String path = "./data/val.txt";
//System.out.println(Math.pow(Math.E, 25));

calculator(path);


}// main()
}
// class Fw