package part_d;

import java.util.*;

public class Anagram {
	private String token;
	private ArrayList<String> anagram_list;
	
	public Anagram(String word){
		this.token = word;
		this.anagram_list = new ArrayList<String>();
	}
	
	public Anagram(String word, ArrayList<String> anagram_list){
		this.token = word;
		this.anagram_list = anagram_list;
	}
	
	
	public String printToken(){
		return this.token;
	}
	
	public ArrayList<String> returnAnaList(){
		return this.anagram_list;
	}
	
}
