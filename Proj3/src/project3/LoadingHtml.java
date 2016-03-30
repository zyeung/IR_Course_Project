package project3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

public class LoadingHtml {
	
	private static RecordManager rmDoc;
	private static RecordManager rmTokenDoc;
	private static PrimaryHashMap<String, String> databaseMap;
	private static PrimaryHashMap<String, ArrayList<String>> tokenDocMap;
	
	public static PrimaryHashMap<String, ArrayList<String>> loadFile(String storagePath)
	{
		try {
			// Initialize a file-based hash map (using jdbm2 [https://code.google.com/p/jdbm2/]) to store the visited pages
			rmDoc = RecordManagerFactory.createRecordManager(storagePath);
			//docStorage is the "table" name
			System.out.println("stated loading..." );
			databaseMap = rmDoc.hashMap("docStorage"); 
			System.out.println("loading finished...size "+ databaseMap.size() );
			
			
			rmTokenDoc = RecordManagerFactory.createRecordManager("tokenDocMap");
			
			// tokenDocMap is the table name
			tokenDocMap = rmTokenDoc.hashMap("tokenDocMap"); 
			tokenDocMap.clear();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		Iterator<Entry<String, String>> iterator = databaseMap.entrySet().iterator();
		int oc = 0;
		HashSet<String> uniquePages = new HashSet<String>();
		while(iterator.hasNext())
		{
			Entry<String, String> entry = iterator.next();
			String url = entry.getKey();
			
			// only need unique pages
			String urlWithoutQuery = URLHandler.removeQuery(url);
			if (urlWithoutQuery != null && urlWithoutQuery.length() > 0) {
				if(!uniquePages.add(urlWithoutQuery)) continue;
			}
			
			String htmlContent = entry.getValue();
			
			//String htmlContent = entry.getValue();
			
			HtmlDoc testDoc = new HtmlDoc(url, htmlContent);
			ArrayList<String> tokenizedContent = tokenize(testDoc.getAllText());
			tokenDocMap.put(url, tokenizedContent);
			//oc ++;
			if (oc%20==0){
				System.out.println("tokenized"+oc );
				try {
					rmTokenDoc.commit();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			oc ++;
		}
		
		try {
			rmTokenDoc.commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tokenDocMap;

	}
	
	private static ArrayList<String> tokenize(String input) 
	{
		ArrayList<String> tokenize = new ArrayList<String>();
		if (input == null)
			return tokenize;

		input = input.toLowerCase();
		String[] parts = input.split("[^a-zA-Z0-9'-]+"); // alphanumeric words 
		return new ArrayList<String>(Arrays.asList(parts));
	}
	
	
}
