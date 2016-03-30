package Pack_Storage;

import java.io.*;
import java.util.*;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

public class DB_Storage implements IDB_Storage {
	private RecordManager database;
	private PrimaryHashMap<String, String> map;
	private long count;
	
	
	public DB_Storage(String path){
		ini(path);
	}
	
	private void ini(String path){
		try {
			this.database = RecordManagerFactory.createRecordManager(path);
			this.map = this.database.hashMap("map_on_disk"); 
			this.count=this.map.size();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	@Override
	public void storeDoc(String url, String text) {
		
		this.map.put(url, text);
		count++;
		
		if (count % 50 == 0) {
			try {
				this.database.commit();
				System.out.println("Committed " + count);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
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
	
	@Override
	public String loadDoc(String url) {
		return this.map.get(url);
	}
	
	@Override
	public Iterable<String> getAllCrawledUrls() {
		return this.map.keySet();
	}
	
}
