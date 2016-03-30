package ui_Frame;

import java.util.ArrayList;
import java.util.List;

import NDCG.Calculator;
import NDCG.Fetcher;

public class NDGG_Main {
	public static void main(String[] args){
		Fetcher fetcher = new Fetcher();
		List<String> googleList = fetcher.fetch("security");
		System.out.println(googleList.toString());
		List<String> ourList = new ArrayList<String>();
		ourList.add("http://mondego.ics.uci.edu/");
		ourList.add("http://nile.ics.uci.edu:9000/wifi/user/account/");
		ourList.add("http://nile.ics.uci.edu:9000/wifi/admin/estates/new");
		ourList.add("http://nile.ics.uci.edu:9000/wifi");
		ourList.add("http://nile.ics.uci.edu:9000/wifi/forgotpassword");
		
		Calculator cal = new Calculator();
		double ndcg = cal.calculate(googleList, ourList);
		System.out.println("NDCG:" + ndcg);
	}
}
