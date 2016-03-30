package part_d;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

import part_b.Frequency;

public class Anagrams_DB {
	HashMap<Long,DB_Entry> map;
	
	public void makeDB(String inputURL) throws IOException{
		map = new HashMap<Long,DB_Entry>();
		String flags = "[^0-9a-zA-Z'_]+";
		
		
		FileInputStream fstream = new FileInputStream(inputURL+"/alldata.txt");
		BufferedReader buffer_reader = new BufferedReader(new InputStreamReader(fstream));
				
		String line;
		line=buffer_reader.readLine();
		//int ii = 0;
		while ( line!= null ){
			//System.out.println(ii++);
			if (line.trim().length()!=0){
				String[] temp_line;
				line = line.toLowerCase();
				temp_line = line.split(flags);
				
				if (temp_line.length>0){
				if (temp_line[0].matches("[a-z]+")&&!temp_line[0].contains("_")){
					long temp_long = caculateHash(temp_line[0]);
					String a = StringSort(temp_line[0]);							
					//System.out.println("a" +a +" " +temp_line[4] );
					if (!map.containsKey(temp_long)){
						DB_Entry temp = new DB_Entry(a, temp_line[0]);
						map.put(temp_long, temp);
					}
					else
						map.get(temp_long).add(temp_line[0]);
				}
				}
				
				
			}
			
			line=buffer_reader.readLine();
		}
		
		buffer_reader.close();
		
		
		

		Enumeration<Long> strEnum = Collections.enumeration(map.keySet());
		
		PrintWriter writer = new PrintWriter(inputURL+"/anagram.txt", "UTF-8");

		while(strEnum.hasMoreElements()) {
		     //System.out.println(strEnum.nextElement());
			long temp = strEnum.nextElement();
			if (map.get(temp).words.size()>1){
				writer.print(map.get(temp).sorted_word);
				Enumeration<String> strEnum_sub = Collections.enumeration(map.get(temp).words.keySet());
				while(strEnum_sub.hasMoreElements()){
					writer.print(" " +strEnum_sub.nextElement());
				}
				writer.print("\n");
			}
		}
		writer.close();
	}
	
	
	
	public long caculateHash(String word){  //Hash by prime 
		int[] prime = new int[] {2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97,101};
		int temp = 1;		
		for(int i=0; i<word.length();i++)
			temp*=prime[word.charAt(i)-'a'];

		return temp;
	}
	
	public String StringSort(String a){
		String b = new String (a);
		char[] a_C = (b.toCharArray());
		Arrays.sort(a_C);
		return b =new String(a_C);
	}
	
	public ArrayList<Anagram> detectAnagrams(ArrayList<String> tokens){
		Set<String> hs = new HashSet<>();
		hs.addAll(tokens);
		tokens.clear();
		tokens.addAll(hs);
		
		ArrayList<Anagram> ana = new ArrayList<Anagram>();
		Collections.sort(tokens);
	
		
		for (int i=0; i<tokens.size(); i++){
			if (!(tokens.get(i).matches("[a-z]+")&&!tokens.get(i).contains("_")))
				continue;
			long temp = caculateHash(tokens.get(i));
			if (map.containsKey(temp) && map.get(temp).words.size()>1){
				ArrayList <String> temp_ana = new ArrayList<String>();
				Enumeration<String> strEnum_sub = Collections.enumeration(map.get(temp).words.keySet());
				while(strEnum_sub.hasMoreElements()){
					String temp_str = strEnum_sub.nextElement();
					if (temp_str != tokens.get(i))
						temp_ana.add(temp_str);
				}
				
				Anagram e = new Anagram (tokens.get(i),temp_ana);
				ana.add(e);
			}
				
		}
		
		
		return ana;
	}
	
	public void print(ArrayList<Anagram> ana){
		try {
			PrintWriter writer = new PrintWriter("/Users/hao/Desktop/221/testing/part_d.txt","UTF-8");
			for (int i=0; i<ana.size();i++){
				writer.print("Word: ");
				writer.println(ana.get(i).printToken());
				int size  = ana.get(i).returnAnaList().size();
				for (int j=0; j<size;j++){
					writer.print(" "+ana.get(i).returnAnaList().get(j));
				}
				writer.println("");
			}
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
