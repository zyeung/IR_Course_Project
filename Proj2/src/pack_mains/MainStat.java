package pack_mains;
import webStat.*;

import java.io.IOException;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
public class MainStat {
	
	private static RecordManager rm;
	private static PrimaryHashMap<String, String> databaseMap;
	
	public static void main (String arg[])
	{
		String storagePath = "/Users/hao/Desktop/221/DocStorage1";
		
		try {
			rm = RecordManagerFactory.createRecordManager(storagePath);
			//docStorage is the "table" name
			databaseMap = rm.hashMap("map_on_disk"); 
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		System.out.println(databaseMap.size());
		
		// reading from database
		URLStat urlStat = new URLStat();
		urlStat.runStats(rm, databaseMap);
		
		TxtStat txtStat = new TxtStat();
		txtStat.runStats(rm, databaseMap);
		
		
		// output statistical results
		System.out.println("Number of total pages: ");
		System.out.println(urlStat.getTotalPages());
		System.out.println("Number of unique pages: ");
		System.out.println(urlStat.getTotalUniquePages());
		System.out.println("Subdomains:");
		for (Frequency s : urlStat.getSubdomainFrequencies()){System.out.println(s);}
		
		System.out.println("500 most commmon words: ");
		for(int i = 0; i<500; i++)
			System.out.println(txtStat.getMostCommonWords().get(i));
		System.out.println("Url of longest page: ");
		System.out.println(txtStat.getLongestDocumentUrl());
		System.out.println("20 most common 2-grams: ");
		for(int i = 0; i < 20; i++)
			System.out.println(txtStat.getMostCommonTwoGrams().get(i));
		System.out.println("20 most common triGrams:" );
		for(int i = 0; i < 20; i++)
			System.out.println(txtStat.getMostCommonTriGrams().get(i));

	}
	

}
