package fr.quentinmachu.jdijkstra.entities;

/**
 * This class represents a link to a node
 * <br/>
 * We note that a link is uni-directional
 * 
 * @author quentinmachu http://quentin-machu.fr
 *
 */
public class Link {
	private Node node;
	private int distance;
	private int danger;
	
	/**
	 * Create a new link to a specified node, with a distance and a danger
	 * @param node
	 * @param distance
	 * @param danger
	 */
	public Link(Node node, int distance, int danger) {
		this.node = node;
		this.distance = distance;
		this.danger = danger;
	}
	
	/**
	 * Create a new link to a specified node, with a distance
	 * @param node
	 * @param distance
	 */
	public Link(Node node, int distance) {
		this.node = node;
		this.distance = distance;
		this.danger = 0;
	}

	/**
	 * Return the target node
	 * @return
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * Return the distance to the node
	 * @return
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * Return the danger associated to this link
	 * @return
	 */
	public int getDanger() {
		return danger;
	}

	@Override
	public String toString() {
		return "Link to node n°"+node.getIndice()+" : ("+distance+","+danger+")";
	}
}
