package project3_2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import IdxStorage.TokenPos;
import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

public class finder {
	private RecordManager database;
	private PrimaryHashMap<String, List<TokenPos>> databaseMap;
	
	private RecordManager filedatabase;
	private PrimaryHashMap<String, List<String>> filedatabaseMap;
	
	public finder(){
		try{
			// Initialize a file-based hash map (using jdbm2 [https://code.google.com/p/jdbm2/]) to store the visited pages
			this.database = RecordManagerFactory.createRecordManager("completeIndex");
			this.databaseMap = this.database.hashMap("completeIndex"); // is the "table" name
			
			this.filedatabase = RecordManagerFactory.createRecordManager("tokenDocMap");
			this.filedatabaseMap = this.filedatabase.hashMap("tokenDocMap");
			
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		System.out.println("finish loading "+ databaseMap.size());
		
		
		Iterator<TokenPos> testing= this.databaseMap.get("mondego").iterator();
		while (testing.hasNext()){
			TokenPos tpp = testing.next();
			System.out.println(tpp.url);
			System.out.println(tpp.tfscore);
		}
		
		//caculateTFScore();
		
	}
	
	public LinkedHashMap<String, String> find(String query){
		LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
		
		query=query.toLowerCase();
		String[] parts = query.split("[^a-zA-Z0-9'-]+");
		List<String> queryToken = (List<String>) Arrays.asList(parts);
		
		// for single:
		if (queryToken.size()<2)
		{
			if(databaseMap.containsKey(query)){
				List<TokenPos> tempList = databaseMap.get(query);
				tempList.sort(new SingleComparater());
				System.out.println("score:" + tempList.get(0).tfscore);
				for(TokenPos tp : tempList){
					String temp = prepareOP(tp);
					result.put(tp.getUrl(), Double.toString( tp.tfscore)+" "+temp);
				}
			}
		
		}// for multiple:
		else{
			//ArrayList<Double> qtvscore = new ArrayList<Double>();
			// caculating for query
			HashMap<String,Double> num = new HashMap<String,Double>();
			
			for (int i=0;i<queryToken.size(); i++){
				if (databaseMap.containsKey(queryToken.get(i))){
					List<TokenPos> tempList = databaseMap.get(queryToken.get(i));
					for (TokenPos tp:tempList){
						if (num.containsKey(tp.url))
						{	
							num.put(tp.url, num.get(tp.url)+tp.tfscore);
						
						}else{
							num.put(tp.url, tp.tfscore);
						}
					}
				}
				
			}
			
			int totalqt = num.size();
			
			//sort
			num = MapUtil.sortByValue( num );
			
			// output
			for(String tp : num.keySet()){
				String temp = prepareMultiOP(tp,queryToken);
				result.put(tp,Double.toString(num.get(tp))+temp);
			}			
						
		}
		
		return result;
	}
	
	
	public void caculateTFScore(){
		Iterator<Entry<String, List<TokenPos>>> iterator = databaseMap.entrySet().iterator();
		int oc =0;
		while(iterator.hasNext()){
			Entry<String, List<TokenPos>> entry = iterator.next();
			//int tf =
			int size = 81242;
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
		//System.out.println("unique words: " +oc );
		
	}
	
	public String prepareOP(TokenPos tp){
		String text = new String();
		
		int first_Pos = tp.pos.get(0);
		int total = tp.pos.size();
		
		text+="...";
		for (int i=(first_Pos>=10?(first_Pos-10):0);i<=first_Pos;i++ ){
			text+=this.filedatabaseMap.get(tp.url).get(i);
			text+=" ";
		}
		
		for (int i=first_Pos+1;i<Math.min((first_Pos+8),total);i++ ){
			text+=this.filedatabaseMap.get(tp.url).get(i);
			text+=" ";
		}
		
		if (tp.pos.size()>1 ){
			int second_Pos = tp.pos.get(1);
			//int total = tp.pos.size();
			
			text+="......";
			for (int i=(second_Pos>=10?(second_Pos-10):0);i<=second_Pos;i++ ){
				text+=this.filedatabaseMap.get(tp.url).get(i);
				text+=" ";
			}
			
			for (int i=second_Pos+1;i<Math.min((second_Pos+8),total);i++ ){
				text+=this.filedatabaseMap.get(tp.url).get(i);
				text+=" ";
			}
		}
		
		return text;
	}
	
	public String prepareMultiOP(String url,List<String> tokens){
		String text = new String("...");
		
		int j=0;
		while ( j<tokens.size() && ( !this.filedatabaseMap.containsKey(url) || !this.filedatabaseMap.get(url).contains(tokens.get(j)))){
			j++;
		}
		
		if (j>=tokens.size()){
			return url;
		}
		int first_Pos = this.filedatabaseMap.get(url).indexOf(tokens.get(j));
		int total = this.filedatabaseMap.get(url).size();
		
		for (int i=(first_Pos>=10?(first_Pos-10):0);i<=first_Pos;i++ ){
			text+=this.filedatabaseMap.get(url).get(i);
			text+=" ";
		}
		
		for (int i=first_Pos+1;i<Math.min((first_Pos+8),total);i++ ){
			text+=this.filedatabaseMap.get(url).get(i);
			text+=" ";
		}
		
		
		return text;
	}
	
}
