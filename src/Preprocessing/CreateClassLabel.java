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

public class CreateClassLabel {
	public static void main(String[] args) throws IOException {
		List<String>all=new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/spambase/testing/spambase_attack_normal.data")));
		String header=br.readLine();
		String contents = br.readLine();
		while(contents!=null){
			StringBuilder sbf = new 
                    StringBuilder("");
			if (contents.contains("Yes")) {
		        sbf.append("1,0");
			}else if (contents.contains("No")) {
		        sbf.append("0,1");
			}
	        all.add(sbf+"\n");	
			contents=br.readLine();
		}
		br.close();

		FileOutputStream fos1 = new FileOutputStream("./data/spambase/testing/spambase_attack_normal_label.txt"); 
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
