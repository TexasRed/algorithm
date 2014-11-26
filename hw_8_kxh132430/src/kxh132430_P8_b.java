import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import graph.DirectedEdge;
import graph.EdgeWeightedDigraph;
import graph.In;
import graph.Queue;

/**
 * The kxh132430_P8_b class implements an algorithm to solve the reward collection problem.
 * The input is an undirected graph G=(V,E), with weights W:E-->Z+, a
 * source node s in V.  In addition each node has a reward amount
 * associated with it, R:V-->Z+.  This amount can be claimed by visiting
 * that node (representing a node that is dirty and cleaning it with a
 * vacuum cleaner).  Starting from s, find a traversal that starts and
 * ends at s, in which the reward collected is a maximum.
 * 
 * Dependencies:	graph.DirectedEdge;
 *					graph.EdgeWeightedDigraph;
 *					graph.In;
 *					graph.Queue;
 *					kxh132430_P8_a
 *
 * @version 	1.0 November 24th, 2014
 * @author 		KAI HUANG              section 013
 * 				VILAS REDDY PODDUTURI  section 016
 * 
 */
public class kxh132430_P8_b {

	public static void main(String[] args) {

		//In in = new In("G:\\test\\p8-data\\p8-partb\\test.txt");

		//Take input from stdin (console/standard input)
		In in = new In();
		
		// Read a sequence of lines to construct an undirected graph
		EdgeWeightedGraph G = new EdgeWeightedGraph(in);

		// If there is a negative-weighted edge, then print an error message.
		if(G.hasNegativeEdge()){
			System.err.println("Negative-weighted edge in graph.");
			System.exit(-1);
		}
		// Calculate the running time of the program.
		long startTime = 0, endTime = 0;
		startTime = System.currentTimeMillis();
		RewardCollection rc = new RewardCollection(G); // Run Reward Collection algorithm on this instance.
		endTime = System.currentTimeMillis();
		// print the length of a shortest path from s to t, the number of shortest paths
		// from s to t, and the running time in msec.
		rc.printPath(G, (endTime - startTime));

	}

}

/**
 * The RewardCollection class implements the Dijkstra algorithm on
 * a single source directed graph with non-negative-weighted edge.
 * 
 * @author KAI
 *
 */
class RewardCollection{

	private long maxCost; // The maximum reward amount we can get by traversing the graph
	private ArrayList<Integer> maxPath;	// The traversing path to get the maximum reward amount.
	private int[] pred; // The predecessor of each vertices on the shortest path tree.

	/**
	 * RewardCollection		A constructor that implements the RewardCollection algorithm.
	 * @param G				The Undirected graph instance of the algorithm.
	 */
	RewardCollection(EdgeWeightedGraph G){

		// Run a Dijkstra algorithm on this instance
		Dijkstra dsp = new Dijkstra(G);
		// Let pred be the array that records the predecessor of each vertices on the shortest path tree.
		pred = dsp.pred;
		dsp = null; // Free the memory space

		// Run a Breadth First Search on the instance. Getting a collection of
		// tours that starts and ends at s, and visit each vertices except s only once.
		BFS bfs = new BFS();
		ArrayList<ArrayList<Integer>> tourSet = bfs.BFS_Search(G);
		bfs = null; // Free the memory space

		// Let maxPath be the tour to collect the maximum rewards
		maxPath = new ArrayList<Integer>(); 

		// Iterate through each tour in the collection of all valid tours in the graph starting and ending at s.
		Iterator<ArrayList<Integer>> itr = tourSet.iterator();
		
		while(itr.hasNext()){
			ArrayList<Integer> tour = itr.next(); // Get one tour from the collection of all valid tours
			long pathCost = 0; // Let pathCost be the reward amount collected along the tour.
			int u = G.S(); 
			// Iterate through each vertices on this tour
			Iterator<Integer> it = tour.iterator();
			while(it.hasNext()){
				int v = it.next();
				if(pred[v] == u){ // If (u,v) is also on the shortest path tree
					pathCost += G.cost[v]; // Add the reward amount at v to pathCost
				}
				u = v;
			}
			// Update the maximum cost and the corresponding tour if needed
			if(pathCost > maxCost){
				maxCost = pathCost;
				maxPath = tour;
			}
		}

	}

	/**
	 * printPath		In the first line of the output, print the maximum reward that can be collected, 
	 * 					and the running time. In the next lines, print the nodes in the traversal, in the order
	 *					visited, and the reward collected at that node.
	 *
	 * @param G			The graph instance on which the RewardCollection algorithm runs. 
	 * @param time		The running time of the RewardCollection algorithm.
	 */
	public void printPath(EdgeWeightedGraph G, long time){

		// In the first line of the output, print the maximum reward 
		// that can be collected, and the running time.
		System.out.println(this.maxCost + " " + time);
		
		// In the next lines, print the nodes in the traversal, in the order
		// visited, and the reward collected at that node.
		System.out.println(G.S() + " " + G.cost[G.S()]);
		// Iterate through the maxPath that collects the maximum rewards amount.
		Iterator<Integer> it = this.maxPath.iterator();
		int u = G.S();
		while(it.hasNext()){
			int v =it.next();
			long cost = 0; // The reward amount is zero if v is not reached via the shortest path
			if(this.pred[v] == u){ // If (u,v) is also on the shortest path tree
				cost = G.cost[v]; // Collect the reward
			} 
			System.out.println(v + " " + cost);
			u = v;
		}
	}

}

/**
 * The EdgeWeightedGraph class extends the EdgeWeightedDigraph class
 * to represent an undirected graph.
 * 
 * @author KAI
 *
 */
class EdgeWeightedGraph extends EdgeWeightedDigraph{

	public int[] cost; // The reward amount associated with each vertex.

	EdgeWeightedGraph(int v) {
		super(v);  // Inherit the constructor from Directed Graph class
		this.cost = new int[v+1]; 
	}

	EdgeWeightedGraph(In in) {
		this(in.readInt());  // Let V be the number of vertex
		int E = in.readInt();  // Let E be the number of edges
		if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
		this.S = in.readInt(); // Let S be the source

		// In the next V lines, read the reward amount at each vertices.
		for(int i = 1; i<= V; i++){
			cost[i] = in.readInt(); 
		}

		// If E is greater than 100000, only read the first 100000 edges from inputs
		if(E > 100000) 
			E = 100000;
		
		// Get the two vertices of an edge from the input
		for (int i = 1; i <= E; i++) {
			int u = in.readInt();
			int v = in.readInt();

			if (u <= 0 || u > V) throw new IndexOutOfBoundsException("vertex " + u + " is not between 1 and " + V);
			if (v <= 0 || v > V) throw new IndexOutOfBoundsException("vertex " + v + " is not between 1 and " + V);

			// Check if there is a negative-weighted edge.
			int w = in.readInt();
			if(w < 0) {
				this.negativeEdge = true; 
			}

			// Add both edge(u,v) and edge(v,u) to the graph
			addEdge(new DirectedEdge(u, v, w));
			addEdge(new DirectedEdge(v, u, w));
		}
	}
}

/**
 * The Node class implements the Node data structure in Breadth First Search
 * @author KAI
 *
 */
class Node{

	Node parent; // The parent of this node in BFS
	State state; // The state of this node

	Node(Node parent, State state){
		this.parent = parent;
		this.state = state;
	}	
}

/**
 * The State class implements the State of a Node in Breadth First Search
 * @author KAI
 *
 */
class State{

	int location;  // The location of the agent
	ArrayList<Integer> path; // The path traversed so far

	/**
	 * A constructor that builds the INITIAL_STATE of the Breadth-First Search
	 * @param location
	 */
	State(int location){
		this.location = location;
		this.path = new ArrayList<Integer>();
	}

	/**
	 * A constructor that constructs a state from an existing location and path
	 * @param location
	 * @param path
	 */
	State(int location, ArrayList<Integer> path){
		this.location = location;
		this.path = path;
	}
	
	/**
	 * A constructor that constructs a state from an existing state
	 * @param state
	 */
	State(State state){
		this.location = state.location;
		this.path = state.path;
	}
}

/**
 * The BFS class implements a Breadth First Search on an undirected Graph
 * @author KAI
 *
 */
class BFS{

	/**
	 * BFS_Search		Breadth First Search on an undirected Graph
	 * @param G			The undirected graph instance
	 * @return			A collection of tours starts and ends at s,
	 * 					and visits each vertex on the path except s only once.
	 */
	public ArrayList<ArrayList<Integer>> BFS_Search(EdgeWeightedGraph G){

		// Let solution be the collection of all valid tours on the graph
		ArrayList<ArrayList<Integer>> solution = new ArrayList<ArrayList<Integer>>();

		//Create the initial state in BFS
		State INITIAL_STATE = new State(G.S());
		
		// Create a node with this initial state in BFS
		Node node = new Node(null, INITIAL_STATE);

		// Create a queue to keep nodes for BFS
		Queue<Node> queue = new Queue<Node>();
		queue.enqueue(node);
		
		// Create an explored set to record the explored states
		HashSet<State> explored = new HashSet<State>();

		// Expanding the nodes until the queue is empty
		while (!(queue.isEmpty())) {
			node = queue.dequeue(); // Get one node from the queue
			explored.add(node.state); // Add this node to explored set

			int u = node.state.location;
			for(DirectedEdge e : G.adj(u)){
				// If the vertex is not on the prior tour path
				if(!(node.state.path.contains(e.to()))){
					// Expand the node by following edge d to generate a child node
					Node childNode = RESULT(node, e);
					if(!(explored.contains(childNode.state))){ // If the state of child node is not explored before
						if(GOAL_TEST(G, childNode.state)){ // Test if we reach the goal state
							solution.add(childNode.state.path); // Add this tour path to the solution
						} else{ 
							queue.enqueue(childNode); // Otherwise enqueue the child node
						}
					}
				}
			}
		}
		return solution; 
	}

	/**
	 * RESULT			Generate a child node by expanding the parent node 
	 * @param node		The parent node to be expanded
	 * @param e			The edge connecting the parent and child node
	 * @return			The child node in BFS
	 */
	public Node RESULT(Node node, DirectedEdge e){
		// Let v be the child of u
		int v = e.to(); 

		// Append vertex v to the tour
		ArrayList<Integer> path = new ArrayList<Integer>(node.state.path);
		path.add(v);

		//Create a new state with this new path and new vertex v
		State newState = new State(v, path);

		//Create a new node with this new state
		Node childNode = new Node(node, newState);

		return childNode; // Return the child node
	}
	
	/**
	 * GOAL_TEST		Test if the BFS reaches the goal state
	 * @param G			The graph instance of the BFS
	 * @param state		The state to be tested
	 * @return			return true if the BFS reaches the goal state, otherwise return false;
	 */
	public boolean GOAL_TEST(EdgeWeightedGraph G, State state){
		return state.location == G.S();
	}
}
