/** Vincent Nguyen, CSE 373, WIN2014
 * Assignment #5, 02/20/14
 * 
 * This program's behavior is to provide an implementation of a graph with 
 * a method that computes the shortest path from one vertex to another 
 * using Dijkstra's algorithm.
 * 
 */

import java.util.*;

/**
 * A representation of a graph.
 * Assumes that we do not have negative cost edges in the graph.
 */
public class MyGraph implements Graph {
    
	private Map<Vertex, Map<Vertex, Integer>> graph;

    /**
     * Creates a MyGraph object with the given collection of vertices
     * and the given collection of edges.
     * @param v a collection of the vertices in this graph
     * @param e a collection of the edges in this graph
     */
    public MyGraph(Collection<Vertex> v, Collection<Edge> e) {
    	graph = new HashMap<Vertex, Map<Vertex, Integer>>();
    	for (Vertex from : v) {
    		if (!graph.containsKey(from)) {
    			graph.put(from, new HashMap<Vertex, Integer>());
    		}
    	}
    	for (Edge current : e) {
    		if (current.getWeight() < 0 || !graph.containsKey(current.getSource()) 
    				|| !graph.containsKey(current.getDestination())) {
    			throw new IllegalArgumentException();
    		}
    		Map<Vertex, Integer> temp = graph.get(current.getSource());
    		if (temp.containsKey(current.getDestination())) {
    			if (temp.get(current.getDestination()) != current.getWeight()) {
    				throw new IllegalArgumentException();
    			}
    		}
    		temp.put(current.getDestination(), current.getWeight());
    		graph.put(current.getSource(), temp);
    	}
    }

    /** 
     * Return the collection of vertices of this graph
     * @return the vertices as a collection (which is anything iterable)
     */
    public Collection<Vertex> vertices() {
		return graph.keySet();
    }

    /** 
     * Return the collection of edges of this graph
     * @return the edges as a collection (which is anything iterable)
     */
    public Collection<Edge> edges() {
    	Set<Edge> edgeSet = new HashSet<Edge>();
    	for (Vertex from : graph.keySet()) {
    		Map<Vertex, Integer> temp = graph.get(from);
    		for (Vertex to : temp.keySet()) {
    			edgeSet.add(new Edge(from, to, temp.get(to)));
    		}
    	}
    	return edgeSet;
    }

    /**
     * Return a collection of vertices adjacent to a given vertex v.
     *   i.e., the set of all vertices w where edges v -> w exist in the graph.
     * Return an empty collection if there are no adjacent vertices.
     * @param v one of the vertices in the graph
     * @return an iterable collection of vertices adjacent to v in the graph
     * @throws IllegalArgumentException if v does not exist.
     */
    public Collection<Vertex> adjacentVertices(Vertex v) {
    	error(v, v);
    	return graph.get(v).keySet();
    }
    
    /**
     * Private method that returns the set of all vertices w, where v -> w
     * Return an empty collection if there are no adjacent vertices.
     * @param v one of the vertices in the graph
     * @return an iterable collection of vertices adjacent to v in the graph
     * @throws IllegalArgumentException if v does not exist.
     */
    private Collection<Vertex> neighbors(Vertex v) {
    	error(v, v);
    	Set<Vertex> adjacents = new HashSet<Vertex>();
    	for (Vertex a : graph.keySet()) {
    		if (graph.get(v).keySet().contains(a)) {
    			adjacents.add(a);
    		}
    	}
    	return adjacents;
    }
    
    /** 
     * Return if a path exists from a to b
     * @return true if a path from a to b exists, false otherwise
     */
    private boolean isPath(Vertex a, Vertex b) {
    	error(a, b);
    	Queue<Vertex> bfs = new LinkedList<Vertex>();
    	for (Vertex v : graph.keySet()) {
    		v.state = false;
    		if (v.equals(a)) {
    			bfs.add(v);
    		}
    	}
    	while (!bfs.isEmpty()) {
    		Vertex temp = bfs.remove();
    		if (temp.equals(b)) {
    			return true;
    		}
    		temp.state = true;
    		for (Vertex check : neighbors(temp)) {
    			if (!check.state) {
    				bfs.add(check);
    			}
    		}
    	}
    	return false;
    }

    /**
     * Test whether vertex b is adjacent to vertex a (i.e. a -> b) in a directed graph.
     * Assumes that we do not have negative cost edges in the graph.
     * @param a one vertex
     * @param b another vertex
     * @return cost of edge if there is a directed edge from a to b in the graph, 
     * return -1 otherwise.
     * @throws IllegalArgumentException if a or b do not exist.
     */
    public int edgeCost(Vertex a, Vertex b) {
    	error(a, b);
    	if (graph.get(a).containsKey(b)) {
    		return graph.get(a).get(b);
    	} else {
    		return -1;
    	}
    }

    /**
     * Returns the shortest path from a to b in the graph, or null if there is
     * no such path.  Assumes all edge weights are nonnegative.
     * Uses Dijkstra's algorithm.
     * @param a the starting vertex
     * @param b the destination vertex
     * @return a Path where the vertices indicate the path from a to b in order
     *   and contains a (first) and b (last) and the cost is the cost of 
     *   the path. Returns null if b is not reachable from a.
     * @throws IllegalArgumentException if a or b does not exist.
     */
    public Path shortestPath(Vertex a, Vertex b) {
    	error(a, b);
    	if (!isPath(a, b)) {
    		return null;
    	}
    	Queue<Vertex> pq = new PriorityQueue<Vertex>();
    	for (Vertex v : graph.keySet()) {
    		v.cost = Integer.MAX_VALUE;
    		v.state = false;
    		v.previous = null;
    		if (v.equals(a)) {
    			v.cost = 0;
    		}
    		pq.add(v);
    	}
    	while (!pq.isEmpty()) {
    		Vertex current = pq.remove();
    		current.state = true;
    		for (Vertex adjacent : neighbors(current)) {
    			if (!adjacent.state) {
    				if (current.cost + edgeCost(current, adjacent) < adjacent.cost) {
    					decreaseKey(pq, adjacent, current, current.cost 
    							+ edgeCost(current, adjacent));
    				}
    			}
    		}
    	}
    	LinkedList<Vertex> build = new LinkedList<Vertex>();
    	Vertex backtrack = null;
    	for (Vertex v : graph.keySet()) {
    		if (v.equals(b)) {
    			backtrack = v;
    		}
    	}
    	int value = backtrack.cost;
    	while (backtrack.previous != null) {
    		build.addFirst(backtrack);
    		backtrack = backtrack.previous;
    	}
    	build.addFirst(backtrack);
    	return new Path(build, value);
    }
    
    /** 
     * Helper method that decreases the key of a particular vertex in the priority
     * queue. It also changes the previous field of the vertex which will later be used
     * for finding the shortest path from one vertex to another
     */
    private void decreaseKey(Queue<Vertex> pq, Vertex adjacent, Vertex current,
    		int cost) {
    	pq.remove(adjacent);
    	adjacent.cost = cost;
    	adjacent.previous = current;
    	pq.add(adjacent);
    }
    
    /** 
     * Throws IllegalArgumentException if the passed vertices are not 
     * part of the set of vertices
     * @throws IllegalArgumentException if the graph doesn't contain
     * a or b
     */
    private void error(Vertex a, Vertex b) {
    	if (!graph.containsKey(a) || !graph.containsKey(b)) {
    		throw new IllegalArgumentException();
    	}
    }
}