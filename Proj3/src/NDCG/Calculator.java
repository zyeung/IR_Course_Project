package NDCG;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculator {
	public Calculator(){};
	
	public double calculate(List<String> googleList, List<String> ourList){
		Map<String, Integer> ranks = new HashMap<String, Integer>();
		int rank = 5;
		for(String url : googleList){
			ranks.put(url, rank--);
		}
		
		double IDCG = 5.0;
		for(int i = 2; i < 6; i++){
			double tmp = (double)(6 - i)/(Math.log(i)/Math.log(2));
			IDCG += tmp;
		}
		System.out.println("IDCG:" + IDCG);
		
		System.out.println("here:" + ourList.get(0) + " : " + ranks.size());
		double DCG = (double)(ranks.containsKey(ourList.get(0))?ranks.get(ourList.get(0)):0);
		for(int i = 2; i <= 5; i++){
			double r = ranks.containsKey(ourList.get(i-1))?ranks.get(ourList.get(i - 1)):0;
			double tmp = r/(Math.log(i)/Math.log(2));
			DCG += tmp;
		}
		System.out.println("DCG: " + DCG);
		
		return DCG/IDCG;
	}
}
