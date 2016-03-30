package NDCG;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Fetcher {
	private final static Pattern FILTER = Pattern.compile(".*(\\.(php|css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov"
			+ "|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz|ico|pfm|c|h|o))$");
	private final String search = "https://www.google.com/search?q=QUERY&hl=en&start=START&btnG=Google+Search&gbv=1";
	
	public Fetcher(){}
	
	public List<String> fetch(String query){
		List<String> urls = new ArrayList<String>();
		query = query.trim();

		
		try{
			query = URLEncoder.encode("site:ics.uci.edu " + query, "UTF-8");
			
			
			String target = search.replace("QUERY", query); target = target.replace("START", "0");
			System.out.println("target: " + target);
			
			
	        int count = 0;
			
	        while(urls.size() < 5){
	        	Document doc = Jsoup.connect(target).userAgent("Mozilla/6.0 (Windows NT 6.2; WOW64; rv:16.0.1) Gecko/20121011 Firefox/16.0.1").get();
	        	Elements es = doc.select("h3[class = r] a[href]");
	        	for(Element e : es){
	        		//System.out.println("here" + e.attr("href"));
	        		Matcher m = Pattern.compile("(http:.*)&sa=").matcher(e.attr("href"));
	        		if(m.find()) {
	        			String url = m.group(1);
	        			//dig out non-html
	        			if (FILTER.matcher(url).matches()) continue;
					
	        			urls.add(m.group(1));
	        			if(++count == 5) break;
	        		}
	        	}
	        	target = search.replace("QUERY", query);
	        	target = target.replace("START", "10");
	        	//System.out.println("target here:" + target);
	        }
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		
		
		return urls;
	}
}
