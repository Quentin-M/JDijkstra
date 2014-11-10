package fr.quentinmachu.jdijkstra.entities;
import java.util.HashMap;

import fr.quentinmachu.jdijkstra.entities.Node;

/**
 * This class defines a graph
 * 
 * @author quentinmachu http://quentin-machu.fr
 *
 */
public class Graph {
	private HashMap<Integer,Node> nodes;

	/**
	 * Construct an empty graph
	 */
	public Graph() {
		nodes = new HashMap<Integer, Node>();
	}
	
	/**
	 * Return the nodes of the graph, mapped using their indices
	 * @return
	 */
	public HashMap<Integer, Node> getNodes() {
		return nodes;
	}
	
	/**
	 * Return the number of the nodes in the graph
	 * @return
	 */
	public int getNodesNumber() {
		return nodes.size();
	}

	/**
	 * Add a new node in the graph. If a node with this indice was previously existing, it is replaced.
	 * @param node
	 */
	public void addNodes(Node node) {
		nodes.put(node.getIndice(), node);
	}

	/**
	 * Return a node specified by its indice.
	 * @param indice
	 * @return the node or null if it doesn't exist
	 */
	public Node getNode(int indice) {
		return nodes.get(indice);
	}
}
