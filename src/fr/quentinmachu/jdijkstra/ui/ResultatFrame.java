package fr.quentinmachu.jdijkstra.ui;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This file is a part of the JDijkstra Software
 * 
 * @author quentinmachu http://quentin-machu.fr
 *
 */
public class ResultatFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	JTextArea text;
	
	public ResultatFrame(String res) {
		setTitle("--- Search result ---");
		setSize(400, 350);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());	
		setLocationRelativeTo(null);
		
		text = new JTextArea(res);
		text.setEditable(false);
		//text.setLineWrap(true);
		//text.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(text);
		
		add(scroll);
		
		setVisible(true);
	}
}
