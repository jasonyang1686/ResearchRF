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

public class Partition {
	public void getPartition (String inpath, String outpath_1, String outpath_2) throws Exception{
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
        int number=(int) (data.size()/3);
        System.out.print(number);
		FileOutputStream fos = new FileOutputStream(outpath_1,true); 
		OutputStreamWriter osw = new OutputStreamWriter(fos);  
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(header+"\n");
        for (int i=0;i<number;i++){
	    bw.write(data.get(i)+"\n");
		}
	    bw.close();
	    
	    fos = new FileOutputStream(outpath_2,true);
	    osw =  new OutputStreamWriter(fos);
	    BufferedWriter bw1 = new BufferedWriter(osw);
		bw1.write(header+"\n");
        for (int i=number;i<data.size();i++){
	    bw1.write(data.get(i)+"\n");
		}
	    bw1.close();
	    
	    osw.close();
	    fos.close();
	
	}
	
	public static void main(String[] args) throws Exception {
		String outpath_1 = "./data/kyoto/training/201512_tendays_removal_binary_100_13.csv";
		String outpath_2 = "./data/kyoto/training/201512_tendays_removal_binary_100_23.csv";
		String inpath = "./data/kyoto/training/201512_tendays_removal_binary_100.csv";

		Partition p=new Partition();

		p.getPartition(inpath, outpath_1,outpath_2);
			
	}
}
