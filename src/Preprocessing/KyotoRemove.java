package Preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class KyotoRemove {
	public static void main(String[] args) throws Exception {
		ArrayList<String>Duration=new ArrayList<String>();
		ArrayList<String>Service=new ArrayList<String>();
		ArrayList<String>SourceBytes=new ArrayList<String>();
		ArrayList<String>DestinationBytes=new ArrayList<String>();
		ArrayList<String>Count=new ArrayList<String>();
		ArrayList<String>Same_srv_rate=new ArrayList<String>();
		ArrayList<String>Serror_rate=new ArrayList<String>();
		ArrayList<String>Srv_serror_rate=new ArrayList<String>();
		ArrayList<String>Dst_host_count=new ArrayList<String>();
		ArrayList<String>Dst_host_srv_count=new ArrayList<String>();
		ArrayList<String>Dst_host_sname_src_port_rate=new ArrayList<String>();
		ArrayList<String>Dst_host_serror_rate=new ArrayList<String>();
		ArrayList<String>Dst_host_srv_serror_rate=new ArrayList<String>();
		ArrayList<String>Flag=new ArrayList<String>();
		ArrayList<String>IDS_detection=new ArrayList<String>();
		ArrayList<String>Malware_detection=new ArrayList<String>();
		ArrayList<String>Ashla_detection=new ArrayList<String>();
		ArrayList<String>Label=new ArrayList<String>();
		ArrayList<String>Source_IP_Address=new ArrayList<String>();
		ArrayList<String>Source_Port_Number=new ArrayList<String>();
		ArrayList<String>Destination_IP_Address=new ArrayList<String>();
		ArrayList<String>Destination_Port_Number=new ArrayList<String>();
		ArrayList<String>Start_Time=new ArrayList<String>();
		ArrayList<String>ConntectionType=new ArrayList<String>();
		
		String head="./data/kyoto/header.txt";
		String sourceDataPath="./data/kyoto/des/201501_twodays_test.txt";
		String destinationDataPath="./data/kyoto/des/201501_twodays_test_removal.txt";
	
		BufferedReader brw = new BufferedReader(new FileReader(new File(sourceDataPath)));
		String lines=brw.readLine();
		int count=0;
		while(lines!=null){
	//		count++;
			String[]contents=lines.split(",");
	        Duration.add(contents[0]);
	        Service.add(contents[1]);
	        SourceBytes.add(contents[2]);
	        DestinationBytes.add(contents[3]);
	        Count.add(contents[4]);
	        Same_srv_rate.add(contents[5]);
	        Serror_rate.add(contents[6]);
	        Srv_serror_rate.add(contents[7]);
	        Dst_host_count.add(contents[8]);
	        Dst_host_srv_count.add(contents[9]);
	        Dst_host_sname_src_port_rate.add(contents[10]);
	        Dst_host_serror_rate.add(contents[11]);
	        Dst_host_srv_serror_rate.add(contents[12]);
	        Flag.add(contents[13]);
//	        IDS_detection.add(contents[14]);
//	        Malware_detection.add(contents[15]);
//	        Ashla_detection.add(contents[16]);
	        Label.add(contents[17]);
//	        Source_IP_Address.add(contents[18]);
	        Source_Port_Number.add(contents[19]);
//	        Destination_IP_Address.add(contents[20]);
	        Destination_Port_Number.add(contents[21]);
//	        Start_Time.add(contents[22]);
	        ConntectionType.add(contents[23]);
			lines=brw.readLine();
	//		System.out.println(count);
		}
		brw.close();
		
		FileOutputStream fos = new FileOutputStream(destinationDataPath); 
		OutputStreamWriter osw = new OutputStreamWriter(fos);  
		BufferedWriter bw = new BufferedWriter(osw);
        for (int i=0;i<Duration.size();i++){
        	if(Label.get(i).equals("-1") ||Label.get(i).equals("1")||Label.get(i).equals("-2")||Label.get(i).equals("Label"))
        		bw.write(Duration.get(i)+","+ConntectionType.get(i)+","+Service.get(i)+","+SourceBytes.get(i)+","+DestinationBytes.get(i)+","+Count.get(i)+Same_srv_rate.get(i)+","+
        				Serror_rate.get(i)+","+Srv_serror_rate.get(i)+","+Dst_host_count.get(i)+","+Dst_host_srv_count.get(i)+","+Dst_host_sname_src_port_rate.get(i)+","+
        				Dst_host_serror_rate.get(i)+","+Flag.get(i)+","+Source_Port_Number.get(i)+","+Destination_Port_Number.get(i)+","+Label.get(i)+"\n");      		      	
        }
		bw.close();
	}
}
