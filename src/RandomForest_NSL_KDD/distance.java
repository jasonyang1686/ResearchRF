
package RandomForest_NSL_KDD;


import java.util.ArrayList;

import java.util.Collections;

import java.util.List;
import java.util.Map;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

public class distance {
	
	public double calculate_distance(Instance a, Instance b){
		double result=0.0;
		for (int i=0;i<a.numAttributes()-1;i++){
		
				result+=(a.value(i)-b.value(i))*(a.value(i)-b.value(i));			
		}
		return result;
	}
	  public double getresult(List<Instance>pos, List<Instance>neg) throws Exception {
            double result=0.0;
            for (int i=0;i<pos.size();i++){
            	List<Double> temp_result= new ArrayList<Double>();
            	for (int j=0;j<neg.size();j++){
            			temp_result.add(calculate_distance(pos.get(i),neg.get(j)));            		
            	}
            	Collections.sort(temp_result);
            	result+=temp_result.get(0);
            }

			    return result;
	  }
	public static void main(String[] args) throws Exception {

	}
}
