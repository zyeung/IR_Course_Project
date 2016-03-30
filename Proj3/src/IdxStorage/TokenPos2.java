package IdxStorage;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import jdbm.RecordManagerFactory;

public class TokenPos2 extends TokenPos implements Serializable {
	public String url;
	public ArrayList<Integer> pos;
	public double tfscore;
	public double title_score;
	public double page_rank;
	public double url_score;
	//static final long serialVersionUID = 120611757968066483；
	
	public TokenPos2(String url, int inipos){
		super(url,inipos);
	}
	
	public void add( int pos){
		this.pos.add(pos);
	}
	
	public String getUrl(){
		return this.url;
	}
	
	public ArrayList<Integer> getPosList(){
		return this.pos;
	}
	
}

