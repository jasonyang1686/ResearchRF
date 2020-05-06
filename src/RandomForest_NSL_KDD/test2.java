package RandomForest_NSL_KDD;

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

public class test2 {

	public String getLabel(String content) {
		String[] contents = content.split(":");
		String[] contents_1 = contents[1].split(" \\(");
		return contents_1[0].trim();
	}

	public String getFirstValue(String content) {
		String[] contents = content.split(" \\(");
		String[] contents_1 = contents[1].split("\\/");
		return contents_1[0].trim();
	}

	public String getLastValue(String content) {
		String[] contents = content.split(" \\(");
		String[] contents_1 = contents[1].split("\\/");
		String[] contents_2 = contents_1[1].split("\\)");
		return contents_2[0].trim();
	}

	public int getLevel(String content) {
		String[] contents = content.split("|");
		int num = 0;
		for (int i = 0; i < contents.length; i++) {
			if (contents[i].contains("|")) {
				num++;
			}
		}
		return num;
	}

	public String getName(String attr) {
		String[] contents = attr.split("   ");
		// System.out.println(contents[contents.length-1].trim());
		return contents[contents.length - 1].trim();
	}

	public void process(String path, List<Integer> pathlist, List<String> classlabels) throws Exception {

		Map<String, Double> finalappear = new HashMap<String, Double>();
		for (int i = 0; i < classlabels.size(); i++) {
			finalappear.put(classlabels.get(i), 0.0);
		}
		// System.out.println(appear.size());

		for (int p = 0; p < pathlist.size(); p++) {
			double total_size = 0.0;
			Map<String, Double> appear = new HashMap<String, Double>();
			List<Integer> duplicate = new ArrayList<Integer>();
			List<String> lines = new ArrayList<String>();
			
			Classifier cls = (Classifier) weka.core.SerializationHelper
					.read(path + "Classifier" + pathlist.get(p) + ".model");
			String[] contents = cls.toString().split("\n");

			for (int i = 0; i < classlabels.size(); i++) {
				appear.put(classlabels.get(i), 0.0);
			}
			
			for (int i = 4; i < contents.length - 3; i++) {
				lines.add(contents[i]);
			}
			System.out.println(lines.size());
			/*
			BufferedReader br2 = new BufferedReader(
					new FileReader(new File("./data/attack/kddcup99/attack/test.txt")));
			String data = br2.readLine();
			while (data!=null){
				lines.add(data);
				data = br2.readLine();
			}
			br2.close();			
			*/
			for (int i = 0; i < lines.size(); i++) {
				if (lines.get(i).contains(":")) {
					total_size += Double.parseDouble(getFirstValue(lines.get(i)));

				}
			}
			
			for (int i = 0; i < lines.size(); i++) {
				System.out.println(lines.get(i));
			}


		}
		
	}

	public static void main(String[] args) throws Exception {
		int threshold = 1;
		List<String> classlabels = new ArrayList<String>();

		BufferedReader br2 = new BufferedReader(
				new FileReader(new File("./data/attack/NSL_KDD/model/KDDTrain_B.data")));
		String[] labels = br2.readLine().split(",");
		br2.close();
		for (int i = 0; i < labels.length - 1; i++) {
			classlabels.add(labels[i]);
		}

		String path = "./data/attack/NSL_KDD/model/5,50/w_models/";

		List<Integer> pathlist = new ArrayList<Integer>();
		for (int i = 0; i < threshold; i++) {
			pathlist.add(i);
		}
		List<String> med_result = new ArrayList<String>();
		test2 t2 = new test2();
		t2.process(path, pathlist, classlabels);
		
		for (int i = 0; i < med_result.size(); i++) {
			String[] contents = med_result.get(i).split(",");
			// System.out.println(med_result.get(i));
		//	System.out.println(contents[0]);
		}
		for (int i = 0; i < med_result.size(); i++) {
			String[] contents = med_result.get(i).split(",");
			// System.out.println(med_result.get(i));
		//	System.out.println(contents[1]);
		}

	}
}