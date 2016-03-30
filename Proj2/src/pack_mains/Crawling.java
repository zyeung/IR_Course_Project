package pack_mains;

import java.util.Set;

import pack_Crawl.CrawlHandler;

public class Crawling {
	
	public static void main(String[] args){
		try{
			System.out.println("Start Crawling!");
			CrawlHandler ch = new CrawlHandler("http://www.ics.uci.edu");
			
			Set<String> result = ch.crawl();
			
			System.out.println("Crawling Complete, total size is:" + result.size());
		}catch( Exception e ){
			System.out.println(e.getMessage());
		}
	}
}
