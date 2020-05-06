
package RandomForest_spambase;
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
 //   loader.setSource(new File("./data/attack/spambase/attack/spambase_attack_normal.data"));
   loader.setSource(new File("./data/spambase/temp/OOB/inbag"+i+".txt"));
    Instances data = loader.getDataSet();
 
    // save ARFF
    ArffSaver saver = new ArffSaver();
    saver.setInstances(data);
 //   saver.setFile(new File("./data/attack/spambase/attack/spambase_attack_normal.arff"));
   saver.setFile(new File("./data/spambase/temp/OOB/inbag"+i+".arff"));
  //  saver.setDestination(new File("./data/attack/kddcup99_B/attack/kddcup99_non_duplicate_10_percent_attack_B.arff"));
    saver.setDestination(new File("./data/spambase/temp/OOB/inbag"+i+".arff"));
    saver.writeBatch();
	  }
 }
}