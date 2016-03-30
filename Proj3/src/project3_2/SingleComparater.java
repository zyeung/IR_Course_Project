package project3_2;

import java.util.Comparator;

import IdxStorage.*;

public class SingleComparater implements Comparator<TokenPos> {
	    @Override
	    public int compare(TokenPos o1, TokenPos o2) {
	        if (o1.tfscore >o2.tfscore) return -1;
	        else return o1.tfscore==o2.tfscore?0:1;
	    }
}

