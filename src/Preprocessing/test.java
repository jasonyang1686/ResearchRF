

package Preprocessing;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class test {


	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"./data/spambase/spambase.data")));
		String[] labels = br.readLine().split(",");
		br.close();
		System.out.println(labels.length);
	    Integer[] arr = new Integer[5];
	    for (int i = 0; i < arr.length; i++) {
	        arr[i] = i;
	    }
	    Collections.shuffle(Arrays.asList(arr));
	    System.out.println(Arrays.toString(arr));

	}
}
