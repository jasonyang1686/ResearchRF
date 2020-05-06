package RandomForest_Kyoto;
 
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
 
public class modelClassifier {
	public List Buildmodel (Classifier cls,int numTest, Instances attack_data,int guess,List<String>medians,List<Integer>real_guess_list) throws Exception{
		  List<Integer>med_result=new ArrayList<Integer>();
			List<String> instanceresult=new ArrayList<String>();
	        int total=0;
		    int num_correct=0;  
		
		for (int i=0;i<attack_data.numInstances()-1;i++){
			if (attack_data.instance(i).classValue()==cls.classifyInstance(attack_data.instance(i))){
				num_correct++;
				//  System.out.println(attackdata.instance(i).classValue());
				//  System.out.println(j48.classifyInstance(attackdata.instance(i)));
	       	 for (int j =0;j<guess;j++ ){ 
	    		 int position=real_guess_list.get(j);  	
	    		 String replace=medians.get(position);
	    		
	    		 total++;
	        if (attack_data.instance(i).attribute(position).isNominal()){
	    	  attack_data.instance(i).setValue(position, replace);
	    		 }else {
	    	  attack_data.instance(i).setValue(position, Double.parseDouble(replace));  
	    		 }
	    	 if (attack_data.instance(i).classValue()!=cls.classifyInstance(attack_data.instance(i))){  
	        	 instanceresult.add(i+":"+j);
	    		 break;
	    	 }
	    	 }  
			}else{
	       	 instanceresult.add(i+":0");
	        }
				
		}
	    for (int i=0;i<instanceresult.size();i++){
	  //	  System.out.println(instanceresult.get(i));
	    }
		 //   System.out.println("number of correction_models:"+num_correct+"/"+m_data.numInstances());
		  //  System.out.println(total); 

		    med_result.add(total);
		    med_result.add(num_correct);
		    return med_result;

	}
	public static void main(String[] args) throws Exception {
        int guess=57;
		int numTest=50;
        List<Integer>guess_list=new ArrayList<Integer>();
        List<Instance>instancedatas= new ArrayList<Instance>();
		int loop=1;
		List<List<Integer>>result=new ArrayList<List<Integer>>();
 
		for(int l=0;l<loop;l++){
        List<String>medians=new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(new File(
    				"./data/attack/spambase/attack/output.txt")));
        String[] output=br.readLine().split(",");
        for (int i=0;i<output.length;i++){
    			medians.add(output[i]);
    		}
    	br.close();	
		
    	String path = "./data/attack/spambase/model/5,50/";
		String attackpath = "./data/attack/spambase/attack/spambase2.data";
		Classifier cls = (Classifier) weka.core.SerializationHelper.read(path+"models/Classifier"+30+".model");	
		
		CSVLoader loader1 = new CSVLoader();
		loader1.setSource(new File(attackpath));
		Instances attack_data = loader1.getDataSet();
		attack_data.setClassIndex(attack_data.numAttributes() - 1);
	    Integer[] arr = new Integer[attack_data.numAttributes()-1];
	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
	    }
	    Collections.shuffle(Arrays.asList(arr));
	    
	    for(int i=0;i<guess;i++){
    	guess_list.add(arr[i]);
    }
	    

		
		 for (int i=0;i<numTest;i++){
		     instancedatas.add((Instance) attack_data.instance(i).copy());	
		 }
 
	        instancedatas.add((Instance)attack_data.get(attack_data.numInstances()-1).copy());


	    J48VoteChange jvc = new J48VoteChange();
		List<Integer>candidate_list=jvc.getresult(cls, numTest,instancedatas,guess,medians,guess_list);
		
		for (Iterator<Integer> iter = guess_list.listIterator(); iter.hasNext(); ) {
			    int a = iter.next();
			    for (int i=0;i<candidate_list.size();i++){
			    if (a==candidate_list.get(i)) {
			        iter.remove();
			    }
			    }
			}


      
		 List<Integer>real_guess_list= new ArrayList<Integer>();

		 for (int i=0;i<candidate_list.size();i++){
			 real_guess_list.add(candidate_list.get(i));
		 }
		 for (int i=0;i<guess_list.size();i++){
			 real_guess_list.add(guess_list.get(i));
		 }

//		 System.out.println(real_guess_list.size());

		 for (int i=0;i<numTest;i++){
		     attack_data.remove(i);	
		 }
/*
		 for(int i=0;i<real_guess_list.size();i++){
			 System.out.print(real_guess_list.get(i)+",");
		 }
		 System.out.println();
*/
	  modelClassifier mc=new modelClassifier();
	     
      result.add(mc.Buildmodel(cls,numTest,attack_data,guess,medians,real_guess_list));
		}
       int total=0;
		int total_w=0;
		for (int i=0;i<result.size();i++){
			total+=result.get(i).get(0);
			total_w+=result.get(i).get(1);
		}
		System.out.println(total/loop+","+total_w/loop); 
	}	
}