package RandomForest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class entropy {
 public double calculate(List<String> path, List<String> classlabels) throws Exception {
		 System.out.println("size :"+path.size());
		 List<Double>totalp=new ArrayList<Double>();
	      for (int k=0;k<classlabels.size();k++){
			  double p=0.0;
		  for (int i=0;i<path.size();i++){
		      String modelpath = path.get(i);
		      classDistribution cd=new classDistribution();
		      List<String> result =cd.process(modelpath, classlabels);
			     for (int m=0;m<result.size();m++){
				  if (classlabels.get(k).equals(result.get(m))){
					  p=p+1.0;
				  }
			  }
		  }
		//	System.out.println(p);
			  totalp.add(p);
		  }
double sum =0.0;
Collections.sort(totalp);
Collections.reverse(totalp);

for (int i=0;i<totalp.size();i++){
	System.out.println(totalp.get(i));
	sum+=(totalp.get(i));
}
System.out.println("sum: "+sum);
double result =0.0;
for (int i=0;i<totalp.size();i++){
	double med = totalp.get(i)/sum;
	if (med!=0.0){	
	result+= (0.0-med)*Math.log(med)/Math.log(2);
	}
}
return result;
	  }
	
	public static void main(String[] args) throws Exception {
		List<String>path = new ArrayList<String>();
/*
		int iteration =100;
		for (int i=0;i<iteration;i++){
			path.add("./data/5,100/w_models/Classifier"+i+".model");
		}
*/
	

	 String candidate = "0,1,2,3,4,11,15,61";
	 String []items = candidate.split(",");
	 for (int i=0;i<items.length;i++){
		 path.add("./data/5,100/w_models/Classifier"+items[i]+".model");	
	 }

		List<String>classlabels = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(new File("./data/statlog/statlogp.data"))); 
        String[] labels = br.readLine().split(",");
        br.close();
	    for (int i=0;i<labels.length-1;i++){
	    	classlabels.add(labels[i]);
	    }
	    entropy en = new entropy();
	    System.out.println(en.calculate(path,classlabels));
	
			
	}
}