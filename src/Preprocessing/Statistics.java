package Preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Statistics {
	public static void main(String[] args) throws Exception {
	String sourceDataPath="./data/kyoto/des/201512_tendays_removal_binary_balance_sampling.csv";
	Map<String, Integer> labels = new HashMap<String, Integer>();
	BufferedReader brw = new BufferedReader(new FileReader(new File(sourceDataPath)));
	String lines=brw.readLine();
	lines=brw.readLine();
	int count=0;
	while(lines!=null){
	    String []contents=lines.split(",");
	    String label=contents[contents.length-1];
	    if(labels.containsKey(label)) {
	    	labels.put(label, labels.get(label)+1);
	    }else {
	    	labels.put(label, 1);
	    }
		lines=brw.readLine();
		
	}
	brw.close();
	
	for (Map.Entry<String, Integer> entry : labels.entrySet())
	{
	    System.out.println(entry.getKey() + ": " + entry.getValue());
	}
}
}
