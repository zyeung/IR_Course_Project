package ui_Frame;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import NDCG.Calculator;
import NDCG.Fetcher;
import project3_2.finder;

public class Main_UI extends JFrame implements ActionListener{
	JTextField jtext;
	private finder finder;
	
	public Main_UI(){
		finder = new finder();

		this.setLayout(new FlowLayout());
		this.setTitle("CS221 Search Engine");
		this.setBounds(350, 150, 600, 150);
		JPanel jp1 = new JPanel();
		jp1.setLayout(new GridLayout(2,1));
		//jf.add(jp1);
		add(jp1);
		
		JLabel jl = new JLabel("ICSearch Engine");
		jl.setFont(new java.awt.Font("Apple Chancery", 1, 50));
		
		
		jp1.add(jl);
		
		JPanel jp2 = new JPanel();
		jp2.setLayout(new FlowLayout());
		
		jp1.add(jp2);
		
		jtext = new JTextField(33);
		//jtext.setBounds(10, 10, 20, 500);
		JButton jb = new JButton("click to search");
		jb.addActionListener(this);
		
		jp2.add(jtext); jp2.add(jb);
		
		this.setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	//do the action to find result in Database
	@Override
    public void actionPerformed(ActionEvent e) {
		String query = jtext.getText();
		if(query.length() > 0){
			LinkedHashMap<String, String> map = finder.find(query.trim().toLowerCase());
			//calculateNDCG(query, map);
			new Result_UI(map, query);
		}
	
    }
	
	//calculate NDCG
	public void calculateNDCG(String query, LinkedHashMap<String, String> map){

		
	}
	public static void main(String[] args){
		Main_UI mui = new Main_UI();
	}
}
