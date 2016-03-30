package part_a;

import java.io.*;
import java.util.*;


public class Utilities{
	
	public ArrayList<String> tokenizeFile(File inputText) throws IOException{
		String flags = "[^0-9a-zA-Z']+";
		if (inputText == null)
			return null;
		
		ArrayList<String> tokenize = new ArrayList<String>();
		
		
		FileInputStream fstream = new FileInputStream(inputText);
		BufferedReader buffer_reader = new BufferedReader(new InputStreamReader(fstream));
		
		String line;
		line=buffer_reader.readLine();
		
		while ( line!= null){
			if (line.trim().length()!=0){
				String[] temp_line;
				line = line.toLowerCase();
				temp_line = line.split(flags);
				
				for (int i=0; i<temp_line.length; i++){
					if (!temp_line[i].isEmpty())
						tokenize.add(temp_line[i]);
				}
			}
			
			line=buffer_reader.readLine();
		}
		buffer_reader.close();
		fstream.close();
		
		
		return tokenize;
	}
	
	
	public void print(ArrayList<String> tokens){
		try {
			PrintWriter writer = new PrintWriter("/Users/hao/Desktop/221/testing/part_a.txt","UTF-8");
			for (int i=0; i<tokens.size(); i++){
				writer.println(tokens.get(i));
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
