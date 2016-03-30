package IdxStorage;

import java.io.*;
import java.util.*;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

public class IdxDB {
	private RecordManager database;
	private PrimaryHashMap<String, ArrayList<TokenPos>> idx_map;
	private long count;
	
	public IdxDB(String path){
		ini(path);
	}
	
	private void ini(String path){
		try {
			this.database = RecordManagerFactory.createRecordManager(path);
			this.idx_map = this.database.hashMap("map_on_disk"); 
			idx_map.clear();
			this.count=this.idx_map.size();
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public void add(String token, TokenPos poslist){
		if (this.idx_map.containsKey(token))
		{
			/*
			//this.idx_map.get(token).add(url, pos);
			if (this.idx_map.get(token).get(this.idx_map.get(token).size()-1).url   ==url){
				//TokenPos temp = new TokenPos(null,0);
				//temp.url = this.idx_map.get(token).get(this.idx_map.get(token).size()-1).url;
				//temp.pos = this.idx_map.get(token).get(this.idx_map.get(token).size()-1).pos;
				//temp.pos.add(pos);
				this.idx_map.get(token).get(this.idx_map.get(token).size()-1).add(pos);
			}
			else{
				this.idx_map.get(token).add(new TokenPos(url,pos));
			}*/
			
			this.idx_map.get(token).add(poslist);
			
		}else{
			ArrayList<TokenPos> newTP = new ArrayList<TokenPos>();
			newTP.add( poslist);
			this.idx_map.put(token,newTP);
		}
		
		if (count % 500 == 0) {
			try {
				this.database.commit();
				System.out.println("Committed " + count);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		count++;
	}
	
	
	public void close() {
		// TODO Auto-generated method stub
		try {
			this.database.commit(); // commit any leftover pages
			this.database.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public Iterable<String> getAllTokens() {
		return this.idx_map.keySet();
	}
	
	
}
