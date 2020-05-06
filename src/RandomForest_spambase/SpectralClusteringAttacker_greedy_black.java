package RandomForest_spambase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class SpectralClusteringAttacker_greedy_black {

	public static ArrayList<Integer> getList(int numAttributes, ArrayList<Integer> positions) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		if (positions.size() == 0) {
			for (int i = 0; i < numAttributes; i++) {
				list.add(i);
			}
		} else {
			for (int i = 0; i < positions.size(); i++) {
				list.add(positions.get(i));
			}

			for (int i = 0; i < numAttributes; i++) {
				if (!list.contains(i)) {
					list.add(i);
				}
			}
		}
		return list;
	}

	public static List<Integer> getList(List<ArrayList<Integer>> model_list, int numCandidate) {
		List<Integer> models = new ArrayList<Integer>();
		for (int i = 0; i < model_list.size(); i++) {
			Integer[] arr = new Integer[model_list.get(i).size()];

			for (int j = 0; j < arr.length; j++) {
				arr[j] = model_list.get(i).get(j);
			}
			Collections.shuffle(Arrays.asList(arr));

			if (numCandidate >= arr.length) {
				for (int m = 0; m < arr.length; m++) {
					models.add(arr[m]);
				}
			} else {
				for (int m = 0; m < numCandidate; m++) {
					models.add(arr[m]);
				}
			}
		}
		return models;
	}

	public static void main(String[] args) throws Exception {

		// String matrix_path="./data/spambase/temp/w_spectral_matrix_15.txt";
		String matrix_path = "./data/kyoto/temp/spectral_matrix.txt";

		int numCluster = 5;
		int numTree = 100;
		int numSelection = 1;

		double[][] count_matrix = new double[numTree][numTree];
		double[][] ratio_matrix = new double[numTree][numTree];

		BufferedReader brm = new BufferedReader(new FileReader(new File(matrix_path)));
		String data = brm.readLine();
		int mnumber = 0;
		while (data != null) {
			String[] t = data.split("  ");
			for (int i = 0; i < t.length; i++) {
				count_matrix[mnumber][i] = Double.parseDouble(t[i]);
			}
			mnumber++;
			data = brm.readLine();
		}
		brm.close();

		SpectralClustering_Own st = new SpectralClustering_Own();

		List<ArrayList<Integer>> model_list = st.clustering(count_matrix, numCluster);

		int numRandomTree = 100;
		List<String> medians = new ArrayList<String>();

		BufferedReader br = new BufferedReader(new FileReader(new File("./data/kyoto/testing/output_normal.txt")));
		// "./data/spambase/testing/output_normal.txt")));
		String[] output = br.readLine().split(",");
		for (int i = 0; i < output.length; i++) {
			medians.add(output[i]);
		}
		br.close();

		String testPath = "./data/kyoto/models_test/";
		String modelPath = "./data/kyoto/w_models_100/";
		// String testPath ="./data/spambase/models_test/";
		// String modelPath ="./data/spambase/w_models_15/";

		List<Integer> models = new ArrayList<Integer>();
		List<ArrayList<Integer>> positionRecord = new ArrayList<ArrayList<Integer>>();

		VoteCount vc = new VoteCount();

		double count = 0;
		double tp = 0;
		double fn = 0;
		double tn = 0;
		double fp = 0;
		double accu = 0;
		double tpr = 0;
		double fpr = 0;
		double numTry = 0;
		double consume_total = 0;

		double loop = 0;
		while (loop < 20) {
			double passCount = 0;
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

			for (int n = 0; n < m_data.size(); n++) {
				int dataCount = 0;
				ArrayList<Integer> positions = new ArrayList<Integer>();
				models = getList(model_list, numSelection);
				Classifier[] m_Classifiers = new Classifier[models.size()];

				for (int m = 0; m < m_Classifiers.length; m++) {
					m_Classifiers[m] = (Classifier) weka.core.SerializationHelper
							.read(testPath + "Classifier" + models.get(m) + ".model");
				}

				if (vc.prediction(m_data.get(n), m_Classifiers) == 0.0) {
					if (m_data.get(n).classValue() == 0.0) {

						int position = vc.getresult(m_Classifiers, m_data.get(n), numAttributes, medians);
						positions.add(position);
						if (!m_data.get(n).attribute(position).isNumeric()) {
							m_data.get(n).setValue(position, medians.get(position));
						} else {
							m_data.get(n).setValue(position, Double.parseDouble(medians.get(position)));
						}
						count++;
						for (int i = 0; i < numAttributes; i++) {
							if (vc.prediction(m_data.get(n), m_Classifiers) == 0.0) {
								count++;
								position = vc.getresult(m_Classifiers, m_data.get(n), numAttributes, medians);
								if (positions.contains(position)) {
									continue;
								} else {
									positions.add(position);
								}
								if (!m_data.get(n).attribute(position).isNumeric()) {
									m_data.get(n).setValue(position, medians.get(position));
								} else {
									m_data.get(n).setValue(position, Double.parseDouble(medians.get(position)));
								}
							} else {
								break;
							}
						}
						// System.out.println(m_data.get(n).toString());
					}
				}

				positionRecord.add(positions);
			}

			for (int n = 0; n < all_data.size(); n++) {
				int dataCount = 0;
				int budget = 10;
				models = getList(model_list, numSelection);
				Classifier[] m_Classifiers = new Classifier[models.size()];

				for (int m = 0; m < m_Classifiers.length; m++) {
					m_Classifiers[m] = (Classifier) weka.core.SerializationHelper
							.read(modelPath + "Classifier" + models.get(m) + ".model");
				}

				if (vc.prediction(all_data.get(n), m_Classifiers) == 0.0) {
					if (all_data.get(n).classValue() == 0.0) {
						tp++;
						int consume = 0;
						ArrayList<Integer> positions = getList(numAttributes, positionRecord.get(n));
						// System.out.println(positionRecord.get(n));
						if (!all_data.get(n).attribute(positions.get(0)).isNumeric()) {
							all_data.get(n).setValue(positions.get(0), medians.get(positions.get(0)));
						} else {
							all_data.get(n).setValue(positions.get(0),
									Double.parseDouble(medians.get(positions.get(0))));
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
						dataCount++;
						int i = 1;
						while (consume < budget && i <= numAttributes + 2) {
							if (vc.prediction(all_data.get(n), m_Classifiers) == 0.0) {
								count++;
								dataCount++;
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
							i++;
						}
						// System.out.println(dataCount);
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
			System.out.println("Total Consume :" + consume_total);
			System.out.println("passCount :" + passCount);
			System.out.println(tp);
			System.out.println("Success Rate:" + (passCount/tp));
			System.out.println((tp + tn) / (tp + fp + tn + fn));
			System.out.println(fp / (fp + tn));
			System.out.println(tp / (tp + fn));
			System.out.println(count / (tp));
			accu += (tp + tn) / (tp + fp + tn + fn);
			fpr += fp / (fp + tn);
			tpr += tp / (tp + fn);
			numTry += count / (tp);
			loop++;
		}
		System.out.println(accu / (loop + 0.0));
		System.out.println(fpr / (loop + 0.0));
		System.out.println(tpr / (loop + 0.0));
		System.out.println(numTry / (loop + 0.0));
	}
}
