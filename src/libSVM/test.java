package libSVM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class test {
	public static void main(String[] args) throws Exception {
    	Map<Double, Integer> output = new HashMap<Double, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(new File("./data/kyoto/experiments/distance.txt")));
		String content = br.readLine();
		int count = 0;
		while (content != null) {
			if(output.containsKey((Double.parseDouble(content) - 0.0) * (Double.parseDouble(content) - 0.0))) {
			output.put((Double.parseDouble(content) - 0.0) * (Double.parseDouble(content) - 0.0), output.get((Double.parseDouble(content) - 0.0) * (Double.parseDouble(content) - 0.0))+1);
			}else {
				output.put((Double.parseDouble(content) - 0.0) * (Double.parseDouble(content) - 0.0), 1);
			}
			content = br.readLine();

		}
		
		br.close();
		
		for (Entry<Double, Integer> entry : output.entrySet()) {
			count++;
		}
		System.out.println(count);
	}
}
