package newDatabase;

import java.io.IOException;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

public class pgrankmain {
		
		

		public static void main(String[] args){
			RecordManager database;
			PrimaryHashMap<String,String> databaseMap;
			String path = "/Users/hao/Desktop/221/docStorage/docStorage";
			 
			try {
				database = RecordManagerFactory.createRecordManager(path);
			
			databaseMap = database.hashMap("docStorage"); 
			System.out.println("Idx DB size" + databaseMap.size());
			
			PageRank a = new PageRank(databaseMap);
			a.print();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}

}
