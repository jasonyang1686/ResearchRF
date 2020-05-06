package Preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class avg {
	public void getAverage (String inpath, String outpath) throws Exception{
		List<Double>data=new ArrayList<Double>();
        BufferedReader br = new BufferedReader(new FileReader(new File(inpath))); 
        String[]numbers=br.readLine().split(",");
        for (int i=0;i<numbers.length-1;i++){
        	data.add(0.0);
        }
        String header = br.readLine();
        double count=0.0;
        while (header!=null){
        	count+=1.0;
        	String[]datas=header.split(",");
        	for (int i=0;i<datas.length-1;i++){
        		data.set(i, data.get(i)+Double.parseDouble(datas[i]));
        	}
        	header =br.readLine();
        }
        br.close();
        
		FileOutputStream fos = new FileOutputStream(outpath); 
		OutputStreamWriter osw = new OutputStreamWriter(fos);  
		BufferedWriter bw = new BufferedWriter(osw);
        for (int i=0;i<data.size();i++){
	    bw.write(data.get(i)/count+",");
		}
	    bw.close();
	
	}
	
	public static void main(String[] args) throws Exception {
		String outpath = "./data/attack/spambase/attack/output_C451.txt";
		String inpath = "./data/attack/spambase/attack/output_C45.txt";

		avg a=new avg();

		a.getAverage(inpath, outpath);
			
	}
}