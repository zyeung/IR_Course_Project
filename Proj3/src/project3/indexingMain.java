package project3;


public class indexingMain {
	
	
	public static void main(String[] args){
		//final String storagePath = "/Users/hao/Desktop/221/";
		String OriginalDBPath = "/Users/hao/Desktop/221/docStorage/docStorage";
		
		LoadingHtml temp  = new LoadingHtml();
		//temp.loadFile(OriginalDBPath);
		System.out.println("loading finish");
		//load database and print it into file
		indexingHandler ih = new indexingHandler("tokenDocMap");
		ih.indexingOperation();
	}
}
