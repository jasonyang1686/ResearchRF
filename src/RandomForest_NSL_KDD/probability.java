package RandomForest_NSL_KDD;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;

public class probability {
	
	public List<String> ig_process(List<String> distribution_result,List<String> classlabels,Double r){
        double total=0.0;
		List<String> dr_value = new ArrayList<String>();
		for (int i=0;i<distribution_result.size();i++){
			String[] dr_content=distribution_result.get(i).split(",");
			total+=Math.exp(-1.0*r*(Double.parseDouble(dr_content[1])));
		}
		double accum=0.0;
		for (int j=0;j<classlabels.size();j++){
		for (int i=0;i<distribution_result.size();i++){
			String[] dr_content=distribution_result.get(i).split(",");
			if (dr_content[0].equals(classlabels.get(j))){
			accum+=Math.exp(-1.0*r*(Double.parseDouble(dr_content[1])))/total;
			dr_value.add(dr_content[0]+","+accum);
			}
		}
		}
       System.out.println(accum);		
        System.out.println(total);
		
		return dr_value;
	}


	public List<String> process(String path, List<Integer> pathlist, List<String> classlabels, Double r) throws Exception {

		List<String> distribution_result = new ArrayList<String>();
		List<String> dr_value = new ArrayList<String>();
		LeafDistribution ln = new LeafDistribution();
		distribution_result = ln.process(path, pathlist, classlabels);
        double total=0.0;
		for (int i=0;i<distribution_result.size();i++){
			String[] dr_content=distribution_result.get(i).split(",");
			total+=Math.exp(-1.0*r*(Double.parseDouble(dr_content[1])));
		}
		double accum=0.0;
		for (int j=0;j<classlabels.size();j++){
		for (int i=0;i<distribution_result.size();i++){
			String[] dr_content=distribution_result.get(i).split(",");
			if (dr_content[0].equals(classlabels.get(j))){
			accum+=Math.exp(-1.0*r*(Double.parseDouble(dr_content[1])))/total;
			dr_value.add(dr_content[0]+","+accum);
			}
		}
		}
       System.out.println("accum :"+accum);		
        System.out.println("total :"+total);
		
		return dr_value;
	}
	
	public String getLabel(List<String>med_result){
        double ran = Math.random();
    //    System.out.println(ran);
			for (int j=0;j<med_result.size();j++) {
				String[]results=med_result.get(j).split(",");
				if(ran<Double.parseDouble(results[1])){
				//	System.out.println(results[1]);
					return results[0];					
				}
			}
			String []large_results=med_result.get(med_result.size()-1).split(",");
			if(ran>Double.parseDouble(large_results[1])){
			return getLabel(med_result);
			}
			return null;
	}

	public static void main(String[] args) throws Exception {

		List<String> classlabels = new ArrayList<String>();

		BufferedReader br2 = new BufferedReader(
				new FileReader(new File("./data/attack/NSL_KDD/model/KDDTrain_B.data")));
		String[] labels = br2.readLine().split(",");
		br2.close();
		for (int i = 0; i < labels.length - 1; i++) {
			classlabels.add(labels[i]);
		}

		String path = "./data/attack/NSL_KDD/model/5,50/models/";

		List<Integer> pathlist = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++) {
			pathlist.add(i);
		}
		
		double r=0;
		List<String> med_result = new ArrayList<String>();
		probability prob = new probability();
		med_result = prob.process(path, pathlist, classlabels,r);
		for (int i=0;i<med_result.size();i++){
		System.out.println(med_result.get(i));
		}
	   Map <String,Integer>test = new HashMap<String, Integer>();
	   for (int i=0; i<labels.length-1;i++){
		   test.put(labels[i], 0);
	   }
	//   System.out.println(med_result.size());
       for (int i=0;i<10000;i++){
                    String label = prob.getLabel(med_result);
					test.put(label, test.get(label)+1);			
       }

		ArrayList<Map.Entry<String, Integer>> list_Data = new ArrayList<Map.Entry<String, Integer>>(
				test.entrySet());

		Collections.sort(list_Data, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				if ((o2.getValue() - o1.getValue()) > 0)
					return 1;
				else if ((o2.getValue() - o1.getValue()) == 0)
					return 0;
				else
					return -1;
			}
		});
		
		int count=0;
		for (Map.Entry<String, Integer> entry : test.entrySet()) {
			count += entry.getValue();
		}
		System.out.println(count);
		for (int i = 0; i < list_Data.size(); i++) {
			 System.out.println(list_Data.get(i).getKey()+","+list_Data.get(i).getValue());
		
		}
	}
}