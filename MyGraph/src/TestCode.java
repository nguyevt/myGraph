import java.util.*;
import java.io.*;


public class TestCode {
	public static void main(String[] args) {
		if(args.length != 2) {
			System.err.println("USAGE: java Paths <vertex_file> <edge_file>");
			System.exit(1);
		}

		MyGraph g = readGraph(args[0],args[1]);
		Collection<Vertex> v = g.vertices();
        Collection<Edge> e = g.edges();
        
		System.out.println("Vertices are "+v);
		System.out.println("Edges are "+e);

		for (Vertex x : v) {
			System.out.println("Adjacent vertices to " + x.getLabel() + " are " + g.adjacentVertices(x));
		}
		
		for (Vertex x : v) {
			for (Vertex y : v) {
				Path shortest = g.shortestPath(x, y);
				System.out.println("Shortest path from " + x.getLabel() + " to " + 
						y.getLabel() + ": ");
				if (shortest != null) {
					for (int i = 0; i < shortest.vertices.size(); i++) {
						System.out.print(shortest.vertices.get(i) + " ");
					}
					System.out.println();
					System.out.println(shortest.cost);
				} else {
					System.out.println("does not exist");
				}
			}
		}
		
		for (Vertex x : v) {
			for (Vertex y : v) {
				if (g.edgeCost(x, y) != -1) {
					System.out.println("Cost from " + x.getLabel() + " to " + y.getLabel() + " is " + g.edgeCost(x, y));
				}
			}
		}
		
		
	}

	public static MyGraph readGraph(String f1, String f2) {
		Scanner s = null;
		try {
			s = new Scanner(new File(f1));
		} catch(FileNotFoundException e1) {
			System.err.println("FILE NOT FOUND: "+f1);
			System.exit(2);
		}

		Collection<Vertex> v = new ArrayList<Vertex>();
		while(s.hasNext())
			v.add(new Vertex(s.next()));

		try {
			s = new Scanner(new File(f2));
		} catch(FileNotFoundException e1) {
			System.err.println("FILE NOT FOUND: "+f2);
			System.exit(2);
		}

		Collection<Edge> e = new ArrayList<Edge>();
		while(s.hasNext()) {
			try {
				Vertex a = new Vertex(s.next());
				Vertex b = new Vertex(s.next());
				int w = s.nextInt();
				e.add(new Edge(a,b,w));
			} catch (NoSuchElementException e2) {
				System.err.println("EDGE FILE FORMAT INCORRECT");
				System.exit(3);
			}
		}

		return new MyGraph(v,e);
	}
}