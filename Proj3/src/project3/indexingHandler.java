package project3;

import IdxStorage.*;

import java.io.IOException;
import java.util.List;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import java.util.*;

public class indexingHandler {
	private RecordManager database;
	private PrimaryHashMap<String, List<String>> databaseMap;
	private IdxDB idb;
	
	public indexingHandler(String path){
		try {
			this.database = RecordManagerFactory.createRecordManager("tokenDocMap");
			this.databaseMap = this.database.hashMap("tokenDocMap"); //docStorage is the "table" name
			this.idb = new IdxDB("indexedDB");
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void indexingOperation(){
		int oc = 0;
		for(String url : databaseMap.keySet()){
			List<String> tokens = databaseMap.get(url);
			HashMap<String,List<Integer>> tempMap = new HashMap<String,List<Integer>>();
			for(int pos = 0; pos < tokens.size(); pos++){
				if(!StopWord.isStopWord(tokens.get(pos))){
					
					if (tempMap.containsKey(tokens.get(pos))){
						tempMap.get(tokens.get(pos)).add(pos);
						continue;
					}
					
					ArrayList<Integer> tal = new ArrayList<Integer>();
					tal.add(pos);
					tempMap.put(tokens.get(pos), tal);
					//idb.add(tokens.get(pos), url, pos);	
				}
			}
			
			Iterator it = tempMap.entrySet().iterator();
			while (it.hasNext()){
				HashMap.Entry pair = (HashMap.Entry)it.next();
				
				TokenPos tempTP= new TokenPos(url,0);
				tempTP.pos = (ArrayList)pair.getValue();				
				tempTP.tfscore = 0;
				
				idb.add((String)pair.getKey(), tempTP);
			}
			
			
			//System.gc();
			oc ++;
			if (oc%200==0){
				System.out.println("indexed"+oc );
				if (oc%4000==0){
					System.gc();
				}
			}
			
		}
		
		idb.close();
	}
}
