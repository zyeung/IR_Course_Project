package Pack_Storage;

public interface IDB_Storage {
	void storeDoc(String url, String text);
	
	
	void close();

	String loadDoc(String url);

	Iterable<String> getAllCrawledUrls();
}
