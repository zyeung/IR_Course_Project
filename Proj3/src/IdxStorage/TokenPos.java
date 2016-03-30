package IdxStorage;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import jdbm.RecordManagerFactory;

public class TokenPos implements Serializable {
	public String url;
	public ArrayList<Integer> pos;
	public double tfscore;
	//static final long serialVersionUID = 120611757968066483；
	
	public TokenPos(String url, int inipos){
		this.url=new String(url);
		this.pos = new ArrayList<Integer>();
		this.pos.add(inipos);
		this.tfscore = 0;
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
