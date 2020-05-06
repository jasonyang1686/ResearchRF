/*******************************************************************************
 * Copyright (c) 2010 Haifeng Li
 *   
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package RandomForest_spambase;

import smile.clustering.SpectralClustering;
import smile.data.AttributeDataset;
import smile.data.NominalAttribute;
import smile.data.parser.DelimitedTextParser;
import smile.validation.RandIndex;
import smile.validation.AdjustedRandIndex;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Haifeng Li
 */
public class SpectralClustering_Own {
    
    /**
     * Test of learn method, of class SpectralClustering.
     */
    @Test
    public List<ArrayList<Integer>> clustering(double[][]matrix,int numCluster) {
       List<ArrayList<Integer>>model_list=new ArrayList<ArrayList<Integer>>();
        DelimitedTextParser parser = new DelimitedTextParser();
        parser.setResponseIndex(new NominalAttribute("class"), 0);
        try {           
            SpectralClustering spectral = new SpectralClustering(matrix, numCluster);
            int[]result=spectral.getClusterLabel();
           for (int n=0;n<numCluster;n++){
        	ArrayList<Integer>median=new ArrayList<Integer>();   
            for(int i=0;i<result.length;i++){
            if(result[i]==n){
             median.add(i);	
            }
           }  
            model_list.add(median);
        }
           } catch (Exception ex) {
            System.err.println(ex);
        }
 //      for (int i=0;i<model_list.size();i++){
  //      	System.out.println(model_list.get(i));
  //      }
        return model_list;
    }

    /**
     * Test of learn method, of class SpectralClustering.
     */

    public static void main(String[] args) throws Exception {
    	String count_path="./data/attack/spambase/model/5,50/temp/count_matrix.txt";
    	String ratio_path="./data/attack/spambase/model/5,50/temp/ratio_matrix.txt";  
    	int numModels=100;
    	int numCluster=10;
    	double[][]count_matrix=new double[numModels][numModels];
    	double[][]ratio_matrix=new double[numModels][numModels]; 	
    	 BufferedReader br = new BufferedReader(new FileReader(new File(count_path))); 
         String data = br.readLine();
         int count=0;
         while (data != null) {
             String []t = data.split("  ");  
             for (int i=0;i<t.length;i++){
             	count_matrix[count][i]=Double.parseDouble(t[i]);
             }
             count++;
             data=br.readLine();
         }
         br.close();
         /*
         count=0;
    	 BufferedReader br2 = new BufferedReader(new FileReader(new File(ratio_path))); 
    	 String data2=br2.readLine();
    	 while(data2!=null){
    		 
         String[] labels = data2.split("  ");
         for (int i=0;i<labels.length;i++){
          	ratio_matrix[count][i]=Double.parseDouble(labels[i]);
          }
          count++;
         data2=br2.readLine();
    	 }
         br2.close();
         */
    	SpectralClustering_Own st2 =new SpectralClustering_Own();
    	st2.clustering(ratio_matrix,numCluster);
    }
}
