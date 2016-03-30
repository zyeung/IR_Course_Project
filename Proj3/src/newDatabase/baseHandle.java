package newDatabase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import IdxStorage.TokenPos;
import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

public class baseHandle {
	private RecordManager database, oridatabase, finaldatabase;
	private PrimaryHashMap<String, ArrayList<TokenPos>> databaseMap;
	private PrimaryHashMap<String, String> originalMap;
	private PrimaryHashMap<String, ArrayList<TokenPos>> finalMap;
	private HashMap<String, Double> PageRank;
	
	private final int SIZE = 81242;
	
	
	
	
	// constructor constructor constructor 
	// constructor constructor constructor 
	// constructor constructor constructor
	
	public baseHandle(String path){
		try {
			// Initialize a file-based hash map (using jdbm2 [https://code.google.com/p/jdbm2/]) to store the visited pages
			this.database = RecordManagerFactory.createRecordManager(path);
			this.oridatabase = RecordManagerFactory.createRecordManager("/Users/hao/Desktop/221/docStorage/docStorage");
			this.finaldatabase = RecordManagerFactory.createRecordManager("completeIndex");
			
			this.databaseMap = this.database.hashMap("map_on_disk"); //docStorage is the "table" name
			this.originalMap = this.oridatabase.hashMap("docStorage"); //docStorage is the "table" name
			this.finalMap = this.finaldatabase.hashMap("completeIndex"); //docStorage is the "table" name
			//this.size = databaseMap.size();
			System.out.println("Idx DB size" + databaseMap.size());
			
			//System.out.println("contains: " + originalMap.containsKey("http://ccsw.ics.uci.edu/15/speakers.html"));

			PageRank = new HashMap<String, Double>();
			
			try (BufferedReader br = new BufferedReader(new FileReader("pgRank.txt"))) {
			    String line;
			    int counter = 0;
			    while ((line = br.readLine()) != null) {
			       // process the line.
			    	line=line.trim();
			    	String line2 = br.readLine().trim();
			    	Double tempd = Double.parseDouble(line2);
			    	PageRank.put(line, tempd);
			    	counter ++;
			    }
			}
			
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void caculateTFScore(){
		Iterator<Entry<String, ArrayList<TokenPos>>> iterator = databaseMap.entrySet().iterator();
		
		int oc =0;
		while(iterator.hasNext()){
			Entry<String, ArrayList<TokenPos>> entry = iterator.next();
			int df = entry.getValue().size();
			ArrayList<TokenPos> list= entry.getValue();
			
			ArrayList<String> urllen = new ArrayList<String>();
			for (TokenPos tp : list){
				urllen.add(tp.url);
			}
			StrLenComp c = new StrLenComp("");
			java.util.Collections.sort(urllen,c );
			
			
			
			for (TokenPos tp : list){
				double addon = helper(entry.getKey(), tp.getUrl());
				
				
				if (urllen.indexOf(tp.getUrl())<5){
					addon+=5;
				}
				
				if (PageRank.containsKey(entry.getKey())){
					addon += PageRank.get(entry.getKey())*14.55432;
				}else{
					addon += 1*14.55432;
				}
				
				int tf = tp.pos.size();
				double score  = (Math.log10(1+tf))*Math.log10((double)SIZE/df);
				tp.tfscore = 7.263523*score + addon;
			}
			
			
			///
			
			this.finalMap.put(entry.getKey(), list);
			
			oc ++;
			if (oc %2000==00){
				
				System.out.println("caculated "+ oc);
				try {
					this.finaldatabase.commit();
					System.out.println("caculated "+ oc);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//System.gc();
				if (oc %10000 ==0)
				{
					System.gc();
				}
			}
			
		}
		try {
			this.finaldatabase.commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//this.database.clearCache();
		//this.database.storeMap("map_on_disk");
		System.out.println("unique words: " +oc );
	}
	
	public void close(){
		try{
			this.database.close();
			this.oridatabase.close();
			this.finaldatabase.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public double helper(String token, String url){
		double addon = 0;

		//token="*"+token+"*";
		
		String html = originalMap.get(url);
		
		// caculating header scores:
		
		Document doc = Jsoup.parse(html);
		String title = doc.title();
		Matcher m = Pattern.compile(token).matcher(title);
		if(m.find()) addon += 3.325386*(60/(url.length()>21?url.length()-20:1));
		
		Elements ele = doc.select("h0, h1, h2, h3, h4, h5, h6");
		for(int i = 1; i <= 6; i++){
			String tag = "h" + i;
			Elements h = ele.select(tag);
			if(!h.isEmpty()){
				Matcher mt = Pattern.compile(token).matcher(h.text());
				if(mt.find()){ addon += (7 - i)*0.684352;
				}
			}
		}
		//System.out.println("addon:" + addon);
		
		
		
		Matcher mt2 = Pattern.compile(token).matcher(url);
		if (mt2.find()){ 
			addon+=6.12343*(60/(url.length()>21?url.length()-20:1));
		}
		return addon;
	}
	
}
