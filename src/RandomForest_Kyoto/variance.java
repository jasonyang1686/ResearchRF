package RandomForest_Kyoto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class variance 
{

	List<Integer> getResult(ArrayList<List<Double>>matrix,int numTree, int numSelection) throws Exception{        
    	List<String>startCount = new ArrayList<String>();
    	List<Integer>random = new ArrayList<Integer>();
        //random number
	    Integer[] arr = new Integer[numTree];	    
	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
    }

		  Collections.shuffle(Arrays.asList(arr));
      for (int i=0; i<arr.length;i++){
    	  random.add(arr[i]);
      }
	      int start= random.get(0);
	//      System.out.println(start);
	    random.remove(0);
  		List<Integer>treelist = new ArrayList<Integer>();
       	classDistribution cd=new classDistribution();
       	
        treelist.add(start);
        double result=0.0;
        result = getSum(matrix, treelist);
   
        for(int i=1;i<numSelection;i++){
        	int number=compare(matrix,treelist,random,result);
        	treelist.add(number);
            random.remove(random.indexOf(number));

      //      System.out.println(treelist);
  
            }
 		return treelist;
        }
    double getSum(ArrayList<List<Double>>matrix,List<Integer>treelist)
    {
    	List<Double>medstd=new ArrayList<Double>();
    	for (int i=0;i<matrix.get(0).size();i++){
    		medstd.add(0.0);
    	}
        for(int i=0;i<treelist.size();i++){
        	for (int j=0;j<matrix.get(treelist.get(i)).size();j++){
        		medstd.set(j, medstd.get(j)+matrix.get(treelist.get(i)).get(j));
        	}
        }
        return getStd(medstd);
    }
    
    double getEntropy(ArrayList<List<Double>>matrix,List<Integer>finalresult) throws IOException{
    	List<Double>listdata=new ArrayList<Double>(); 
    	List<Double>normalizeddata=new ArrayList<Double>();
    	double sum=0.0;
    	for(int i=0;i<matrix.get(0).size();i++){
    		listdata.add(0.0);
    	}
    	double entropy=0.0;
    	for (int i=0;i<finalresult.size();i++){  		
    			for (int m=0;m<matrix.get(finalresult.get(i)).size();m++){
    			listdata.set(m, listdata.get(m)+matrix.get(finalresult.get(i)).get(m));
    			}
    	}
  //  	System.out.println(listdata);
    	for (int i=0;i<listdata.size();i++){
    		sum+=listdata.get(i);
    	}
    	for (int i=0;i<listdata.size();i++){

    		normalizeddata.add(listdata.get(i)/sum);
    	}
    	for (int i=0;i<normalizeddata.size();i++){
    		if(normalizeddata.get(i)!=0)
    		entropy+=-1.0*normalizeddata.get(i)*Math.log(normalizeddata.get(i));
    	}
    	return entropy;
    }
    double gettopk(ArrayList<List<Double>>matrix,List<Integer>finalresult, int k) throws IOException{
    	List<Double>listdata=new ArrayList<Double>(); 
    	double sum=0.0;
    	for(int i=0;i<matrix.get(0).size();i++){
    		listdata.add(0.0);
    	}
    	for (int i=0;i<finalresult.size();i++){  		
    			for (int m=0;m<matrix.get(finalresult.get(i)).size();m++){
    			listdata.set(m, listdata.get(m)+matrix.get(finalresult.get(i)).get(m));
    			}
    	}
    	Collections.sort(listdata,Collections.reverseOrder());
  //  	System.out.println(listdata);
    	for (int i=0;i<k;i++){
    		sum+=listdata.get(i);
    	}

    	return sum/(finalresult.size()+0.0);
    }
	
	int compare(ArrayList<List<Double>>matrix,List<Integer>treelist, List<Integer>random,double result) throws Exception{
		Map<Integer,Double>appear = new HashMap<Integer, Double>();
 	
		for(int i=1;i<random.size();i++){
        	treelist.add(random.get(i));
        	appear.put(random.get(i), getSum(matrix,treelist));
        	treelist.remove(treelist.size()-1);
        	
		}
	    ArrayList<Map.Entry<Integer,Double>> list_Data = new ArrayList<Map.Entry<Integer, Double>>(appear.entrySet());  
  	    
  	    Collections.sort(list_Data, new Comparator<Map.Entry<Integer,Double>>(){    
  	      public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2)  
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
  	    
	//	System.out.println(list_Data.get(list_Data.size()-1));
		return list_Data.get(list_Data.size()-1).getKey();
	}
	
    double getMean(List<Double>data, int size)
    {
        double sum = 0.0;
        for(double a : data)
            sum += a;
        return sum/size;
    }
    
    double getStd(List<Double>data)
    {
    	int size=data.size();
        double mean = getMean(data, size);
        double temp = 0;
        for(double a :data)
            temp += (mean-a)*(mean-a);
        return Math.sqrt(temp/size);
    }

    double getVariance(List<Double>data)
    {
    	int size=data.size();
        double mean = getMean(data, size);
        double temp = 0;
        for(double a :data)
            temp += (mean-a)*(mean-a);
        return temp/size;
    }

    double getStdDev(List<Double>data)
    {
        return Math.sqrt(getVariance(data));
    }

    public double median(List<Double>data) 
    {
       double[]datas=new double[data.size()];
       for (int i=0;i<datas.length;i++){
    	   datas[i]=data.get(i);
       }
       Arrays.sort(datas);

       if (datas.length % 2 == 0) 
       {
          return (datas[(datas.length / 2) - 1] + datas[datas.length / 2]) / 2.0;
       } 
       else 
       {
          return datas[datas.length / 2];
       }
    }
    
    public static void main(String[] args) throws Exception {
		String path = "./data/attack/spambase/model/5,50/variance.txt"; 
		ArrayList<List<Double>>matrix= new ArrayList<List<Double>>();
        BufferedReader br = new BufferedReader(new FileReader(new File(path))); 
        String data = br.readLine();
        while (data != null) {
        	List<Double>result= new ArrayList<Double>();
            String []t = data.split(",");  
            for (int i=0;i<t.length;i++){
            	result.add(Double.parseDouble(t[i]));
            }
            matrix.add(result);
            data=br.readLine();
        }
        br.close();
     List<String>classlabels= new ArrayList<String>();
     List<String>med_result= new ArrayList<String>();
   	 BufferedReader br2 = new BufferedReader(new FileReader(new File("./data/attack/spambase/model/spambase1.data"))); 
     String[] labels = br2.readLine().split(",");
     br2.close();
     for (int i=0;i<labels.length-1;i++){
     	classlabels.add(labels[i]); 
     }
    	int numTree=100;
    	int numSelection=5;   	
    	variance va= new variance();
    	List<Integer>finalresult = new ArrayList<Integer>();
		String modelspath = "./data/attack/spambase/model/5,50/models/";
		for(int i=0;i<100;i++){
        finalresult=va.getResult(matrix,numTree, numSelection);  
        double entropy = va.getEntropy(matrix,finalresult);
        System.out.println(finalresult);  
        System.out.println(entropy);  
		}
        /*
        LeafDistribution ln=new LeafDistribution();
		med_result=ln.process(modelspath, finalresult, classlabels);
        for (int i=0;i<med_result.size();i++){
  	  	    System.out.println(med_result.get(i));        	
        }
        */
    }
}