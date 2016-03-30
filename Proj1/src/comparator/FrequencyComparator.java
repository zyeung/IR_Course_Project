package comparator;

import part_b.Frequency;
import java.util.*;

public class FrequencyComparator implements Comparator<Frequency> {
	
	public int compare(Frequency a, Frequency b){
		return b.printFre()!=a.printFre()?b.printFre()-a.printFre():a.printToken().compareTo(b.printToken());
	}
}
