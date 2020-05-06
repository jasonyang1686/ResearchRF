
package RandomForest_Credit;
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
	  for(int i=0;i<100;i++){
    CSVLoader loader = new CSVLoader();
    loader.setSource(new File("./data/attack/spambase/model/5,50/OOB/inbag"+i+".txt"));
    Instances data = loader.getDataSet();
 
    // save ARFF
    ArffSaver saver = new ArffSaver();
    saver.setInstances(data);
    saver.setFile(new File("./data/attack/spambase/model/5,50/OOB/inbag"+i+".arff"));
    saver.setDestination(new File("./data/attack/spambase/model/5,50/OOB/inbag"+i+".arff"));
    saver.writeBatch();
	  }
  }
}