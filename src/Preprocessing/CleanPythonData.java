package Preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class CleanPythonData {
	public static void main(String[] args) throws IOException {
		List<String>all=new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/spambase/testing/spambase_attack_normal.data")));
		String header=br.readLine();
		String contents = br.readLine();
		while(contents!=null){
			StringBuilder sbf = new 
                    StringBuilder(""); 
			String[]content=contents.split(",");
	        for (int x=0;x < content.length-3; x++) {
	        	sbf.append(content[x]+",");
	        }
	        sbf.append(content[content.length-3]);
	        all.add(sbf+"\n");
			contents=br.readLine();
		}
		br.close();
		FileOutputStream fos1 = new FileOutputStream("./data/spambase/testing/spambase_attack_normal.txt"); 
		OutputStreamWriter osw1 = new OutputStreamWriter(fos1);  
		BufferedWriter bw1 = new BufferedWriter(osw1);


	   for (int x=0;x<all.size();x++) {
			bw1.write(all.get(x));   
	   }
		bw1.close();
		osw1.close();
		fos1.close();
	}
}
