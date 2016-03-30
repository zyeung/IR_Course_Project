import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import part_a.*;
import part_b.*;
import part_c.*;
import part_d.*;


public class Testing {
	
	public static void main(String args[]){
		File file = new File(args[0]);
		Utilities test_a = new Utilities();
		ArrayList<String> part_a = new ArrayList<String>();
		try {
			part_a = test_a.tokenizeFile(file);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		// Testing of part_A
		Testing_A(part_a);
		
		// Testing of part_B
		Testing_B(part_a);
		
		//Testing of part_C
		Testing_C(part_a);
	
		
		
		//Testing D
		Testing_D(part_a);
		
		return;
	}
	
	public static void Testing_A(ArrayList<String> token){
		Utilities test_a = new Utilities();
		test_a.print(token);
	}
	
	public static void Testing_B(ArrayList<String> token){
		Frequencies test_b = new Frequencies();
		ArrayList<Frequency> part_b = new ArrayList<Frequency>();
		part_b = test_b.computeWordFrequencies(token);
		Frequencies.print(part_b);
	}
	
	public static void Testing_C(ArrayList<String> token){
		ThreeGrams test_c  = new ThreeGrams();
		ArrayList<Frequency> part_c = new ArrayList<Frequency>();
		part_c = test_c.computeThreeGramFrequencies(token);
		test_c.print(part_c);
	}
	
	public static void Testing_D(ArrayList<String> token){
		Anagrams_DB temp = new Anagrams_DB();
		try {
			temp.makeDB("/Users/hao/Desktop/221/dict_copy");
			ArrayList<Anagram> ana = new ArrayList<Anagram>(); 
			ana = temp.detectAnagrams(token);
			temp.print(ana);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}


