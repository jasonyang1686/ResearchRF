package RandomForest_spambase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class SmarterAttacker_Greedy_Consume_White {
	public static void main(String[] args) throws Exception {
		int numRandomTree = 100;
		List<String> medians = new ArrayList<String>();

		BufferedReader br = new BufferedReader(new FileReader(new File(
				// "./data/kyoto/testing/output_normal_retrain_white.txt")));
				// "./data/kyoto/testing/output_normal_retrain_white.txt")));
				// "./data/spambase/testing/output_normal.txt")));
				"./data/kyoto/testing/output_normal.txt")));
		String[] output = br.readLine().split(",");
		for (int i = 0; i < output.length; i++) {
			medians.add(output[i]);
		}
		br.close();

		ArffLoader loader = new ArffLoader();
		// loader.setSource(new
		// File("./data/spambase/testing/spambase_attack_normal.arff"));
		// loader.setSource(new
		// File("./data/kyoto/testing/201501_twodays_test_removal_binary_balance_sampling.arff"));
		// Instances m_data = loader.getDataSet();
		// m_data.setClassIndex(m_data.numAttributes() - 1);
		// loader.reset();

		// loader.setSource(new
		// File("./data/spambase/testing/spambase_retrain_attack_normal.arff"));
		// loader.setSource(new
		// File("./data/spambase/testing/spambase_all_normal.arff"));
		loader.setSource(new File("./data/kyoto/testing/201512_week_removal_binary_balance_sampling.arff"));
		Instances all_data = loader.getDataSet();
		all_data.setClassIndex(all_data.numAttributes() - 1);
		int numAttributes = all_data.numAttributes() - 1;

		// String path = "./data/kyoto/IG/w_models_100_new/";
		// String path ="./data/spambase/models_black_box/";
		// String path ="./data/spambase/models/";
		String path = "./data/kyoto/w_models_100/";
		List<Integer> models = new ArrayList<Integer>();
		for (int i = 0; i < numRandomTree; i++) {
			models.add(i);
		}

		Classifier[] m_Classifiers = new Classifier[models.size()];

		for (int m = 0; m < m_Classifiers.length; m++) {
			m_Classifiers[m] = (Classifier) weka.core.SerializationHelper
					.read(path + "Classifier" + models.get(m) + ".model");
		}

		VoteCount vc = new VoteCount();

		double count = 0;
		double passCount = 0;
		double tp = 0;
		double fn = 0;
		double tn = 0;
		double fp = 0;
		double consume_total = 0;

		for (int n = 0; n < all_data.size(); n++) {
			int consume = 0;
			int budget = 5;
			if (vc.prediction(all_data.get(n), m_Classifiers) == 0.0) {
				if (all_data.get(n).classValue() == 0.0) {
					tp++;
					int position = vc.getresult(m_Classifiers, all_data.get(n), numAttributes, medians);
					if (!all_data.get(n).attribute(position).isNumeric()) {
						all_data.get(n).setValue(position, medians.get(position));
					} else {
						all_data.get(n).setValue(position, Double.parseDouble(medians.get(position)));
					}
					count++;
					if (position == 0) {
						consume = consume + 1;
					}
					if (position == 1) {
						consume = consume + 1;
					}
					if (position == 2) {
						consume = consume + 5;
					}
					if (position == 3) {
						consume = consume + 3;
					}
					if (position == 4) {
						consume = consume + 3;
					}
					if (position == 5) {
						consume = consume + 3;
					}
					if (position == 6) {
						consume = consume + 3;
					}
					if (position == 7) {
						consume = consume + 3;
					}
					if (position == 8) {
						consume = consume + 3;
					}
					if (position == 9) {
						consume = consume + 3;
					}
					if (position == 10) {
						consume = consume + 3;
					}
					if (position == 11) {
						consume = consume + 1;
					}
					if (position == 12) {
						consume = consume + 5;
					}

					while (consume < budget) {
						if (vc.prediction(all_data.get(n), m_Classifiers) == 0.0) {
							count++;
							position = vc.getresult(m_Classifiers, all_data.get(n), numAttributes, medians);
							if (position == 0) {
								consume = consume + 1;
							}
							if (position == 1) {
								consume = consume + 1;
							}
							if (position == 2) {
								consume = consume + 5;
							}
							if (position == 3) {
								consume = consume + 3;
							}
							if (position == 4) {
								consume = consume + 3;
							}
							if (position == 5) {
								consume = consume + 3;
							}
							if (position == 6) {
								consume = consume + 3;
							}
							if (position == 7) {
								consume = consume + 3;
							}
							if (position == 8) {
								consume = consume + 3;
							}
							if (position == 9) {
								consume = consume + 3;
							}
							if (position == 10) {
								consume = consume + 3;
							}
							if (position == 11) {
								consume = consume + 1;
							}
							if (position == 12) {
								consume = consume + 5;
							}
							if (!all_data.get(n).attribute(position).isNumeric()) {
								all_data.get(n).setValue(position, medians.get(position));
							} else {
								all_data.get(n).setValue(position, Double.parseDouble(medians.get(position)));
							}
						} else {
							consume_total += consume;
							passCount++;
							break;
						}
					}

					// System.out.println(all_data.get(n).toString());
				} else {
					fp++;
				}
			} else {
				if (all_data.get(n).classValue() == 0.0) {
					fn++;
				} else {
					tn++;
				}
			}
		}
		System.out.println("total consume :" + consume_total);
		System.out.println("passCount :" + passCount);
		System.out.println("True Positive: " + tp);
		System.out.println("False Negative: " + fn);
		System.out.println("True Negative: " + tn);
		System.out.println("False Positive: " + fp);
		System.out.println("Total Tries: " + count);
		System.out.println((tp) / (tp + fp));
		System.out.println((tp) / (tp + fn));
		System.out.println((passCount + fn) / (tp + fn));
		System.out.println(fp / (fp + tn));
		System.out.println(tp / (tp + fn));
		System.out.println(count / (tp));
	}
}
