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
 * This class permits to run Dijkstra algorithm to find the shortest path between two points
 * 
 * @author quentinmachu http://quentin-machu.fr
 *
 */
public class MonoObjectiveDijkstra implements Dijkstra {
	private Graph g;
	private ArrayList<Node> closedList; // Explored nodes
	private ArrayList<Node> openedList; // Nodes to explore
	
	private HashMap<Node, Integer> distances; // List of distances
	private HashMap<Node, Node> bestNextNode; // Path used. To go to Node 4, I used Node 2 : [4] [2]

	public MonoObjectiveDijkstra(Graph g) {
		this.g = g;
		closedList = new ArrayList<Node>();
		openedList = new ArrayList<Node>();
		distances = new HashMap<Node, Integer>();
		bestNextNode = new HashMap<Node, Node>();
	}
	
	private void init(Node start) {
		for (Iterator<Node> i = g.getNodes().values().iterator() ; i.hasNext() ;) {
			// For each node in the graph
			distances.put(i.next(), Integer.MAX_VALUE);
		}
		
		// Add the start node to the list of nodes to explore and initialize distance
		distances.put(start, 0);
		openedList.add(start);
	}
	
	/**
	 * Run a simple Dijkstra algorithm from start node to end node
	 * 
	 * @param start
	 * @param end
	 * @return A Result object containing the best path
	 */
	public Result run(Node start, Node end) {
		if(start==null || end==null) throw new IllegalArgumentException("Start or end node not provided");
		
		/*System.out.println("-------- Dijkstra Mono-Objectif ---------");
		System.out.println("On cherche le plus court du " + start + " vers le " + end);*/
		
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
						
			// For each successor
			for(Link succ : toExplore.getLinks()) {
				//System.out.println("- Etude du successeur: "+succ.getNoeud());
				//System.out.println("-- Ancienne Distance vers: "+succ.getNoeud()+" <= " + dist.get(succ.getNoeud()));
				
				// Invalid node ?
				if(succ.getNode()==null) throw new IllegalStateException("The explored node is invalid.");
				
				// If the opened list doesn't contain the successor node, add it
				if(!openedList.contains(succ.getNode()) && !closedList.contains(succ.getNode())) openedList.add(succ.getNode());
				
				if(succ.getDistance()+distances.get(toExplore) < distances.get(succ.getNode())) {
					// New shortest path found
					//System.out.println("-- Nouvelle Distance vers: "+succ.getNoeud()+" <= " + dist.get(succ.getNoeud()));
					distances.put(succ.getNode(), (succ.getDistance()+distances.get(toExplore)));
					bestNextNode.put(succ.getNode(), toExplore);
				} else {
					// Use previous path
					distances.put(succ.getNode(), distances.get(succ.getNode()));
				}
			}
			//System.out.println();
		}
		
		long endTime = System.currentTimeMillis();
		/*System.out.println("Temps d'éxécution: "  + (float)(end-start) + " ms");
		System.out.println("-----------------------------------------\n");*/
		
		ArrayList<Path> c = new ArrayList<Path>();
		c.add(backtrack(start, end));
		return new Result(start, end, (endTime-startTime), c);
	}

	/**
	 * Pick a node which has not been explored yet (not in S) and which has the minimum combination value
	 * @param end
	 * @return The best node to explore
	 */
	private Node findNextNode(Node end) {
		Node n_min = null;
		double dist_min = Double.MAX_VALUE;
		
		for (Iterator<Node> i = openedList.iterator() ; i.hasNext() ;) {
		    Node n = i.next();
			if(!closedList.contains(n)) {
				double d = Math.pow(n.getX()-end.getX(), 2)+Math.pow(n.getY()-end.getY(), 2);
				if (distances.get(n)+Math.sqrt(d) < dist_min)  {
					dist_min = distances.get(n) + Math.sqrt(d);
					n_min = n;
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
