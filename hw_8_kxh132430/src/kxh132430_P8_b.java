import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import graph.DirectedEdge;
import graph.EdgeWeightedDigraph;
import graph.In;
import graph.Queue;

public class kxh132430_P8_b {

	public static void main(String[] args) {

		In in = new In("G:\\test\\p8-data\\p8-partb\\test.txt");

		EdgeWeightedGraph G2 = new EdgeWeightedGraph(in);

		long startTime = 0, endTime = 0;
		startTime = System.currentTimeMillis();
		RewardCollection rc = new RewardCollection(G2);
		endTime = System.currentTimeMillis();
		
		rc.printPath(G2, (endTime - startTime));
		
	}

}

class RewardCollection{
	
	private long maxCost;
	private ArrayList<Integer> maxPath;
	private int[] pred;
	
	RewardCollection(EdgeWeightedGraph G){

		Dijkstra dsp = new Dijkstra(G);
		pred = dsp.pred;
		dsp = null;
		
		BFS bfs = new BFS();
		ArrayList<ArrayList<Integer>> tourSet = bfs.BFS_Search(G);
		bfs = null;
		
		maxPath = new ArrayList<Integer>(); 

		Iterator<ArrayList<Integer>> itr = tourSet.iterator();

		while(itr.hasNext()){
			
			ArrayList<Integer> tour = itr.next();
			
			long pathCost = 0;
			int u = G.S(); 
			
			Iterator<Integer> it = tour.iterator();
			while(it.hasNext()){
				int v = it.next();
				if(pred[v] == u){
					pathCost += G.cost[v];
				}
				u = v;
			}

			if(pathCost > maxCost){
				maxCost = pathCost;
				maxPath = tour;
			}
		}
	
	}
	
	public void printPath(EdgeWeightedGraph G, long time){
		
		System.out.println(this.maxCost + " " + time);
		System.out.println(G.S() + " " + G.cost[G.S()]);
		
		Iterator<Integer> it = this.maxPath.iterator();
		int u = G.S();
		while(it.hasNext()){
			int v =it.next();
			long cost = 0;
			if(this.pred[v] == u){
				cost = G.cost[v];
			} 
			System.out.println(v + " " + cost);
			u = v;
		}
	}
	
}

class EdgeWeightedGraph extends EdgeWeightedDigraph{
	
	public int[] cost;
	
	EdgeWeightedGraph(int v) {
		super(v);
		this.cost = new int[v+1];
	}
	
	EdgeWeightedGraph(In in) {
    	this(in.readInt());
        int E = in.readInt();
        if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
        this.S = in.readInt();
        
        for(int i = 1; i<= V; i++){
        	cost[i] = in.readInt();
        }
        
        if(E > 100000) 
        	E = 100000;
        for (int i = 1; i <= E; i++) {
            
        	int u = in.readInt();
            
            int v = in.readInt();
            
            if (u <= 0 || u > V) throw new IndexOutOfBoundsException("vertex " + u + " is not between 1 and " + V);
            
            if (v <= 0 || v > V) throw new IndexOutOfBoundsException("vertex " + v + " is not between 1 and " + V);

            int w = in.readInt();
            
            if(w < 0) {
            	this.negativeEdge = true;
            }
            
            addEdge(new DirectedEdge(u, v, w));
            addEdge(new DirectedEdge(v, u, w));
        }
    }
}

class Node{
	
	Node parent;
	State state;
	
	Node(Node parent, State state){
		this.parent = parent;
		this.state = state;
	}	
}

class State{
	
	int location;
	ArrayList<Integer> path;
	
	State(int location){
		this.location = location;
		this.path = new ArrayList<Integer>();
	}
	
	State(int location, ArrayList<Integer> path){
		this.location = location;
		this.path = path;
	}
	
	State(State state){
		this.location = state.location;
		this.path = state.path;
	}
}

class BFS{
	
	public ArrayList<ArrayList<Integer>> BFS_Search(EdgeWeightedGraph G){
		
		ArrayList<ArrayList<Integer>> solution = new ArrayList<ArrayList<Integer>>();
		
		State INITIAL_STATE = new State(G.S());
		
		Node node = new Node(null, INITIAL_STATE);
		
		Queue<Node> queue = new Queue<Node>();
		queue.enqueue(node);
		
		HashSet<State> explored = new HashSet<State>();
		
		while (!(queue.isEmpty())) {
			node = queue.dequeue();
			explored.add(node.state);
			//System.out.println("now exanding:" + node.state.location);
			
			int u = node.state.location;
			
			for(DirectedEdge e : G.adj(u)){
				
				if(!(node.state.path.contains(e.to()))){
					Node childNode = RESULT(node, e);
					if(!(explored.contains(childNode.state))){
						if(GOAL_TEST(G, childNode.state)){
							solution.add(childNode.state.path);
						} else{
							queue.enqueue(childNode);
						}
					}
				}
			}
		}
		return solution;
	}
	
	public Node RESULT(Node node, DirectedEdge e){
		
		int v = e.to();
		
		ArrayList<Integer> path = new ArrayList<Integer>(node.state.path);
		path.add(v);
		
		State newState = new State(v, path);
		
		Node childNode = new Node(node, newState);
		
		return childNode;
	}
	
	public boolean GOAL_TEST(EdgeWeightedGraph G, State state){
	    return state.location == G.S();
	}
}
