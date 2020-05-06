
package RandomForest_LR;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
 
import java.io.File;
 
public class arff_converter {
  /**
   * takes 2 arguments:
   * - CSV input file
   * - ARFF output file
   */
  public static void main(String[] args) throws Exception {

 
    // load CSV

    CSVLoader loader = new CSVLoader();
    loader.setSource(new File("./data/attack/spambase_LR/model/spambase_model_50.data"));
    Instances data = loader.getDataSet();
 
    // save ARFF
    ArffSaver saver = new ArffSaver();
    saver.setInstances(data);
    saver.setFile(new File("./data/attack/spambase_LR/model/spambase_model_50.arff"));
    saver.setDestination(new File("./data/attack/spambase_LR/model/spambase_model_50.arff"));
    saver.writeBatch();
	  
  }
}