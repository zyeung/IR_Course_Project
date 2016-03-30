package part_b;

import part_a.*;
import comparator.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class Frequencies {
	public ArrayList<Frequency> computeWordFrequencies(ArrayList<String> tokens){
		if (tokens == null) return null;
		
		ArrayList<Frequency> freq_list = new ArrayList<Frequency>();
		ArrayList<Frequency> sorted_freq_list = new ArrayList<Frequency>();
		HashMap<String,Integer> tokenmap = new HashMap<String,Integer>();
		
		for (int i=0; i<tokens.size(); i++){
			if (!tokens.get(i).isEmpty()){
				String temp = tokens.get(i);
				tokenmap.put(temp, tokenmap.containsKey(temp)?tokenmap.get(temp)+1:1);
			}
		}
		
		Enumeration<String> strEnum = Collections.enumeration(tokenmap.keySet());
		while(strEnum.hasMoreElements()) {
		     //System.out.println(strEnum.nextElement());
			String temp = strEnum.nextElement();
			Frequency frequency = new Frequency(temp, tokenmap.get(temp));
			freq_list.add(frequency);

		}
		//System.out.println(freq_list.size());
		sorted_freq_list = sorting_tokens(freq_list);
		
		return sorted_freq_list;
	}
	
	public static ArrayList<Frequency> sorting_tokens(ArrayList<Frequency> tokens){
		FrequencyComparator com = new FrequencyComparator();
		Collections.sort(tokens, com);
		return tokens;
	}
	
	public static void print(ArrayList<Frequency> tokens){
		//System.out.println("printing.. "+tokens.size());
		try {
			PrintWriter writer = new PrintWriter("/Users/hao/Desktop/221/testing/part_b.txt","UTF-8");
			for (int i=0; i<tokens.size();i++)
				writer.println(tokens.get(i).printAll());
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
