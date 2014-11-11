package fr.quentinmachu.jdijkstra.algorithms;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fr.quentinmachu.jdijkstra.entities.Graph;
import fr.quentinmachu.jdijkstra.entities.Label;
import fr.quentinmachu.jdijkstra.entities.Link;
import fr.quentinmachu.jdijkstra.entities.Node;
import fr.quentinmachu.jdijkstra.entities.Path;
import fr.quentinmachu.jdijkstra.entities.Result;

/**
 * This class permits to run Dijkstra algorithm to find best paths between two points considering distance and danger
 * 
 * @author quentinmachu http://quentin-machu.fr
 *
 */
public class BiOjectiveDijkstra  implements Dijkstra {
	private Graph g;
	private ArrayList<Label> closedList; // Explored nodes
	private ArrayList<Label> openedList; // Nodes to explore
	
	// For each node, we associate an array of non-dominated labels
	private HashMap<Node, ArrayList<Label>> labels;

	public BiOjectiveDijkstra(Graph g) {
		this.g = g;
		closedList = new ArrayList<Label>();
		openedList = new ArrayList<Label>();
		
		labels = new HashMap<Node, ArrayList<Label>>();
	}
	
	/**
	 * Initialize Dijkstra algorithm
	 * @param start
	 */
	private void init(Node start) {
		// Label for start node
		Label startL = new Label(null, start, 0, 0);
		ArrayList<Label> AL = new ArrayList<Label>();
		AL.add(startL);
		labels.put(start, AL);
		
		// Add start label to opened list
		openedList.add(startL);
	}
	
	/**
	 * Run a Multi-Criteria Dijkstra algorithm from start node to end node
	 * 
	 * @param start
	 * @param end
	 * @return A Result object containing best paths
	 */
	public Result run(Node start, Node end) throws IllegalArgumentException, IllegalStateException {
		if(start==null || end==null) throw new IllegalArgumentException("Start or end node not provided");
		
		/*System.out.println("-------- Dijkstra Multi-Objectif --------");
		System.out.println("On cherche le plus court du " + depart + " vers le " + end);*/
		
		// Initialize algorithm
		long startTime = System.currentTimeMillis();
		init(start);
		
		Label toExplore = null;
		do { // While we didn't explore the end node yet
			toExplore = findNextLabel(end); // Pick the label to explore (which has the minimum combination value)
			if(toExplore==null) throw new IllegalStateException("No label to explore has been found.");
			
			//System.out.println("Exploration: "+toExplore);
			
			// Node has been explored, add it as explored and remove it from opened list
			closedList.add(toExplore);
			openedList.remove(toExplore);
						
			// For each successor
			for(Link succ : toExplore.getNode().getLinks()) {
				//System.out.println("- Etude du successeur: "+succ.getNoeud());
				//System.out.println("-- Ancienne Distance vers: "+succ.getNoeud()+" <= " + dist.get(succ.getNoeud()));
								
				// Invalid node ?
				if(succ.getNode()==null) throw new IllegalStateException("The explorer node is invalid.");
				
				// Create label & verify that it's not dominated in the list of known labels
				Label L = new Label(toExplore, succ.getNode(), toExplore.getDistance()+succ.getDistance(), toExplore.getDanger()+succ.getDanger());
				if(checkDomination(L)) {
					// Not a dominated solution
					if(labels.get(succ.getNode())==null) labels.put(succ.getNode(), new ArrayList<Label>());

					// Add the label in the list of non-dominated solutions for the node & add it to opened list if not already present
					labels.get(succ.getNode()).add(L);
					if(!openedList.contains(L) && !closedList.contains(L)) openedList.add(L);
				}
			}
			//System.out.println();
		} while(toExplore.getNode()!=end);
		
		long endTime = System.currentTimeMillis();
		/*System.out.println("Temps d'éxécution: "  + (float)(end-start) + " ms");
		System.out.println("-----------------------------------------\n");*/
		
		return new Result(g, start, end, (endTime-startTime), backtrack(end));
	}

	/**
	 * Verify if the label is dominated
	 * @param l1
	 * @return true is the label isn't dominated
	 */
	private boolean checkDomination(Label l1) {
		// Can't be dominated if the label is the only one
		if(labels.get(l1.getNode())==null) return true;
		
		for(Label l2 : labels.get(l1.getNode())) {
			if(l1.getDistance()>=l2.getDistance() && l1.getDanger()>=l2.getDanger() && (l1.getDistance()>l2.getDistance() || l1.getDanger()>l2.getDanger()) ) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Pick a label which has not been explored yet (not in S) and which has the minimum distance value
	 * @param end
	 * @return The best label to explore
	 */
	private Label findNextLabel(Node end) {
		Label l_min = null;
		double dist_min = Double.MAX_VALUE;
		
		for (Iterator<Label> i = openedList.iterator() ; i.hasNext() ;) {
		    Label n = i.next();
			if(!closedList.contains(n)) {
				double d = Math.pow(n.getNode().getX()-end.getX(), 2)+Math.pow(n.getNode().getY()-end.getY(), 2);
				if(n.getDistance()+Math.sqrt(d) < dist_min)  {
					dist_min = n.getDistance() + Math.sqrt(d);
					l_min = n;
				}
			}
		}
		return l_min;
	}
	
	/**
	 * Build best paths from Dijkstra results
	 * @param end
	 * @return An array of non-dominated paths
	 */
	private ArrayList<Path> backtrack(Node end) {
		ArrayList<Path> chemins = new ArrayList<Path>();
		
		// For each label leading to the end node
		for(Iterator<Label> it = labels.get(end).iterator(); it.hasNext(); ) {
			// We have a non-dominated path
			Label l = it.next();
					
			// Build path nodes
			ArrayList<Node> pathNodes = new ArrayList<Node>();
			pathNodes.add(l.getNode());
			while(l.getPred()!=null) {
				l = l.getPred();
				pathNodes.add(l.getNode());
			}
			
			// Reverse path nodes
			ArrayList<Node> reversed = new ArrayList<Node>();
			for(int i=pathNodes.size()-1; i>=0; i--)
			    reversed.add(pathNodes.get(i));
			
			// Create path
			Path path = new Path();
			path.setDistance(l.getDistance());
			path.setPathNodes(reversed);
			path.setDanger(l.getDanger());
			
			// Add path to the list
			chemins.add(path);
		}
			
		return chemins;
	}
	
	/**
	 * Get graph
	 * @return
	 */
	public Graph getGraph() {
		return g;
	}
}
