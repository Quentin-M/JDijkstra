package fr.quentinmachu.jdijkstra.algorithms;

import fr.quentinmachu.jdijkstra.entities.Node;
import fr.quentinmachu.jdijkstra.entities.Result;

public interface Dijkstra {
	/**
	 * Run Dijkstra algorithm between start and end nodes
	 * @param start
	 * @param end
	 * @return A result containing best path(s)
	 */
	Result run(Node start, Node end);
}
