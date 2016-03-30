package newDatabase;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import jdbm.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import IdxStorage.TokenPos;

public class PageRank {
	//private RecordManager dbpgrk;
	public HashMap<String, ArrayList<String>> out = new HashMap<String, ArrayList<String>>();
	public HashMap<String, ArrayList<String>> in = new HashMap<String, ArrayList<String>>();
	public HashMap<String, Double> pageRank = new HashMap<String, Double>();
	public final Double w=0.85;
	
	public PageRank(PrimaryHashMap<String,String> DocMap){
		Iterator<Entry<String, String>> iterator = DocMap.entrySet().iterator();
		Set<String> remove = new HashSet<String>();
		while (iterator.hasNext()){
			
			Entry<String,String> et = iterator.next();
			
			URI uri;
			String temp = new String();
			try {
				uri = new URI(et.getKey().replace(" ", "%20"));
				temp = uri.getScheme() + "://" + uri.getAuthority()+uri.getPath();
				
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (temp.isEmpty()||remove.contains(temp))
			{
				continue;
			}
			pageRank.put(et.getKey(), 1.0);
			remove.add(temp);
			
			Document doc = Jsoup.parse(et.getValue());
			//.getClString title = doc.title();
			ArrayList<String> outlist = new ArrayList<String>();
			Elements links = doc.select("a[href]");
			for (Element link : links){
				outlist.add(link.attr("abs:href"));
			}
			
			if (!outlist.isEmpty()&&!et.getKey().isEmpty()){
				out.put (et.getKey(), outlist);
			}
		}
		System.out.println("out finished"+Integer.toString(out.size())+" "+Integer.toString(pageRank.size()));
		
		Iterator<Entry<String,ArrayList<String>>> it = out.entrySet().iterator();
		while (it.hasNext()){
			Entry<String,ArrayList<String>> entry = it.next();
			
			if (!DocMap.containsKey(entry.getKey()))
			{
				continue;
			}
			
			//if (entry.getKey())
			
			for (String link:entry.getValue()){
				if (in.containsKey(link)){
					in.get(link).add(entry.getKey());
				}
				else
				{
					ArrayList<String> newin = new ArrayList<String>();
					newin.add(entry.getKey());
					in.put(link, newin);
				}
			}
		}
		System.out.println("in finished" + in.size());
		
		
		
		int counter =1;
		while (counter <40000){
			System.out.println("it "+counter);
			Iterator<Entry<String,Double>> itrk = pageRank.entrySet().iterator();
			//int itsize = 0;
			while (itrk.hasNext()){
				Entry<String,Double> erk= itrk.next();
				double rk = 1-w;
				
				if ( !in.containsKey(erk.getKey()) || in.get(erk.getKey()).isEmpty())
				{	
					continue;
				}
				
				for (String inlink : in.get(erk.getKey())){
					rk+=w*(pageRank.get(inlink)/out.get(inlink).size());
				}
				
				pageRank.put(erk.getKey(), 1+rk);
				//itsize++;
			}
			//System.out.print(Integer.toString(itsize));
			counter ++;
		}
		System.out.println("finished PageRank");
	}
	
	public void print(){
		Iterator<Entry<String,Double>> itrk = pageRank.entrySet().iterator();
		PrintWriter writer;
		try {
			writer = new PrintWriter("pgRank.txt", "UTF-8");
		
		while (itrk.hasNext()){
			Entry<String,Double> erk= itrk.next();
			// print to file:
			
			writer.println(erk.getKey());
			writer.println(Double.toString(erk.getValue()));
		}
		
		writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("finished");
		
		
	}
}
