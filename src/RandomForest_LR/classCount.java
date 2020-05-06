package RandomForest_LR;

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

//Count the number of features appear in total models
public class classCount {
	public String getName(String attr) {
		String[] contents = attr.split("   ");
		// System.out.println(contents[contents.length-1].trim());
		return contents[contents.length - 1].trim();
	}

	public List<String> process(String path, List<Integer> pathlist, List<String> classlabels) throws Exception {
		Map<String, Integer> count = new HashMap<String, Integer>();

		for (int i = 0; i < classlabels.size(); i++) {
			count.put(classlabels.get(i), 0);
		}
		for (int p = 0; p < pathlist.size(); p++) {

			Map<String, Integer> appear = new HashMap<String, Integer>();
			for (int i = 0; i < classlabels.size(); i++) {
				appear.put(classlabels.get(i), 0);
			}

			Classifier cls = (Classifier) weka.core.SerializationHelper
					.read(path + "Classifier" + pathlist.get(p) + ".model");
			String[] contents = cls.toString().split("\n");
			List<String> lines = new ArrayList<String>();
			for (int i = 3; i < contents.length - 3; i++) {
				lines.add(contents[i]);
			}

			for (int i = 0; i < lines.size(); i++) {
				String attr = "";
				if (lines.get(i).contains(">") || lines.get(i).contains("<")) {
					if (lines.get(i).contains(">")) {
						String[] attrs = lines.get(i).split(">");
						attr = getName(attrs[0]);
					} else if (lines.get(i).contains("<")) {
						String[] attrs = lines.get(i).split("<");
						attr = getName(attrs[0]);
					}
				} else if (lines.get(i).contains(" = ")) {
					String[] attrs = lines.get(i).split(" = ");
					attr = getName(attrs[0]);
				}

				appear.put(attr, 1);
			}

			for (Map.Entry<String, Integer> entry : appear.entrySet()) {

				if (count.containsKey(entry.getKey())) {
					count.put(entry.getKey(), count.get(entry.getKey()) + entry.getValue());
				}

			}
		}
		ArrayList<Map.Entry<String, Integer>> list_Data = new ArrayList<Map.Entry<String, Integer>>(count.entrySet());

		Collections.sort(list_Data, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				if ((o2.getValue() - o1.getValue()) > 0)
					return 1;
				else if ((o2.getValue() - o1.getValue()) == 0)
					return 0;
				else
					return -1;
			}
		});
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < list_Data.size(); i++) {

			System.out.println(list_Data.get(i).getKey() + "," + list_Data.get(i).getValue());
		}

		return result;

	}

	public static void main(String[] args) throws Exception {
		List<Integer> pathlist = new ArrayList<Integer>();
		List<String> classlabels = new ArrayList<String>();

		BufferedReader br2 = new BufferedReader(
				new FileReader(new File("./data/attack/credit/model/credit_model.data")));
		String[] labels = br2.readLine().split(",");
		br2.close();
		for (int i = 0; i < labels.length - 1; i++) {
			classlabels.add(labels[i]);
		}
		String path = "./data/attack/credit/model/5,50/models/";

		for (int i = 0; i < 100; i++) {
			pathlist.add(i);
		}
		List<String> result = new ArrayList<String>();
		classCount cc = new classCount();
		result = cc.process(path, pathlist, classlabels);

	}

}