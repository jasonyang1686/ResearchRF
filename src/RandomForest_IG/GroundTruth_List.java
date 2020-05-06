package RandomForest_IG;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import weka.core.Instances;
import weka.core.converters.ArffLoader;
public class GroundTruth_List {
public static void main(String[] args) throws Exception {
	


    BufferedReader br = new BufferedReader(new FileReader(new File("./data/attack/NSL_KDD/model/NSL_KDD_model_B.csv"))); 
    String[] labels = br.readLine().split(",");
    br.close();
	
	 BufferedReader br1 = new BufferedReader(new FileReader(new File(
				"./data/attack/NSL_KDD/model/5,50/temp/ground_truth.txt")));
	 String static_weight=br1.readLine();
	 while(static_weight!=null){
		String[] weights=static_weight.split(":");
			System.out.println(labels[Integer.parseInt(weights[0])]);
			static_weight=br1.readLine();
		}
		br1.close();
				 

	}
}
