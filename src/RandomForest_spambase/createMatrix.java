package RandomForest_spambase;

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

public class createMatrix {

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

	public double ratioMatrix(String path, List<Integer> pathlist, List<String> classlabels) throws Exception {
		double ratioresult = 0.0;

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
			
			for (int i = 3; i < contents.length - 3; i++) {
				lines.add(contents[i]);
			}

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
				if (lines.get(i).contains(">") || lines.get(i).contains("<")) {
					if (!duplicate.contains(i)) {
						// System.out.println(getLevel(lines.get(i)));
						String attr = "";
						if (lines.get(i).contains(">")) {
							String[] attrs = lines.get(i).split(">");
							attr = attrs[0];
						} else if (lines.get(i).contains("<")) {
							String[] attrs = lines.get(i).split("<");
							attr = attrs[0];
						}
						int close = 0;
						int yes_total = 0;
						int yes_right = 0;
						int no_total = 0;
						int no_right = 0;
						int yes_total_r = 0;
						int yes_right_r = 0;
						int no_total_r = 0;
						int no_right_r = 0;
						if (lines.get(i).contains(":")) {

							String classResult = getLabel(lines.get(i));
							if (classResult.equals("No")) {
								no_total += Integer.parseInt(getFirstValue(lines.get(i)));
								no_right += Integer.parseInt(getLastValue(lines.get(i)));
							} else {
								yes_total += Integer.parseInt(getFirstValue(lines.get(i)));
								yes_right += Integer.parseInt(getLastValue(lines.get(i)));
							}
						}
						for (int j = i + 1; j < lines.size(); j++) {
							String attrj = "";
							if (lines.get(j).contains(">")) {
								String[] attrs = lines.get(j).split(">");
								attrj = attrs[0];
							} else if (lines.get(i).contains("<")) {
								String[] attrs = lines.get(j).split("<");
								attrj = attrs[0];
							}
							if (attrj.equals(attr)) {
								duplicate.add(j);
								close = j;
								// System.out.println(close);
								break;
							} else {
								if (lines.get(j).contains(":")) {

									String classResult = getLabel(lines.get(j));
									if (classResult.equals("No")) {
										no_total += Integer.parseInt(getFirstValue(lines.get(j)));
										no_right += Integer.parseInt(getLastValue(lines.get(j)));
									} else {
										yes_total += Integer.parseInt(getFirstValue(lines.get(j)));
										yes_right += Integer.parseInt(getLastValue(lines.get(j)));
									}
								}
							}

						}

						for (int j = close; j < lines.size(); j++) {
							if (getLevel(lines.get(j)) < getLevel(lines.get(close))) {
								// System.out.println(j);
								break;
							} else {
								if (lines.get(j).contains(":")) {

									String classResult = getLabel(lines.get(j));
									if (classResult.equals("No")) {
										no_total_r += Integer.parseInt(getFirstValue(lines.get(j)));
										no_right_r += Integer.parseInt(getLastValue(lines.get(j)));
									} else {
										yes_total_r += Integer.parseInt(getFirstValue(lines.get(j)));
										yes_right_r += Integer.parseInt(getLastValue(lines.get(j)));
									}
								}
							}

						}

						if (getLevel(lines.get(i)) >= 0) {
							double result = 0.0;
							double result_1 = 0.0;
							result = (yes_total-yes_right+no_right+0.0) / (yes_total + no_total +0.0);
							result_1 = (no_total_r -no_right_r+yes_right_r+ 0.0) / (yes_total_r + no_total_r + 0.0);

								double finalresult = Math.abs(result + result_1 - 1)*(yes_total-yes_right+no_right+yes_total_r +no_right_r-yes_right_r+0.0)/total_size;
										//*(yes_total+no_total+yes_total_r+no_total_r)/total_size;
								if (appear.get(getName(attr)) < finalresult) {
									appear.put(getName(attr), finalresult);
								}

							
						}
					}
				} 
			}
			double median_result=0.0;
			for (Map.Entry<String, Double> entry : appear.entrySet()) {
				median_result+=entry.getValue();
				finalappear.put(entry.getKey(), finalappear.get(entry.getKey()) + entry.getValue());
			}
		}
		ArrayList<Map.Entry<String, Double>> list_Data = new ArrayList<Map.Entry<String, Double>>(
				finalappear.entrySet());

		Collections.sort(list_Data, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				if ((o2.getValue() - o1.getValue()) > 0)
					return 1;
				else if ((o2.getValue() - o1.getValue()) == 0)
					return 0;
				else
					return -1;
			}
		});
		for (int i = 0; i < list_Data.size(); i++) {
		//	 System.out.println(list_Data.get(i).getKey()+":"+list_Data.get(i).getValue());
			ratioresult += list_Data.get(i).getValue();
		}

		return ratioresult;

	}

	public List<String> countMatrix(String path, int pathNum1, int pathNum2, List<String> classlabels)
			throws Exception {

		List<String> count = new ArrayList<String>();
		Map<String, Integer> appear1 = new HashMap<String, Integer>();
		Map<String, Integer> appear2 = new HashMap<String, Integer>();
		List<String> lines_1 = new ArrayList<String>();
		List<String> lines_2 = new ArrayList<String>();
		Classifier cls = (Classifier) weka.core.SerializationHelper.read(path + "Classifier" + pathNum1 + ".model");
		String[] contents = cls.toString().split("\n");

		for (int i = 3; i < contents.length - 3; i++) {
			lines_1.add(contents[i]);
		}
		
		for (int i=0;i<lines_1.size();i++){
			String attr = "";
			if (lines_1.get(i).contains(">") || lines_1.get(i).contains("<")) {
				if (lines_1.get(i).contains(">")) {
					String[] attrs = lines_1.get(i).split(">");
					attr = getName(attrs[0]);
				} else if (lines_1.get(i).contains("<")) {
					String[] attrs = lines_1.get(i).split("<");
					attr = getName(attrs[0]);
				}
			}
			
			appear1.put(attr, 1);
		}
		
		Classifier cls2 = (Classifier) weka.core.SerializationHelper.read(path + "Classifier" + pathNum2 + ".model");
		String[] contents2 = cls2.toString().split("\n");

		for (int i = 3; i < contents2.length -3; i++) {
			lines_2.add(contents2[i]);

		}
		
		for (int i=0;i<lines_2.size();i++){
			String attr = "";
			if (lines_2.get(i).contains(">") || lines_2.get(i).contains("<")) {
				if (lines_2.get(i).contains(">")) {
					String[] attrs = lines_2.get(i).split(">");
					attr = getName(attrs[0]);
				} else if (lines_2.get(i).contains("<")) {
					String[] attrs = lines_2.get(i).split("<");
					attr = getName(attrs[0]);
				}
			}
			
			appear2.put(attr, 1);
		}
		for (Map.Entry<String, Integer> entry1 : appear1.entrySet()) {

			if (appear2.containsKey(entry1.getKey())) {
				count.add(entry1.getKey());
			}
		}

		return count;

	}

	public static void main(String[] args) throws Exception {
		int numModel = 100;
		List<String> classlabels = new ArrayList<String>();
		Map<String, Integer> label = new HashMap<String, Integer>();
		Map<Integer, Integer> count = new HashMap<Integer, Integer>();
		BufferedReader br2 = new BufferedReader(
				new FileReader(new File("./data/spambase/training/spambase_model_normal.data")));
		String[] labels = br2.readLine().split(",");
		br2.close();
		for (int i = 0; i < labels.length - 1; i++) {
			classlabels.add(labels[i]);
			label.put(labels[i], i);
			count.put(i, 0);
		}
		FileOutputStream fos = new FileOutputStream("./data/spambase/temp/w_spectral_matrix_20.txt");
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osw);
		String path = "./data/spambase/w_models_20/";
		createMatrix cm = new createMatrix();

		for (int i = 0; i < numModel; i++) {
			for (int j = 0; j < numModel; j++) {
				if (i == j) {
				//	System.out.print("0  ");
					bw.write("0  ");
				} else {
					// System.out.print(cm.countMatrix(path, i, j,
					// classlabels).size()+" ");
					bw.write(cm.countMatrix(path, i, j, classlabels).size()-1 + "  ");
				}
			}
		//	System.out.println();
			bw.write("\n");
		}
		
		
	/*	
		  for (int i=0;i<numModel;i++){ 
			  for (int j=0;j<numModel;j++){ 
				  if(i==j){ // System.out.print("0  "); 
					  bw.write("0  "); 
					  }else{
						  List<Integer>pathlist=new ArrayList<Integer>(); 
						  List<Integer>pathlist1=new ArrayList<Integer>(); 
		  pathlist.add(i);
		  pathlist.add(j); 
		 System.out.print(cm.ratioMatrix(path,pathlist, classlabels)+"  ");
		 bw.write(cm.ratioMatrix(path,pathlist, classlabels)+"  "); 
		 } 
				  }
		 System.out.println(); 
		 bw.write("\n"); 
		 }
		 */
		bw.close();
		
	}

}