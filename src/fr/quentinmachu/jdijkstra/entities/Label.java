package fr.quentinmachu.jdijkstra.entities;

/**
 * This file is a part of the JDijkstra Software
 * 
 * @author quentinmachu http://quentin-machu.fr
 *
 */
public class Label {
	private Label p;
	private Node n;
	private int distance;
	private int danger;
	
	public Label(Label pred, Node depart, int distance, int danger) {
		setPred(pred);
		setNode(depart);
		setDistance(distance);
		setDanger(danger);
	}
	
	public Node getNode() {
		return n;
	}
	
	public void setNode(Node n) {
		this.n = n;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public int getDanger() {
		return danger;
	}
	
	public void setDanger(int danger) {
		this.danger = danger;
	}
	
	public Label getPred() {
		return p;
	}
	
	public void setPred(Label pred) {
		this.p = pred;
	}
	
	public String toString() {
		if(p==null) return "Noeud n°"+n.getIndice()+" --- D: "+distance+" --- S: "+danger;
		else return "Noeud n°"+p.getNode().getIndice()+"  -> Noeud n°"+n.getIndice()+" --- D: "+distance+" --- S: "+danger;
	}
}
