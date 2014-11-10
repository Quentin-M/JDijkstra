package fr.quentinmachu.jdijkstra.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fr.quentinmachu.jdijkstra.entities.Graph;
import fr.quentinmachu.jdijkstra.entities.Link;
import fr.quentinmachu.jdijkstra.entities.Node;
import fr.quentinmachu.jdijkstra.entities.Path;
import fr.quentinmachu.jdijkstra.entities.Result;

/**
 * This class permits to run Dijkstra algorithm using linear combination to find one shortest/less dangerous path between two points
 * 
 * @author quentinmachu http://quentin-machu.fr
 *
 */
public class SimulatedBiOjectiveDijkstra implements Dijkstra {
	private Graph g;
	private ArrayList<Node> closedList; // Explored nodes
	private ArrayList<Node> openedList; // Nodes to explore
	
	private HashMap<Node, Double> combinations; // List of linear combinations
	private HashMap<Node, Integer> distances; // List of distances
	private HashMap<Node, Integer> dangers; // List of dangers
	
	private HashMap<Node, Node> bestNextNode; // Path used. To go to Node 4, I used Node 2 : [4] [2]
	
	public SimulatedBiOjectiveDijkstra(Graph g) {
		this.g = g;
		closedList = new ArrayList<Node>();
		openedList = new ArrayList<Node>();
		combinations = new HashMap<Node, Double>();
		distances = new HashMap<Node, Integer>();
		dangers = new HashMap<Node, Integer>();
		bestNextNode = new HashMap<Node, Node>();
	}
	
	/**
	 * Initialize Dijkstra algorithm
	 * @param start
	 */
	private void init(Node start) {
		for (Iterator<Node> i = g.getNodes().values().iterator() ; i.hasNext() ;) {
			// For each node in the graph
			combinations.put(i.next(), Double.MAX_VALUE);
		}
		
		// Add the start node to the list of nodes to explore and initialize its values
		openedList.add(start);
		combinations.put(start, 0.0);
		distances.put(start, 0);
		dangers.put(start, 0);
	}
	
	/**
	 * Run a Linear Combination Dijkstra algorithm from start node to end node and consider distance as important as danger (coef=0.5)
	 * 
	 * @param start
	 * @param end
	 * @return A Result object containing the best path
	 */
	public Result run(Node start, Node end) {
		return run(start, end, 0.5);
	}
	
	/**
	 * Run a Linear Combination Dijkstra algorithm from start node to end node
	 * 
	 * @param start
	 * @param end
	 * @param coef Define how much we consider the importance of distance against danger. 1 means we consider distance only and 0 means we consider danger only
	 * @return A Result object containing the best path
	 */
	public Result run(Node start, Node end, double coef) throws IllegalArgumentException, IllegalStateException {
		if (coef>1 || coef<0) throw new IllegalArgumentException("Coef must lay between 0 and 1.");
		if(start==null || end==null) throw new IllegalArgumentException("Start or end node not provided");
				
		/*System.out.println("-------- Dijkstra Comb. Linéaire --------");
		System.out.println("On cherche le plus court du " + start + " vers le " + end);
		System.out.println("Distance: "+coef*100+"%");
		System.out.println("Sécurité: "+(1-coef)*100+"%");*/
		
		// Initialize algorithm
		long startTime = System.currentTimeMillis();
		init(start);
		
		Node toExplore = null;
		while(toExplore!=end) { // While we didn't explore the end node yet
			toExplore = findNextNode(end); // Pick the node to explore (which has the minimum combination value)
			if(toExplore==null) throw new IllegalStateException("No node to explore has been found.");
			
			//System.out.println("Exploration: "+toExplore);
			
			// Node has been explored, add it as explored and remove it from opened list
			closedList.add(toExplore);
			openedList.remove(toExplore);
			
			// End condition
			if(toExplore == end) {
				long endTime = System.currentTimeMillis();
				/*System.out.println("Temps d'éxécution: "  + (float)(end-start) + " ms");
				System.out.println("-----------------------------------------\n");*/
				
				ArrayList<Path> c = new ArrayList<Path>();
				c.add(backtrack(start, end));
				return new Result(start, end, (endTime-startTime), c, coef);
			}
			
			// For each successor
			for(Link succ : toExplore.getLinks()) {
				//System.out.println("- Etude du successeur: "+succ.getNoeud());
				//System.out.println("-- Ancienne combinaison vers: "+succ.getNoeud()+" <= " + combine.get(succ.getNoeud()));
				
				// Invalid node ?
				if(succ.getNode()==null) throw new IllegalStateException("The explored node is invalid.");
				
				// If the opened list doesn't contain the successor node, add it
				if(!openedList.contains(succ.getNode()) && !closedList.contains(succ.getNode())) openedList.add(succ.getNode());

				double succCombination = coef*succ.getDistance() + (1-coef)*succ.getDanger();
				if(succCombination+combinations.get(toExplore) < combinations.get(succ.getNode())) {
					// New shortest path found
					//System.out.println("-- Nouvelle combinaison vers: "+succ.getNoeud()+" <= " + combine.get(succ.getNoeud()));
					combinations.put(succ.getNode(), succCombination+combinations.get(toExplore));
					distances.put(succ.getNode(), succ.getDistance()+distances.get(toExplore));
					dangers.put(succ.getNode(), succ.getDanger()+dangers.get(toExplore));
					bestNextNode.put(succ.getNode(), toExplore);
				} else {
					// Use previous path
					combinations.put(succ.getNode(), combinations.get(succ.getNode()));
					distances.put(succ.getNode(), distances.get(succ.getNode()));
					dangers.put(succ.getNode(), dangers.get(succ.getNode()));
				}
			}
			//System.out.println();
		}

		throw new IllegalStateException("Oops? I didn't explore the end node.");
	}

	/**
	 * Pick a node which has not been explored yet (not in S) and which has the minimum combination value
	 * @param end
	 * @return The best node to explore
	 */
	private Node findNextNode(Node end) {
		Node n_min = null;
		double combine_min = Integer.MAX_VALUE;
		
		for (Iterator<Node> i = openedList.iterator() ; i.hasNext() ;) {
		    Node n = i.next();
			if(!closedList.contains(n)) {
				if (combinations.get(n)< combine_min)  {
					combine_min = combinations.get(n);
					n_min = n ;
				}
			}
		}
		return n_min;
	}
	
	/**
	 * Build a path from Dijkstra results
	 * @param start
	 * @param end
	 * @return The best path
	 */
	private Path backtrack(Node start, Node end) {
		// Build path nodes
		ArrayList<Node> pathNodes = new ArrayList<Node>();
		
		pathNodes.add(end);

		Node last = bestNextNode.get(end);
		pathNodes.add(last);
		while(last!=start) {
			last = bestNextNode.get(last);
			pathNodes.add(last);
		}
		
		// Reverse path nodes
		ArrayList<Node> reversed = new ArrayList<Node>();
		for(int i=pathNodes.size()-1; i>=0; i--)
		    reversed.add(pathNodes.get(i));
					
		// Create path
		Path c = new Path();
		c.setPathNodes(reversed);
		c.setCombination(combinations.get(end)) ;
		c.setDanger(dangers.get(end));
		c.setDistance(distances.get(end));
		
		return c;
	}
	
	/**
	 * Get graph
	 * @return
	 */
	public Graph getGraph() {
		return g;
	}
}
