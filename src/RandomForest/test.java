package RandomForest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;

public class test {
	
public static void calculator(String path) throws Exception{
    BufferedReader br = new BufferedReader(new FileReader(new File(path))); 
    String data = br.readLine();  
    /*
	FileOutputStream fos = new FileOutputStream("./data/kddcup99/kddcup99p.data"); 
	OutputStreamWriter osw = new OutputStreamWriter(fos);  
	BufferedWriter bw = new BufferedWriter(osw); 
	*/
	Classifier cls = (Classifier) weka.core.SerializationHelper.read(path);	
	/*
    while (data != null) { 
    	
    	String[]datas=data.split(",");
    	if (datas[0].equals(0+"")){
    		count++;
    	}


    	
    	if (datas[2].equals("tftp_u")|| datas[2].equals("http_443")||datas[2].equals("netstat")||datas[2].equals("printer")||datas[2].equals("sql_net")||datas[2].equals("bgp")||datas[2].equals("whois")||datas[2].equals("csnet_ns")||datas[2].equals("Z39_50")||datas[2].equals("uucp_path")||datas[2].equals("domain")||datas[2].equals("rje")||datas[2].equals("name")||datas[2].equals("sunrpc")||datas[2].equals("pop_2")||datas[2].equals("gopher")||datas[2].equals("pop_3")||datas[2].equals("IRC")||datas[2].equals("X11")||datas[2].equals("pm_dump")||datas[2].equals("red_i")||datas[2].equals("tim_i")){
    		
    	}
    	else{
    		bw.append(data+"\n");
    	}
    	
        data = br.readLine();    
         
   }
 //   bw.close();
*/
    System.out.println(cls.toString());
}

public static void main(String[] args) throws Exception { 

String path = "./data/attack/spambase/model/5,50/models/Classifier10.model";
//System.out.println(Math.pow(Math.E, 25));

calculator(path);


}// main()
}
// class Fw