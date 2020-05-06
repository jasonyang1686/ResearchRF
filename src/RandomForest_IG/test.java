package RandomForest_IG;

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

import org.apache.commons.lang3.StringUtils;

import weka.classifiers.Classifier;

//same as classCount.java
public class test {
	public void process(String path) throws Exception{
		Map<String, Integer> count = new HashMap<String, Integer>();	

			
		

//		Classifier cls = (Classifier) weka.core.SerializationHelper.read(path+"Classifier"+10+".model");

		
  
  	//    System.out.println(cls.toString());

        
	}
public static void main(String[] args) throws Exception {
	List<Integer> pathlist = new ArrayList<Integer>();
	List<String>classlabels = new ArrayList<String>();
	Map<String, Integer> label = new HashMap<String, Integer>();
	Map<Integer, Integer> count = new HashMap<Integer, Integer>();	

	//	String path = "./data/attack/NSL_KDD/model/5,50/models/";

	    List<String>result = new ArrayList<String>();	

		System.out.println((int)(Math.log(13)+1));


	}
	
}