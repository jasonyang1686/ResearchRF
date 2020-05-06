package libSVM;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class ProbAttack {
	public static Map<Double, Integer> readOutput(String path) throws IOException {
		Map<Double, Integer> output = new HashMap<Double, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		String content = br.readLine();
		int count = 0;
		while (content != null) {
			output.put((Double.parseDouble(content) - 0.0) * (Double.parseDouble(content) - 0.0), count);
			content = br.readLine();
			count++;
		}
		br.close();
		return output;
	}

	public static void main(String[] args) throws Exception {
		String trainPath = "./data/kyoto/des/201512_tendays_removal_binary_balance_sampling.arff";
		String testPath = "./data/kyoto/des/201512_week_removal_binary_balance_sampling.arff";
		String attackPath = "./data/kyoto/des/201501_twodays_test_removal_binary_balance_sampling.arff";
		String outputPath = "./data/kyoto/experiments/distance.txt";
		int numTest = 10;
		int numTestSet = 100;

		ArffLoader loader = new ArffLoader();
		loader.setSource(new File(trainPath));
		Instances trainData = loader.getDataSet();
		System.out.println("training set size: " + trainData.numInstances());
		trainData.setClassIndex(trainData.numAttributes() - 1);
		loader.reset();

		loader.setSource(new File(testPath));
		Instances testData = loader.getDataSet();
		System.out.println("testing set size: " + testData.numInstances());
		testData.setClassIndex(testData.numAttributes() - 1);
		loader.reset();

		loader.setSource(new File(attackPath));
		Instances attackDataAll = loader.getDataSet();
		System.out.println("attack set size: " + attackDataAll.numInstances());
		attackDataAll.setClassIndex(attackDataAll.numAttributes() - 1);
		loader.reset();

		RandomForest RF = new RandomForest();
		RF.setMaxDepth(5);
		RF.setNumTrees(100);
		RF.buildClassifier(trainData);
		RF.setPrintTrees(true);
		RF.setDontCalculateOutOfBagError(false);

		SMO smo = new SMO();
		String[] options = smo.getOptions();
		PolyKernel poly = new PolyKernel();
		smo.setKernel(poly);
		final long start = System.currentTimeMillis();
		smo.buildClassifier(attackDataAll);

		double result = 0.0;
		for (int i = 0; i < attackDataAll.numInstances(); i++) {
			if (smo.classifyInstance(attackDataAll.instance(i)) == testData.instance(i).classValue()) {
				result++;
			}
		}
			
		for (int i = 0; i < numTest; i++) {
			System.out.println(i);
			Map<Double, Integer> output = new HashMap<Double, Integer>();
			output = readOutput(outputPath);
			List<Double> values = new ArrayList<Double>();
			for (Entry<Double, Integer> entry : output.entrySet()) {
				values.add(entry.getKey());
			}
			System.out.println(values.size());
			Collections.sort(values);

			for (int j = 0; j < numTestSet; j++) {
				int position = output.get(values.get(j));
				attackDataAll.get(position).setValue(attackDataAll.numAttributes() - 1,
						RF.classifyInstance(attackDataAll.get(position)));
			}
			smo.buildClassifier(attackDataAll);
			
			  FileOutputStream fos = new FileOutputStream(outputPath); 
			  OutputStreamWriter osw = new OutputStreamWriter(fos);  
			  BufferedWriter bw = new BufferedWriter(osw);
			  bw.write("");
			  bw.close();
			
			for (int m = 0; m < attackDataAll.numInstances(); m++) {
				if (smo.classifyInstance(attackDataAll.instance(m)) == testData.instance(i).classValue()) {
					result++;
				}
			}
		}
		// test
		result = 0.0;
		for (int i = 0; i < testData.numInstances(); i++) {
			if (smo.classifyInstance(testData.instance(i)) == testData.instance(i).classValue()) {
				result++;
			}
		}
		System.out.println("after modified:");
		System.out.println(result / (testData.numInstances() + 0.0));

		result = 0.0;
		for (int i = 0; i < testData.numInstances(); i++) {
			if (RF.classifyInstance(testData.instance(i)) == testData.instance(i).classValue()) {
				result++;
			}
		}
		System.out.println("random forest:");
		System.out.print(result / (testData.numInstances() + 0.0));

	}

}
