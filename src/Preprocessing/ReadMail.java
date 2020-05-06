package Preprocessing;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class ReadMail {

	   
	  
	   public static void main(String args[]) throws Exception{
		   
	       String path="./data/ADCG SS14 Challenge 02/TT_new/TEST_";
	       String newPath="./data/ADCG SS14 Challenge 02/TT_text/TEST_";
		   int count=2500;
		   for (int i=1;i<=count;i++)
		   display(path,newPath,i);  
	  
	   }  
	  
	   public static void display(String emlFile,String newFile, int i) throws Exception{

		   BufferedReader br = new BufferedReader(new FileReader(emlFile+i+".eml"));	        
			FileOutputStream fos = new FileOutputStream(newFile+i+".txt"); 
			OutputStreamWriter osw = new OutputStreamWriter(fos);  
			BufferedWriter bw = new BufferedWriter(osw);
			String line=br.readLine();
	        while(line!=null) {
	        	bw.write(line+"\n");
	        line=br.readLine();
	        }
			bw.close();
			br.close();
	    }  
	}  

