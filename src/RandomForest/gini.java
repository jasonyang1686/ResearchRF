package RandomForest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class gini {
	public double calculate(List<String> path, List<String> classlabels)
			throws Exception {
		System.out.println("size :" + path.size());
		List<Double> totalp = new ArrayList<Double>();
		List<Double> s = new ArrayList<Double>();
		double y = 0;
		Map<Double, Double> dist = new HashMap<Double, Double>();
		
		
		for (int k = 0; k < classlabels.size(); k++) {
			double p = 0.0;
			for (int i = 0; i < path.size(); i++) {
				String modelpath = path.get(i);
				classDistribution cd = new classDistribution();
				List<String> result = cd.process(modelpath, classlabels);
				for (int m = 0; m < result.size(); m++) {
					if (classlabels.get(k).equals(result.get(m))) {
						p = p + 1.0;
					}
				}
			}
			// System.out.println(p);
			totalp.add(p);
		}
		

		for (int i = 0; i < totalp.size(); i++) {
			if (dist.containsKey(totalp.get(i))) {
				dist.put(totalp.get(i), dist.get(totalp.get(i)) + 1.0);
			} else {
				dist.put(totalp.get(i), 1.0);
			}
		}

		Set<Double> hs = new HashSet<>();
		hs.addAll(totalp);
		totalp.clear();
		totalp.addAll(hs);
		System.out.println(totalp.size());
		
		Collections.sort(totalp);

		for (int i = 0; i < totalp.size(); i++) {
			y += dist.get(totalp.get(i));
		}
        System.out.println("y:"+y);
		for (int i = 0; i < totalp.size(); i++) {
	        System.out.println(dist.get(totalp.get(i)));
			dist.put(totalp.get(i), dist.get(totalp.get(i)) / y);
		}

		double result = 0.0;
		s.add(0.0);
		for (int i = 0; i < totalp.size(); i++) {
			s.add(s.get(i)+totalp.get(i) * (dist.get(totalp.get(i))));
		}
		for (int i = 0; i < totalp.size(); i++) {
			result += dist.get(totalp.get(i)) * (s.get(i) + s.get(i + 1));
		}
		return 1.0 - result / s.get(s.size() - 1);
	}

	public static void main(String[] args) throws Exception {
		List<String> path = new ArrayList<String>();

		int iteration = 100;
		for (int i = 0; i < iteration; i++) {
			path.add("./data/5,100/w_models/Classifier" + i + ".model");
		}

/*	
		  String candidate = "0,1,2,4,5,6,7,8,10,11,12,14,17,18,19,20,21,22,25,26,27,28,29,30,31,34,35,37,38,39,40,41,42,46,48,52,53,54,55,56,57,58,59,62,63,64,75,76,77,81,83,86,87,90"; 
		  String[]items = candidate.split(","); 
		  for (int i=0;i<items.length;i++){
		  path.add("./data/5,100/w_models/Classifier"+items[i]+".model"); 
		  }
*/
		List<String> classlabels = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/spambase/spambase.data")));
		String[] labels = br.readLine().split(",");
		br.close();
		for (int i = 0; i < labels.length - 1; i++) {
			classlabels.add(labels[i]);
		}
		gini gn = new gini();
		System.out.println(gn.calculate(path, classlabels));

	}
}