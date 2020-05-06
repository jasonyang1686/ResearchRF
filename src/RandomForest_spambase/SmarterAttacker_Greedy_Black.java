package RandomForest_spambase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class SmarterAttacker_Greedy_Black {

	public static ArrayList<Integer> getList(int numAttributes, ArrayList<Integer> positions) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		if (positions.size() == 0) {
			for (int i = 0; i < numAttributes; i++) {
				list.add(i);
			}
		} else {
			// System.out.println(positions.size());
			for (int i = 0; i < positions.size(); i++) {
				list.add(positions.get(i));
			}

			for (int i = 0; i < numAttributes; i++) {
				if (!list.contains(i)) {
					list.add(i);
				}
			}
		}
		// list.remove(new Integer(2));
		// list.remove(new Integer(12));
		// System.out.println(list.size());
		// System.out.println(list);
		return list;
	}

	public static void main(String[] args) throws Exception {

		int numRandomTree = 100;

		List<String> medians = new ArrayList<String>();

		BufferedReader br = new BufferedReader(new FileReader(new File(
			//	"./data/kyoto/testing/output_normal.txt")));
		 "./data/kyoto/testing/output_normal_retrain_black.txt")));
		// "./data/spambase/testing/output_normal.txt")));
		String[] output = br.readLine().split(",");
		for (int i = 0; i < output.length; i++) {
			medians.add(output[i]);
		}
		br.close();

		ArffLoader loader = new ArffLoader();
		// loader.setSource(new
		// File("./data/spambase/testing/spambase_all_normal.arff"));
		loader.setSource(new File("./data/kyoto/testing/201512_week_removal_binary_balance_sampling.arff"));
		Instances m_data = loader.getDataSet();
		m_data.setClassIndex(m_data.numAttributes() - 1);
		loader.reset();

		// loader.setSource(new
		// File("./data/spambase/testing/spambase_all_normal.arff"));
		loader.setSource(new File("./data/kyoto/testing/201512_week_removal_binary_balance_sampling.arff"));
		Instances all_data = loader.getDataSet();
		all_data.setClassIndex(all_data.numAttributes() - 1);
		int numAttributes = all_data.numAttributes() - 1;

		String testPath = "./data/kyoto/models_test/";
		String modelPath = "./data/kyoto/models_23_2/";
		// String testPath ="./data/spambase/models_test/";
		// String modelPath ="./data/spambase/models/";

		List<Integer> models = new ArrayList<Integer>();
		for (int i = 0; i < numRandomTree; i++) {
			models.add(i);
		}

		Classifier[] test_Classifiers = new Classifier[models.size()];

		for (int test = 0; test < test_Classifiers.length; test++) {
			test_Classifiers[test] = (Classifier) weka.core.SerializationHelper
					.read(testPath + "Classifier" + models.get(test) + ".model");
		}

		List<ArrayList<Integer>> positionRecord = new ArrayList<ArrayList<Integer>>();

		VoteCount vc = new VoteCount();

		for (int n = 0; n < all_data.size(); n++) {
			ArrayList<Integer> positions = new ArrayList<Integer>();
			if (vc.prediction(all_data.get(n), test_Classifiers) == 0.0) {
				if (all_data.get(n).classValue() == 0.0) {
					int position = vc.getresult(test_Classifiers, all_data.get(n), numAttributes, medians);
					positions.add(position);
					if (!all_data.get(n).attribute(position).isNumeric()) {
						all_data.get(n).setValue(position, medians.get(position));
					} else {
						all_data.get(n).setValue(position, Double.parseDouble(medians.get(position)));
					}

					for (int i = 0; i < numAttributes; i++) {
						if (vc.prediction(all_data.get(n), test_Classifiers) == 0.0) {

							position = vc.getresult(test_Classifiers, all_data.get(n), numAttributes, medians);
							positions.add(position);
							if (!all_data.get(n).attribute(position).isNumeric()) {
								all_data.get(n).setValue(position, medians.get(position));
							} else {
								all_data.get(n).setValue(position, Double.parseDouble(medians.get(position)));
							}
						} else {
							break;
						}
					}
					// System.out.println(all_data.get(n).toString());
					// System.out.println(n);
				}

			}
			positionRecord.add(positions);
		}

		Classifier[] m_Classifiers = new Classifier[models.size()];

		for (int m = 0; m < m_Classifiers.length; m++) {
			m_Classifiers[m] = (Classifier) weka.core.SerializationHelper
					.read(modelPath + "Classifier" + models.get(m) + ".model");
		}

		double total_count = 0;
		double passCount = 0;
		double consume_total = 0;
		double tp = 0;
		double fn = 0;
		double tn = 0;
		double fp = 0;

		for (int n = 0; n < m_data.size(); n++) {
			int budget = 5;
			int count = 0;
			if (vc.prediction(m_data.get(n), m_Classifiers) == 0.0) {
				if (m_data.get(n).classValue() == 0.0) {
					int consume = 0;
					tp++;
					ArrayList<Integer> positions = getList(numAttributes, positionRecord.get(n));

					if (!m_data.get(n).attribute(positions.get(0)).isNumeric()) {
						m_data.get(n).setValue(positions.get(0), medians.get(positions.get(0)));
					} else {
						m_data.get(n).setValue(positions.get(0), Double.parseDouble(medians.get(positions.get(0))));
					}
					count++;
					if (positions.get(0) == 0) {
						consume = consume + 1;
					}
					if (positions.get(0) == 1) {
						consume = consume + 1;
					}
					if (positions.get(0) == 2) {
						consume = consume + 5;
					}
					if (positions.get(0) == 3) {
						consume = consume + 3;
					}
					if (positions.get(0) == 4) {
						consume = consume + 3;
					}
					if (positions.get(0) == 5) {
						consume = consume + 3;
					}
					if (positions.get(0) == 6) {
						consume = consume + 3;
					}
					if (positions.get(0) == 7) {
						consume = consume + 3;
					}
					if (positions.get(0) == 8) {
						consume = consume + 3;
					}
					if (positions.get(0) == 9) {
						consume = consume + 3;
					}
					if (positions.get(0) == 10) {
						consume = consume + 3;
					}
					if (positions.get(0) == 11) {
						consume = consume + 1;
					}
					if (positions.get(0) == 12) {
						consume = consume + 5;
					}

					total_count++;
					int i = 1;
					while (consume < budget && i <= numAttributes) {
						if (vc.prediction(m_data.get(n), m_Classifiers) == 0.0) {
							count++;
							int position = positions.get(i);

							if (positions.get(position) == 0) {
								consume = consume + 1;
							}
							if (positions.get(position) == 1) {
								consume = consume + 1;
							}
							if (positions.get(position) == 2) {
								consume = consume + 5;
							}
							if (positions.get(position) == 3) {
								consume = consume + 3;
							}
							if (positions.get(position) == 4) {
								consume = consume + 3;
							}
							if (positions.get(position) == 5) {
								consume = consume + 3;
							}
							if (positions.get(position) == 6) {
								consume = consume + 3;
							}
							if (positions.get(position) == 7) {
								consume = consume + 3;
							}
							if (positions.get(position) == 8) {
								consume = consume + 3;
							}
							if (positions.get(position) == 9) {
								consume = consume + 3;
							}
							if (positions.get(position) == 10) {
								consume = consume + 3;
							}
							if (positions.get(position) == 11) {
								consume = consume + 1;
							}
							if (positions.get(position) == 12) {
								consume = consume + 5;
							}
							if (!m_data.get(n).attribute(position).isNumeric()) {
								m_data.get(n).setValue(position, medians.get(position));
							} else {
								m_data.get(n).setValue(position, Double.parseDouble(medians.get(position)));
							}
						} else {
							consume_total += consume;
							passCount++;
							break;
						}

						i++;
					}
					total_count += count;
					// System.out.println(m_data.get(n).toString());
					// System.out.println(count);
				} else {
					fp++;
				}
			} else {
				if (m_data.get(n).classValue() == 0.0) {
					fn++;
				} else {
					tn++;
				}
			}
		}
		System.out.println("total consume :" + consume_total);
		System.out.println("passCount :" + passCount);
		System.out.println("True Positive: " + tp);
		System.out.println("Success Rate:" + (passCount/tp));
		System.out.println("False Negative: " + fn);
		System.out.println("True Negative: " + tn);
		System.out.println("False Positive: " + fp);
		System.out.println("Total Tries: " + total_count);
		System.out.println((tp + tn) / (tp + fp + tn + fn));
		// System.out.println((passCount+fn)/(tp+fn));
		System.out.println(fp / (fp + tn));
		System.out.println(tp / (tp + fn));
		System.out.println(total_count / (tp));

	}

}
