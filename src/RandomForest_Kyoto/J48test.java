package RandomForest_Kyoto;
 
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
 
public class J48test {

	public static void main(String[] args) throws Exception {
	
    	String path = "./data/attack/spambase/model/spambase1.data";
		String attackpath = "./data/attack/spambase/attack/spambase2.data";
		String outpath="./data/j48result.txt";
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(path));
		Instances data = loader.getDataSet();
		data.setClassIndex(data.numAttributes()-1);
		
		CSVLoader loader1 = new CSVLoader();
		loader1.setSource(new File(attackpath));
		Instances attack_data = loader1.getDataSet();
		attack_data.setClassIndex(attack_data.numAttributes() - 1);
	    
		J48 j48 = new J48();

		/*
		for (int i=0;i<options.length;i++){
		System.out.println(options[i]);
		}
		*/

		j48.setMinNumObj(90);
		j48.setUnpruned(false);
		j48.buildClassifier(data);
		/*
		 ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./data/attack/spambase/model/5,50/models/Classifier11.model"));
Classifier j48 = (Classifier) ois.readObject();
ois.close();
*/
		int number=0;
		
		 for (int i=0;i<attack_data.size();i++){
			 if (attack_data.instance(i).classValue()==1.0)
			    System.out.println(attack_data.instance(i));
		 //       System.out.println(j48.classifyInstance(attack_data.instance(i)));
		 //       System.out.println(attack_data.instance(i).classValue());
//		        j48.
		     if(j48.classifyInstance(attack_data.instance(i))==attack_data.instance(i).classValue()){
		    	 number++;
		     }
		 }
		 weka.core.SerializationHelper.write("./data/j48.model", j48);
        System.out.println(number);
  
FileOutputStream fos = new FileOutputStream(outpath); 
OutputStreamWriter osw = new OutputStreamWriter(fos);  
BufferedWriter bw = new BufferedWriter(osw);
bw.write(j48.toString());
bw.close();
	}	
	
}