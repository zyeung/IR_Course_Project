package newDatabase;

public class newMain {
	public static void main(String[] args){

		baseHandle bh = new baseHandle("indexedDB");
		
		bh.caculateTFScore();
		bh.close();
		
		System.out.println("Complete!");
	}
}
