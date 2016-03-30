package part_b;

public class Frequency {
	private String token;
	private int frequency;
	
	public Frequency(String word){
		this.token = word;
		this.frequency = 0;
	}
	
	public Frequency(String word, int counter){
		this.token = word;
		this.frequency = counter;
	}
	
	public void recordFre(){
		this.frequency++;
	}
	
	public String printToken(){
		return this.token;
	}
	
	public int printFre(){
		return this.frequency;
	}
	
	public String printAll(){
		return this.token+" : "+this.frequency;
	}
	
}
