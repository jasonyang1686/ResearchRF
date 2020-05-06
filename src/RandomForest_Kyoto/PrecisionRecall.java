package RandomForest_Kyoto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;


public class PrecisionRecall {

public static void main(String[] args) throws Exception {
	for(int numAttr=1;numAttr<58;numAttr++){

	List<String>classlabels = new ArrayList<String>();
	List<String>groundtruth=new ArrayList<String>();
	List<String>differentialratio=new ArrayList<String>();
	
	 BufferedReader br = new BufferedReader(new FileReader(new File("./data/attack/spambase/model/spambase_model.data"))); 
     String[] labels = br.readLine().split(",");
     br.close();
     for (int i=0;i<labels.length-1;i++){
     	classlabels.add(labels[i]); 
     }
     
     BufferedReader br1 = new BufferedReader(new FileReader(new File(
				"./data/attack/spambase/model/5,50/temp/ground_truth.txt")));
	 String gt=br1.readLine();
	 while(gt!=null){
		String[] weights=gt.split(":");
			groundtruth.add(classlabels.get(Integer.parseInt(weights[0])));
			gt=br1.readLine();
		}
		br1.close();
		
		 BufferedReader br2 = new BufferedReader(new FileReader(new File(
					"./data/attack/spambase/model/5,50/temp/distribution.txt")));
		 String static_weight=br2.readLine();
		 while(static_weight!=null){
			String[] weights=static_weight.split("=");
				differentialratio.add((weights[0]));
				static_weight=br2.readLine();
			}
			br2.close();
		double count=0;	
		for (int i=0;i<numAttr;i++){
			for (int j=0;j<numAttr;j++){
				if (groundtruth.get(i).equals(differentialratio.get(j))){
					count++;
				}
			}
		}
		
		System.out.println(count/numAttr);
	}
	}
}