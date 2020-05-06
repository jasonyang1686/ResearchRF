package RandomForest_KDD;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;


public class test3 {
	
	public String getLabel(String content){
		String[]contents=content.split(":");
		String[]contents_1=contents[1].split(" \\(");
		return contents_1[0].trim();
	}
	public String getFirstValue(String content){
		String[]contents=content.split(" \\(");
		String[]contents_1=contents[1].split("\\/");
		return contents_1[0].trim();
	}
	public String getLastValue(String content){
		String[]contents=content.split(" \\(");
		String[]contents_1=contents[1].split("\\/");
		String[]contents_2=contents_1[1].split("\\)");
		return contents_2[0].trim();
	}
	public int getLevel(String content){
		String[]contents=content.split("|");
		int num=0;
		for(int i=0;i<contents.length;i++){
			if (contents[i].contains("|")){
				num++;
			}
		}
		return num;
	}
	public String getName(String attr){
		String[]contents=attr.split("   ");
//		System.out.println(contents[contents.length-1].trim());
		return contents[contents.length-1].trim();
	}
	
	public void process(String path, List<Integer>pathlist,List<String> classlabels) throws Exception{
		Map<String, Double> finalappear = new HashMap<String, Double>();
        for(int i=0;i<classlabels.size();i++){
        	finalappear.put(classlabels.get(i), 0.0);
        }
 
for (int p=0;p<pathlist.size();p++){
	
	int num=0;
	Map<String, Double> appear = new HashMap<String, Double>();

		List<String>lines=new ArrayList<String>();
		Classifier cls = (Classifier) weka.core.SerializationHelper.read(path+"Classifier"+pathlist.get(p)+".model");
		String[]contents = cls.toString().split("\n");

		for (int i=3;i<contents.length-3;i++){
	//		for (String label:classlabels){
	       // 	 appear.put(classlabels.get(k), 0.0);
                   if(contents[i].contains("service"))
			       lines.add(contents[i]);
			  	
	//		}
		}
		System.out.println(p);
            System.out.println(lines.size());



}



        
	}
public static void main(String[] args) throws Exception {

	List<String>classlabels = new ArrayList<String>();
	 BufferedReader br2 = new BufferedReader(new FileReader(new File("./data/attack/kddcup99/model/kddcup99_duplicate_10_percent_model.data"))); 
     String[] labels = br2.readLine().split(",");
     br2.close();
//     System.out.println(labels.length-1);
     for (int i=0;i<labels.length-1;i++){
     	classlabels.add(labels[i]); 

     }

		String path = "./data/attack/kddcup99/model/5,50/w_models/";

        List<Integer>pathlist = new ArrayList<Integer>();
        for (int i=0;i<100;i++){
        	pathlist.add(i);
        }
        List<String>med_result=new ArrayList<String>();	
		test3 test3=new test3();
	    test3.process(path, pathlist, classlabels);


	}
}