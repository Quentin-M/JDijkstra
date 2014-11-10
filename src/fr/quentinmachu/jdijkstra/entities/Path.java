package fr.quentinmachu.jdijkstra.entities;

import java.util.ArrayList;

import fr.quentinmachu.jdijkstra.entities.Node;

/**
 * This class represents a path in a graph
 * 
 * @author quentinmachu http://quentin-machu.fr
 *
 */
public class Path {
	private int distance;
	private int danger;
	private double combination;
	
	private ArrayList<Node> pathNodes;
	
	/**
	 * Initialize a new path with infinity values and no node
	 */
	public Path() {
		pathNodes = new ArrayList<Node>();
		distance = Integer.MAX_VALUE;
		combination = Integer.MAX_VALUE;
		danger = Integer.MAX_VALUE;
	}
	
	/**
	 * Return the total distance of the path
	 * @return
	 */
	public int getDistance() {
		return distance;
	}
	
	/**
	 * Set the total distance of the path
	 * @param distance
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	/**
	 * Return the total danger of the path
	 * @return
	 */
	public int getDanger() {
		return danger;
	}
	
	/**
	 * Set the total danger of the path
	 * @param danger
	 */
	public void setDanger(int danger) {
		this.danger = danger;
	}
	
	/**
	 * Return the combination value between danger and distance (used with LinearCombinationDijkstra)
	 * @return
	 */
	public double getCombination() {
		return combination;
	}
	
	/**
	 * Set the combination value between danger and distance (used with LinearCombinationDijkstra)
	 * @param combine
	 */
	public void setCombination(double combine) {
		this.combination = combine;
	}
	
	/**
	 * Return the nodes of the path (in the right order)
	 */
	public ArrayList<Node> getPathNodes() {
		return pathNodes;
	}
	
	/**
	 * Set the nodes of the path (in the right order)
	 * @param path
	 */
	public void setPathNodes(ArrayList<Node> path) {
		this.pathNodes = path;
	}

	@Override
	public String toString() {
		String r = new String();
		
		r = r + "--------------PATH---------------------\nDistance: "+distance+"\n";
		if(danger!=Integer.MAX_VALUE) r = r + "Danger: "+danger+"\nCombination: "+combination+"\n";
		
		for(Node n : pathNodes) {
			r = r + n + " -> ";
		}
		
		// On retire la flèche de fin
		r = r.substring(0, r.length()-3);
		
		r = r + "\n---------------------------------------";
		
		return r;
	}
}
