import graph.DirectedEdge;
import graph.EdgeWeightedDigraph;
import graph.EdgeWeightedDirectedCycle;
import graph.In;
import graph.IndexMinPQ;
import graph.Queue;

/**
 * The kxh132430_P8_a class implements an algorithm to find the number 
 * of simple shortest paths from a source node s to each node.
 * The input is a directed graph G=(V,E) as input, with edge weights
 * W:E-->Z (negative weights are possible).  The output is the number of
 * shortest paths (not necessarily disjoint) from s to each vertex u in
 * the graph.  If the graph has a negative or zero cycle, reachable from s,
 * then print a message "Non-positive cycle in graph.  DAC is not applicable".
 * 
 * 
 * Dependencies:	graph.DirectedEdge;
 *					graph.EdgeWeightedDigraph;
 *					graph.EdgeWeightedDirectedCycle;
 *					graph.In;
 *					graph.IndexMinPQ;
 *					graph.Queue;
 * 
 * @version 	1.0 November 24th, 2014
 * @author 		KAI HUANG              section 013
 * 				VILAS REDDY PODDUTURI  section 016
 *
 */
public class kxh132430_P8_a {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//In in = new In("G:\\test\\p8-data\\channel-50-2.txt");

		//Take input from stdin (console/standard input)
		In in = new In();

		// Read a sequence of lines to construct a directed graph
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);

		// Test if the graph has a negative or zero cycle, reachable from s,
		EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);

		// If there is a cycle, then print an error message.
		if(finder.hasCycle()){
			System.err.println("Non-positive cycle in graph. DAC is not applicable.");
			System.exit(-1);
		}

		// Calculate the running time of the program
		long startTime = 0, endTime = 0;

		// If there is a negative-weighted edge in the graph, run BellmanFord algorithm.
		if(G.hasNegativeEdge()){
			startTime = System.currentTimeMillis();
			BellmanFord bmf = new BellmanFord(G);
			endTime = System.currentTimeMillis();
			// print the length of a shortest path from s to t, the number of shortest paths
			// from s to t, and the running time in msec.
			bmf.printPath(G, (endTime - startTime));

		} else {	// Otherwise run Dijkstra algorithm on the graph
			startTime = System.currentTimeMillis();
			Dijkstra dsp = new Dijkstra(G);
			endTime = System.currentTimeMillis();
			// print the length of a shortest path from s to t, the number of shortest paths
			// from s to t, and the running time in msec.
			dsp.printPath(G, (endTime - startTime));

		}
	}
}

/**
 * The Dijkstra class implements the Dijkstra algorithm on
 * a single source directed graph with non-negative-weighted edge.
 * 
 * @author KAI
 *
 */
class Dijkstra{

	private long[] dist;   // The distance of each vertex to source     
	int[] pred;			   // The predecessor of each vertex
	private long[] count;  // The total number of shortest paths from source to each vertex
	private IndexMinPQ<Long> pq; // A priority queue to store the vertices with the dist[u] as priority

	/**
	 * Dijkstra		A constructor that implements the Dijkstra algorithm.
	 * @param G		The Directed Graph instance of the algorithm.
	 */
	Dijkstra(EdgeWeightedDigraph G){
		// Initialization: 
		int s = G.S();
		dist = new long[G.V() + 1];
		pred = new int[G.V() + 1];
		count = new long[G.V() + 1];

		// Set the distance of each vertex to s as infinity, except for s itself.
		for (int u = 1; u <= G.V(); u++){
			dist[u] = Long.MAX_VALUE; // set dist[u] as infinity
			count[u] = 0;
		}
		dist[s] = 0;  // set dist[0] as 0
		count[s] = 1; // There is one shortest path from s to s

		// Dijkstra algorithm:
		// Process each vertex u on the priority queue in ascending order of the distance from s to u.
		pq = new IndexMinPQ<Long>(G.V() + 1);
		pq.insert(s, dist[s]); // Insert s to the priority queue.
		// Process the vertex on queue until the queue is empty.
		while (!pq.isEmpty()) {
			int u = pq.delMin(); // Extract the vertex with the minimum distance to s from the priority queue.
			for (DirectedEdge e : G.adj(u))
				relax(e); // Relax all the edges incident to this vertex
		}
	}

	/**
	 * relax		Relax an edge (u,v) and update the priority queue 
	 * 				if dist[u] + w(u,v) > dist[v]. 
	 * @param e		The edge to be relaxed.
	 */
	private void relax(DirectedEdge e) {
		int u = e.from(), v = e.to();
		// If dist[v] needs to be updated
		if (dist[v] > dist[u] + e.weight()) { 
			dist[v] = dist[u] + e.weight();
			pred[v] = u;  // Set u as the predecessor of v
			count[v] = count[u];  // There are equal number of shortest paths from s to u and from s to v
			if (pq.contains(v))  // If v is already on the priority queue, decreaseKey, adjust the index.
				pq.decreaseKey(v, dist[v]);
			else                
				pq.insert(v, dist[v]); // Insert v to the priority queue if it is not on it.
		} else if(dist[v] == dist[u] + e.weight()){ // If dist[v] is the minimum distance from s to v.
			count[v] += count[u];  // Count[s->v] = Count[s->v] + Count[s->u->v]
		}
	}

	/**
	 * reachable	Test if vertex u is reachable from s.
	 * @param u		The vertex to reach from s.		
	 * @return		If u is reachable from s, return true, otherwise return false.
	 */
	public boolean reachable(int u) {
		return dist[u] < Long.MAX_VALUE;
	}

	/**
	 * hasPred		Test if u has a predecessor in the shortest path tree.
	 * @param u		A given vertex in the graph.	
	 * @return		If u has a predecessor, return true, otherwise return false.
	 */
	public boolean hasPred(int u) {
		return pred[u] != 0;
	}

	/**
	 * printPath		Output the lengths of shortest paths from s to each vertex u in the graph, 
	 * 					the predecessor node of u in that shortest path, and the number of shortest 
	 * 					paths from s to u. If there is no path from s to a vertex, print INF as the 
	 * 					length, and - as the predecessor.
	 * @param G			The graph instance on which the BellmanFord algorithm runs. 
	 * @param time		The running time of the BellmanFord algorithm.
	 */
	public void printPath(EdgeWeightedDigraph G, long time){
		// Print the length of a shortest path from s to t, 
		// the number of shortest paths from s to t, and the running time.
		int t = G.T();
		System.out.println(dist[t] + " " + count[t] + " " + time);
		if(G.V() <= 100){ // Print the shortest path details if |V| is less than or equal to 100
			for(int u = 1; u <= G.V(); u++){
				String distance = reachable(u)? String.valueOf(dist[u]) : "INF";
				String predecessor = hasPred(u)? String.valueOf(pred[u]) : "-";
				System.out.println(u + " " + distance + " " + predecessor + " " + count[u]);
			}
		}
	}   

}

/**
 * The BellmanFord class implements the BellmanFord algorithm on
 * a single source directed graph with negative-weighted edge allowed.
 * 
 * @author KAI
 *
 */
class BellmanFord{
	private long[] dist; 	// The distance of each vertex to source             
	private int[] pred; 	// The predecessor of each vertex
	private long[] count;	// The total number of shortest paths from source to each vertex
	private boolean[] inQueue; // A boolean value to record if a vertex is in the queue
	private Queue<Integer> queue;  // A queue of vertices to be relaxed
	private int[] num; // The number of relax operations on a given vertex

	/**
	 * BellmanFord	A constructor that implements the BellmanFord algorithm.
	 * @param G		The Directed Graph instance of the algorithm.
	 */
	BellmanFord(EdgeWeightedDigraph G){

		// Initialization: 
		int s = G.S();
		dist = new long[G.V() + 1];
		pred = new int[G.V() + 1]; // Set the predecessor of each vertex as 0.
		count = new long[G.V() + 1];
		num = new int[G.V() + 1];
		inQueue = new boolean[G.V() + 1];

		// Set the distance of each vertex to s as infinity, except for s itself.
		for (int u = 1; u <= G.V(); u++){
			dist[u] = Long.MAX_VALUE; 
			count[u] = 0;  // Set the number of shortest path from source to each vertex as 0.
		}
		dist[s] = 0;  // Set the distance of s to s as 0.
		count[s] = 1;  // There is one path from s to s.

		// Bellman-Ford algorithm
		// Create a queue to store the vertices to be relaxed, enqueue s.
		queue = new Queue<Integer>();
		queue.enqueue(s);
		inQueue[s] = true;
		// Relax the vertex on queue until the queue is empty.
		while (!queue.isEmpty()) {
			int u = queue.dequeue();
			if((num[u]++) >= G.V()){ // Check if the graph has a non-positive cycle.
				throw new RuntimeException("Non-positive cycle in graph. DAC is not applicable.");
			}
			inQueue[u] = false;
			relax(G, u); // Relax a given vertex u
		}
	}

	/**
	 * relax		Relax a given vertex u, update its neighbor v and
	 * 				put vertex v on queue, if dist[u] + w(u,v) < dist[v].
	 * @param G		The graph that contains all neighbors of vertex u in adj[u].
	 * @param u		The vertex u to be relaxed.
	 */
	public void relax(EdgeWeightedDigraph G, int u) {

		// For each neighbor v of u, relax them and put them on queue if dist[v] is updated.
		for (DirectedEdge e : G.adj(u)) {
			int v = e.to();
			// If dist[v] needs to be updated
			if (dist[v] > dist[u] + e.weight()) {
				dist[v] = dist[u] + e.weight(); // Update dist[v]
				pred[v] = u; // Set u as the predecessor of v
				count[v] = count[u]; // There are equal number of shortest paths from s to u and from s to v
				if (!inQueue[v]) { // If v is not in queue, enqueue v.
					queue.enqueue(v); 
					inQueue[v] = true;
				} 
			} else if(dist[v] == dist[u] + e.weight()){ // If dist[v] is the minimum distance from s to v.
				count[v] += count[u]; // Count[s->v] = Count[s->v] + Count[s->u->v]
			}
		}
	}

	/**
	 * reachable	Test if vertex u is reachable from s.
	 * @param u		The vertex to reach from s.		
	 * @return		If u is reachable from s, return true, otherwise return false.
	 */
	public boolean reachable(int u) {
		return dist[u] < Long.MAX_VALUE;
	}

	/**
	 * hasPred		Test if u has a predecessor in the shortest path tree.
	 * @param u		A given vertex in the graph.	
	 * @return		If u has a predecessor, return true, otherwise return false.
	 */
	public boolean hasPred(int u) {
		return pred[u] != 0;
	}

	/**
	 * printPath		Output the lengths of shortest paths from s to each vertex u in the graph, 
	 * 					the predecessor node of u in that shortest path, and the number of shortest 
	 * 					paths from s to u. If there is no path from s to a vertex, print INF as the 
	 * 					length, and - as the predecessor.
	 * @param G			The graph instance on which the BellmanFord algorithm runs. 
	 * @param time		The running time of the BellmanFord algorithm.
	 */
	public void printPath(EdgeWeightedDigraph G, long time){
		// Print the length of a shortest path from s to t, 
		// the number of shortest paths from s to t, and the running time.
		int t = G.T();
		System.out.println(dist[t] + " " + count[t] + " " + time);
		if(G.V() <= 100){ // Print the shortest path details if |V| is less than or equal to 100
			for(int u = 1; u <= G.V(); u++){
				String distance = reachable(u)? String.valueOf(dist[u]) : "INF";
				String predecessor = hasPred(u)? String.valueOf(pred[u]) : "-";
				System.out.println(u + " " + distance + " " + predecessor + " " + count[u]);
			}
		}
	} 
}
