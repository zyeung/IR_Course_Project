package pack_Crawl;

import java.net.URI;
import java.net.URISyntaxException;

public class URLAnalyser {
	
	//return the domain of url
	public static String getDomain(String url){
		try{
			//replace space
			URI uri = new URI(url.replace(" ", "%20"));
			String host = uri.getHost();
			
			if(host != null) {
				host = host.toLowerCase();
				if (host.startsWith("www.")){
					host = host.substring(4);
				}

				return host;
			}
		}catch (Exception e) {
			System.out.println("Error in getDomain: " + e.getMessage());
			//e.printStackTrace();
		}

		return null;
	}
	
	//return url without path part
	public static String removePath(String url) {
		try {
			URI uri = new URI(url.replace(" ", "%20"));
			return uri.getScheme() + "://" + uri.getAuthority();
		}
		catch (URISyntaxException e) {
			System.out.println("Error in removePath: " + e.getMessage() + " at " + url);
		}

		return null;
	}
	
	//return url without Query part
	public static String removeQuery(String url) {
		try {
			URI uri = new URI(url.replace(" ", "%20"));
			return uri.getScheme() + "://" + uri.getAuthority() + uri.getPath();
		}
		catch (URISyntaxException e) {
			System.out.println("Error in removeQuery: "+ e.getMessage() + url);
		}

		return null;
	}
}
