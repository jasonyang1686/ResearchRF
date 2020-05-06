
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


public class shuffle {


	public static void main(String[] args) throws IOException {
		int number =1000;
		List<String>data=new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/statlog/statlogp.data")));
		String header=br.readLine();
		String contents = br.readLine();
		while(contents!=null){
			data.add(contents);
			contents=br.readLine();
		}
		br.close();
	    Integer[] arr = new Integer[number];
	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
	    }
	    Collections.shuffle(Arrays.asList(arr));
		FileOutputStream fos1 = new FileOutputStream("./data/attack/statlog/model/statlog.data"); 
		OutputStreamWriter osw1 = new OutputStreamWriter(fos1);  
		BufferedWriter bw1 = new BufferedWriter(osw1);
		bw1.write(header+"\n");
		for (int j=0;j<number*8/10;j++){
	    bw1.write(data.get(arr[j])+"\n");
		}
		bw1.close();
		FileOutputStream fos2 = new FileOutputStream("./data/attack/statlog/attack/statlog.data"); 
		OutputStreamWriter osw2 = new OutputStreamWriter(fos2);  
		BufferedWriter bw2 = new BufferedWriter(osw2);
		bw2.write(header+"\n");
		for (int j=number*8/10;j<number;j++){
	    bw2.write(data.get(arr[j])+"\n");
		}
		bw2.close();

	}
}
