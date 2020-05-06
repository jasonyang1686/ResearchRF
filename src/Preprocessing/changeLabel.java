
package Preprocessing;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class changeLabel {

	public void change (String inpath, String outpath) throws Exception{
        List<List<String>>data=new ArrayList<List<String>>();
        BufferedReader br = new BufferedReader(new FileReader(new File(inpath))); 
        br.readLine();

        String header = br.readLine();
        double count=0.0;
        while (header!=null){
            List<String>median=new ArrayList<String>();
        	String[]datas=header.split(",");
        	for (int i=0;i<datas.length-1;i++)
        		median.add(datas[i]+",");
        	if(datas[datas.length-1].equals("1")){
        		median.add("Normal");
        	}else{
        		median.add("Attack");
        	}
            data.add(median);
        	header =br.readLine();
        }
        br.close();
        
		FileOutputStream fos = new FileOutputStream(outpath); 
		OutputStreamWriter osw = new OutputStreamWriter(fos);  
		BufferedWriter bw = new BufferedWriter(osw);
        for (int i=0;i<data.size();i++){
        	for (int j=0;j<data.get(i).size();j++){
	    bw.write(data.get(i).get(j));
		}
    	    bw.write("\n");
        }
	    bw.close();
	
	}
	
	public static void main(String[] args) throws Exception {
		String outpath = "./data/kyoto/des/201501_twodays_test_removal_binary.csv";
		String inpath = "./data/kyoto/des/201501_twodays_test_removal.txt";

		changeLabel cl=new changeLabel();

		cl.change(inpath, outpath);
			
	}
}
