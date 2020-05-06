package Preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class AddHead {
	public static void main(String[] args) throws Exception {
		String head="./data/kyoto/header.txt";
		String sourceDataPath="./data/kyoto/source/Kyoto2016/2015/01/";
		String destinationDataPath="./data/kyoto/des/201501_twodays_test.txt";
		ArrayList<String>contents=new ArrayList<String>();
		File dataset=new File(sourceDataPath);
		ArrayList<String> fileList = new ArrayList<String>(Arrays.asList(dataset.list()));
		BufferedReader brw = new BufferedReader(new FileReader(new File(head)));
		String labels=brw.readLine();
		contents.add(labels);
		brw.close();
		
		for (int i=0;i<fileList.size();i++) {
	        brw = new BufferedReader(new FileReader(new File(sourceDataPath+fileList.get(i)))); 	
	        String line=brw.readLine();
			while(line!=null){
				String newLine=line.replaceAll("	", ",");
				contents.add(newLine);
				line=brw.readLine();
			}
			brw.close();
		}
		
		FileOutputStream fos = new FileOutputStream(destinationDataPath); 
		OutputStreamWriter osw = new OutputStreamWriter(fos);  
		BufferedWriter bw = new BufferedWriter(osw);
        for (int i=0;i<contents.size();i++){
        		bw.write(contents.get(i)+"\n");      		      	
        }
		bw.close();
		
	//    System.out.println(line);
	}
}
