package project3;

import java.io.BufferedWriter;
import java.lang.*;
import java.math.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import IdxStorage.*;

public class outPut {
	private RecordManager database;
	private PrimaryHashMap<String, ArrayList<TokenPos>> databaseMap;
	private final String filePath = "test_output_idx.txt";
	private final int size = 81242;
	
	public outPut(String path){
		try {
			// Initialize a file-based hash map (using jdbm2 [https://code.google.com/p/jdbm2/]) to store the visited pages
			this.database = RecordManagerFactory.createRecordManager(path);
			this.databaseMap = this.database.hashMap("map_on_disk"); //docStorage is the "table" name
			//this.size = databaseMap.size();
			System.out.println("Idx DB size" + databaseMap.size());
			caculateTFScore();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printResult(){
		File file = new File(filePath);
		OutputStream os;
		try {
			os = new FileOutputStream(file);
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
		
		Iterator<Entry<String, ArrayList<TokenPos>>> iterator = databaseMap.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, ArrayList<TokenPos>> entry = iterator.next();
			
			String token = entry.getKey();
			List<TokenPos> list= entry.getValue();
			
			//write a token and its index into the file
			
				bw.write(token + ":(");
				bw.flush();
				
				for(TokenPos tp : list){
					String url = tp.getUrl();
					List<Integer> posList = tp.getPosList();
					bw.write(url + ": "+ tp.tfscore+ "; ");
					
					//write positions
					Iterator<Integer> it = posList.iterator();
					while(it.hasNext()){
						bw.write(it.next().toString());
						if(it.hasNext()) bw.write(",");
						else bw.write(";");
					}
					bw.flush();
				}
				
				bw.write(")\r\n");
				bw.flush();
			
		}
		bw.flush();
		this.database.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	public void caculateTFScore(){
		Iterator<Entry<String, ArrayList<TokenPos>>> iterator = databaseMap.entrySet().iterator();
		int oc =0;
		while(iterator.hasNext()){
			Entry<String, ArrayList<TokenPos>> entry = iterator.next();
			//int tf = 
			int df = entry.getValue().size();
			List<TokenPos> list= entry.getValue();
			
			//ArrayList<TokenPos> newList = new ArrayList<TokenPos>();
			for (TokenPos tp : list){
				
				int tf = tp.pos.size();
				double score  = (Math.log10(1+tf))*Math.log10((double)size/df);
				tp.tfscore = score;
				//newList.add(tp);
			}
			
			
			oc ++;
			if (oc %2000==00){
				
				System.out.println("caculated "+ oc);
			
				try {
					this.database.commit();
					System.out.println("caculated "+ oc);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		try {
			this.database.commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//this.database.clearCache();
		//this.database.storeMap("map_on_disk");
		System.out.println("unique words: " +oc );
		
	}
}
