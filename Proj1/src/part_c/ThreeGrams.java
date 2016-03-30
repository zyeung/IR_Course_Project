package part_c;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import part_a.*;
import part_b.*;
import comparator.*;

public class ThreeGrams {
	public ArrayList<Frequency> computeThreeGramFrequencies(ArrayList<String> tokens){
		if (tokens==null)
			return null;
		
		ArrayList<Frequency> freq = new ArrayList<Frequency>();
		ArrayList<Frequency> sorted_freq = new ArrayList<Frequency>();

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		for (int i=0; i<tokens.size()-2;i++){
			String tg = tokens.get(i)+" "+tokens.get(i+1)+" "+tokens.get(i+2);
			if (!tg.isEmpty()){
				map.put(tg, map.containsKey(tg)?map.get(tg)+1:1);
			}
		}
		
		Enumeration<String> strEnum = Collections.enumeration(map.keySet());
		while(strEnum.hasMoreElements()) {
		     //System.out.println(strEnum.nextElement());
			String temp = strEnum.nextElement();
			Frequency frequency = new Frequency(temp, map.get(temp));
			freq.add(frequency);

		}
		
		sorted_freq = Frequencies.sorting_tokens(freq);
		//freq.clear();
		
		return sorted_freq;
	}
		
	public void print(ArrayList<Frequency> a){
		try {
			PrintWriter writer = new PrintWriter("/Users/hao/Desktop/221/testing/part_c.txt","UTF-8");
			for (int i=0; i<a.size();i++)
				writer.println(a.get(i).printAll());
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
}
