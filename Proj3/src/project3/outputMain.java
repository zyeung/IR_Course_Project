package project3;

public class outputMain {
	
	private final static String PATH = "test_output_idx.txt";
	
	public static void main(String[] args){
		//load database and print it into file
		outPut op = new outPut("indexedDB");
		op.printResult();
		
		System.out.println("Output index complete!");
	}
}
