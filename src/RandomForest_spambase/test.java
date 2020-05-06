package RandomForest_spambase;

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

public class test {
	
	public static void main(String[] args) throws Exception {

		String path = "./data/test.txt";

        BufferedReader br = new BufferedReader(new FileReader(new File(path))); 
        
        String content = br.readLine();
        int count = 0;
        while (content != null) { 
           int number = Integer.parseInt(content);
          //  if(number<10) {
            	count+=number;
          //  }
            content=br.readLine(); 
       }
        br.close();	
        System.out.println(count);
	}
}