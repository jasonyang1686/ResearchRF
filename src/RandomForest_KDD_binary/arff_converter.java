
package RandomForest_KDD_binary;
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
  //  loader.setSource(new File("./data/attack/kddcup99_B/attack/kddcup99_non_duplicate_10_percent_attack_B.data"));
    loader.setSource(new File("./data/OOB/inbag"+i+".txt"));
    Instances data = loader.getDataSet();
 
    // save ARFF
    ArffSaver saver = new ArffSaver();
    saver.setInstances(data);
  //  saver.setFile(new File("./data/attack/kddcup99/attack_B/kddcup99_non_duplicate_10_percent_attack_B.arff"));
    saver.setFile(new File("./data/OOB/inbag"+i+".arff"));
  //  saver.setDestination(new File("./data/attack/kddcup99_B/attack/kddcup99_non_duplicate_10_percent_attack_B.arff"));
    saver.setDestination(new File("./data/OOB/inbag"+i+".arff"));
    saver.writeBatch();
	  }
  }
}