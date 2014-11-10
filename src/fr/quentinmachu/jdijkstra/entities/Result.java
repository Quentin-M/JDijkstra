package fr.quentinmachu.jdijkstra.entities;

import java.util.ArrayList;

/**
 * This file is a part of the JDijkstra Software
 * 
 * @author quentinmachu http://quentin-machu.fr
 *
 */
public class Result {
	public Node start;
	public Node end;
	public double coef;
	
	public float exec_time;
	public ArrayList<Path> paths;
	public String errorMsg;
	
	public Result(String s) {
		errorMsg = s;
	}
	
	public Result(Node start, Node end, float exec_time, ArrayList<Path> paths) {
		this.start = start;
		this.end = end;
		this.exec_time = exec_time;
		this.paths = paths;
	}
	
	public Result(Node start, Node end, float exec_time, ArrayList<Path> paths, double coef) {
		this(start, end, exec_time, paths);
		this.coef = coef;
	}

	@Override
	public String toString() {
		String s = new String();
		
		if(errorMsg==null) {
			s=s+("-------- Dijkstra --------\n");
			s=s+("On cherche le plus court du " + start + " vers le " + end+"\n");
			s=s+("Temps d'éxécution: "  + (exec_time) + " ms\n");
			s=s+("-----------------------------------------\n");
			
			s=s+"\n\n";
			
			for(Path c: paths) {
				s=s+c+"\n\n";
			}
		} else {
			s=s+errorMsg;
		}
		return s;
	}
}
