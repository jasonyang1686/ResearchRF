package RandomForest_LR;
 

import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
//step2, get coefficients from each sub data 
public class GetCoefficients {
	public void process (String inputPath,String outputPath,int numModels) throws Exception{
		for(int i=0;i<numModels;i++) {
	CSVLoader loader = new CSVLoader();
	loader.setSource(new File(inputPath+"model"+i+".data"));
	Instances data = loader.getDataSet();
	data.setClassIndex(data.numAttributes()-1);
	Logistic LR = new Logistic();
	LR.buildClassifier(data);
    String contents=LR.toString();
	System.out.println(contents);
    String[]lines=contents.split("\n");
	List<String>coef=new ArrayList<String>();
    for (int l=0;l<lines.length;l++) {
    	if (lines[l].contains("=")) {
    				coef.add(lines[l+1]);
    				coef.add(lines[l+2]);
    				coef.add(lines[l+3]);
    				coef.add(lines[l+4]);
    				coef.add(lines[l+5]);

    				break;
    }
    }
	FileOutputStream fos = new FileOutputStream(outputPath+i+".txt"); 
	OutputStreamWriter osw = new OutputStreamWriter(fos);  
	BufferedWriter bw = new BufferedWriter(osw); 
	for(int j=0;j<coef.size();j++) {
	bw.write(coef.get(j)+"\n");
	}
	bw.close();
	
		}
	}
	public static void main(String[] args) throws Exception {
		String inputPath = "./data/spambase/LR/models/";
		String outputPath = "./data/spambase/LR/coefficients/";
		int numModels=100;
		GetCoefficients gc=new GetCoefficients();
		gc.process(inputPath,outputPath,numModels);
			
	}
}