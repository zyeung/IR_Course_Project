package pack_Crawl;
import Pack_Storage.IDB_Storage;

//import Pack_Storage.IDB_Storage;

public class ExtraParams {
	private String seed;
	private IDB_Storage idb_s;
	
	public ExtraParams(String seed, IDB_Storage idb_s){
		this.seed = seed;
		this.idb_s = idb_s;
	}
	
	public String getSeed(){
		return seed;
	}
	public IDB_Storage getIDB(){
		return idb_s;
	}
}
