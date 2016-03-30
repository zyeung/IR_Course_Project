package pack_Crawl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class OurCrawler extends WebCrawler{
	private final static Pattern FILTER = Pattern.compile(".*(\\.(php|css|js|bmp|gif|jpe?g|png|mid|mp2|mp3|mp4|wav|avi|mov"
			+ "|mpeg|rmvb|iso|torrent|m4v|pdf|rm|wmv|swf|wma|zip|rar|gz|h|o))$");
	//http://2cto.com/Article/201511/450527.html
	
	private final static Pattern CFILTER = Pattern.compile(".*\\events/\\d+");	
	private URLSet urlSet = new URLSet();
	private ExtraParams extra;
	
	//private final String PATH = "LocalData/";

	@Override
	public void onStart() {
		// Get the extra parameters
		extra = (ExtraParams) myController.getCustomData();
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL weburl) {

		// Only focus on HTML pages
		//Transfer: http://www.ics.uci.edu/PAGE.PHP ->  http://www.ics.uci.edu/page.php
		String url = weburl.getURL().toLowerCase(); 
		//use filter to check the site
		if(FILTER.matcher(url).matches()) return false;

		
		//check if site is subdomain of SEED
		String currentUrlDomain = URLAnalyser.getDomain(weburl.getURL());
		String seedUrlDomain = URLAnalyser.getDomain(extra.getSeed());
		//if not belong to the domain of seed, not crawl
		if (currentUrlDomain == null || !currentUrlDomain.endsWith(seedUrlDomain)) return false;
		
		//check if crawled before
		if(urlSet.URLCrawled(weburl.getURL())) return false;
		
		//check if it's events site
		String path = weburl.getPath();
		if(CFILTER.matcher(path).matches()) return false;
		
		//check if infinite
		if(!urlSet.checkInfinite(weburl.getURL())) return false;
		
		return true;
	}

	@Override
	public void visit(Page page) {
		
		//add url to crawled set and display crawling info
		String url = page.getWebURL().getURL();
		urlSet.addURL(url);
		System.out.println("Now Crawling: " + url);

		//Fetch HTML content from page and store them
		if (page.getParseData() instanceof HtmlParseData) { //we only need HTML data
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String htmlContent = htmlParseData.getHtml();
			
			//keep documents that are below 2M
			if (htmlContent.length() <= 2097152) {
				//Store the HTML Content
				extra.getIDB().storeDoc(url, htmlContent);
				/*url = url.replace(" ", "%20").replace("/", "%2F").replace(":", "%3A");
				
				String filePath = PATH+url;
				//write html content into file
				File file = new File(filePath);
				if(!file.exists()){
					try{
						file.createNewFile();
					}catch(IOException e){
						System.out.println("In Visit: "+ e.getMessage());
					}
					try {
					  FileWriter fw = new FileWriter(file, true);
					  BufferedWriter bw = new BufferedWriter(fw);
					  bw.write(htmlContent);
					  bw.flush();
					  bw.close();
					  fw.close();
					} catch (IOException e) {
					   e.printStackTrace();
					}*/
				
			}
		}
	}

	@Override
	public Object getMyLocalData() {
		return urlSet.getMyLocalData();
	}
}
