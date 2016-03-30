package webStat;

import webStat.StopWord;
import webStat.HtmlDoc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jdbm.PrimaryHashMap;
import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

public class TxtStat {
	private String longestDocumentUrl;
	private List<String> mostCommonWords;
	private List<String> mostCommonTwoGrams;
	private List<String> mostCommonTriGrams;

	public String getLongestDocumentUrl() {
		return this.longestDocumentUrl;
	}

	public List<String> getMostCommonWords() {
		return this.mostCommonWords;
	}

	public List<String> getMostCommonTwoGrams() {
		return this.mostCommonTwoGrams;
	}
	
	public List<String> getMostCommonTriGrams()
	{
		return this.mostCommonTriGrams;
	}
	public void runStats(RecordManager rm, PrimaryHashMap<String, String> DBMap) 
	{
		runStats(rm, DBMap, "stats/tokenFrequencies", "stats/twoGramFrequencies", "stat/triGramFrequencies");
	}
	
	public void runStats(RecordManager rm, PrimaryHashMap<String, String> DBMap, String tokenFilePath, String twoGramFilePath, String triGramFilePath) {
		RecordManager tokenManager = null;
		RecordManager twoGramManager = null;
		RecordManager triGramManager = null;
		
		try {
			// Initialize a file-based tree map (using jdbm2 [https://code.google.com/p/jdbm2/]) for tokens and 2-grams/trigrams .
						
			tokenManager = RecordManagerFactory.createRecordManager(tokenFilePath);
			twoGramManager = RecordManagerFactory.createRecordManager(twoGramFilePath);
			triGramManager = RecordManagerFactory.createRecordManager(triGramFilePath);

			PrimaryTreeMap<String, Integer> tokenMap = tokenManager.treeMap("tokenMap");
			PrimaryTreeMap<String, Integer> twoGramMap = twoGramManager.treeMap("twoGramMap");
			PrimaryTreeMap<String, Integer> triGramMap = triGramManager.treeMap("triGramMap");

			// Scan all the crawled documents
			long longestDocumentCount = 0;
			String longestDocumentUrl = "";
			long count = 0;
			
			HtmlDocIte htmlIte = new HtmlDocIte(DBMap.entrySet().iterator());
			
			for (HtmlDoc doc : htmlIte) {
				// Display number of processed documents to keep track of progress				
				if (count % 100 == 0)
					System.out.println(count);
				
				count++;

				// Get the tokens in the document
				ArrayList<String> tokens = tokenize(doc.getAllText());

				int tokenCount = 0;
				for (int i = 0; i < tokens.size(); i++) 
				{
					String currentToken = tokens.get(i);
					if (!isInterestingWord(currentToken))
						continue;

					// Keep track of token count (to find longest document)
					tokenCount++;

					// Keep track of token frequency
					increaseFrequency(tokenMap, currentToken);

					// Keep track of 2-gram frequency
					if (i + 1 < tokens.size())
					{
						String nextToken = tokens.get(i + 1);
						if (!isInterestingWord(nextToken))
							continue;

						String twoGram = currentToken + " " + nextToken;
						increaseFrequency(twoGramMap, twoGram);
						
						// triGram frequency
						if(i + 2 < tokens.size())
						{
							nextToken = tokens.get(i + 2);
							if(!isInterestingWord(nextToken))
								continue;
							
							String triGram = twoGram + " " + nextToken;
							increaseFrequency(triGramMap, triGram);
						}
					}
					
					
				}

				// Keep track of longest document
				if (tokenCount > longestDocumentCount) {
					longestDocumentCount = tokenCount;
					longestDocumentUrl = doc.getUrl();
				}
				
				if (count % 10000 == 0) {					
					tokenManager.commit();
					twoGramManager.commit();
					triGramManager.commit();
					System.out.println("Commit");
				}
			}

			// Commit any leftovers
			tokenManager.commit();
			twoGramManager.commit();
			triGramManager.commit();

			// Results
			this.longestDocumentUrl = longestDocumentUrl;
			this.mostCommonWords = getTopMostCommon(tokenMap, 500); // top 500 most common words
			this.mostCommonTwoGrams = getTopMostCommon(twoGramMap, 20); // top 20 most common twoGrams 
			this.mostCommonTriGrams = getTopMostCommon(triGramMap, 20); // top 20 most common triGrams
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		finally {
			// Close the file-backed tree maps
			try {
				if (tokenManager != null)
					tokenManager.close();
			}
			catch (IOException ex2) {
				ex2.printStackTrace();
			}
			
			try {
				if (twoGramManager != null)
					twoGramManager.close();
			}
			catch (IOException ex2) {
				ex2.printStackTrace();
			}		
		}
	}
	
	private List<String> getTopMostCommon(PrimaryTreeMap<String, Integer> map, int n) {
		// Get the top n most common tokens
		ArrayList<String> mostCommon = new ArrayList<String>();
		ArrayList<Frequency> freqs = new ArrayList<Frequency>();

		// Convert the dictionary to a frequency list
		for (String token : map.keySet()) {
			Frequency freq = new Frequency(token, map.get(token));
			freqs.add(freq);
		}

		// Sorting by frequency (descending) and break ties with alphabetical order (ascending)
		WordFrequencyComparator comparator = new WordFrequencyComparator();
		Collections.sort(freqs, comparator);

		// Keep only the top n (or the entire list if it's <= n)
		for (int i = 0; i < Math.min(n, freqs.size()); i++) {
			mostCommon.add(freqs.get(i).getText());
		}

		return mostCommon;
	}

	private void increaseFrequency(PrimaryTreeMap<String, Integer> map, String token) {
		Integer count = map.get(token);
		if (count == null)
			count = 0;
		map.put(token, count + 1);
	}

	private ArrayList<String> tokenize(String input) {
		ArrayList<String> tokenize = new ArrayList<String>();
		if (input == null)
			return tokenize;

		input = input.toLowerCase();
		String[] parts = input.split("[^a-zA-Z0-9'-]+"); // alphanumeric words 
		return new ArrayList<String>(Arrays.asList(parts));
	}

	private static Pattern REGEX_PATTERN = Pattern.compile(".*\\d.*"); // compile only use
	private static Matcher REGEX_MATCHER = REGEX_PATTERN.matcher("");

	private boolean isInterestingWord(String token) {
		return token.length() >= 4 && // filter out words shorter than 4 letters
				!REGEX_MATCHER.reset(token).matches() && // filter out words with numbers
				!StopWord.isStopWord(token); // filter out stop words
	}
	
	public class WordFrequencyComparator implements Comparator<Frequency>
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
