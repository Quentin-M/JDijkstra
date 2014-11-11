package fr.quentinmachu.jdijkstra.entities;

import java.util.ArrayList;

/**
 * This class wraps results from a Dijkstra algorithm run. 
 * 
 * @author quentinmachu http://quentin-machu.fr
 *
 */
public class Result {
	private Graph graph;
	private Node start;
	private Node end;
	private double coef;
	
	private float exec_time;
	private ArrayList<Path> paths;
	
	/**
	 * Create a new Result with required values
	 * 
	 * @param g
	 * @param start
	 * @param end
	 * @param exec_time
	 * @param paths
	 */
	public Result(Graph g, Node start, Node end, float exec_time, ArrayList<Path> paths) {
		this.start = start;
		this.end = end;
		this.exec_time = exec_time;
		this.paths = paths;
		this.coef = -1;
	}
	
	/**
	 * Create a new Result with required values, including combination coef
	 * 
	 * @param start
	 * @param end
	 * @param exec_time
	 * @param paths
	 * @param coef
	 */
	public Result(Graph g, Node start, Node end, float exec_time, ArrayList<Path> paths, double coef) {
		this(g, start, end, exec_time, paths);
		this.coef = coef;
	}

	/**
	 * Return the graph on Dijkstra algorithm ran
	 * @return
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * Return starting node of the path finding
	 * @return
	 */
	public Node getStart() {
		return start;
	}

	/**
	 * Research the target node of the path finding
	 * @return
	 */
	public Node getEnd() {
		return end;
	}

	/**
	 * Return the combination value (in case of SimulatedBiObjectiveDijkstra)
	 * Or -1 if the algorithm wasn't SimulatedBiObjectiveDijkstra
	 * 
	 * @return
	 */
	public double getCoef() {
		return coef;
	}

	/**
	 * Return the execution time of the algorithm, exprimed in ms
	 * @return
	 */
	public float getExec_time() {
		return exec_time;
	}

	/**
	 * Return a list containing best paths.
	 * It may contain an unique path (MonoObjectiveDijkstra, SimulatedBiObjectiveDijskstra) or multiple paths (BiObjectiveDijkstra)
	 * 
	 * @return
	 */
	public ArrayList<Path> getPaths() {
		return paths;
	}

	@Override
	public String toString() {
		String s = new String();
		
		s=s+("-------- Dijkstra --------\n");
		s=s+("Shortest path from " + start + " to " + end+"\n");
		s=s+("Execution time: "  + (exec_time) + " ms\n");
		s=s+("-----------------------------------------\n\n");
				
		for(Path c: paths) s=s+c+"\n\n";
			
		return s;
	}
}
