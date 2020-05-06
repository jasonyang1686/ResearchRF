package RandomForest_spambase;

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


public class treeDistribution {
	
	public String getLabel(String content){
		String[]contents=content.split(":");
		String[]contents_1=contents[1].split(" \\(");
		return contents_1[0].trim();
	}
	public String getValue(String content){
		String[]contents=content.split(" \\(");
		String[]contents_1=contents[1].split("\\/");
		return contents_1[0].trim();
	}
	public int getLevel(String content){
		String[]contents=content.split("|");
		int num=0;
		for(int i=0;i<contents.length;i++){
			if (contents[i].contains("|")){
				num++;
			}
		}
		return num;
	}
	public String getName(String attr){
		String[]contents=attr.split("   ");
//		System.out.println(contents[contents.length-1].trim());
		return contents[contents.length-1].trim();
	}
	
	public List<String> process(String path, List<Integer>pathlist,List<String> classlabels) throws Exception{
		Map<String, Double> finalappear = new HashMap<String, Double>();
        for(int i=0;i<classlabels.size();i++){
        	finalappear.put(classlabels.get(i), 0.0);
        }
   //     System.out.println(appear.size());
for (int p=0;p<pathlist.size();p++){
	Map<String, Double> appear = new HashMap<String, Double>();
		List<Integer>duplicate=new ArrayList<Integer>();
		List<String>lines=new ArrayList<String>();
		Classifier cls = (Classifier) weka.core.SerializationHelper.read(path+"Classifier"+pathlist.get(p)+".model");
		String[]contents = cls.toString().split("\n");
		for (int i=0;i<contents.length;i++){
			for (int k=0;k<classlabels.size();k++){
	        	 appear.put(classlabels.get(k), 0.0);
				//careful, there is a space!
			  	if (contents[i].contains(classlabels.get(k)+" ")){
			       lines.add(contents[i]);
			  	}
			}
		}

		
		for (int i=0;i<lines.size();i++){
			if (!duplicate.contains(i)){
		//		System.out.println(getLevel(lines.get(i)));
                String attr="";
            	if (lines.get(i).contains(">")){
            		String[] attrs = lines.get(i).split(">");
            		attr=attrs[0];
            	}else{
            		String[] attrs = lines.get(i).split("<");
            		attr=attrs[0];
            	}
    			    int close=0;
    				int yes_num=0;
    				int no_num=0;
    				int yes_num_1=0;
    				int no_num_1=0;
    				if(lines.get(i).contains(":")){

        				String classResult= getLabel(lines.get(i));
        				if (classResult.equals("No")){
        					no_num+=Integer.parseInt(getValue(lines.get(i)));
        				}else{
        					yes_num+=Integer.parseInt(getValue(lines.get(i)));
        				}
    				}
            		for (int j=i+1;j<lines.size();j++){
                        String attrj="";
                    	if (lines.get(j).contains(">")){
                    		String[] attrs = lines.get(j).split(">");
                    		attrj=attrs[0];
                    	}else{
                    		String[] attrs = lines.get(j).split("<");
                    		attrj=attrs[0];
                    	}
            			if (attrj.equals(attr)){
            				duplicate.add(j);
            				close=j;
            			//	System.out.println(close);       				
            				break;
            			}else{
            				if(lines.get(j).contains(":")){

            				String classResult= getLabel(lines.get(j));
            				if (classResult.equals("No")){
            					no_num+=Integer.parseInt(getValue(lines.get(j)));
            				}else{
            					yes_num+=Integer.parseInt(getValue(lines.get(j)));
            				}            				
            				}
            			}
            			
            		}
            		
            		for (int j=close;j<lines.size();j++){
            			if (getLevel(lines.get(j))<getLevel(lines.get(close))){
            	//			System.out.println(j);
            				break;
            			}else{
            				if(lines.get(j).contains(":")){

            				String classResult= getLabel(lines.get(j));
            				if (classResult.equals("No")){
            					no_num_1+=Integer.parseInt(getValue(lines.get(j)));
            				}else{
            					yes_num_1+=Integer.parseInt(getValue(lines.get(j)));
            				}            				
            				}
            			}
            			 
            		}
            		
            		if (getLevel(lines.get(i))>=2){
            			double result =0.0;
            			double result_1=0.0;
            			   result = (yes_num+0.0)/(yes_num+no_num+0.0);
  
             			   result_1 = (no_num_1+0.0)/(yes_num_1+no_num_1+0.0);
             			
            			if ((yes_num+no_num+yes_num_1+no_num_1)>120){
            				double finalresult=Math.abs(result+result_1-1);
            			//	if ((yes_num+no_num)<=(yes_num_1+no_num_1)){
                    	//	 appear.put(getName(attr), finalresult);
            			//	}else
            		//	appear.put(getName(attr), appear.get(getName(attr))+finalresult);
            			appear.put(getName(attr), finalresult);
            		}
			}
			}
		}
		for (Map.Entry<String, Double> entry : appear.entrySet()) {
			finalappear.put(entry.getKey(), finalappear.get(entry.getKey())+entry.getValue());
		}
		for (int i=0;i<classlabels.size();i++){
		   if (appear.containsKey(classlabels.get(i))){
			   System.out.print(appear.get(classlabels.get(i))+",");
		   }else{
			   System.out.print(0+","); 
		   }
		}
		System.out.println();
}
	    ArrayList<Map.Entry<String,Double>> list_Data = new ArrayList<Map.Entry<String, Double>>(finalappear.entrySet());  
  	    
  	    Collections.sort(list_Data, new Comparator<Map.Entry<String,Double>>(){    
  	      public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2)  
  	      {  
  	        if ((o2.getValue() - o1.getValue())>0)  
  	          return 1;  
  	        else if((o2.getValue() - o1.getValue())==0)  
  	          return 0;  
  	        else   
  	          return -1;  
  	      }  
  	        } 
  	    );

       List<String>result = new ArrayList<String>();
  	    for (int i=0;i<list_Data.size();i++){
  	  	//    System.out.println(list_Data.get(i).getKey()+":"+list_Data.get(i).getValue());
  	    	result.add(list_Data.get(i).getKey()+"="+list_Data.get(i).getValue());
  	    }   
  	    

  	    return result;
        
	}
public static void main(String[] args) throws Exception {
	int numTree =100;
	List<String>classlabels = new ArrayList<String>();
	Map<String, Integer> label = new HashMap<String, Integer>();
	Map<Integer, Integer> count = new HashMap<Integer, Integer>();	
	Map<String, Double> getResult = new HashMap<String, Double>();	
	BufferedReader br2 = new BufferedReader(
			new FileReader(new File("./data/spambase/training/spambase_model_normal.data")));
	//		new FileReader(new File("./data/kyoto/training/201512_tendays_removal_binary_balance_sampling.csv")));
	String[] labels = br2.readLine().split(",");
	br2.close();
	for (int i = 0; i < labels.length - 1; i++) {
		classlabels.add(labels[i]);
	}
 //    System.out.println(classlabels.size());
		String path = "./data/spambase/w_models_15/";

        List<Integer>pathlist = new ArrayList<Integer>();
        for (int i=0;i<numTree;i++){
        	pathlist.add(i);
        }
        List<String>med_result=new ArrayList<String>();	
		treeDistribution ln=new treeDistribution();
		med_result=ln.process(path, pathlist, classlabels);
        for (int i=0;i<med_result.size();i++){
  	  	    System.out.println(med_result.get(i));        	
        }


	}
}