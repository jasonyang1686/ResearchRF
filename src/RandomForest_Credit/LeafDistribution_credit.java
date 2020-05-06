package RandomForest_Credit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.io.FileReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;

public class LeafDistribution_credit {

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

	public List<String> process(String path, List<Integer> pathlist, List<String> classlabels) throws Exception {
		double total_size = 0.0;
		Map<String, Double> finalappear = new HashMap<String, Double>();
		for (int i = 0; i < classlabels.size(); i++) {
			finalappear.put(classlabels.get(i), 0.0);
		}
		// System.out.println(appear.size());

		for (int p = 0; p < pathlist.size(); p++) {
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

								double finalresult = Math.abs(result + result_1 - 1);
										//*(yes_total+no_total+yes_total_r+no_total_r)/total_size;
								if (appear.get(getName(attr)) < finalresult) {
									appear.put(getName(attr), finalresult);
								}

							
						}
					}
				} else if (lines.get(i).contains(" = ")) {
					if (!duplicate.contains(i)) {
						duplicate.add(i);
						String attr = "";
						int yes_total = 0;
						int no_total = 0;
						int yes_right = 0;
						int no_right = 0;
						int count=0;
						String[] attrs = lines.get(i).split(" = ");
						attr = attrs[0];
						if (lines.get(i).contains(":")) {

							String classResult = getLabel(lines.get(i));
							if (classResult.equals("No")) {
                               count++;
								no_total += Integer.parseInt(getFirstValue(lines.get(i)));
								no_right += Integer.parseInt(getLastValue(lines.get(i)));
							} else {
							    count++;
								yes_total += Integer.parseInt(getFirstValue(lines.get(i)));
								yes_right += Integer.parseInt(getLastValue(lines.get(i)));
							}
						}
						for (int j = i + 1; j < lines.size(); j++) {
							if (getLevel(lines.get(j)) < getLevel(lines.get(i))) {
								break;
							} else if (getLevel(lines.get(j)) == getLevel(lines.get(i))) {
							    count++;
								duplicate.add(j);
							}
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
						if (getLevel(lines.get(i)) >=0 ) {
							double result = 0.0;
							double result_1 = 0.0;
							result = (yes_total - yes_right + 0.0)/ (yes_total + 0.0);
							result_1 = (no_total - no_right + 0.0) / (no_total + 0.0);

								double finalresult = Math.abs(result + result_1 - 1.0);
										//*(yes_total+no_total)/total_size;
								if (appear.get(getName(attr)) < finalresult) {
									appear.put(getName(attr), finalresult);
								}						
						}

					}
				}
			}
			for (Map.Entry<String, Double> entry : appear.entrySet()) {
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

		List<String> result = new ArrayList<String>();
		for (int i = 0; i < list_Data.size(); i++) {
			 System.out.println(list_Data.get(i).getKey()+"="+list_Data.get(i).getValue());
			result.add(list_Data.get(i).getKey() + "=" + list_Data.get(i).getValue());
		}

		return result;

	}

	public static void main(String[] args) throws Exception {
		int threshold = 100;
		List<String> classlabels = new ArrayList<String>();

		BufferedReader br2 = new BufferedReader(
				new FileReader(new File("./data/attack/credit/model/credit_model_1.data")));
		String[] labels = br2.readLine().split(",");
		br2.close();
		for (int i = 0; i < labels.length - 1; i++) {
			classlabels.add(labels[i]);
		}

		String path = "./data/attack/credit/model/5,50/w_models/";

		List<Integer> pathlist = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++) {
			pathlist.add(i);
		}
		List<String> med_result = new ArrayList<String>();
		LeafDistribution_credit lnc = new LeafDistribution_credit();
		med_result = lnc.process(path, pathlist, classlabels);
		
		for (int i = 0; i < med_result.size(); i++) {
			String[] contents = med_result.get(i).split("=");
			// System.out.println(med_result.get(i));
			System.out.println(contents[0]);
		}
		for (int i = 0; i < med_result.size(); i++) {
			String[] contents = med_result.get(i).split("=");
			// System.out.println(med_result.get(i));
			System.out.println(contents[1]);
		}

	}
}