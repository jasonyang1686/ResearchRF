package RandomForest_Credit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class OOB {
	public void TrainData (String outpath, String inpath, int iteration) throws Exception{
		List<String>originaldata=new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(new File("./data/attack/credit/model/credit_model_1.data"))); 
        String header = br.readLine();
        while (header!=null){
        	originaldata.add(header+"\n");
        	header =br.readLine();
        }
        br.close();
        
        List<List<String>> out = new ArrayList<List<String>>();  
        List<List<String>> in = new ArrayList<List<String>>();
        
        for (int i=0;i<iteration;i++){
        	out.add(new ArrayList<String>());
        	in.add(new ArrayList<String>());        	
        }
        
        for (int i=0;i<iteration;i++){
        	out.get(i).add(originaldata.get(0));
        	in.get(i).add(originaldata.get(0));
        }
        
        BufferedReader brin = new BufferedReader(new FileReader(new File(inpath))); 
        String datain = brin.readLine();
        while (datain != null) {
            String t[] = datain.split(",");  
            for (int i=0;i<iteration;i++){
            	if (t[0].equals(i+"")){
            		in.get(i).add(originaldata.get(Integer.parseInt(t[1])+1));
            	}
            }
            datain=brin.readLine();
        }
        brin.close();
        
        BufferedReader brout = new BufferedReader(new FileReader(new File(outpath))); 
        String dataout = brout.readLine();
        while (dataout != null) {
            String t[] = dataout.split(",");  
            for (int i=0;i<iteration;i++){
            	if (t[0].equals(i+"")){
            		out.get(i).add(originaldata.get(Integer.parseInt(t[1])+1));
            	}
            }
            dataout=brout.readLine();
        }
        brout.close();
        System.out.println(in.get(0).size());
        for (int i=0;i<iteration;i++){
		FileOutputStream fos1 = new FileOutputStream("./data/OOB/inbag"+i+".txt"); 
		OutputStreamWriter osw1 = new OutputStreamWriter(fos1);  
		BufferedWriter bw1 = new BufferedWriter(osw1);
		for (int j=0;j<in.get(i).size();j++){
	    bw1.write(in.get(i).get(j));
		}
	    bw1.close();
		FileOutputStream fos2 = new FileOutputStream("./data/OOB/outbag"+i+".txt"); 
		OutputStreamWriter osw2 = new OutputStreamWriter(fos2);  
		BufferedWriter bw2 = new BufferedWriter(osw2);  
		for (int j=0;j<out.get(i).size();j++){
		    bw2.write(out.get(i).get(j));
			}
		    bw2.close();
	    
        }
	}
	
	public static void main(String[] args) throws Exception {
		String outpath = "./data/outbag.txt";
		String inpath = "./data/inbag.txt";
		int iteration=100;
		OOB oob=new OOB();

		oob.TrainData(outpath, inpath, iteration);
			
	}
}