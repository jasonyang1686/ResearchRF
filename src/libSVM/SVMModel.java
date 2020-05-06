package libSVM;

import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
 
public class SVMModel {
	public void BuildModel (String path) throws Exception{
	ArffLoader loader = new ArffLoader();
	loader.setSource(new File(path));
	Instances data = loader.getDataSet();
	data.setClassIndex(data.numAttributes()-1);
	SMO smo = new SMO();
	String[]options=smo.getOptions();
	PolyKernel poly = new PolyKernel();
	smo.setKernel(poly);
	final long start = System.currentTimeMillis();
    smo.buildClassifier(data);
 //   smo.SVMOutput();
	final long end = System.currentTimeMillis();
	
	System.out.println("execution time: "+(end-start));
    loader.setSource(new File("./data/kyoto/des/201512_week_removal_binary_balance_sampling.arff"));
    Instances testdata = loader.getDataSet();
    testdata.setClassIndex(testdata.numAttributes()-1);
    double count=0.0;
    for(int i=0;i<testdata.numInstances();i++) {
    if(smo.classifyInstance(testdata.instance(i))==testdata.instance(i).classValue()) {
    	count++;
    }
   }
//	FileOutputStream fos = new FileOutputStream("./data/result.txt"); 
//	OutputStreamWriter osw = new OutputStreamWriter(fos);  
//	BufferedWriter bw = new BufferedWriter(osw); 
    System.out.print(count/(testdata.numInstances()+0.0));
	  weka.core.SerializationHelper.write("./data/kyoto/experiments/model", smo);
	/*
bw.write(RandomF.toString());
	int sum = data.numInstances(); 
	System.out.print(sum);   

	bw.close();
	*/
	}
	public static void main(String[] args) throws Exception {
		String path = "./data/kyoto/des/201501_twodays_test_removal_binary_balance_sampling.arff";
	//	String path = "./data/test.txt";
		SVMModel svm=new SVMModel();
		svm.BuildModel(path);
			
	}
}
