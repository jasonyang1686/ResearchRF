
package RandomForest_LR;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
 
public class test {
  /**
   * takes 2 arguments:
   * - CSV input file
   * - ARFF output file
   */
  public static void main(String[] args) throws Exception {
	  
		String path = "./data/attack/spambase_LR/model/spambase_model_50.arff";
		
      ArffLoader loader = new ArffLoader();	
		loader.setSource(new File(path));
		Instances data = loader.getDataSet();
		data.setClassIndex(data.numAttributes()-1);
		Logistic LR = new Logistic();
		LR.buildClassifier(data);
		loader.reset();
		
		loader.setSource(new File("./data/attack/spambase_LR/attack/spambase_all.arff"));
		Instances all_data = loader.getDataSet();
		all_data.setClassIndex(all_data.numAttributes() - 1);
		
        List<String>medians=new ArrayList<String>();
        
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/attack/spambase_LR/attack/output.txt")));
		String[] output=br.readLine().split(",");
		for (int i=0;i<output.length;i++){
			medians.add(output[i]);
		}
		br.close();
		int count_pos=0;
		int total_pos=0;
	      for (int i = 0; i < all_data.numInstances(); i++) {
	    	  
              if (all_data.instance(i).classValue()==0.0){
            	  total_pos++;
            	//  all_data.instance(i).setValue(26, Double.parseDouble(medians.get(26)));  
            	//  all_data.instance(i).setValue(40, Double.parseDouble(medians.get(40)));  
                  if (LR.classifyInstance(all_data.instance(i))!=0.0){ 
                	  count_pos++;
                  }
              }
	      }
	  System.out.println(count_pos);
	  System.out.println(total_pos);
  }
}