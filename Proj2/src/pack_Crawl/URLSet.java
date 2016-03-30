package pack_Crawl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class URLSet {
	private Set<String>  crawledUrl = new HashSet<String>();
	private Map<String, Integer> visitCount = new HashMap<String, Integer>();
	private final int MAX_VISIT = 12;
	
	//add a new url is to be crawled
	public boolean addURL(String url){
		return crawledUrl.add(url);
	}
	
	//check if url is crawled
	public boolean URLCrawled(String url){
		return crawledUrl.contains(url);
	}
	
	//avoid infinite loop in one page
	public boolean checkInfinite(String url){
		String mainUrl = URLAnalyser.removeQuery(url);
		if( mainUrl == null ) return true;
		
		//get visit times of this url
		int count = visitCount.containsKey(mainUrl)?visitCount.get(mainUrl):1;
		//check if it's been visited too many times
		if( count >= MAX_VISIT ) return false;
		else{
			visitCount.put(mainUrl, count + 1);
			return true;
		}
	}
	
	//return local data
	public Object getMyLocalData(){
		return crawledUrl;
	}
}
