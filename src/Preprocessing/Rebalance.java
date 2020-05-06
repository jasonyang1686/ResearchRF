
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


public class Rebalance {


	public static void main(String[] args) throws IOException {

		List<String>negative=new ArrayList<String>();
		List<String>positive=new ArrayList<String>();
		List<String>all=new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/kyoto/des/201501_twodays_test_removal_binary.csv")));
		String header=br.readLine();
		String contents = br.readLine();
		while(contents!=null){
			String[]content=contents.split(",");
			if (content[content.length-1].equals("Normal")) {
			positive.add(contents);
			all.add(contents);
			}
			else {
			negative.add(contents);
			all.add(contents);
			}
			contents=br.readLine();
		}
		br.close();
   
	   int count=positive.size();
	   System.out.println(count);
		FileOutputStream fos1 = new FileOutputStream("./data/kyoto/des/201501_twodays_test_removal_binary_balance.csv"); 
		OutputStreamWriter osw1 = new OutputStreamWriter(fos1);  
		BufferedWriter bw1 = new BufferedWriter(osw1);
		bw1.write(header+"\n");
		int i=0;
		int p=0;
		while(count>=0){	
	    	bw1.write(all.get(i)+"\n");
	    	String[]label=all.get(i).split(",");
	    if(label[label.length-1].equals("Attack")) {
	    	count--;
	    }else {
	    	p++;
	    }
	    i++;
		}
		for (int j=p-1;j<positive.size();j++) {
			bw1.write(positive.get(j)+"\n");
		}
		bw1.close();


	}
}