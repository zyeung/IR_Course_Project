package webStat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import jdbm.PrimaryHashMap;
import jdbm.RecordManager;
import webStat.URLHandler;
import webStat.Frequency;

public class URLStat 
{
	
	private long totalPages;
	private long totalUniquePages;
	private List<Frequency> subdomainFreq;

	public long getTotalPages() 
	{
		return this.totalPages;
	}

	public long getTotalUniquePages() 
	{
		return this.totalUniquePages;
	}

	public List<Frequency> getSubdomainFrequencies() 
	{
		return this.subdomainFreq;
	}

	public void runStats(RecordManager rm, PrimaryHashMap<String, String> DBMap) 
	{

		// Keep track of all pages and unique pages (don't count different URL queries as different pages)
		long totalCount = 0;
		HashSet<String> uniquePages = new HashSet<String>();
		for (String url : DBMap.keySet()) 
		{
			// Display number of processed documents to keep track of progress				
			if (totalCount % 100 == 0)
				System.out.println(totalCount);

			totalCount++;

			String urlWithoutQuery = URLHandler.removeQuery(url);
			if (urlWithoutQuery != null && urlWithoutQuery.length() > 0)
				uniquePages.add(urlWithoutQuery);
		}
			
		System.gc();
		// Count the number of unique subdomains
		HashMap<String, Integer> subdomainMap = new HashMap<String, Integer>();
		for (String uniqueUrl : uniquePages) 
		{
			String urlWithoutPath = URLHandler.removePath(uniqueUrl);
			if (urlWithoutPath == null)
				continue;

			// Increment count
			Integer currentCount = subdomainMap.get(urlWithoutPath);
			if (currentCount == null)
				currentCount = 0;
			subdomainMap.put(urlWithoutPath, currentCount + 1);
		}
		System.gc();

		//-- Convert to frequency list
		ArrayList<Frequency> frequencies = new ArrayList<Frequency>();
		for (String url : subdomainMap.keySet()) 
		{
			Frequency frequency = new Frequency(url, subdomainMap.get(url));
			frequencies.add(frequency);
		}
		System.gc();

		//-- Order by frequency (desc) and break ties with alphabetical order (asc)
		URLFrequencyComparator comparator = new URLFrequencyComparator();
		Collections.sort(frequencies, comparator);

		// Results
		this.totalPages = totalCount;
		this.totalUniquePages = uniquePages.size();
		this.subdomainFreq = frequencies;
	}
	
	
	public class URLFrequencyComparator implements Comparator<Frequency>
	{
	    
	    public int compare(Frequency x, Frequency y)
	    {
	    	// Order by frequency (descending)
	    	if (x.getFrequency() < y.getFrequency())
	    		return 1;
	    	else if (x.getFrequency() > y.getFrequency()) 
	    		return -1;
	    	else 
	    		// Alphabetical order for ties (ascending)
	    		return x.getText().compareTo(y.getText());
	    }
	}
	
}

