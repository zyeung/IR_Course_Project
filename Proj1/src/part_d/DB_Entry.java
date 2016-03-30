package part_d;

import java.util.*;
public class DB_Entry {
	String  sorted_word;
	HashMap <String,Integer> words;
	
	public DB_Entry(String a,String b){
		this.sorted_word = new String( a);
		this.words = new HashMap <String,Integer>();
		this.words.put(b,1);
	}
	
	public void add(String b){
		if (!this.words.containsKey(b))
			this.words.put(b,1);	
	}
}