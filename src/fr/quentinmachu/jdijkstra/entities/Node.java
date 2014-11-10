package fr.quentinmachu.jdijkstra.entities;
import java.util.ArrayList;

/**
 * This file defines a node of a graph
 * 
 * @author quentinmachu http://quentin-machu.fr
 *
 */
public class Node {
	private int indice;
	private ArrayList<Link> links;
	private double x;
	private double y;
	
	/**
	 * Create a new node with an indice and its coordinates
	 * @param indice
	 * @param x
	 * @param y
	 */
	public Node(int indice, double x, double y) {
		links = new ArrayList<Link>();
		this.indice = indice;
		this.x = x;
		this.y = y;
	}

	/**
	 * Return the indice of the node
	 * @return
	 */
	public int getIndice() {
		return indice;
	}

	/**
	 * Return the links which start from this node
	 * @return
	 */
	public ArrayList<Link> getLinks() {
		return links;
	}

	/**
	 * Add a new link which start from this node
	 * @param arc
	 */
	public void addLink(Link arc) {
		links.add(arc);
	}
	
	/**
	 * Return the number of links
	 * @return
	 */
	public int getLinkNumbers() {
		return links.size();
	}

	/**
	 * Return the X coordinate of the node
	 * @return
	 */
	public double getX() {
		return x;
	}

	/**
	 * Return the Y coordinate of the node
	 * @return
	 */
	public double getY() {
		return y;
	}

	@Override
	public String toString() {
		return "Node n°"+indice;
	}
}
