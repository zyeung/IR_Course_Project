package pack_Crawl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import Pack_Storage.DB_Storage;

public class CrawlHandler {
	private String SEED;
	
	public CrawlHandler(String seed){
		this.SEED = seed;
	}
	
	public Set<String> crawl(){
		Set<String> UrlCrawled = new HashSet<String>();

		try {
			// Setup the crawler configuration
			//from edu
			CrawlConfig crawlConfig = new CrawlConfig();
			crawlConfig.setUserAgentString("IR W16 WebCrawler 22081516 84561910 67448368");
			crawlConfig.setPolitenessDelay(300);
			crawlConfig.setCrawlStorageFolder("/Users/hao/Desktop/221/DocStorage1");
			crawlConfig.setMaxDepthOfCrawling(25);
			crawlConfig.setMaxPagesToFetch(-1);
			crawlConfig.setResumableCrawling(true);
			
			//set up extra information
			DB_Storage db_s = new DB_Storage("/Users/hao/Desktop/221/DocStorage1");
			ExtraParams extra = new ExtraParams(SEED, db_s);

			// Instantiate controller
			//from edu
			PageFetcher pageFetcher = new PageFetcher(crawlConfig); 
			RobotstxtConfig robotsTxtConfig = new RobotstxtConfig();
			RobotstxtServer robotsTxtServer = new RobotstxtServer(robotsTxtConfig, pageFetcher);
			CrawlController controller = new CrawlController(crawlConfig, pageFetcher, robotsTxtServer);

			controller.addSeed(SEED);
			controller.setCustomData(extra);//
			
			// Start crawling
			controller.start(OurCrawler.class, 6); //6 threads

			// Get list of crawled URLs for each crawler
			List<Object> crawlingData = controller.getCrawlersLocalData();
			
			
			System.out.println("Almost Done");

			for (Object data : crawlingData) {
				Set<String> set = (Set<String>) data;
				UrlCrawled.addAll(set);
			}
		}
		catch (Exception e) {
			System.out.println("Error in crawl: " + e.getMessage());			
		}

		return UrlCrawled;
		
	}
	
}
