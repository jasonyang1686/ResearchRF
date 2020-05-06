package RandomForest_Kyoto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class ModelSelection {
	/*
	public void getResult(List<String>path,List<String>classes) throws Exception {
		List<Double>accuracy=new ArrayList<Double>();
        BufferedReader br = new BufferedReader(new FileReader(new File("./data/attack/spambase/model/5,50/temp/acu.txt"))); 	
        String nodeweight=br.readLine();
		while(nodeweight!=null){
			accuracy.add(1.0-Double.parseDouble(nodeweight));
			nodeweight=br.readLine();
		}
		br.close();		
		Double total=0.0;
		Double totalvalue=0.0;
		List<String>candidate=new ArrayList<String>();	
		List<Double>acu_candidate=new ArrayList<Double>();
	    gini gn = new gini();
		for (int i=0;i<path.size();i++){
			candidate.add(path.get(i));
			acu_candidate.add(accuracy.get(i));	
	//		System.out.println(en.calculate(candidate, classes));
            Double result = ((totalvalue)+accuracy.get(i))/(acu_candidate.size()+0.0)*(1.0-gn.calculate(candidate, classes));
         //   System.out.println("totalvalue: "+totalvalue);
         //   System.out.println("result: "+result);
            if (total<result){
            	total=result;
            	totalvalue+=accuracy.get(i);
            }else{
            	candidate.remove(candidate.size()-1);
                acu_candidate.remove(acu_candidate.size()-1);
            }

		//	System.out.println(result);
		}
		for (int i=0;i<candidate.size();i++){
		System.out.println(candidate.get(i));
		}
		}
	

	public static void main(String[] args) throws Exception {
		int iteration=10;
		List<String>path = new ArrayList<String>();
		for (int i=0;i<iteration;i++){
			path.add("./data/attack/spambase/model/5,50/w_models/Classifier"+i+".model");
		}

		List<String>classes = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(new File("./data/attack/spambase/model/statlog1.data"))); 
        String[] labels = br.readLine().split(",");
        br.close();
        for(int i=0;i<labels.length-1;i++){
        	classes.add(labels[i]);
        }
        
		ModelSelection ms = new ModelSelection();
		ms.getResult(path,classes);
	}
*/
}