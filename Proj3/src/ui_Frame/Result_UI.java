package ui_Frame;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import NDCG.Calculator;
import NDCG.Fetcher;


public class Result_UI extends JFrame implements MouseListener {
	
	public Result_UI(Map<String, String> resultMap, String query){
		System.out.println("map:" + resultMap.size());

		this.setBounds(350, 150, 600, 500);
		
		JPanel jp = new JPanel(new GridLayout(20,1,2,0));//new FlowLayout(FlowLayout.LEFT, 1, 1))
		jp.setBorder(BorderFactory.createEmptyBorder(10,10,0,0));
		
		//add content to the UI
		List<String> ourList = new ArrayList<String>();

		int counter = 0;
		for(String url : resultMap.keySet() ){
			if (counter>4) break;
			ourList.add(url);
			
			JLabel urlLabel = new JLabel("<HTML><U>" + url + "</U></HTML>");
			urlLabel.setForeground(Color.blue);
			urlLabel.setFont(new Font("Times New Roman", 0, 24));
			urlLabel.addMouseListener(this);
			
			JTextArea snippetText = new JTextArea(resultMap.get(url));
			snippetText.setLineWrap(true);
			snippetText.setOpaque(false);
			
			jp.add(urlLabel);
			jp.add(snippetText);
			counter++;
		}
		
		Fetcher fetcher = new Fetcher();
		List<String> googleList = fetcher.fetch(query);

		System.out.println(googleList.toString());
		System.out.println("our:" + ourList.size() + " google:" + googleList.size());
		Calculator cal = new Calculator();
		double ndcg = cal.calculate(googleList, ourList);
		System.out.println("NDCG:" + ndcg);

		
		//if no result
		if(resultMap.isEmpty()){
			JTextArea empty = new JTextArea("According to related laws, your search result is forbidden!");
			empty.setForeground(Color.red);
			empty.setFont(new Font("Times New Roman", 1, 29));
			empty.setLineWrap(true);
			empty.setOpaque(false);
			
			jp.add(empty);
		}
		JScrollPane jsp = new JScrollPane();
		jsp.setViewportView(jp);

		jsp.setPreferredSize(new Dimension(490,500));
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.getContentPane().add(jsp);
		
		setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	@Override
    public void mouseClicked(MouseEvent e) {
		JLabel jl = (JLabel)e.getSource();
		Desktop desktop = Desktop.getDesktop();
		try{
			String url = jl.getText();
			desktop.browse(new URI(url.substring(9, url.length() - 11)));
		}catch(Exception e1){
			System.out.println(e1.getMessage());
		}

    }
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
