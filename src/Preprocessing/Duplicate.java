package Preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Duplicate {
	public void getData (double percent,String inpath, String outpath) throws Exception{
		List<String>data=new ArrayList<String>();
		
        BufferedReader br = new BufferedReader(new FileReader(new File(inpath))); 
        String header = br.readLine();
        
        String content = br.readLine();
        
        while (content != null) { 
            data.add(content);
            content=br.readLine(); 
       }
        br.close();

	    
        double count=0.0;
        int number=data.size();
        System.out.print(number);
		FileOutputStream fos = new FileOutputStream(outpath); 
		OutputStreamWriter osw = new OutputStreamWriter(fos);  
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(header+"\n");
		for(int j=0;j<percent;j++) {
        for (int i=0;i<(number/1);i++){        	
	    bw.write(data.get(i)+"\n");
		}
		}
	    bw.close();
	
	}
	
	public static void main(String[] args) throws Exception {
		double percent = 5;
		String outpath = "./data/kyoto/training/201512_tendays_removal_binary_100.csv";
		String inpath = "./data/kyoto/training/201512_tendays_removal_binary_20.csv";

		 Duplicate d=new Duplicate();

		d.getData(percent,inpath, outpath);
			
	}
}
