package fr.quentinmachu.jdijkstra.ui;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.quentinmachu.jdijkstra.algorithms.SimulatedBiOjectiveDijkstra;
import fr.quentinmachu.jdijkstra.algorithms.BiOjectiveDijkstra;
import fr.quentinmachu.jdijkstra.algorithms.MonoObjectiveDijkstra;
import fr.quentinmachu.jdijkstra.entities.Graph;
import fr.quentinmachu.jdijkstra.entities.Link;
import fr.quentinmachu.jdijkstra.entities.Node;
import fr.quentinmachu.jdijkstra.entities.Result;

/**
 * This file is a part of the JDijkstra Software
 * <br/>
 * I have to admit that this GUI *sucks* a lot but I created it in 20min just to have one. Soz~
 * 
 * @author quentinmachu http://quentin-machu.fr
 *
 */
public class MainFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JTextField filepath_noeuds;
	private JTextField filepath_arcs;
	private JTextField depart;
	private JTextField arrive;
	
	private JRadioButton radio_dij_mono;
	private JRadioButton radio_dij_bi;
	private JRadioButton radio_dij_bis;
	
	private JSlider combine;
	private Component combine_space;
	
	private JButton generer;
	private ButtonGroup button_group;

	private JPanel panel;
	
	public MainFrame() {
		setTitle("--- Shortest path finding ---");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		
		panel = new JPanel();
		panel.setLayout(new GridLayout(8, 2));
		
		// Files
		filepath_noeuds = new JTextField("data/paris_noeuds.txt");
		filepath_arcs = new JTextField("data/paris_arcs.txt");
		JLabel filepath_noeuds_l = new JLabel("Nodes file: ");
		JLabel filepath_arcs_l = new JLabel("Links file: ");
		panel.add(filepath_noeuds_l);
		panel.add(filepath_noeuds);
		panel.add(filepath_arcs_l);
		panel.add(filepath_arcs);
		
		// Start/end nodes
		depart = new JTextField("480");
		arrive = new JTextField("581");
		JLabel depart_l = new JLabel("Start node indice: ");
		JLabel arrive_l = new JLabel("End node indice: ");
		panel.add(depart_l);
		panel.add(depart);
		panel.add(arrive_l);
		panel.add(arrive);
		
		// Boutons
		JLabel radio_l = new JLabel("Algorithm: ");
		panel.add(radio_l);
	    radio_dij_mono = new JRadioButton("Single-objective Dijkstra");
	    radio_dij_mono.setMnemonic(KeyEvent.VK_M);
	    radio_dij_mono.setSelected(true);
	    radio_dij_mono.addActionListener(this);

	    radio_dij_bi = new JRadioButton("Multi-criteria Dijkstra");
	    radio_dij_bi.setMnemonic(KeyEvent.VK_L);
	    radio_dij_bi.setSelected(false);
	    radio_dij_bi.addActionListener(this);
	    
	    radio_dij_bis = new JRadioButton("Multi-criteria Dijkstra (using linear combination)");
	    radio_dij_bis.setMnemonic(KeyEvent.VK_B);
	    radio_dij_bis.setSelected(false);
	    radio_dij_bis.addActionListener(this);
	    
	    JLabel space = new JLabel("");
	    JLabel space2 = new JLabel("");
	    
	    button_group = new ButtonGroup();
	    button_group.add(radio_dij_mono);
	    button_group.add(radio_dij_bi);
	    button_group.add(radio_dij_bis);
	    
	    panel.add(radio_dij_mono);
	    panel.add(space);
	    panel.add(radio_dij_bis);
	    panel.add(space2);
	    panel.add(radio_dij_bi);
	    
	    generer = new JButton("Rechercher");
	    generer.addActionListener(this);
	    add(generer, BorderLayout.SOUTH);
	    
	    // Slider
	    combine = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 50);
	    combine_space = new JLabel("Préférence");
	    Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
	    labelTable.put(new Integer(0), new JLabel("Security"));
	    labelTable.put(new Integer(100), new JLabel("Distance"));
	    labelTable.put(new Integer(50), new JLabel("50%"));
	    combine.setLabelTable(labelTable);
	    combine.setPaintLabels(true);

	    add(panel);
	    pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==radio_dij_mono || e.getSource()==radio_dij_bi) {
			panel.remove(combine_space);
			panel.remove(combine);
			panel.repaint();
			panel.validate();
		} else if(e.getSource()==radio_dij_bis) {
			panel.add(combine_space);
			panel.add(combine);
			panel.repaint();
			panel.validate();
		} else {
			String fp_n = filepath_noeuds.getText();
			String fp_a = filepath_arcs.getText();
			
			int indice_depart = 0, indice_arrive = 0;
			try {
				indice_depart = Integer.parseInt(depart.getText());
				indice_arrive = Integer.parseInt(arrive.getText());
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(this, "Something wrong happened: indices of the nodes must be integers.", "Oops!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			double coef = combine.getValue(); // 1 = Full Distance
			coef = coef/100;
			
			Graph g0 = new Graph();
			try {
				generateGraph(g0, fp_n, fp_a);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(this, "Something wrong happened: Graph can't be generated from non-existing or invalid files.", "Oops!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			try {
				Result result = null;
				if(getSelection(button_group)==radio_dij_mono) {
					MonoObjectiveDijkstra dij = new MonoObjectiveDijkstra(g0);
					result = dij.run(g0.getNode(indice_depart), g0.getNode(indice_arrive));
				} else if(getSelection(button_group)==radio_dij_bi) {
					BiOjectiveDijkstra dij = new BiOjectiveDijkstra(g0);
					result = dij.run(g0.getNode(indice_depart), g0.getNode(indice_arrive));
				} else {
					SimulatedBiOjectiveDijkstra dij = new SimulatedBiOjectiveDijkstra(g0);
					result = dij.run(g0.getNode(indice_depart), g0.getNode(indice_arrive), coef);
				}
				
				new ResultatFrame(result.toString());
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(this, "Something wrong happened: Algorithm failed.", "Oops!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	// This method returns the selected radio button in a button group
	private static JRadioButton getSelection(ButtonGroup group) {
	    for (Enumeration<?> e=group.getElements(); e.hasMoreElements(); ) {
	        JRadioButton b = (JRadioButton)e.nextElement();
	        if (b.getModel() == group.getSelection()) {
	            return b;
	        }
	    }
	    return null;
	}
	
	private void generateGraph(Graph g, String n, String a) throws IOException {
		importNodes(g, n);
		importLinks(g, a);
	}
	
	private void importNodes(Graph g, String filepath) throws IOException {
		FileInputStream fstream = null;
		fstream = new FileInputStream(filepath);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		String strLine;
		while((strLine = br.readLine()) != null) {
			String valeurs[];
			valeurs = strLine.split(" ");
			
			Node n = new Node(Integer.parseInt(valeurs[0]), Double.parseDouble(valeurs[1]), Double.parseDouble(valeurs[2]));
			g.addNodes(n);
		}
		
		in.close();
	}
	
	private void importLinks(Graph g, String filepath) throws IOException {
		FileInputStream fstream = null;
		fstream = new FileInputStream(filepath);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		String strLine;
		while((strLine = br.readLine()) != null) {
			String valeurs[];
			valeurs = strLine.split(" ");
			
			Node n = g.getNode(Integer.parseInt(valeurs[0]));
			Link a = new Link(g.getNode(Integer.parseInt(valeurs[1])), Integer.parseInt(valeurs[2]), Integer.parseInt(valeurs[3]));
			n.addLink(a);
		}
		in.close();
	}
	
	public static void main(String[] args) throws IOException {
		new MainFrame();
	}
}
