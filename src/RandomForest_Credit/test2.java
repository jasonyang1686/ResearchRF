package RandomForest_Credit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.CSVLoader;


public class test2 {

public static void main(String[] args) throws Exception {

	List<String>classlabels = new ArrayList<String>();
	Map<String, Double> appear = new HashMap<String, Double>();
	CSVLoader loader = new CSVLoader();
	loader.setSource(new File("./data/attack/kddcup99/attack/kddcup99_duplicate_10_percent_attack.data"));
	Instances m_data = loader.getDataSet();
	m_data.setClassIndex(m_data.numAttributes() - 1);
	
	for(int i=0;i<m_data.numInstances();i++){
		String []contents=m_data.instance(i).toString().split(",");
		String content = contents[2];
		if (appear.containsKey(content)){
          appear.put(content,appear.get(content)+1.0);		 	         
		}else{
			appear.put(content, 1.0);
		}
	}
	
	
	
     Iterator it = appear.entrySet().iterator();
     while (it.hasNext()) {
         Map.Entry pair = (Map.Entry)it.next();
         System.out.println(pair.getKey() + " = " + pair.getValue());
         it.remove(); // avoids a ConcurrentModificationException
     }


		String path = "./data/attack/kddcup99/model/5,50/models/";

        List<Integer>pathlist = new ArrayList<Integer>();
        for (int i=0;i<1;i++){
        	pathlist.add(i);
        }
        Classifier cls = (Classifier) weka.core.SerializationHelper.read(path+"Classifier"+pathlist.get(0)+".model");
        System.out.println(cls.toString());
        List<String>med_result=new ArrayList<String>();	


	}
}