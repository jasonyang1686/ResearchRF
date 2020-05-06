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

public class Sampling {
	public void getSample (double percent,String inpath, String outpath) throws Exception{
		List<String>data=new ArrayList<String>();
		
        BufferedReader br = new BufferedReader(new FileReader(new File(inpath))); 
        String header = br.readLine();
        
        String content = br.readLine();
        
        while (content != null) { 
            data.add(content);
            content=br.readLine(); 
       }
        br.close();
        Integer[] arr = new Integer[data.size()];
	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
	    }
	    Collections.shuffle(Arrays.asList(arr));
	    
        double count=0.0;
        int number=(int) (data.size()*percent/1);
        System.out.print(number);
		FileOutputStream fos = new FileOutputStream(outpath,true); 
		OutputStreamWriter osw = new OutputStreamWriter(fos);  
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(header+"\n");
        for (int i=0;i<number;i++){
	    bw.write(data.get(arr[i])+"\n");
		}
	    bw.close();
	
	}
	
	public static void main(String[] args) throws Exception {
		double percent = 0.1;
		String outpath = "./data/kyoto/des/201512_tendays_removal_binary_balance_sampling.csv";
		String inpath = "./data/kyoto/des/201512_tendays_removal_binary_balance.csv";

		Sampling s=new Sampling();

		s.getSample(percent,inpath, outpath);
			
	}
}