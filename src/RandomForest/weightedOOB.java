package RandomForest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.Randomizable;
import weka.core.Utils;
import weka.core.WeightedInstancesHandler;
import weka.core.converters.CSVLoader;

public class weightedOOB {

  public void getresult(String path, List<Integer>models, int numTree) throws Exception {
	double m_OutOfBagError=0.0;  
	Classifier[]m_Classifiers= new Classifier[models.size()];

	//  System.out.print(RandomF.toString());    

    // remove instances with missing class
	CSVLoader loader1 = new CSVLoader();
	loader1.setSource(new File("./data/attack/statlog/model/statlog1.data"));
	Instances m_data = loader1.getDataSet();
	m_data.setClassIndex(m_data.numAttributes() - 1);
	
	
    boolean[][]inbag=new boolean[numTree][m_data.size()];
 //   System.out.println(.length);
    
    BufferedReader br = new BufferedReader(new FileReader(new File("./data/inbag.txt"))); 
    String labels = br.readLine();
    while(labels!=null){
        String[]contents=labels.split(",");
        inbag[Integer.parseInt(contents[0])][Integer.parseInt(contents[1])]=true;
    	labels=br.readLine();
    }
    br.close();
  
    // calc OOB error?

      double outOfBagCount = 0.0;
      double errorSum = 0.0;
      boolean numeric = m_data.classAttribute().isNumeric();
      
      List<Double> treeCount = new ArrayList<Double>();  
      for (int m=0;m< m_Classifiers.length; m++){
    	  m_Classifiers[m] = (Classifier) weka.core.SerializationHelper.read(path+"w_models/Classifier"+models.get(m)+".model");
    	  treeCount.add(0.0);
      }      
      List<Double> treeResult = new ArrayList<Double>();  
      for (int m=0;m< m_Classifiers.length; m++){
    	  treeResult.add(0.0);
      }
      for (int i = 0; i < m_data.numInstances(); i++) {
   //     	 System.out.println(i);
        double vote;
        double[] votes;
        if (numeric)
          votes = new double[1];
        else
          votes = new double[m_data.numClasses()];
        
        // determine predictions for instance
        int voteCount = 0;

        for (int j = 0; j < m_Classifiers.length; j++) {
          if (inbag[models.get(j)][i]==true){
            continue;
          }
          if (numeric) {
            double pred = m_Classifiers[j].classifyInstance(m_data.instance(i));
 
            if (!Utils.isMissingValue(pred)) {
              treeResult.set(j, treeResult.get(j)+StrictMath.abs(pred-m_data.instance(i).classValue()) * m_data.instance(i).weight());	
              treeCount.set(j,treeCount.get(j)+m_data.instance(i).weight());
              votes[0] += pred;
              voteCount++;
            }
          } else {
        	  System.out.println("non numeric");
            voteCount++;
            double[] newProbs = m_Classifiers[j].distributionForInstance(m_data.instance(i));
            treeCount.set(j,treeCount.get(j)+m_data.instance(i).weight());
            if(Utils.maxIndex(newProbs)!=m_data.instance(i).classValue()){
              treeResult.set(j,treeResult.get(j)+m_data.instance(i).weight());
            }
            // average the probability estimates
            for (int k = 0; k < newProbs.length; k++) {
              votes[k] += newProbs[k];
            }
          }
        }
   
    //    System.out.println("size :"+result.size());
        
        // "vote"

        if (numeric) {
          if (voteCount == 0) {
            vote = Utils.missingValue();
          } else {
            vote = votes[0] / voteCount;    // average
          }
        } else {
          if (Utils.eq(Utils.sum(votes), 0)) {            
            vote = Utils.missingValue();
          } else {
            vote = Utils.maxIndex(votes);   // predicted class
            Utils.normalize(votes);
          }
        }
               
        // error for instance
        if (!Utils.isMissingValue(vote)) {
          outOfBagCount += m_data.instance(i).weight();
          if (numeric) {
            errorSum += StrictMath.abs(vote - m_data.instance(i).classValue()) 
              * m_data.instance(i).weight();
          }
          else {
            if (vote != m_data.instance(i).classValue())
              errorSum += m_data.instance(i).weight();
          }
        }
        
      }

      for (int m=0;m<treeCount.size(); m++){
     // 	  System.out.println("size: "+treeCount.get(m));
        }
      if (outOfBagCount > 0) {
        m_OutOfBagError = errorSum /outOfBagCount;
        System.out.println("error"+m_OutOfBagError);
      }
      for (int m=0;m<treeResult.size();m++){
  	    System.out.println(treeResult.get(m)/treeCount.get(m));
  	    }        
      
    }
	public static void main(String[] args) throws Exception {
		String path = "./data/attack/statlog/model/5,50/";
		int numTree=10;
	    List<Integer>models = new ArrayList<Integer>();
	    
//    String candidate = "0,1,2,4,5,6,7,8,10,11,12,14,17,18,19,20,21,22,25,26,27,28,29,30,31,34,35,37,38,39,40,41,42,46,48,52,53,54,55,56,57,58,59,62,63,64,75,76,77,81,83,86,87,90";
//		 String []items = candidate.split(",");
	    
//		 for (int i=0;i<items.length;i++){
//			 models.add(Integer.parseInt(items[i]));	
		 for (int i=0;i<10;i++){
		   models.add(i);	    
		 }

	    weightedOOB wo = new weightedOOB();
	    wo.getresult(path, models,numTree);
	}
}
