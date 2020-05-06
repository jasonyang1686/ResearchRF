
package Preprocessing;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
 
import java.io.File;
 
public class ArffConverter {
  /**
   * takes 2 arguments:
   * - CSV input file
   * - ARFF output file
   */
  public static void main(String[] args) throws Exception {

 
    // load CSV
//	  for(int i=0;i<100;i++){
    CSVLoader loader = new CSVLoader();
    loader.setSource(new File("./data/kyoto/des/201512_tendays_removal_binary_balance_sampling.csv"));
    Instances data = loader.getDataSet();
 
    // save ARFF
    ArffSaver saver = new ArffSaver();
    saver.setInstances(data);
    saver.setFile(new File("./data/kyoto/des/201512_twodays_removal_binary_balance_sampling.arff"));
    saver.setDestination(new File("./data/kyoto/des/201512_tendays_removal_binary_balance_sampling.arff"));
    saver.writeBatch();
//	  }
  }
}